package view.styles;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Objects;

public abstract class GameStyle {
    // رنگ‌های تم فانتزی شبانه بهبود یافته
    public static final Color PRIMARY_COLOR     = new Color(25, 40, 65, 140);    // آبی تیره با عمق بیشتر
    public static final Color HIGHLIGHT_COLOR   = new Color(120, 170, 255, 180); // آبی روشن‌تر و جادویی‌تر برای هاور
    public static final Color TEXT_COLOR        = new Color(240, 250, 255, 255); // سفید آبی درخشان و خواناتر
    public static final Color DARKER_PRIMARY    = new Color(10, 25, 50, 220);    // آبی خیلی تیره برای بخش‌های پس‌زمینه یا عمقی
    public static final Color TITLE_COLOR       = new Color(180, 220, 255, 255); // آبی روشن‌تر با حس فانتزی برای عنوان‌ها
    public static final Color TEXT_SHADOW       = new Color(40, 60, 120, 150);

    // فونت بازی
    private static final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 50);



    public static JPanel createBackGroundPanel(String path) {
        Image backGroundImage = (new ImageIcon(Objects.requireNonNull(GameStyle.class.getResource(path)))).getImage();
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.drawImage(backGroundImage, 0, 0, getWidth(), getHeight(), null);
            }
        };

    }


    public static JLabel create3DTitrLabel(String text) {
        int depth = 5;
        JLabel label = new JLabel(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // استفاده از فونت تنظیم شده
                g2d.setFont(TITLE_FONT);
                FontMetrics fm = g2d.getFontMetrics();

                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

                // رسم سایه‌های سه بعدی
                g2d.setColor(TEXT_SHADOW);
                for (int i = 1; i <= depth; i++) {
                    g2d.drawString(getText(), x + i, y + i);
                }

                // رسم متن اصلی;
                g2d.setColor(TITLE_COLOR);
                g2d.drawString(getText(), x, y);
            }

            @Override
            public Dimension getPreferredSize() {
                FontMetrics fm = getFontMetrics(TITLE_FONT);
                return new Dimension(
                        fm.stringWidth(getText()) + depth + 20, // 20 پدینگ اضافه
                        fm.getHeight() + depth + 20
                );
            }

            @Override
            public Dimension getMinimumSize() {
                return getPreferredSize();
            }
        };

        // تنظیم فونت اگر ارائه شده باشد
        label.setFont(TITLE_FONT);

        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
}