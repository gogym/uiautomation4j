package io.getbit.uiautomation.mac.screenshot;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 截图工具类 - 通过 macOS CoreGraphics CGWindowListCreateImage API 实现
 *
 * <p>截图流程：
 * <ol>
 *   <li>调用 CGWindowListCreateImage 创建 CGImage</li>
 *   <li>通过 CGDataProvider 将 CGImage 数据读取到 Java 内存</li>
 *   <li>转换为 BufferedImage</li>
 *   <li>保存为图片文件（PNG/JPG/BMP）</li>
 *   <li>释放 CGImage 资源</li>
 * </ol>
 *
 * <p>支持两种截图模式：
 * <ul>
 *   <li>全屏截图：{@link #captureScreen(String)}</li>
 *   <li>区域截图：{@link #captureRegion(String, int, int, int, int)}</li>
 * </ul>
 *
 * @see <a href="https://developer.apple.com/documentation/coregraphics/1454716-cgwindowlistcreateimage">CGWindowListCreateImage</a>
 */
public class Screenshot {

    /**
     * CoreGraphics 框架 JNA 绑定（截图相关）
     */
    interface CoreGraphics extends Library {
        CoreGraphics INSTANCE = Native.load("CoreGraphics", CoreGraphics.class);

        /**
         * 创建窗口列表的截图
         *
         * @param bounds    截图区域（CGRect，double[4]: x, y, width, height），null 表示全屏
         * @param listOption 窗口列表选项（kCGWindowListOptionOnScreenOnly = 1）
         * @param windowID  指定窗口 ID（0 表示所有窗口）
         * @param imageOption 图像选项（kCGWindowImageDefault = 0）
         * @return CGImageRef 指针
         */
        Pointer CGWindowListCreateImage(double[] bounds, int listOption,
                                         int windowID, int imageOption);

        /** 获取 CGImage 宽度 */
        int CGImageGetWidth(Pointer image);

        /** 获取 CGImage 高度 */
        int CGImageGetHeight(Pointer image);

        /** 获取 CGImage 每行字节数 */
        int CGImageGetBytesPerRow(Pointer image);

        /** 获取 CGImage 位图信息 */
        Pointer CGImageGetDataProvider(Pointer image);

        /** 从 DataProvider 复制数据到缓冲区 */
        Pointer CGDataProviderCopyData(Pointer provider);

        /** 获取 CFData 长度 */
        long CFDataGetLength(Pointer data);

        /** 获取 CFData 字节指针 */
        Pointer CFDataGetBytePtr(Pointer data);

        /** 释放 CGImage */
        void CGImageRelease(Pointer image);

        /** 释放 CFData */
        void CFRelease(Pointer cf);
    }

    /** 截图选项：仅屏幕可见窗口 */
    private static final int kCGWindowListOptionOnScreenOnly = 1;
    /** 截图选项：默认 */
    private static final int kCGWindowImageDefault = 0;

    /**
     * 截取整个屏幕并保存到文件
     *
     * @param filePath 保存路径（支持 PNG/JPG/BMP 格式）
     */
    public static void captureScreen(String filePath) {
        captureRegion(filePath, 0, 0, 0, 0);
    }

    /**
     * 截取指定区域并保存到文件
     *
     * <p>通过 CGWindowListCreateImage 截取屏幕指定矩形区域。
     * 如果 width 和 height 都为 0，则截取全屏。</p>
     *
     * @param filePath 保存路径
     * @param x        区域左上角 X 坐标
     * @param y        区域左上角 Y 坐标
     * @param width    区域宽度（0 表示全屏）
     * @param height   区域高度（0 表示全屏）
     */
    public static void captureRegion(String filePath, int x, int y, int width, int height) {
        // 构建截图区域（CGRect = {x, y, width, height}）
        double[] bounds = null;
        if (width > 0 && height > 0) {
            bounds = new double[]{x, y, width, height};
        }

        // 创建截图
        Pointer cgImage = CoreGraphics.INSTANCE.CGWindowListCreateImage(
                bounds, kCGWindowListOptionOnScreenOnly, 0, kCGWindowImageDefault);

        if (cgImage == null || cgImage == Pointer.NULL) {
            throw new RuntimeException("截图失败：CGWindowListCreateImage 返回 null");
        }

        try {
            int imgWidth = CoreGraphics.INSTANCE.CGImageGetWidth(cgImage);
            int imgHeight = CoreGraphics.INSTANCE.CGImageGetHeight(cgImage);
            int bytesPerRow = CoreGraphics.INSTANCE.CGImageGetBytesPerRow(cgImage);

            // 获取像素数据
            Pointer dataProvider = CoreGraphics.INSTANCE.CGImageGetDataProvider(cgImage);
            Pointer cfData = CoreGraphics.INSTANCE.CGDataProviderCopyData(dataProvider);
            long dataLength = CoreGraphics.INSTANCE.CFDataGetLength(cfData);
            Pointer bytePtr = CoreGraphics.INSTANCE.CFDataGetBytePtr(cfData);

            // 复制数据到 Java 数组
            byte[] pixels = bytePtr.getByteArray(0, (int) dataLength);

            // 创建 BufferedImage（CGImage 使用 BGRA 格式）
            BufferedImage image = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
            for (int row = 0; row < imgHeight; row++) {
                for (int col = 0; col < imgWidth; col++) {
                    int offset = row * bytesPerRow + col * 4;
                    if (offset + 3 < pixels.length) {
                        int b = pixels[offset] & 0xFF;
                        int g = pixels[offset + 1] & 0xFF;
                        int r = pixels[offset + 2] & 0xFF;
                        int a = pixels[offset + 3] & 0xFF;
                        int rgb = (a << 24) | (r << 16) | (g << 8) | b;
                        image.setRGB(col, row, rgb);
                    }
                }
            }

            // 释放 CFData
            CoreGraphics.INSTANCE.CFRelease(cfData);

            // 保存图片
            String format = getFileFormat(filePath);
            try {
                ImageIO.write(image, format, new File(filePath));
            } catch (Exception e) {
                throw new RuntimeException("保存图片失败: " + filePath, e);
            }
        } finally {
            // 释放 CGImage
            CoreGraphics.INSTANCE.CGImageRelease(cgImage);
        }
    }

    /**
     * 根据文件扩展名确定图片格式
     *
     * @param filePath 文件路径
     * @return 图片格式（png/jpg/bmp）
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
        return "png"; // 默认 PNG
    }

    private Screenshot() {
        // 工具类禁止实例化
    }
}
