package view.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class ImageUtils {

    /**
     * تغییر اندازه تصویر یک JLabel با کیفیت بالا و نمایش در وسط بدون کشیدگی
     * @param label               JLabel مورد نظر
     * @param originalImage           مسیر تصویر
     * @param width               عرض جدید
     * @param height              ارتفاع جدید
     * @param preserveAspectRatio آیا نسبت ابعاد حفظ شود؟
     */
    public static void resizeLabelImage(JLabel label,
                                        Image originalImage,
                                        int width,
                                        int height,
                                        boolean preserveAspectRatio) {

        // محاسبه اندازه‌های جدید با حفظ نسبت ابعاد
        int newWidth = width;
        int newHeight = height;

        if (preserveAspectRatio) {
            double aspectRatio = (double) originalImage.getWidth(null) / originalImage.getHeight(null);

            // محاسبه اندازه‌های جدید با حفظ نسبت
            if (width / aspectRatio <= height) {
                newHeight = (int) (width / aspectRatio);
            } else {
                newWidth = (int) (height * aspectRatio);
            }
        }

        // ایجاد تصویر جدید با کیفیت بالا
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // تنظیمات کیفیت بالا
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // محاسبه موقعیت برای قرارگیری در وسط
        int x = (width - newWidth) / 2;
        int y = (height - newHeight) / 2;

        // رسم تصویر با اندازه جدید در موقعیت مرکزی
        g2d.drawImage(originalImage, x, y, newWidth, newHeight, null);
        g2d.dispose();

        // اعمال تصویر جدید روی JLabel
        label.setIcon(new ImageIcon(resizedImage));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
    }

    // نسخه ساده‌تر بدون نیاز به حفظ نسبت ابعاد
    public static void resizeLabelImage(JLabel label, Image originalImage, int width, int height) {
        resizeLabelImage(label, originalImage, width, height, false);
    }
}