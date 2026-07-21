package com.gettyio.uiautomation.win.screenshot;

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
 */
public class Screenshot {

    /**
     * 截取屏幕指定区域并保存为图片文件
     *
     * @param filePath 保存路径
     * @param x        左上角 X 坐标
     * @param y        左上角 Y 坐标
     * @param width    宽度
     * @param height   高度
     */
    public static void captureRegion(String filePath, int x, int y, int width, int height) {
        if (width <= 0 || height <= 0) {
            return;
        }

        // 获取屏幕 DC
        WinDef.HDC hdcScreen = User32.INSTANCE.GetDC(null);
        WinDef.HDC hdcMem = GDI32.INSTANCE.CreateCompatibleDC(hdcScreen);

        // 创建兼容位图
        WinDef.HBITMAP hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcScreen, width, height);
        WinNT.HANDLE hOld = GDI32.INSTANCE.SelectObject(hdcMem, hBitmap);

        // 复制屏幕区域到位图
        GDI32.INSTANCE.BitBlt(hdcMem, 0, 0, width, height, hdcScreen, x, y, GDI32.SRCCOPY);

        // 恢复 DC
        GDI32.INSTANCE.SelectObject(hdcMem, hOld);

        // 将位图转换为 BufferedImage
        BufferedImage image = hbitmapToBufferedImage(hBitmap, width, height);

        // 清理 GDI 资源
        GDI32.INSTANCE.DeleteObject(hBitmap);
        GDI32.INSTANCE.DeleteDC(hdcMem);
        User32.INSTANCE.ReleaseDC(null, hdcScreen);

        // 保存图片
        if (image != null) {
            try {
                String format = getImageFormat(filePath);
                ImageIO.write(image, format, new File(filePath));
            } catch (IOException e) {
                throw new RuntimeException("保存截图失败: " + filePath, e);
            }
        }
    }

    /**
     * 截取整个屏幕
     *
     * @param filePath 保存路径
     */
    public static void captureScreen(String filePath) {
        int screenWidth = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CXSCREEN);
        int screenHeight = User32.INSTANCE.GetSystemMetrics(WinUser.SM_CYSCREEN);
        captureRegion(filePath, 0, 0, screenWidth, screenHeight);
    }

    /**
     * 将 HBITMAP 转换为 BufferedImage
     */
    private static BufferedImage hbitmapToBufferedImage(WinDef.HBITMAP hBitmap, int width, int height) {
        WinDef.HDC hdcScreen = User32.INSTANCE.GetDC(null);
        WinDef.HDC hdcMem = GDI32.INSTANCE.CreateCompatibleDC(hdcScreen);

        WinGDI.BITMAPINFO bmi = new WinGDI.BITMAPINFO();
        bmi.bmiHeader.biWidth = width;
        bmi.bmiHeader.biHeight = -height; // 自上而下
        bmi.bmiHeader.biPlanes = 1;
        bmi.bmiHeader.biBitCount = 32;
        bmi.bmiHeader.biCompression = WinGDI.BI_RGB;

        // 使用 Memory 代替 byte[] 作为 Pointer 传入 GetDIBits
        int bufferSize = width * height * 4;
        Memory pixels = new Memory(bufferSize);

        int result = GDI32.INSTANCE.GetDIBits(hdcMem, hBitmap, 0, height,
                pixels, bmi, WinGDI.DIB_RGB_COLORS);

        GDI32.INSTANCE.DeleteDC(hdcMem);
        User32.INSTANCE.ReleaseDC(null, hdcScreen);

        if (result == 0) {
            return null;
        }

        // 创建 BufferedImage（BGRA 格式）
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int idx = (row * width + col) * 4;
                int b = pixels.getByte(idx) & 0xFF;
                int g = pixels.getByte(idx + 1) & 0xFF;
                int r = pixels.getByte(idx + 2) & 0xFF;
                int a = pixels.getByte(idx + 3) & 0xFF;
                int argb = (a << 24) | (r << 16) | (g << 8) | b;
                image.setRGB(col, row, argb);
            }
        }

        return image;
    }

    /**
     * 根据文件扩展名获取图片格式
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
