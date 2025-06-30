package view.screens;

import controller.NavigationManager;
import controller.GameController;
import controller.UserManagement;
import view.components.*;
import view.styles.GameStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class SignUpScreen extends BaseBackgroundPanel {
    public static final String name = "SIGNUP";

    // رنگ‌ها
    private static final Color ERROR_COLOR = new Color(255, 80, 80);
    private static final Color SUCCESS_COLOR = new Color(0, 200, 83);
    private static final Color BUTTON_PRIMARY_COLOR = new Color(0, 122, 255);
    private static final Color BUTTON_SECONDARY_COLOR = new Color(71, 71, 81);

    // فونت‌ها
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 18);
    private static final Font ERROR_FONT = new Font("Segoe UI", Font.PLAIN, 12);

    // ابعاد
    private static final Dimension BUTTON_SIZE_LARGE = new Dimension(180, 45);
    private static final Dimension BUTTON_SIZE_SMALL = new Dimension(120, 40);
    private static final Dimension FIELD_SIZE = new Dimension(220, 35);

    private UserNameField userNameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private JLabel errorLabel;

    public SignUpScreen() {
        super(false, false);
        setName(name);
        initializeComponents();
    }

    @Override
    public void initializeComponents() {
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(8, 8, 16, 8);

        // عنوان
        mainPanel.add(GameStyle.create3DTitrLabel("Create Account"), gbc);

        // فیلدهای فرم
        gbc.insets = new Insets(4, 8, 4, 8);
        mainPanel.add(createFormPanel(), gbc);

        // پیام خطا
        errorLabel = new JLabel();
        errorLabel.setFont(ERROR_FONT);
        errorLabel.setForeground(ERROR_COLOR);
        errorLabel.setVisible(false);
        gbc.insets = new Insets(8, 8, 16, 8);
        mainPanel.add(errorLabel, gbc);

        // دکمه‌ها
        gbc.insets = new Insets(8, 8, 8, 8);
        mainPanel.add(createButtonPanel(), gbc);

        return mainPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        userNameField = new UserNameField();
        passwordField = new PasswordField();
        confirmPasswordField = new PasswordField();

        // تنظیمات سایز و EchoChar
        Arrays.asList(userNameField, passwordField, confirmPasswordField).forEach(field -> {
            field.setPreferredSize(FIELD_SIZE);
            field.setMinimumSize(FIELD_SIZE);
            if (field instanceof PasswordField) {
                ((PasswordField) field).setEchoChar('*');
            }
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

        // Confirm Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(createFormLabel("Confirm:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(confirmPasswordField, gbc);

        return formPanel;
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(new Color(220, 220, 220));
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);

        JButton loginButton = createMagicButton("Login", BUTTON_SECONDARY_COLOR, BUTTON_SIZE_SMALL);
        loginButton.addActionListener(_ -> NavigationManager.showPanel(LoginScreen.name, false));

        JButton signUpButton = createMagicButton("Sign Up", BUTTON_PRIMARY_COLOR, BUTTON_SIZE_LARGE);
        signUpButton.addActionListener(_ -> processRegistration());

        panel.add(loginButton);
        panel.add(signUpButton);
        return panel;
    }

    private JButton createMagicButton(String text, Color backgroundColor, Dimension size) {
        Font font = new Font("Segoe UI", Font.BOLD, 14);
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

        button.setFont(font);
        button.setPreferredSize(size);
        button.setForeground(new Color(220, 220, 220));
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

    private void processRegistration() {
        errorLabel.setVisible(true);
        StringBuilder errors = new StringBuilder("<html>");
        boolean hasError = false;

        if (!isValidUserName()) {
            errors.append("• Invalid username<br>");
            hasError = true;
        }
        if (!isValidUserPassword()) {
            errors.append("• Password required<br>");
            hasError = true;
        }
        if (!Arrays.equals(passwordField.getPassword(), confirmPasswordField.getPassword())) {
            errors.append("• Passwords don't match<br>");
            hasError = true;
        }

        if (hasError) {
            errorLabel.setForeground(ERROR_COLOR);
            errorLabel.setText(errors.toString());
        } else {
            errorLabel.setForeground(SUCCESS_COLOR);
            errorLabel.setText("Registration successful!");

            GameController.setCurrentUser(new UserManagement(
                    userNameField.getText().trim(),
                    passwordField.getPassword()
            ));

            Timer timer = new Timer(1500, _ -> NavigationManager.showPanel(HomeScreen.name));
            timer.setRepeats(false);
            timer.start();
        }
    }

    private boolean isValidUserPassword() {
        return passwordField.getPassword().length > 0;
    }

    private boolean isValidUserName() {
        String username = userNameField.getText().trim();
        return !username.isEmpty() && !UserManagement.isUserNameExist(username);
    }
}
