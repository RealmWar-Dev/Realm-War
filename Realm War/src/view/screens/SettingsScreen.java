package view.screens;

import controller.NavigationManager;
import view.components.BaseBackgroundPanel;
import view.components.Buttons;
import view.components.TopIconPanel;
import view.styles.Fade;
import view.styles.GameStyle;

import javax.swing.*;
import java.awt.*;

public class SettingsScreen extends BaseBackgroundPanel {
    public static String name = "SETTINGS";

    public SettingsScreen() {
        super(false, false);
        initializeComponents();
    }

    @Override
    public void initializeComponents() {
        setName(name);
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        panel.add(creatTitle(c), c);

        panel.add(creatButtonPanel(c), c);


        add(panel, BorderLayout.CENTER);
    }

    private JPanel creatButtonPanel(GridBagConstraints c) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        JButton soundSettingsButton = new Buttons.SimpleButton("Sound Settings");
        soundSettingsButton.addActionListener(_ -> NavigationManager.showPanel(SettingsScreen.name));
        buttonPanel.add(soundSettingsButton, gbc);

        buttonPanel.setOpaque(false);
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 0, 0);
        return buttonPanel;
    }


    private JLabel creatTitle(GridBagConstraints c) {
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(40, 0, 30, 0);
        return GameStyle.create3DTitrLabel("Settings");
    }

}
