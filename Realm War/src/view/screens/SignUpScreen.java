package view.screens;

import controller.NavigationManager;
import controller.UserManager;
import database.DatabaseManager;
import model.User;
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
    private static final Color ERROR_COLOR = new Color(0xFF5252);           // قرمز روشن
    private static final Color SUCCESS_COLOR = new Color(0x00C853);         // سبز روشن
    private static final Color BUTTON_PRIMARY_COLOR = new Color(0x007AFF);  // آبی نئونی اصلی
    private static final Color BUTTON_SECONDARY_COLOR = new Color(0x3A3A3A); // خاکستری تیره
    private static final Color TEXT_COLOR = new Color(220, 220, 220);        // خاکستری خیلی روشن برای خوانایی بالا
    private static final Color GLASS_BG_COLOR = new Color(255, 255, 255, 40); // سفید با شفافیت


    // فونت‌ها
    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 18);
    private static final Font ERROR_FONT = new Font("Segoe UI Emoji", Font.PLAIN, 12);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    // ابعاد
    private static final Dimension BUTTON_SIZE_LARGE = new Dimension(180, 45);
    private static final Dimension BUTTON_SIZE_SMALL = new Dimension(120, 40);
    private static final Dimension FIELD_SIZE = new Dimension(220, 35);

    private UserNameField userNameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private JLabel errorLabel;
    private User user;

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
        // پنل والد اصلی
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        // پنل شیشه‌ای دقیق روی محتوای فرم
        JPanel glassPanel = getJPanel();

        // تیتر
        JLabel title = new JLabel("Create Account");
        title.setFont(TITLE_FONT);
        title.setForeground(TEXT_COLOR);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setBorder(new EmptyBorder(10, 0, 20, 0));
        glassPanel.add(title);
        glassPanel.add(Box.createVerticalStrut(15));

        // فرم
        JPanel formPanel = createFormPanel();
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassPanel.add(formPanel);
        glassPanel.add(Box.createVerticalStrut(10));

        // پیام خطا
        errorLabel = new JLabel();
        errorLabel.setFont(ERROR_FONT);
        errorLabel.setForeground(ERROR_COLOR);
        errorLabel.setVisible(false);
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassPanel.add(errorLabel);
        glassPanel.add(Box.createVerticalStrut(15));

        // دکمه‌ها
        JPanel buttons = createButtonPanel();
        buttons.setAlignmentX(Component.CENTER_ALIGNMENT);
        glassPanel.add(buttons);

        // افزودن پنل شیشه‌ای به وسط صفحه
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        wrapper.add(glassPanel, gbc);

        return wrapper;
    }

    private JPanel getJPanel() {
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
        glassPanel.setBorder(new EmptyBorder(25, 35, 25, 35)); // حاشیه نرم درون پنل
        return glassPanel;
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
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

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

        String username = userNameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirm = new String(confirmPasswordField.getPassword()).trim();


        user = UserManager.register(errors, username, password, confirm);
        if (user != null) {
            // موفقیت
            errorLabel.setForeground(SUCCESS_COLOR);
            errorLabel.setText("Registration successful!");

            Timer timer = new Timer(1500, _ -> NavigationManager.showPanel(HomeScreen.name));
            timer.setRepeats(false);
            timer.start();
        } else {
            // خطا
            errorLabel.setForeground(ERROR_COLOR);
            errorLabel.setText(errors.toString());
        }

    }

}
