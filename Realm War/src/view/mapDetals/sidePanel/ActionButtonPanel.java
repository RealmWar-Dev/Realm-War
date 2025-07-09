package view.mapDetals.sidePanel;

import javax.swing.*;
import java.awt.*;

public class ActionButtonPanel extends JPanel {
    public ActionButtonPanel(Runnable onBuildUnit, Runnable onUpgrade, Runnable onEndTurn) {
        setLayout(new GridLayout(3, 1, 10, 10));
        setOpaque(false);
        setBorder(BorderFactory.createTitledBorder("Actions"));

        JButton buildButton = new JButton("Build Unit");
        JButton upgradeButton = new JButton("Upgrade");
        JButton endTurnButton = new JButton("End Turn");

        buildButton.addActionListener(e -> onBuildUnit.run());
        upgradeButton.addActionListener(e -> onUpgrade.run());
        endTurnButton.addActionListener(e -> onEndTurn.run());

        add(buildButton);
        add(upgradeButton);
        add(endTurnButton);
    }
}
