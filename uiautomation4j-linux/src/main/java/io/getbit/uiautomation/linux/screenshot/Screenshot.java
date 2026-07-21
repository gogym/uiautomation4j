package io.getbit.uiautomation.linux.screenshot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 截图工具类 - 通过 Java AWT Robot API 实现
 *
 * <p>在 Linux 上使用 Java 内置的 Robot 类进行截图，无需额外的本地库。
 * 支持全屏截图和区域截图。</p>
 *
 * <p>注意：在无头（headless）环境下无法使用截图功能。</p>
 */
public class Screenshot {

    /**
     * 截取整个屏幕并保存到文件
     *
     * @param filePath 保存路径（支持 PNG/JPG/BMP 格式）
     */
    public static void captureScreen(String filePath) {
        try {
            Robot robot = new Robot();
            Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            BufferedImage image = robot.createScreenCapture(screenRect);
            saveImage(image, filePath);
        } catch (AWTException e) {
            throw new RuntimeException("截图失败：无法创建 Robot。请确保 DISPLAY 环境变量已设置。", e);
        } catch (Exception e) {
            throw new RuntimeException("截图失败: " + e.getMessage(), e);
        }
    }

    /**
     * 截取指定区域并保存到文件
     *
     * @param filePath 保存路径
     * @param x        区域左上角 X 坐标
     * @param y        区域左上角 Y 坐标
     * @param width    区域宽度
     * @param height   区域高度
     */
    public static void captureRegion(String filePath, int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            captureScreen(filePath);
            return;
        }

        try {
            Robot robot = new Robot();
            Rectangle region = new Rectangle(x, y, width, height);
            BufferedImage image = robot.createScreenCapture(region);
            saveImage(image, filePath);
        } catch (AWTException e) {
            throw new RuntimeException("截图失败：无法创建 Robot。请确保 DISPLAY 环境变量已设置。", e);
        } catch (Exception e) {
            throw new RuntimeException("截图失败: " + e.getMessage(), e);
        }
    }

    /**
     * 保存图片到文件
     *
     * @param image    图片
     * @param filePath 文件路径
     */
    private static void saveImage(BufferedImage image, String filePath) {
        try {
            String format = getFileFormat(filePath);
            ImageIO.write(image, format, new File(filePath));
        } catch (Exception e) {
            throw new RuntimeException("保存图片失败: " + filePath, e);
        }
    }

    /**
     * 根据文件扩展名确定图片格式
     *
     * @param filePath 文件路径
     * @return 图片格式
     */
    private static String getFileFormat(String filePath) {
        String lower = filePath.toLowerCase();
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
            return "jpg";
        } else if (lower.endsWith(".bmp")) {
            return "bmp";
        } else if (lower.endsWith(".gif")) {
            return "gif";
        }
        return "png";
    }

    private Screenshot() {
        // 工具类禁止实例化
    }
}
