package view.mapDetals.sidePanel;

import view.mapDetals.mapPanel.*;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {
    private final TileInfoPanel tileInfoPanel;

    public SidePanel(Runnable onBuildUnit, Runnable onUpgrade, Runnable onEndTurn) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(200, 0));
        setOpaque(false);

        tileInfoPanel = new TileInfoPanel();
        ActionButtonPanel buttonPanel = new ActionButtonPanel(onBuildUnit, onUpgrade, onEndTurn);

        add(tileInfoPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void updateTileInfo(TileButton tile) {
        tileInfoPanel.updateInfo(tile);
    }
}
