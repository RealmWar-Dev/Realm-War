package view.screens;

import controller.MatchRoomManager;
import controller.NavigationManager;
import controller.UserManager;
import model.User;
import view.components.BaseBackgroundPanel;
import view.components.PasswordField;
import view.components.UserNameField;
import view.styles.GameStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MatchRoomScreen extends BaseBackgroundPanel {
    public static final String name = "MATCH_ROOM";

    private static final Color TEXT_COLOR = new Color(240, 240, 240);
    private static final Color ACCENT_COLOR = new Color(30, 136, 229);
    private static final Color SUCCESS_COLOR = new Color(67, 160, 71);
    private static final Color ERROR_COLOR = new Color(229, 57, 53);
    private static final Color SECONDARY_COLOR = new Color(120, 144, 156);
    private static final Color GLASS_COLOR = new Color(255, 255, 255, 40);

    private static final Font LABEL_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 15);
    private static final Font BUTTON_FONT = new Font("Segoe UI Emoji", Font.BOLD, 15);
    private static final Font STATUS_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 14);

    private MatchRoomManager matchRoom;
    private JLabel player1Label, player2Label, statusLabel;
    private JButton joinButton, startButton, signUpButton;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public MatchRoomScreen() {
        super(false, false);
        setName(name);
        initializeComponents();
        setupUI();
    }

    @Override
    public void initializeComponents() {
        User currentUser = UserManager.getCurrentUser();
        matchRoom = new MatchRoomManager(currentUser);

        player1Label = createLabel("Player 1: " + (currentUser != null ? currentUser.getUsername() : "N/A"), LABEL_FONT);
        player2Label = createLabel("Player 2: (Waiting...)", LABEL_FONT);
        statusLabel = createLabel(" ", STATUS_FONT);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        usernameField = new UserNameField();
        passwordField = new PasswordField();

        joinButton = createMagicButton("JOIN MATCH", ACCENT_COLOR, _ -> tryJoin(), new Dimension(280, 42));
        startButton = createMagicButton("START GAME", SUCCESS_COLOR, _ -> startGame(), new Dimension(280, 42));
        startButton.setEnabled(false);
        signUpButton = createMagicButton("SIGN UP", SECONDARY_COLOR, _ -> NavigationManager.showPanel(SignUpScreen.name, false), new Dimension(280, 36));
    }

    private void tryJoin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("⚠ Please fill in all fields.");
            return;
        }

        User secondUser = UserManager.login(username, password);
        if (secondUser != null) {
            if (matchRoom.join(secondUser)) {
                updateUIAfterJoin(secondUser);
            } else {
                showError("You can't play with yourself.");
            }
        } else {
            showError("❌ Invalid username or password.");
        }
    }

    private void updateUIAfterJoin(User secondUser) {
        player2Label.setText("Player 2: " + secondUser.getUsername());
        statusLabel.setText("✅ Ready to start!");
        statusLabel.setForeground(SUCCESS_COLOR);

        usernameField.setEnabled(false);
        passwordField.setEnabled(false);
        joinButton.setEnabled(false);
        joinButton.setText("✔ Joined");
        joinButton.setBackground(SUCCESS_COLOR);
        startButton.setEnabled(true);
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setForeground(ERROR_COLOR);
    }

    private void startGame() {
        // TODO: نمایش صفحه بازی
        // NavigationManager.showPanel(GameScreen.name, true);
    }

    private void setupUI() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        JPanel glassPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(GLASS_COLOR);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2d.dispose();
            }
        };
        glassPanel.setOpaque(false);
        glassPanel.setLayout(new BoxLayout(glassPanel, BoxLayout.Y_AXIS));
        glassPanel.setBorder(new EmptyBorder(25, 30, 10, 30));
        glassPanel.setPreferredSize(new Dimension(600, 500));

        // تیتیر
        JLabel title = GameStyle.create3DTitrLabel("MATCH ROOM");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // بازیکنان
        JPanel playersPanel = new JPanel();
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        playersPanel.setOpaque(false);
        playersPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        player1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        playersPanel.add(player1Label);
        playersPanel.add(Box.createVerticalStrut(6));
        playersPanel.add(player2Label);

        // separator
        JSeparator separator = new JSeparator();
        separator.setMaximumSize(new Dimension(300, 10));
        separator.setForeground(new Color(255, 255, 255, 100));
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);

        // دکمه‌ها
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(startButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(signUpButton);

        // اضافه کردن اجزا
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(title);
        glassPanel.add(Box.createVerticalStrut(20));
        glassPanel.add(playersPanel);
        glassPanel.add(Box.createVerticalStrut(25));
        glassPanel.add(createInputPanel("username :", usernameField));
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(createInputPanel("password :", passwordField));
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(statusLabel);
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(joinButton);
        glassPanel.add(Box.createVerticalStrut(10));
        glassPanel.add(separator);
        glassPanel.add(Box.createVerticalStrut(20));
        glassPanel.add(buttonPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        wrapper.add(glassPanel, gbc);
        add(wrapper, BorderLayout.CENTER);
    }

    private JPanel createInputPanel(String labelText, JComponent inputField) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        JLabel label = createLabel(labelText, LABEL_FONT);

        inputField.setPreferredSize(new Dimension(250, 30));
        inputField.setMaximumSize(new Dimension(250, 30));
        inputField.setMinimumSize(new Dimension(250, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(inputField, gbc);

        return panel;
    }



    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JButton createMagicButton(String text, Color bgColor, java.awt.event.ActionListener action, Dimension size) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color fill = getModel().isPressed() ? bgColor.darker()
                        : getModel().isRollover() ? bgColor.brighter()
                        : bgColor;

                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Color.WHITE);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(BUTTON_FONT);
        button.setForeground(TEXT_COLOR);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(size);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.repaint(); }
            public void mouseExited(MouseEvent e) { button.repaint(); }
        });

        button.addActionListener(action);
        return button;
    }
}
