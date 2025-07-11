package view.screens;

import controller.NavigationManager;
import controller.UserManager;
import model.User;
import view.components.BaseBackgroundPanel;
import view.styles.GameStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class UserPanelScreen extends BaseBackgroundPanel {
    public static final String name = "USER_PANEL";
    private static final Dimension PROGRESS_BAR_SIZE = new Dimension(250, 16);
    private static final Dimension CARD_PANEL_SIZE = new Dimension(500, 160);
    private static final Color GLASS_BG_COLOR = new Color(255, 255, 255, 40);

    public UserPanelScreen() {
        super(false, false);
        setName(name);
        initializeComponents();
    }

    @Override
    public void initializeComponents() {
        if (!UserManager.isLoggedIn() || UserManager.getCurrentUser() == null) {
            NavigationManager.goBack();
            return;
        }

        add(createGlassMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createGlassMainPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        // ساخت پنل شیشه‌ای وسط صفحه
        JPanel glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setComposite(AlphaComposite.SrcOver.derive(0.85f));
                g2d.setColor(GLASS_BG_COLOR);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.dispose();
            }
        };
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setOpaque(false);
        glassPanel.setBorder(new EmptyBorder(25, 40, 10, 40));
        glassPanel.setMaximumSize(new Dimension(600, 300));

        // تیتر
        JLabel title = GameStyle.create3DTitrLabel("User Panel");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassPanel.add(title);
        glassPanel.add(Box.createVerticalStrut(10));

        // اطلاعات کاربر
        glassPanel.add(createUserInfoPanel());
        glassPanel.add(Box.createVerticalStrut(10));

        // آمار
        glassPanel.add(createStatsPanel());
        glassPanel.add(Box.createVerticalStrut(10));

        // دکمه خروج
        glassPanel.add(createLogoutButton());

        wrapper.add(glassPanel);
        return wrapper;
    }

    private JPanel createUserInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        addProfilePicture(panel);
        addUserInfoLabels(panel);
        addLevelProgressBar(panel);

        return panel;
    }

    private void addProfilePicture(JPanel panel) {
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(
                    getClass().getResource("/view/assets/images/icons/user_login.png")
            ));
            Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaled));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(label);
        } catch (NullPointerException e) {
            System.err.println("User icon not found at: /view/assets/image/icons/user_login.png");
        }
    }

    private void addUserInfoLabels(JPanel panel) {
        User user = UserManager.getCurrentUser();
        if (user == null) return;

        panel.add(createLabel(user.getUsername(), new Font("Segoe UI", Font.BOLD, 20), Color.WHITE));
        panel.add(Box.createVerticalStrut(2));
        panel.add(createLabel("Level: " + user.getLevel(), new Font("Segoe UI", Font.PLAIN, 14), new Color(200, 200, 200)));
    }

    private void addLevelProgressBar(JPanel panel) {
        User user = UserManager.getCurrentUser();
        if (user == null) return;

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(user.getLevel());
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 200, 150));
        progressBar.setBackground(new Color(40, 40, 40));
        progressBar.setPreferredSize(PROGRESS_BAR_SIZE);
        progressBar.setMaximumSize(PROGRESS_BAR_SIZE);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(5));
        panel.add(progressBar);
    }

    private JPanel createStatsPanel() {
        User user = UserManager.getCurrentUser();
        if (user == null) return new JPanel();

        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 10));
        panel.setOpaque(false);
        panel.setMaximumSize(CARD_PANEL_SIZE);

        panel.add(createStatCard("Score", String.valueOf(user.getScore()), new Color(255, 215, 0)));
        panel.add(createStatCard("Wins", String.valueOf(user.getWins()), new Color(0, 200, 150)));
        panel.add(createStatCard("Losses", String.valueOf(user.getLosses()), new Color(220, 50, 50)));
        panel.add(createStatCard("Rank", "#" + calculateRank(user), new Color(150, 150, 255)));

        return panel;
    }

    private String calculateRank(User user) {
        int score = user.getScore();
        if (score > 1000) return "1";
        if (score > 500) return "2";
        if (score > 200) return "3";
        return "N/A";
    }

    private JPanel createStatCard(String title, String value, Color valueColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(40, 40, 40, 180));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        card.add(createLabel(title, new Font("Segoe UI", Font.PLAIN, 10), new Color(180, 180, 180)));
        card.add(Box.createVerticalStrut(5));
        card.add(createLabel(value, new Font("Segoe UI", Font.BOLD, 13), valueColor));

        return card;
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JPanel createLogoutButton() {
        JButton logoutButton = new JButton("Log Out");
        logoutButton.setPreferredSize(new Dimension(120, 20));
        logoutButton.setBackground(new Color(200, 80, 80));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 10));
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        logoutButton.addActionListener(_ -> {
            UserManager.logout();
            NavigationManager.showPanel("HOME");
        });

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(logoutButton);
        return panel;
    }
}
