package view.components;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public abstract class BaseBackgroundPanel extends JPanel {
    private  final Image backgroundImage;

    public BaseBackgroundPanel(boolean isInMenu, boolean isInGame) {
        // بارگذاری تصویر پس‌زمینه
        String imagePath = "/view/assets/image/background.png";
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
        this.backgroundImage = icon.getImage();

        // تنظیمات اولیه پنل
        setLayout(new BorderLayout()); // یا هر Layout Manager دلخواه دیگر
        setOpaque(false); // برای نمایش پس‌زمینه

        add(new TopIconPanel(isInMenu, isInGame), BorderLayout.NORTH);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // رسم پس‌زمینه
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    // متدهای abstract که کلاس‌های فرزند باید پیاده‌سازی کنند
    public abstract void initializeComponents();
}