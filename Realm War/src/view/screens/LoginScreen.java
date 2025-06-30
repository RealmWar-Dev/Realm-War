package view.screens;

import controller.NavigationManager;
import view.components.*;
import view.styles.GameStyle;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class LoginScreen extends BaseBackgroundPanel {
    private PasswordField passwordField;
    private UserNameField userNameField;
    private JTextPane errorLabel;

    public static final String name = "LOGIN";
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 18);
    private static final Dimension FIELD_SIZE = new Dimension(250, 35);
    private static final Color ERROR_COLOR = new Color(255, 80, 80);
    private static final Color SUCCESS_COLOR = new Color(0, 255, 128);

    private static final Dimension BUTTON_SIZE_LARGE = new Dimension(180, 45);
    private static final Dimension BUTTON_SIZE_SMALL = new Dimension(120, 40);
    private static final Color BUTTON_PRIMARY_COLOR = new Color(0, 122, 255);
    private static final Color BUTTON_SECONDARY_COLOR = new Color(71, 71, 81);


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
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(0, 50, 50, 50));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 20);
        gbc.anchor = GridBagConstraints.CENTER;

        // عنوان
        gbc.gridwidth = 2;
        gbc.gridy = 0;
        panel.add(GameStyle.create3DTitrLabel("Login") ,  gbc);

        // فیلدهای فرم
        gbc.gridy++;
        panel.add(createFieldsPanel(), gbc);

        // پیام خطا
        gbc.gridy++;
        panel.add(createErrorPanel(), gbc);

        // دکمه‌ها
        gbc.gridy++;
        panel.add(createButtonPanel(), gbc);

        return panel;
    }


    private JPanel createFieldsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 20);
        gbc.anchor = GridBagConstraints.LINE_END;

        // فیلد نام کاربری
        userNameField = new UserNameField();
        userNameField.setPreferredSize(FIELD_SIZE);

        // فیلد رمز عبور
        passwordField = new PasswordField();
        passwordField.setPreferredSize(FIELD_SIZE);
        passwordField.setEchoChar('*');

        // اضافه کردن کامپوننت‌ها
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(createFormLabel("Username:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(userNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        panel.add(createFormLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        panel.add(passwordField, gbc);

        return panel;
    }

    private JPanel createErrorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(400, 60));

        errorLabel = new JTextPane();
        errorLabel.setEditable(false);
        errorLabel.setOpaque(false);
        errorLabel.setForeground(ERROR_COLOR);
        errorLabel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        errorLabel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        errorLabel.setVisible(false);

        panel.add(errorLabel, BorderLayout.CENTER);
        return panel;
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(new Color(220, 220, 220));
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        return label;
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

    private void showError(String message) {
        errorLabel.setForeground(ERROR_COLOR);
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void showSuccess(String message) {
        errorLabel.setForeground(SUCCESS_COLOR);
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panel.setOpaque(false);

        JButton loginButton = createMagicButton("Sign Up", BUTTON_SECONDARY_COLOR, BUTTON_SIZE_SMALL);
        loginButton.addActionListener(_ -> NavigationManager.showPanel(SignUpScreen.name, false));

        JButton signUpButton = createMagicButton("Login", BUTTON_PRIMARY_COLOR, BUTTON_SIZE_LARGE);
        signUpButton.addActionListener(_ -> processLogin());

        panel.add(loginButton);
        panel.add(signUpButton);
        return panel;
    }

    private void processLogin() {
        String username = userNameField.getText().trim();
        char[] password = passwordField.getPassword();

        if (username.isEmpty()) {
            showError("⚠️ Please enter your username");
            return;
        }

        if (password.length == 0) {
            showError("⚠️ Please enter your password");
            return;
        }

    /*boolean success = GameController.loginUser(username, password);
    if (success) {
        showSuccess("Login successful! Redirecting...");
        Timer timer = new Timer(1500, _ -> NavigationManager.showPanel(HomeScreen.NAME));
        timer.setRepeats(false);
        timer.start();
    } else {
        showError("Invalid username or password");
    }*/

        Arrays.fill(password, '0'); // پاکسازی رمز عبور از حافظه
    }

}
