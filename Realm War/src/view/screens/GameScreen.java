package view.screens;

import controller.*;
import view.components.BaseBackgroundPanel;
import view.mapDetals.*;
import view.mapDetals.mapPanel.*;
import view.mapDetals.sidePanel.SidePanel;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends BaseBackgroundPanel {
    public static final String name = "GAME";

    private final TopBarPanel topBar;
    private SidePanel sidePanel;
    private final TurnTimerManager turnTimer;
    private final GameController gameController;
    private JPanel mainPanel;

    protected MapPanel mapPanel;

    public GameScreen(MatchRoomManager matchRoom) {
        super(false,  true);

        gameController = GameController.getInstance();
        gameController.startNewGame(matchRoom.getPlayer1(), matchRoom.getPlayer2() , 10 , 10);

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

        mapPanel.setTileSelectionListener(tile -> sidePanel.updateTileInfo(tile));

        mainPanel.add(mapPanel, BorderLayout.CENTER);

        sidePanel = new SidePanel(
                () -> {
                    TileButton selected = mapPanel.getSelectedTile();
                    if (selected != null) {
                        gameController.buildUnitAt(selected.getRow(), selected.getCol());
                        // به‌روزرسانی ظاهر خونه
                        selected.setType(TileType.UNIT);
                    } else {
                        showMessage();
                    }
                },
                () -> {
                    TileButton selected = mapPanel.getSelectedTile();
                    if (selected != null) {
                        gameController.upgradeAt(selected.getRow(), selected.getCol());
                        // برای تست فقط پیام نشون بده
                    } else {
                        showMessage();
                    }
                },
                this::endTurn // پایان نوبت اصلی
        );
        mainPanel.add(sidePanel, BorderLayout.EAST);


        revalidate();
        repaint();
    }

    private void showMessage() {
        JOptionPane.showMessageDialog(this, "Please select a tile first.");
    }


    private void endTurn() {
        JOptionPane.showMessageDialog(this, "Your turn ended!");

        gameController.nextTurn();
        topBar.updateAll();
        turnTimer.reset(gameController.getGameStatus().getTurnTimeLimit());
    }
}
