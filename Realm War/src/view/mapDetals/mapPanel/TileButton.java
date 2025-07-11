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
        setIcon(getIconForVisualType(visualType));

        if (isSelected) {
            setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
    }


    private Icon getIconForBlockType(BlockType type) {
        String path = switch (type) {
            case EMPTY -> "/view/assets/images/icons/empty tile.png";
            case FOREST -> "/view/assets/images/icons/forest tile.png";
            case VOID -> "/view/assets/images/icons/void tile.png";
            default -> null;
        };

        if (path == null) return null;

        try {
            java.net.URL imageURL = getClass().getResource(path);
            if (imageURL == null) {
                System.err.println("⛔ آیکون پیدا نشد: " + path);
                return null;
            }
            ImageIcon icon = new ImageIcon(imageURL);
            Image scaledImage = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("⛔ خطا در بارگذاری آیکون برای نوع: " + type);
            return null;
        }
    }

    public TileVisualType getVisualType() {
        return visualType;
    }

    public void setVisualType(TileVisualType visualType) {
        this.visualType = visualType;
        updateAppearance();
    }

    private Icon getIconForVisualType(TileVisualType type) {
        String path = switch (type) {
            case EMPTY -> "/view/assets/images/icons/empty tile.png";
            case FOREST -> "/view/assets/images/icons/forest tile.png";
            case VOID -> "/view/assets/images/icons/void tile.png";
            case UNIT -> "/view/assets/images/icons/unit tile.jpg";
            case STRUCTURE -> "/view/assets/images/icons/structure tile.jpg";
            default -> null;
        };

        if (path == null) return null;

        try {
            java.net.URL imageURL = getClass().getResource(path);
            if (imageURL == null) {
                System.err.println("⛔ آیکون پیدا نشد: " + path);
                return null;
            }
            ImageIcon icon = new ImageIcon(imageURL);
            Image scaledImage = icon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("⛔ خطا در بارگذاری آیکون برای نوع: " + type);
            return null;
        }
    }


    public enum TileVisualType {
        EMPTY,
        FOREST,
        VOID,
        UNIT,
        STRUCTURE,
        ABSORBED,
        UNKNOWN
    }

}
