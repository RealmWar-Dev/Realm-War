package view.mapDetals.sidePanel;

import view.mapDetals.mapPanel.MapPanel;
import view.mapDetals.mapPanel.TileButton;
import model.block.Block;
import model.unit.Unit;
import model.structure.Structure;
import model.kingdom.Kingdom;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {
    private final TileInfoPanel tileInfoPanel;
    private final ActionButtonPanel buttonPanel;

    public SidePanel(Runnable onBuildUnit, Runnable onUpgrade, Runnable onEndTurn) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 0));
        setOpaque(false);

        tileInfoPanel = new TileInfoPanel();
        buttonPanel = new ActionButtonPanel(onBuildUnit, onUpgrade, onEndTurn);

        add(tileInfoPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Updated method to receive detailed tile information.
     * This method will now pass the block and its state to TileInfoPanel
     * and can also enable/disable buttons in ActionButtonPanel based on the state.
     *
     * @param tile The clicked TileButton.
     * @param block The corresponding Block model object.
     * @param state The determined BlockState.
     */
    public void updateTileInfo(TileButton tile, Block block, MapPanel.BlockState state) {
        tileInfoPanel.updateInfo(tile, block, state);
        buttonPanel.setButtonsEnabled(state);
    }

    // You might also want a method to clear info if nothing is selected
    public void clearTileInfo() {
        tileInfoPanel.clearInfo();
        buttonPanel.disableAllActionButtons(); // Disable all actions if no tile is selected or valid
    }
}