package view.components;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public abstract class BaseBackgroundPanel extends JPanel {
    private final Image backgroundImage;
    private final boolean disableBackground; // ⚠️ اضافه شده

    public BaseBackgroundPanel(boolean isInMenu, boolean disableBackground) {
        this.disableBackground = disableBackground;

        // بارگذاری تصویر فقط وقتی لازم باشد
        if (!disableBackground) {
            String imagePath = "/view/assets/image/background.png";
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
            this.backgroundImage = icon.getImage();
        } else {
            this.backgroundImage = null;
        }

        setLayout(new BorderLayout());
        setOpaque(false); // تا بشه روی پس‌زمینه ترسیم کرد

        add(new TopIconPanel(isInMenu), BorderLayout.NORTH);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!disableBackground && backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // وقتی پس‌زمینه غیرفعال است، رنگ مشکی ساده بزن
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public abstract void initializeComponents();
}
