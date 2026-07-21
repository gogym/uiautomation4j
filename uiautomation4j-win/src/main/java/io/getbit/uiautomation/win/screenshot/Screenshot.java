package io.getbit.uiautomation.win.screenshot;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * 截图工具类
 * 通过 Win32 GDI API 实现屏幕截图
 *
 * <p>截图流程（基于 GDI 双缓冲技术）：
 * <ol>
 *   <li>获取屏幕设备上下文（DC）</li>
 *   <li>创建内存 DC 和兼容位图</li>
 *   <li>使用 BitBlt 将屏幕区域复制到内存位图</li>
 *   <li>通过 GetDIBits 将位图数据读取到 Java 内存</li>
 *   <li>转换为 BufferedImage 并保存为图片文件</li>
 *   <li>释放所有 GDI 资源</li>
 * </ol>
 *
 * <p>支持多种图片格式：PNG、JPG、BMP、GIF（根据文件扩展名自动识别）。
 *
 * <p>使用示例：
 * <pre>
 * // 截取整个屏幕
 * Screenshot.captureScreen("screenshot.png");
 *
 * // 截取指定区域
 * Screenshot.captureRegion("region.png", 100, 100, 800, 600);
 * </pre>
 */
public class Screenshot {

    /**
     * 截取屏幕指定区域并保存为图片文件
     *
     * <p>GDI 截图流程：
     * <ol>
     *   <li>GetDC(null) - 获取屏幕设备上下文</li>
     *   <li>CreateCompatibleDC - 创建内存 DC（双缓冲）</li>
     *   <li>CreateCompatibleBitmap - 创建与屏幕兼容的位图</li>
     *   <li>SelectObject - 将位图选入内存 DC</li>
     *   <li>BitBlt - 将屏幕区域复制到内存位图</li>
     *   <li>转换为 BufferedImage 并保存</li>
     *   <li>清理所有 GDI 资源（DeleteObject、DeleteDC、ReleaseDC）</li>
     * </ol>
     *
     * @param filePath 保存路径（扩展名决定图片格式：png/jpg/bmp/gif）
     * @param x        截取区域左上角 X 坐标（像素）
     * @param y        截取区域左上角 Y 坐标（像素）
     * @param width    截取区域宽度（像素，必须 > 0）
     * @param height   截取区域高度（像素，必须 > 0）
     */
    public static void captureRegion(String filePath, int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }

        // 第1步：获取屏幕设备上下文（DC）
        WinDef.HDC hdcScreen = User32.INSTANCE.GetDC(null);
        // 第2步：创建与屏幕兼容的内存 DC（用于双缓冲绘图）
        WinDef.HDC hdcMem = GDI32.INSTANCE.CreateCompatibleDC(hdcScreen);

