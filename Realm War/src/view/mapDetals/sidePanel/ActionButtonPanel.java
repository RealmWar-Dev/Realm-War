package view.mapDetals.sidePanel;

import view.mapDetals.mapPanel.MapPanel;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ActionButtonPanel extends JPanel {
    private final JButton buildButton;
    private final JButton upgradeButton;
    private final JButton endTurnButton;
    private final Map<MapPanel.BlockState, Boolean[]> buttonEnablementRules;

    public ActionButtonPanel(Runnable onBuildUnit, Runnable onUpgrade, Runnable onEndTurn) {
        setLayout(new GridLayout(3, 1, 10, 10));
        setOpaque(false);
        setBorder(BorderFactory.createTitledBorder("Actions"));

        buildButton = new JButton("Build Unit");
        upgradeButton = new JButton("Upgrade");
        endTurnButton = new JButton("End Turn");

        buildButton.addActionListener(e -> onBuildUnit.run());
        upgradeButton.addActionListener(e -> onUpgrade.run());
        endTurnButton.addActionListener(e -> onEndTurn.run());

        add(buildButton);
        add(upgradeButton);
        add(endTurnButton);

        buttonEnablementRules = new HashMap<>();
        setupButtonRules();

        disableAllActionButtons();
    }

    private void setupButtonRules() {
        // Rules are defined as: [buildButton, upgradeButton, endTurnButton]
        buttonEnablementRules.put(MapPanel.BlockState.EMPTY_OWNED_BY_ACTIVE_PLAYER, new Boolean[]{true, false, true});
        buttonEnablementRules.put(MapPanel.BlockState.FRIENDLY_UNIT_ON_BLOCK, new Boolean[]{false, true, true});
        buttonEnablementRules.put(MapPanel.BlockState.FRIENDLY_STRUCTURE_ON_BLOCK, new Boolean[]{true, true, true});
        buttonEnablementRules.put(MapPanel.BlockState.CONQUERABLE, new Boolean[]{true, false, true});

        buttonEnablementRules.put(MapPanel.BlockState.ENEMY_UNIT_ON_BLOCK, new Boolean[]{false, false, true});
        buttonEnablementRules.put(MapPanel.BlockState.ENEMY_STRUCTURE_ON_BLOCK, new Boolean[]{false, false, true});
        buttonEnablementRules.put(MapPanel.BlockState.UNUSABLE, new Boolean[]{false, false, true});
        buttonEnablementRules.put(MapPanel.BlockState.EMPTY_OWNED_BY_ENEMY, new Boolean[]{false, false, true});
    }

    /**
     * Sets the enabled state of action buttons based on the current block state.
     * @param state The determined BlockState of the clicked tile.
     */
    public void setButtonsEnabled(MapPanel.BlockState state) {
        Boolean[] rules = buttonEnablementRules.getOrDefault(state, new Boolean[]{false, false, true});

        buildButton.setEnabled(rules[0]);
        upgradeButton.setEnabled(rules[1]);
        endTurnButton.setEnabled(rules[2]);
    }

    public void disableAllActionButtons() {
        buildButton.setEnabled(false);
        upgradeButton.setEnabled(false);
        // endTurnButton usually remains enabled, depending on game state logic
    }
}