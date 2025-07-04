package view.screens;

import controller.NavigationManager;
import controller.UserManager;
import database.DatabaseManager;
import model.User;
import view.components.BaseBackgroundPanel;
import view.components.TopIconPanel;
import view.styles.GameStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class UserPanelScreen extends BaseBackgroundPanel {
    public static final String name = "USER_PANEL";
    private static final Dimension PROGRESS_BAR_SIZE = new Dimension(200, 15);
    private static final int BORDER_PADDING = 20;
    private static final Dimension STAT_PANEL_SIZE = new Dimension(500, 160);

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

        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(BORDER_PADDING, BORDER_PADDING, BORDER_PADDING, BORDER_PADDING));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 0, 10, 0);

        gbc.gridy = 0;
        panel.add(GameStyle.create3DTitrLabel("User Panel"), gbc);

        gbc.gridy++;
        panel.add(createUserInfoPanel(), gbc);

        gbc.gridy++;
        panel.add(createStatsPanel(), gbc);

        gbc.gridy++;
        panel.add(createLogoutButton(), gbc);

        return panel;
    }

    private JPanel createLogoutButton() {
        JButton logoutButton = new JButton("Log Out");
        logoutButton.setPreferredSize(new Dimension(180, 30));
        logoutButton.setBackground(new Color(193, 87, 87));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setFont(new Font("Tahoma", Font.BOLD, 14));
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(_ -> {
            DatabaseManager.updateUserStats(UserManager.getCurrentUser());
            UserManager.logout();
            NavigationManager.showPanel("HOME");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(logoutButton);
        return buttonPanel;
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
                    getClass().getResource(TopIconPanel.USER_LOGIN_ICON_PATH)
            ));
            Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            JLabel label = new JLabel(new ImageIcon(scaled));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);

            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            panel.add(label);
        } catch (NullPointerException e) {
            System.err.println("User icon not found at: " + TopIconPanel.USER_LOGIN_ICON_PATH);
        }
    }

    private void addUserInfoLabels(JPanel panel) {
        User currentUser = UserManager.getCurrentUser();
        if (currentUser == null) return;

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(createLabel(currentUser.getUsername(),
                new Font("Tahoma", Font.BOLD, 18), Color.WHITE));
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(createLabel("Level: " + currentUser.getLevel(),
                new Font("Tahoma", Font.PLAIN, 14), new Color(200, 200, 200)));
    }

    private void addLevelProgressBar(JPanel panel) {
        User currentUser = UserManager.getCurrentUser();
        if (currentUser == null) return;

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(currentUser.getLevel());
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(0, 200, 150));
        progressBar.setBackground(new Color(50, 50, 50));
        progressBar.setPreferredSize(PROGRESS_BAR_SIZE);
        progressBar.setMaximumSize(PROGRESS_BAR_SIZE);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(progressBar);
    }

    private JPanel createStatsPanel() {
        User currentUser = UserManager.getCurrentUser();
        if (currentUser == null) return new JPanel();

        JPanel panel = new JPanel(new GridLayout(2, 2, 15, 15));
        panel.setOpaque(false);
        panel.setPreferredSize(STAT_PANEL_SIZE);

        panel.add(createStatCard("Score", String.valueOf(currentUser.getScore()),
                new Color(255, 215, 0)));
        panel.add(createStatCard("Wins", String.valueOf(currentUser.getWins()),
                new Color(0, 200, 150)));
        panel.add(createStatCard("Losses", String.valueOf(currentUser.getLosses()),
                new Color(220, 50, 50)));
        panel.add(createStatCard("Rank", "#" + calculateRank(currentUser),
                new Color(150, 150, 255)));

        return panel;
    }

    private String calculateRank(User user) {
        // منطق محاسبه رتبه کاربر
        int score = user.getScore();
        if (score > 1000) return "1";
        if (score > 500) return "2";
        if (score > 200) return "3";
        return "N/A";
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(30, 30, 30, 200));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        panel.add(createLabel(title, new Font("Tahoma", Font.PLAIN, 12),
                new Color(180, 180, 180)));
        panel.add(Box.createRigidArea(new Dimension(0, 2)));
        panel.add(createLabel(value, new Font("Tahoma", Font.BOLD, 16), color));

        return panel;
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}