        // 第3步：创建与屏幕兼容的位图，并选入内存 DC
        WinDef.HBITMAP hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcScreen, width, height);
        WinNT.HANDLE hOld = GDI32.INSTANCE.SelectObject(hdcMem, hBitmap);

        // 第4步：使用 BitBlt 将屏幕指定区域复制到内存位图
        // SRCCOPY 表示直接复制源像素
        GDI32.INSTANCE.BitBlt(hdcMem, 0, 0, width, height, hdcScreen, x, y, GDI32.SRCCOPY);

        // 第5步：恢复内存 DC 中原来的对象（避免资源泄漏）
        GDI32.INSTANCE.SelectObject(hdcMem, hOld);

        // 第6步：将 GDI 位图转换为 Java BufferedImage
        BufferedImage image = hbitmapToBufferedImage(hBitmap, width, height);

        // 第7步：释放 GDI 资源（必须按顺序释放）
        GDI32.INSTANCE.DeleteObject(hBitmap);  // 删除位图对象
        GDI32.INSTANCE.DeleteDC(hdcMem);       // 删除内存 DC
        User32.INSTANCE.ReleaseDC(null, hdcScreen); // 释放屏幕 DC

        // 第8步：将 BufferedImage 保存为图片文件
        if (image != null) {
            try {
                // 根据文件扩展名确定图片格式（png/jpg/bmp/gif）
                String format = getImageFormat(filePath);
                ImageIO.write(image, format, new File(filePath));
            } catch (IOException e) {
                throw new RuntimeException("保存截图失败: " + filePath, e);
            }
        }
    }

    /**
     * 截取整个屏幕并保存为图片文件
     * <p>通过 GetSystemMetrics 获取屏幕分辨率，然后调用 {@link #captureRegion}
     *
     * @param filePath 保存路径（扩展名决定图片格式）
     */
    public static void captureScreen(String filePath) {
        // 获取主屏幕的宽度和高度（像素）
        int screenWidth = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CXSCREEN);
        int screenHeight = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CYSCREEN);
        captureRegion(filePath, 0, 0, screenWidth, screenHeight);
    }

    /**
     * 将 GDI HBITMAP 转换为 Java BufferedImage
     *
     * <p>转换流程：
     * <ol>
     *   <li>构建 BITMAPINFO 结构（32位 BGRA，自上而下）</li>
     *   <li>分配 JNA Memory 作为像素缓冲区（width * height * 4 字节）</li>
     *   <li>调用 GetDIBits 将位图数据读入缓冲区</li>
     *   <li>逐像素将 BGRA 数据转换为 ARGB 格式的 BufferedImage</li>
     * </ol>
     *
     * <p>注意：GDI 位图使用 BGRA 字节序，而 BufferedImage 使用 ARGB 整数格式，
     * 需要进行字节序转换。
     *
     * @param hBitmap GDI 位图句柄
     * @param width   位图宽度（像素）
     * @param height  位图高度（像素）
     * @return 转换后的 BufferedImage，如果 GetDIBits 失败则返回 null
     */
    private static BufferedImage hbitmapToBufferedImage(WinDef.HBITMAP hBitmap, int width, int height) {
        WinDef.HDC hdcScreen = User32.INSTANCE.GetDC(null);
        WinDef.HDC hdcMem = GDI32.INSTANCE.CreateCompatibleDC(hdcScreen);

        // 构建 BITMAPINFO：描述位图的元数据
        WinGDI.BITMAPINFO bmi = new WinGDI.BITMAPINFO();
        bmi.bmiHeader.biWidth = width;
        bmi.bmiHeader.biHeight = -height; // 负值表示自上而下的位图（与 GDI 默认自下而上相反）
        bmi.bmiHeader.biPlanes = 1;       // 必须为 1
        bmi.bmiHeader.biBitCount = 32;    // 32位色深（BGRA）
        bmi.bmiHeader.biCompression = WinGDI.BI_RGB; // 未压缩格式

        // 分配像素缓冲区：每个像素 4 字节（B, G, R, A）
        // 使用 JNA Memory 而非 byte[] 因为 GetDIBits 需要 Pointer 参数
        int bufferSize = width * height * 4;
        Memory pixels = new Memory(bufferSize);

        // 调用 GetDIBits 将位图数据读入缓冲区
        int result = GDI32.INSTANCE.GetDIBits(hdcMem, hBitmap, 0, height,
                pixels, bmi, WinGDI.DIB_RGB_COLORS);

        GDI32.INSTANCE.DeleteDC(hdcMem);
        User32.INSTANCE.ReleaseDC(null, hdcScreen);

        if (result == 0) {
            return null;
        }

        // 创建 ARGB 格式的 BufferedImage
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // 逐像素将 BGRA 字节数据转换为 ARGB 整数
        // GDI 输出格式：每像素 4 字节 [B, G, R, A]
        // BufferedImage TYPE_INT_ARGB 格式：每像素 1 个 int [A<<24 | R<<16 | G<<8 | B]
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = (row * width + col) * 4; // 当前像素在缓冲区中的字节偏移
                int b = pixels.getByte(idx) & 0xFF;     // 蓝色分量
                int g = pixels.getByte(idx + 1) & 0xFF; // 绿色分量
                int r = pixels.getByte(idx + 2) & 0xFF; // 红色分量
                int a = pixels.getByte(idx + 3) & 0xFF; // Alpha 分量
                int argb = (a << 24) | (r << 16) | (g << 8) | b; // 组合为 ARGB 整数
                image.setRGB(col, row, argb);
            }
        }

        return image;
    }

    /**
     * 根据文件扩展名获取图片格式名称
     * <p>支持的格式：jpg/jpeg、bmp、gif，其他扩展名默认返回 "png"
     *
     * @param filePath 文件路径
     * @return 图片格式名称（用于 ImageIO.write）
     */
    private static String getImageFormat(String filePath) {
        int dotIndex = filePath.lastIndexOf('.');
        if (dotIndex == -1) {
            return "png";
        }
        String ext = filePath.substring(dotIndex + 1).toLowerCase();
        switch (ext) {
            case "jpg":
            case "jpeg":
                return "jpg";
            case "bmp":
                return "bmp";
            case "gif":
                return "gif";
            default:
                return "png";
        }
    }
}
