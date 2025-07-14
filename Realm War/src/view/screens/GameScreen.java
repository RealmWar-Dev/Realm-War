package view.screens;

import controller.*;
import view.components.BaseBackgroundPanel;
import view.mapDetals.TopBarPanel;
import view.mapDetals.mapPanel.MapPanel;
import view.mapDetals.mapPanel.TileButton;
import view.mapDetals.sidePanel.SidePanel;

import model.block.Block; // Import Block for onTileInteraction
import java.util.logging.Level; // For logging
import java.util.logging.Logger; // For logging

import javax.swing.*;
import java.awt.*;

// GameScreen now implements MapPanel.MapInteractionListener
public class GameScreen extends BaseBackgroundPanel implements MapPanel.MapInteractionListener {
    public static final String name = "GAME";
    private static final Logger LOGGER = Logger.getLogger(GameScreen.class.getName()); // Logger for GameScreen

    private final TopBarPanel topBar;
    private SidePanel sidePanel; // No longer final, as it's initialized later
    private final TurnTimerManager turnTimer;
    private final GameController gameController;
    private JPanel mainPanel;

    protected MapPanel mapPanel;

    // A field to potentially hold a selected unit for actions (as discussed previously)
    // private Unit selectedUnitForAction; // Uncomment and manage this if you have unit selection logic

    public GameScreen(MatchRoomManager matchRoom) {
        super(false,  true);

        gameController = GameController.getInstance();
        gameController.startNewGame(matchRoom.getPlayer1(), matchRoom.getPlayer2(), 10, 10);

        topBar = new TopBarPanel();

        turnTimer = new TurnTimerManager(
                gameController.getGameStatus().getTurnTimeLimit(),
                this::endTurn,
                topBar::updateTimer
        );
        turnTimer.start();

        setUpUI();
        initializeComponents();
    }

    private void setUpUI() {
        setBackground(Color.BLACK);
        setOpaque(true);

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(topBar, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    @Override
    public void initializeComponents() {
        int rows = 10;
        int cols = 10;

        mapPanel = new MapPanel(rows, cols);

        // FIXED: Changed setTileSelectionListener to setMapInteractionListener
        // And passed 'this' as the listener, as GameScreen now implements it.
        mapPanel.setMapInteractionListener(this); // GameScreen now handles map interactions

        // همگام‌سازی پنل نقشه با داده‌های واقعی بازی
        mapPanel.syncWithGameMap(gameController.getGameMap());

        mainPanel.add(mapPanel, BorderLayout.CENTER);

        // Initialize SidePanel after mapPanel
        sidePanel = new SidePanel(
                () -> {
                    TileButton selected = mapPanel.getSelectedTile();
                    if (selected != null) {
                        // In a real game, you'd need to check the BlockState and
                        // conditions before allowing building.
                        // You should ideally get the block from the GameMap using selected.getRow()/getCol()
                        Block selectedBlock = gameController.getGameMap().getBlockAt(selected.getRow(), selected.getCol());
                        if (selectedBlock != null && selectedBlock.getKingdom() == gameController.getActiveKingdom()) {
                            gameController.buildUnitAt(selected.getRow(), selected.getCol());
                        } else {
                            showMessage("Cannot build here. Block is not owned by active player or other conditions apply.");
                        }
                    } else {
                        showMessage("Please select a tile first.");
                    }
                },
                () -> {
                    TileButton selected = mapPanel.getSelectedTile();
                    if (selected != null) {
                        // Similar check needed for upgrading.
                        Block selectedBlock = gameController.getGameMap().getBlockAt(selected.getRow(), selected.getCol());
                        if (selectedBlock != null && selectedBlock.getKingdom() == gameController.getActiveKingdom() &&
                                (selectedBlock.getUnit() != null || selectedBlock.getStructure() != null)) {
                            gameController.upgradeAt(selected.getRow(), selected.getCol());
                        } else {
                            showMessage("Cannot upgrade here. No friendly unit/structure or block not owned by active player.");
                        }
                    } else {
                        showMessage("Please select a tile first.");
                    }
                },
                this::endTurn
        );

        mainPanel.add(sidePanel, BorderLayout.EAST);

        revalidate();
        repaint();
    }

    private void showMessage(String message) { // Modified to accept a message
        JOptionPane.showMessageDialog(this, message);
    }

    private void endTurn() {
        JOptionPane.showMessageDialog(this, "Your turn ended!");

        gameController.nextTurn();
        topBar.updateAll();
        turnTimer.reset(gameController.getGameStatus().getTurnTimeLimit());
        // After turn ends and map state potentially changes, refresh map display
        mapPanel.syncWithGameMap(gameController.getGameMap());
        sidePanel.clearTileInfo(); // Clear side panel info on turn end
    }

    /**
     * Implements the MapInteractionListener interface.
     * This method is called by MapPanel when a tile is clicked,
     * providing detailed information about the clicked block's state.
     */
    @Override
    public void onTileInteraction(TileButton clickedTile, Block block, MapPanel.BlockState state) {
        LOGGER.log(Level.INFO, "GameScreen received tile interaction: ({0},{1}), BlockType: {2}, State: {3}",
                new Object[]{clickedTile.getRow(), clickedTile.getCol(), block.getBlockType(), state});

        // Pass all detailed information to the SidePanel's update method
        sidePanel.updateTileInfo(clickedTile, block, state);

        // Example logic for actions (This is where you'd implement the decision tree
        // based on the BlockState and potentially other game states like 'selectedUnitForAction')
        switch (state) {
            case EMPTY_OWNED_BY_ACTIVE_PLAYER:
                LOGGER.log(Level.INFO, "Actions: Build new structure or move unit.");
                // The ActionButtonPanel already enables/disables buttons based on this.
                // If you have a separate "move unit" action, it would be handled here.
                break;
            case FRIENDLY_UNIT_ON_BLOCK:
                LOGGER.log(Level.INFO, "Actions: Select friendly unit, move, attack, set status.");
                // selectedUnitForAction = block.getUnit(); // Example: set a globally selected unit
                break;
            case ENEMY_UNIT_ON_BLOCK:
                LOGGER.log(Level.INFO, "Actions: View enemy unit, attack with selected friendly unit.");
                break;
            case FRIENDLY_STRUCTURE_ON_BLOCK:
                LOGGER.log(Level.INFO, "Actions: View friendly structure, upgrade, produce units.");
                break;
            case ENEMY_STRUCTURE_ON_BLOCK:
                LOGGER.log(Level.INFO, "Actions: View enemy structure, attack with selected friendly unit.");
                break;
            case CONQUERABLE:
                LOGGER.log(Level.INFO, "Actions: Absorb block.");
                break;
            case UNUSABLE:
                LOGGER.log(Level.INFO, "Actions: View info (unusable).");
                break;
            case EMPTY_OWNED_BY_ENEMY:
                LOGGER.log(Level.INFO, "Actions: View info (enemy owned empty).");
                break;
            default:
                LOGGER.log(Level.INFO, "No specific actions defined for this state.");
                break;
        }

        revalidate();
        repaint();
    }
}