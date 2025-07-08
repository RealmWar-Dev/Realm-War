package view.screens;

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
import java.util.Arrays;

public class LoginScreen extends BaseBackgroundPanel {
    public static final String name = "LOGIN";

    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 18);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private static final Font ERROR_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 14);

    private static final Dimension FIELD_SIZE = new Dimension(250, 35);
    private static final Dimension BUTTON_SIZE_LARGE = new Dimension(180, 45);
    private static final Dimension BUTTON_SIZE_SMALL = new Dimension(120, 40);

    private static final Color ERROR_COLOR = new Color(0xFF5252);
    private static final Color SUCCESS_COLOR = new Color(0x00C853);
    private static final Color BUTTON_PRIMARY_COLOR = new Color(0x007AFF);
    private static final Color BUTTON_SECONDARY_COLOR = new Color(0x3A3A3A);
    private static final Color GLASS_BG_COLOR = new Color(255, 255, 255, 40);
    private static final Color TEXT_COLOR = new Color(220, 220, 220);

    private UserNameField userNameField;
    private PasswordField passwordField;
    private JTextPane errorLabel;
    protected User user;

    public LoginScreen() {
        super(false, false);
        setName(name);
        initializeComponents();
    }
    @Override
    public void initializeComponents() {
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

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
        glassPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // تیتر
        JLabel title = GameStyle.create3DTitrLabel("Login");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassPanel.add(title);
        glassPanel.add(Box.createVerticalStrut(20));

        // فرم ورود
        glassPanel.add(createFormPanel());
        glassPanel.add(Box.createVerticalStrut(10));

        // پیام خطا یا موفقیت
        glassPanel.add(createErrorPanel());
        glassPanel.add(Box.createVerticalStrut(15));

        // دکمه‌ها
        glassPanel.add(createButtonPanel());

        wrapper.add(glassPanel);
        return wrapper;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        userNameField = new UserNameField();
        passwordField = new PasswordField();
        passwordField.setEchoChar('*');

        Arrays.asList(userNameField, passwordField).forEach(field -> {
            field.setPreferredSize(FIELD_SIZE);
            field.setMinimumSize(FIELD_SIZE);
        });

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(createFormLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(userNameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(createFormLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordField, gbc);

        return formPanel;
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT_COLOR);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
    }

    private JPanel createErrorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 60));

        errorLabel = new JTextPane();
        errorLabel.setEditable(false);
        errorLabel.setOpaque(false);
        errorLabel.setFont(ERROR_FONT);
        errorLabel.setForeground(ERROR_COLOR);
        errorLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        errorLabel.setVisible(false);

        panel.add(errorLabel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);

        JButton signUpButton = createMagicButton("Sign Up", BUTTON_SECONDARY_COLOR, BUTTON_SIZE_SMALL);
        signUpButton.addActionListener(_ -> NavigationManager.showPanel(SignUpScreen.name, false));

        JButton loginButton = createMagicButton("Login", BUTTON_PRIMARY_COLOR, BUTTON_SIZE_LARGE);
        loginButton.addActionListener(_ -> processLogin());

        panel.add(signUpButton);
        panel.add(loginButton);
        return panel;
    }

    private JButton createMagicButton(String text, Color backgroundColor, Dimension size) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2d.setColor(backgroundColor.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(backgroundColor.brighter());
                } else {
                    g2d.setColor(backgroundColor);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(GameStyle.HIGHLIGHT_COLOR);
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(BUTTON_FONT);
        button.setPreferredSize(size);
        button.setForeground(TEXT_COLOR);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.repaint();
            }
        });

        return button;
    }

    private void processLogin() {
        String username = userNameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty()) {
            showError("⚠️ Please enter your username");
            return;
        }

        if (password.isEmpty()) {
            showError("⚠️ Please enter your password");
            return;
        }

        user = UserManager.login(username, password);
        if (user != null) {
            showSuccess();
            Timer timer = new Timer(1500, _ -> NavigationManager.showPanel(HomeScreen.name, false));
            timer.setRepeats(false);
            timer.start();
        } else {
            showError("❌ Invalid username or password");
        }
    }

    private void showError(String message) {
        errorLabel.setForeground(ERROR_COLOR);
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showSuccess() {
        errorLabel.setForeground(SUCCESS_COLOR);
        errorLabel.setText("✅ Login successful! Redirecting...");
        errorLabel.setVisible(true);
    }
}
