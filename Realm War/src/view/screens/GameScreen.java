package view.screens;

import controller.NavigationManager;
import view.components.Buttons;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {


    public GameScreen() {
        setLayout(new BorderLayout());
        setBackground(new Color(25, 25, 25)); // پس‌زمینه کلی تیره

        // نوار بالا
        JPanel topBar = createTopBar();
        add(topBar, BorderLayout.NORTH);

        // پنل وسط شامل نقشه و نوار کناری
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JPanel mapPanel = createMapPlaceholder();
        centerPanel.add(mapPanel, BorderLayout.CENTER);

        JPanel sidePanel = createSidePanel();
        //sidePanel.setVisible(false);//مخفی کردن این پنل در نمایش اولیه
        centerPanel.add(sidePanel, BorderLayout.EAST);
        // TODO: در sidePanel باید اطلاعات دقیق خانه انتخاب شده نمایش داده شود
        // همچنین دکمه‌ها باید به عملکردهای واقعی بازی وصل شوند (ساخت نیرو، ارتقا و پایان نوبت)

        add(centerPanel, BorderLayout.CENTER);

        // نوار پایین (دکمه برگشت)
        JPanel bottomBar = createBottomBar();
        add(bottomBar, BorderLayout.SOUTH);
        // TODO: دکمه برگشت باید رویداد کلیک داشته باشد و به منوی اصلی بازگردد
    }

    private JPanel createTopBar() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(40, 40, 40));
        panel.setPreferredSize(new Dimension(0, 50));
        panel.setLayout(new BorderLayout());

        // TODO: اضافه کردن وضعیت بازی تا به الان در وسط این پنل


        // TODO: اضافه کردن تایمر و نوبت بازیکن در سمت راست پنل


        return panel;
    }

    private JPanel createMapPlaceholder() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600, 600));
        panel.setBackground(Color.GRAY); // نقشه هنوز ساخته نشده
        panel.setBorder(BorderFactory.createTitledBorder("Map"));

        // TODO: این پنل باید به پنل نقشه واقعی بازی تبدیل شود که شامل بلاک‌ها و قابلیت انتخاب خانه‌ها باشد
        // همچنین باید قابلیت کلیک روی خانه‌ها و انتخاب آنها اضافه شود


        return panel;
    }

    private JPanel createSidePanel() {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(220, 600));
        panel.setBackground(new Color(50, 50, 50));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JLabel infoLabel = new JLabel("Tile Info:");
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JButton buildBtn = new JButton("Build Unit");
        buildBtn.setPreferredSize(new Dimension(100, 50));
        JButton upgradeBtn = new JButton("Upgrade");
        upgradeBtn.setPreferredSize(new Dimension(100, 50));
        JButton endTurnBtn = new JButton("End Turn");
        endTurnBtn.setPreferredSize(new Dimension(100, 50));

        buildBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        upgradeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        endTurnBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(infoLabel);
        panel.add(buildBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(upgradeBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(endTurnBtn);

        // TODO: اینجا باید Event Listener برای دکمه‌ها اضافه شود که عملیات مورد نظر را اجرا کنند
        // همچنین infoLabel باید اطلاعات خانه انتخاب شده را به‌روز کند

        return panel;
    }

    private JPanel createBottomBar() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 30, 30));
        panel.setPreferredSize(new Dimension(0, 80)); // ارتفاع بیشتر برای جا دادن کامل دکمه
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // استفاده از BoxLayout عمودی

        JButton backButton = new JButton("← Back to Menu");
        backButton.setMaximumSize(new Dimension(200, 50)); // دکمه حداکثر اندازه
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(50, 50, 50));

        // فاصله‌ی بالا و پایین
        panel.add(Box.createVerticalStrut(10));
        panel.add(backButton);
        panel.add(Box.createVerticalStrut(10));

        backButton.addActionListener(_ -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to leave the game?\n" +
                            "Leaving now will be considered a loss for you and a victory for your opponent.",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                // TODO: ثبت باخت بازیکن فعلی و برد حریف
                NavigationManager.showPanel("HOME");
            }
        });

        return panel;
    }

}
