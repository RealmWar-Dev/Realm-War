package view.mapDetals.mapPanel;

import model.block.Block.BlockType;

import javax.swing.*;
import java.awt.*;

public class TileButton extends JButton {
    private TileVisualType visualType = TileVisualType.EMPTY;
    private final int row;
    private final int col;
    private BlockType blockType;
    private boolean isSelected = false;

    public TileButton(int row, int col, BlockType blockType) {
        this.row = row;
        this.col = col;
        this.blockType = blockType;

        setPreferredSize(new Dimension(32, 32));
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(true);

        updateAppearance();
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        updateAppearance();
    }

    public void setBlockType(BlockType blockType) {
        this.blockType = blockType;
        updateAppearance();
    }

    public BlockType getBlockType() {
        return blockType;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    private void updateAppearance() {
        setBackground(Color.LIGHT_GRAY);
        setIcon(null);
        setText("");

        switch (visualType) {
            case EMPTY:
                setBackground(new Color(200, 255, 200));
                break;
            case FOREST:
                setBackground(new Color(100, 180, 100));
                setIcon(getIconForPath("/view/assets/images/icons/forest_tile.png"));
                break;
            case VOID:
                setBackground(new Color(50, 50, 50));
                setIcon(getIconForPath("/view/assets/images/icons/void_tile.png"));
                break;

            case FRIENDLY_UNIT:
                setBackground(new Color(150, 255, 150));
                setIcon(getIconForPath("/view/assets/images/icons/friendly_unit.png"));
                setText("FU");
                break;
            case ENEMY_UNIT:
                setBackground(new Color(255, 150, 150));
                setIcon(getIconForPath("/view/assets/images/icons/enemy_unit.png"));
                setText("EU");
                break;

            case FRIENDLY_STRUCTURE:
                setBackground(new Color(150, 150, 255));
                setIcon(getIconForPath("/view/assets/images/icons/friendly_structure.png"));
                setText("FS");
                break;
            case ENEMY_STRUCTURE:
                setBackground(new Color(255, 100, 100));
                setIcon(getIconForPath("/view/assets/images/icons/enemy_structure.png"));
                setText("ES");
                break;

            case OWNED_EMPTY:
                setBackground(new Color(200, 200, 255));
                break;
            case OWNED_FOREST:
                setBackground(new Color(100, 100, 200));
                setIcon(getIconForPath("/view/assets/images/icons/owned_forest_tile.png"));
                break;
            case ENEMY_OWNED_EMPTY:
                setBackground(new Color(255, 200, 200));
                break;
            case ENEMY_OWNED_FOREST:
                setBackground(new Color(200, 100, 100));
                setIcon(getIconForPath("/view/assets/images/icons/enemy_owned_forest_tile.png"));
                break;

            case UNKNOWN:
            default:
                setBackground(Color.MAGENTA);
                setText("?");
                break;
        }

        if (isSelected) {
            setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        }
    }

    public TileVisualType getVisualType() {
        return visualType;
    }

    public void setVisualType(TileVisualType visualType) {
        this.visualType = visualType;
        updateAppearance();
    }

    private Icon getIconForPath(String path) {
        if (path == null) return null;

        try {
            java.net.URL imageURL = getClass().getResource(path);
            if (imageURL == null) {
                System.err.println("⛔ Icon not found: " + path);
                return null;
            }
            ImageIcon icon = new ImageIcon(imageURL);
            Image scaledImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("⛔ Error loading icon for path: " + path + ": " + e.getMessage());
            return null;
        }
    }
    public enum TileVisualType {
        EMPTY,
        FOREST,
        VOID,
        UNKNOWN,
        FRIENDLY_UNIT,
        ENEMY_UNIT,
        FRIENDLY_STRUCTURE,
        ENEMY_STRUCTURE,
        OWNED_EMPTY,
        OWNED_FOREST,
        ENEMY_OWNED_EMPTY,
        ENEMY_OWNED_FOREST,
    }
}