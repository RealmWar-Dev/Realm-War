package view.mapDetals.mapPanel;

import model.map.GameMap;
import model.block.Block;
import model.unit.Unit;
import model.structure.Structure;
import controller.GameController;
import model.kingdom.Kingdom;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(MapPanel.class.getName());

    private final int rows;
    private final int cols;
    private final TileButton[][] tiles;
    private TileButton selectedTile;
    private GameMap currentLoadedGameMap;

    public interface MapInteractionListener {
        void onTileInteraction(TileButton clickedTile, Block block, BlockState state);
    }

    private MapInteractionListener interactionListener;

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
        TileButton clickedTile = (TileButton) e.getSource();

        if (selectedTile != null) {
            selectedTile.setSelected(false);
        }

        selectedTile = clickedTile;
        selectedTile.setSelected(true);

        if (currentLoadedGameMap == null) {
            LOGGER.log(Level.WARNING, "Attempted to click tile before GameMap was loaded.");
            return;
        }

        Block clickedBlock = currentLoadedGameMap.getBlockAt(clickedTile.getRow(), clickedTile.getCol());
        if (clickedBlock == null) {
            LOGGER.log(Level.SEVERE, "Clicked tile at ({0},{1}) has no corresponding Block in GameMap.",
                    new Object[]{clickedTile.getRow(), clickedTile.getCol()});
            return;
        }

        BlockState currentState = determineBlockState(clickedBlock);

        LOGGER.log(Level.INFO, "Tile ({0},{1}) clicked. State: {2}",
                new Object[]{clickedTile.getRow(), clickedTile.getCol(), currentState});

        if (interactionListener != null) {
            interactionListener.onTileInteraction(clickedTile, clickedBlock, currentState);
        }
    }

    private BlockState determineBlockState(Block block) {
        Unit unitOnBlock = block.getUnit();
        Structure structureOnBlock = block.getStructure();
        Kingdom blockOwner = block.getKingdom();
        Kingdom activePlayerKingdom = GameController.getInstance().getActiveKingdom();

        if (block.getBlockType() == Block.BlockType.VOID) {
            return BlockState.UNUSABLE;
        }

        if (unitOnBlock != null) {
            if (unitOnBlock.getOwnerKingdom() == activePlayerKingdom) {
                return BlockState.FRIENDLY_UNIT_ON_BLOCK;
            } else {
                return BlockState.ENEMY_UNIT_ON_BLOCK;
            }
        }

        if (structureOnBlock != null) {
            if (structureOnBlock.getOwnerKingdom() == activePlayerKingdom) {
                return BlockState.FRIENDLY_STRUCTURE_ON_BLOCK;
            } else {
                return BlockState.ENEMY_STRUCTURE_ON_BLOCK;
            }
        }

        if (blockOwner == null) {
            return BlockState.CONQUERABLE;
        } else if (blockOwner == activePlayerKingdom) {
            return BlockState.EMPTY_OWNED_BY_ACTIVE_PLAYER;
        } else {
            return BlockState.EMPTY_OWNED_BY_ENEMY;
        }
    }

    public enum BlockState {
        EMPTY_OWNED_BY_ACTIVE_PLAYER,
        FRIENDLY_UNIT_ON_BLOCK,
        ENEMY_UNIT_ON_BLOCK,
        FRIENDLY_STRUCTURE_ON_BLOCK,
        ENEMY_STRUCTURE_ON_BLOCK,
        CONQUERABLE,
        UNUSABLE,
        EMPTY_OWNED_BY_ENEMY
    }

    public void setMapInteractionListener(MapInteractionListener listener) {
        this.interactionListener = listener;
    }

    public TileButton getTile(int row, int col) {
        return tiles[row][col];
    }

    public TileButton getSelectedTile() {
        return selectedTile;
    }

    public void syncWithGameMap(GameMap gameMap) {
        this.currentLoadedGameMap = gameMap;

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Block block = gameMap.getBlockAt(row, col);
                TileButton tile = tiles[row][col];

                tile.setBlockType(block.getBlockType());

                if (gameMap.hasStructureAt(row, col)) {
                    Structure structure = gameMap.getStructureAt(row, col);
                    if (structure.getOwnerKingdom() == GameController.getInstance().getActiveKingdom()) {
                        tile.setVisualType(TileButton.TileVisualType.FRIENDLY_STRUCTURE);
                    } else {
                        tile.setVisualType(TileButton.TileVisualType.ENEMY_STRUCTURE);
                    }
                } else if (gameMap.hasUnitAt(row, col)) {
                    Unit unit = gameMap.getUnitAt(row, col);
                    if (unit.getOwnerKingdom() == GameController.getInstance().getActiveKingdom()) {
                        tile.setVisualType(TileButton.TileVisualType.FRIENDLY_UNIT);
                    } else {
                        tile.setVisualType(TileButton.TileVisualType.ENEMY_UNIT);
                    }
                } else {
                    Kingdom blockOwner = block.getKingdom();
                    Kingdom activePlayerKingdom = GameController.getInstance().getActiveKingdom();

                    if (block.getBlockType() == Block.BlockType.VOID) {
                        tile.setVisualType(TileButton.TileVisualType.VOID);
                    } else if (blockOwner == activePlayerKingdom) {
                        switch (block.getBlockType()) {
                            case EMPTY -> tile.setVisualType(TileButton.TileVisualType.OWNED_EMPTY);
                            case FOREST -> tile.setVisualType(TileButton.TileVisualType.OWNED_FOREST);
                            default -> tile.setVisualType(TileButton.TileVisualType.UNKNOWN);
                        }
                    } else if (blockOwner != null && blockOwner != activePlayerKingdom) {
                        switch (block.getBlockType()) {
                            case EMPTY -> tile.setVisualType(TileButton.TileVisualType.ENEMY_OWNED_EMPTY);
                            case FOREST -> tile.setVisualType(TileButton.TileVisualType.ENEMY_OWNED_FOREST);
                            default -> tile.setVisualType(TileButton.TileVisualType.UNKNOWN);
                        }
                    } else {
                        switch (block.getBlockType()) {
                            case EMPTY -> tile.setVisualType(TileButton.TileVisualType.EMPTY);
                            case FOREST -> tile.setVisualType(TileButton.TileVisualType.FOREST);
                            default -> tile.setVisualType(TileButton.TileVisualType.UNKNOWN);
                        }
                    }
                }
                if (block.isAbsorbed()) {
                    tile.setBorder(BorderFactory.createLineBorder(block.getKingdom().getColor(), 2));
                } else {
                    tile.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
                }
            }
        }
    }
}