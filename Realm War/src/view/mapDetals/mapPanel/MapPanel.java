package view.mapDetals.mapPanel;

import model.map.GameMap;
import model.block.Block;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class MapPanel extends JPanel {
    private final int rows;
    private final int cols;
    private final TileButton[][] tiles;
    private TileButton selectedTile;

    public interface TileSelectionListener {
        void onTileSelected(TileButton tile);
    }

    private TileSelectionListener selectionListener;

    public MapPanel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.tiles = new TileButton[rows][cols];

        setLayout(new GridLayout(rows, cols, 2, 2));
        setBackground(Color.BLACK);
        initTiles();
    }

    private void initTiles() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TileButton tile = new TileButton(row, col, Block.BlockType.EMPTY);
                tile.addActionListener(this::onTileClicked);
                tiles[row][col] = tile;
                add(tile);
            }
        }
    }

    private void onTileClicked(ActionEvent e) {
        TileButton clicked = (TileButton) e.getSource();

        if (selectedTile != null) {
            selectedTile.setSelected(false);
        }

        selectedTile = clicked;
        selectedTile.setSelected(true);

        if (selectionListener != null) {
            selectionListener.onTileSelected(selectedTile);
        }
    }

    public void setTileSelectionListener(TileSelectionListener listener) {
        this.selectionListener = listener;
    }

    public TileButton getTile(int row, int col) {
        return tiles[row][col];
    }

    public TileButton getSelectedTile() {
        return selectedTile;
    }

    /**
     * 🧩 همگام‌سازی MapPanel با GameMap داده‌شده (به‌روزرسانی نوع بلاک‌ها)
     */
    public void syncWithGameMap(GameMap gameMap) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Block block = gameMap.getBlockAt(row, col);
                TileButton tile = tiles[row][col];

                tile.setBlockType(block.getBlockType());

                // بررسی اولویت نمایش ساختمان روی یونیت و بلاک
                if (gameMap.hasStructureAt(row, col)) {
                    tile.setVisualType(TileButton.TileVisualType.STRUCTURE);
                } else if (gameMap.hasUnitAt(row, col)) {
                    tile.setVisualType(TileButton.TileVisualType.UNIT);
                } else {
                    switch (block.getBlockType()) {
                        case EMPTY -> tile.setVisualType(TileButton.TileVisualType.EMPTY);
                        case FOREST -> tile.setVisualType(TileButton.TileVisualType.FOREST);
                        case VOID -> tile.setVisualType(TileButton.TileVisualType.VOID);
                        default -> tile.setVisualType(TileButton.TileVisualType.UNKNOWN);
                    }
                }
            }
        }
    }



}
