package view.components;

import view.styles.GameStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.Objects;

public abstract class Buttons {
    // رنگ‌های پیش‌فرض
    protected static final Color PRIMARY_COLOR = GameStyle.PRIMARY_COLOR;
    protected static final Color HIGHLIGHT_COLOR = GameStyle.HIGHLIGHT_COLOR;
    protected static final Color DARKER_PRIMARY =GameStyle.DARKER_PRIMARY;
    protected static final Color TEXT_COLOR = GameStyle.TEXT_COLOR;

    // فونت پیش‌فرض
    protected static Font gameFont = new Font("Arial", Font.PLAIN, 12);

    /**
     * ایجاد دکمه ساده با متن
     *
     * @param text متن دکمه
     * @return دکمه طراحی شده
     */
    public static JButton createSimpleButton(String text) {

        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // طراحی پس‌زمینه دکمه
                if (getModel().isPressed()) {
                    g2.setColor(DARKER_PRIMARY);
                } else if (getModel().isRollover()) {
                    g2.setColor(HIGHLIGHT_COLOR);
                } else {
                    g2.setColor(PRIMARY_COLOR);
                }

                // شکل گرد برای دکمه
                int arc = 70;
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

                // حاشیه دکمه
                g2.setColor(HIGHLIGHT_COLOR);
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

                // متن دکمه
                g2.setColor(TEXT_COLOR);
                g2.setFont(gameFont.deriveFont(Font.BOLD, 30));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(getText(), x, y);

                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(340, 60);
            }
        };

        // تنظیمات استایل
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // تنظیم فونت مناسب
        button.setFont(gameFont.deriveFont(Font.BOLD, 20)); // اندازه فونت را کاهش دهید

        return button;
    }

    public static void setBackgroundButton(String iconPath, JButton button) {
        try {
            // بارگذاری تصویر با اندازه مناسب
            BufferedImage image = ImageIO.read(Objects.requireNonNull(Buttons.class.getResource(iconPath)));
            Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // اندازه ثابت کوچک
            button.setIcon(new ImageIcon(scaledImage));

        } catch (Exception e) {
            System.err.println("خطا در بارگذاری آیکون: " + iconPath);
            button.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
        }
    }

    public static class SimpleButton extends JButton {


        public SimpleButton(String text) {

            super(text);

            // تنظیمات استایل
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // تنظیم فونت مناسب
            setFont(gameFont.deriveFont(Font.BOLD, 20)); // اندازه فونت را کاهش دهید
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // طراحی پس‌زمینه دکمه
            if (getModel().isPressed()) {
                g2.setColor(DARKER_PRIMARY);
            } else if (getModel().isRollover()) {
                g2.setColor(HIGHLIGHT_COLOR);
            } else {
                g2.setColor(PRIMARY_COLOR);
            }

            // شکل گرد برای دکمه
            int arc = 70;
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

            // حاشیه دکمه
            g2.setColor(HIGHLIGHT_COLOR);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

            // متن دکمه
            g2.setColor(TEXT_COLOR);
            g2.setFont(gameFont.deriveFont(Font.BOLD, 30));
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
            g2.drawString(getText(), x, y);

            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(340, 60);
        }
    }

    public static class IconButton extends JButton {


        public IconButton(String iconPath , ActionListener action) {

            setText("");
            setFocusPainted(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            // تنظیم حاشیه صفر برای دکمه‌های آیکون
            setBorder(BorderFactory.createEmptyBorder());
            setMargin(new Insets(0, 0, 0, 0));
            // اضافه کردن اکشن
            if (action != null) {
                addActionListener(action);
            }

            setBackgroundButton(iconPath, this);
        }
    }

}