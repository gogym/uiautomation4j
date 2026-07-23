package io.getbit.uiautomation.win.com;

import io.getbit.uiautomation.exception.AutomationException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * STA（单线程单元）任务执行器
 *
 * <p>UIA COM 对象必须在 STA 线程中创建和调用。Java 默认线程是 MTA 模式，
 * 直接在非 STA 线程调用 UIA COM 接口会导致 {@code RPC_E_WRONG_THREAD} 或随机崩溃。</p>
 *
 * <p>本类创建一个专用的 STA 线程，所有 UIA COM 操作都通过 {@link #submit(Callable)}
 * 提交到该线程执行，确保 COM 对象的线程亲和性得到满足。</p>
 *
 * <p>设计要点：</p>
 * <ul>
 *   <li>单线程保证所有 COM 对象在同一线程创建和使用（STA 线程亲和性）</li>
 *   <li>线程启动时以 {@code COINIT_APARTMENTTHREADED} 模式初始化 COM</li>
 *   <li>通过 {@link BlockingQueue} + {@link FutureTask} 实现任务提交与同步等待</li>
 *   <li>调用方通过 {@code Future.get()} 阻塞等待结果，API 对外保持同步</li>
 * </ul>
 *
 * <p>使用示例：</p>
 * <pre>
 * StaTaskExecutor executor = new StaTaskExecutor();
 * executor.start();
 *
 * // 提交 COM 操作到 STA 线程
 * String name = executor.submit(() -&gt; {
 *     IUIAutomation automation = IUIAutomation.create();
 *     IUIAutomationElement root = automation.getRootElement();
 *     return root.getName();
 * }).get();
 *
 * // 使用完毕后关闭
 * executor.shutdown();
 * </pre>
 */
public class StaTaskExecutor {

    /** STA 工作线程 */
    private volatile Thread staThread;

    /** 任务队列，STA 线程从此队列中取任务执行 */
    private final BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();

    /** 标记 STA 线程是否正在运行 */
    private volatile boolean running = false;

    /**
     * 启动 STA 线程
     *
     * <p>创建一个守护线程，以 STA 模式初始化 COM，然后循环从任务队列中取任务执行。
     * 线程设置为守护线程（daemon），这样当 JVM 退出时线程会自动终止。</p>
     *
     * @throws IllegalStateException 如果已经启动
     */
    public synchronized void start() {
        if (running) {
            throw new IllegalStateException("STA 执行器已经启动");
        }
        running = true;
        staThread = new Thread(() -> {
            // 在 STA 线程中初始化 COM
            Win32Util.initCOM();
            try {
                while (running || !taskQueue.isEmpty()) {
                    try {
                        // 阻塞等待任务，最多等待 100ms 后检查 running 标志
                        Runnable task = taskQueue.poll(100, java.util.concurrent.TimeUnit.MILLISECONDS);
                        if (task != null) {
                            task.run();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            } finally {
                Win32Util.uninitCOM();
            }
        }, "UIA-STA-Thread");
        staThread.setDaemon(true);
        staThread.start();
    }

    /**
     * 提交任务到 STA 线程执行并同步等待结果
     *
     * <p>将任务包装为 {@link FutureTask} 放入任务队列，然后在调用线程上
     * 阻塞等待 STA 线程执行完毕。如果 STA 线程执行任务时抛出异常，
     * 异常会被重新抛出（包装为 {@link AutomationException}）。</p>
     *
     * <p><b>重要：</b>不要在 STA 线程内部调用此方法，否则会导致死锁。
     * STA 线程内部的方法应直接执行 COM 调用，无需再次提交。</p>
     *
     * @param <T>  返回值类型
     * @param task 要执行的任务
     * @return 任务执行结果
     * @throws AutomationException 如果任务执行失败或线程被中断
     */
    public <T> T submit(Callable<T> task) {
        if (!running && (staThread == null || !staThread.isAlive())) {
            throw new AutomationException("STA 执行器未启动或已关闭");
        }
        FutureTask<T> futureTask = new FutureTask<>(task);
        taskQueue.add(futureTask);
        try {
            return futureTask.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AutomationException("STA 任务被中断", e);
        } catch (java.util.concurrent.ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof AutomationException) {
                throw (AutomationException) cause;
            }
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new AutomationException("STA 任务执行失败", cause);
        }
    }

    /**
     * 提交无返回值的任务到 STA 线程执行
     *
     * @param task 要执行的任务
     */
    public void submit(Runnable task) {
        submit(() -> {
            task.run();
            return null;
        });
    }

    /**
     * 关闭 STA 线程
     *
     * <p>设置 running 标志为 false，等待 STA 线程处理完剩余任务后退出。
     * 最多等待 5 秒，超时后强制中断线程。</p>
     */
    public synchronized void shutdown() {
        running = false;
        if (staThread != null) {
            try {
                staThread.join(5000);
                if (staThread.isAlive()) {
                    staThread.interrupt();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            staThread = null;
        }
    }

    /**
     * 检查 STA 执行器是否正在运行
     *
     * @return true 表示正在运行
     */
    public boolean isRunning() {
        return running && staThread != null && staThread.isAlive();
    }

    /**
     * 检查当前线程是否是 STA 线程
     *
     * @return true 表示当前线程就是 STA 工作线程
     */
    public boolean isCurrentThreadSta() {
        return Thread.currentThread() == staThread;
    }
}
