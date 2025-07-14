package view.mapDetals.sidePanel;

import view.mapDetals.mapPanel.MapPanel;
import view.mapDetals.mapPanel.TileButton;
import model.block.Block;
import model.unit.Unit;
import model.structure.Structure;
import model.kingdom.Kingdom;

import javax.swing.*;
import java.awt.*;

public class TileInfoPanel extends JPanel {
    private final JLabel cordLabel;
    private final JLabel typeLabel;
    private final JLabel ownerLabel;
    private final JLabel contentsLabel;
    private final JLabel stateLabel;

    public TileInfoPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // Changed to BoxLayout for better vertical alignment
        setOpaque(false);
        setBorder(BorderFactory.createTitledBorder("Tile Information")); // Changed title for clarity

        cordLabel = new JLabel("Coordinates: -");
        cordLabel.setFont(new Font("Serif", Font.BOLD, 14));
        typeLabel = new JLabel("Block Type: -"); // Clarified label
        typeLabel.setFont(new Font("Serif", Font.BOLD, 14));
        ownerLabel = new JLabel("Owner: -"); // New label for owner
        ownerLabel.setFont(new Font("Serif", Font.BOLD, 14));
        contentsLabel = new JLabel("Contents: -"); // New label for unit/structure
        contentsLabel.setFont(new Font("Serif", Font.BOLD, 14));
        stateLabel = new JLabel("State: -"); // New label for BlockState
        stateLabel.setFont(new Font("Serif", Font.BOLD, 14));

        add(cordLabel);
        add(typeLabel);
        add(ownerLabel);
        add(contentsLabel);
        add(stateLabel);

        // Add some vertical space
        add(Box.createVerticalStrut(5));
    }

    // Updated method signature to receive Block and BlockState
    public void updateInfo(TileButton tile, Block block, MapPanel.BlockState state) {
        cordLabel.setText("Coordinates: " + tile.getRow() + "," + tile.getCol());
        typeLabel.setText("Block Type: " + block.getBlockType().name());

        // Set owner information
        Kingdom owner = block.getKingdom();
        if (owner != null) {
            ownerLabel.setText("Owner: " + owner.getName() + " (Color: " + owner.getColorName() + ")");
            ownerLabel.setForeground(owner.getColor()); // Set label color to kingdom color
        } else {
            ownerLabel.setText("Owner: Unowned");
            ownerLabel.setForeground(Color.BLACK); // Default color
        }

        // Set contents information (Unit or Structure)
        Unit unit = block.getUnit();
        Structure structure = block.getStructure();
        if (unit != null) {
            contentsLabel.setText("Unit: " + unit.getUnitRank() + " (HP: " + unit.getCurrentHealth() + "/" + unit.getMaxHealth() + ")");
            contentsLabel.setForeground(unit.getOwnerKingdom().getColor()); // Color based on unit owner
        } else if (structure != null) {
            contentsLabel.setText("Structure: " + structure.getStructureType() + " (HP: " + structure.getCurrentHealth() + "/" + structure.getMaxHealth() + ")");
            contentsLabel.setForeground(structure.getOwnerKingdom().getColor()); // Color based on structure owner
        } else {
            contentsLabel.setText("Contents: Empty");
            contentsLabel.setForeground(Color.BLACK);
        }

        stateLabel.setText("State: " + state.toString().replace("_", " ")); // Display BlockState, make it readable
        revalidate();
        repaint();
    }

    public void clearInfo() {
        cordLabel.setText("Coordinates: -");
        typeLabel.setText("Block Type: -");
        ownerLabel.setText("Owner: -");
        contentsLabel.setText("Contents: -");
        stateLabel.setText("State: -");
        ownerLabel.setForeground(Color.BLACK); // Reset color
        contentsLabel.setForeground(Color.BLACK); // Reset color
        revalidate();
        repaint();
    }
}