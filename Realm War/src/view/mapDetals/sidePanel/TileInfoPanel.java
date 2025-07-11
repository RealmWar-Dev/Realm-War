package view.mapDetals.sidePanel;

import view.mapDetals.mapPanel.*;

import javax.swing.*;
import java.awt.*;


public class TileInfoPanel extends JPanel {
    private final JLabel cordLabel;
    private final JLabel typeLabel;

    public TileInfoPanel() {
        setLayout(new GridLayout(2, 1));
        setOpaque(false);
        setBorder(BorderFactory.createTitledBorder("place Information"));

        cordLabel = new JLabel("Coordinates: -");
        cordLabel.setFont(new Font("Serif", Font.BOLD, 14));
        typeLabel = new JLabel("Type: -");
        typeLabel.setFont(new Font("Serif", Font.BOLD, 14));

        add(cordLabel);
        add(typeLabel);
    }

    public void updateInfo(TileButton tile) {
        cordLabel.setText("Coordinates: " + tile.getRow() + "," + tile.getCol());
        typeLabel.setText("Type: " + tile.getVisualType());
    }
}
