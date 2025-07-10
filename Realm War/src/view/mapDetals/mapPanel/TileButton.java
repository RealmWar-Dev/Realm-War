package view.mapDetals.mapPanel;


import javax.swing.*;
import java.awt.*;

public class TileButton extends JButton {
    private final int row;
    private final int col;
    private TileType type;
    private boolean selected;

    public TileButton(int row, int col, TileType type) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.selected = false;
        setPreferredSize(new Dimension(25, 25));
        setFocusPainted(false);
        updateAppearance();
    }

    public void setType(TileType type) {
        this.type = type;
        updateAppearance();
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        updateAppearance();
    }

    private void updateAppearance() {
        if (selected) {
            setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        } else {
            setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }

        switch (type) {
            case EMPTY -> setBackground(Color.LIGHT_GRAY);
            case OBSTACLE -> setBackground(Color.DARK_GRAY);
            case UNIT -> setBackground(Color.BLUE);
            case BUILDING -> setBackground(Color.GREEN);
        }
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public TileType getType() {
        return type;
    }
}
