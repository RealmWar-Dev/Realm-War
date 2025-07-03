package view.screens;


import controller.GameController;

import controller.NavigationManager;
import controller.UserManager;
import view.components.*;
import view.styles.GameStyle;

import javax.swing.*;
import java.awt.*;


public class HomeScreen extends BaseBackgroundPanel {
    public static JButton userOrLoginButton;

    public static String name = "HOME";
    public HomeScreen() {
        super(true ,false);
        setName(name);
        initializeComponents();
    }

    @Override
    public void initializeComponents() {

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);

        // پنل مرکزی شامل Welcome و دکمه‌ها
        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 20, 0);
        centerPanel.add(GameStyle.create3DTitrLabel("HOME"), gbc);

        gbc.gridy++;
        centerPanel.add(createButtonLabel(), gbc);

        panel.add(centerPanel, BorderLayout.CENTER);

        add(panel, BorderLayout.CENTER);
    }


    private JPanel createButtonLabel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0); // کاهش فاصله بین دکمه‌ها
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;


        gbc.gridy = 0;
        JButton startButton = new Buttons.SimpleButton("Start");
        startButton.addActionListener(_ -> {
            if (UserManager.isLoggedIn()){
            }
            else {
                JOptionPane.showMessageDialog(null , "please Login Then Start" , "You are not logged in !" , JOptionPane.ERROR_MESSAGE );
            }
        });
        buttonPanel.add(startButton, gbc);

        gbc.gridy++;
        userOrLoginButton = UserManager.isLoggedIn() ? new Buttons.SimpleButton("User") : new Buttons.SimpleButton("Login");
        userOrLoginButton.addActionListener(_ -> {
                if (!UserManager.isLoggedIn())
                    NavigationManager.showPanel(LoginScreen.name , false );
                else
                    NavigationManager.showPanel(UserPanelScreen.name );
                });
        buttonPanel.add(userOrLoginButton, gbc);

        gbc.gridy++;
        JButton settingsButton = new Buttons.SimpleButton("Settings");
        settingsButton.addActionListener(_ -> NavigationManager.showPanel(SettingsScreen.name));
        buttonPanel.add(settingsButton, gbc);

        gbc.gridy++;
        JButton exitButton = new Buttons.SimpleButton("Exit");
        exitButton.addActionListener(_ -> System.exit(0));
        buttonPanel.add(exitButton, gbc);

        buttonPanel.setBackground(new Color(255, 255, 255, 0));
        buttonPanel.setOpaque(false);

        return buttonPanel;
    }


}
