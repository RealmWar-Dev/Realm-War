package view.screens;

import controller.*;
import view.components.BaseBackgroundPanel;
import view.mapDetals.*;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends BaseBackgroundPanel {
    public static final String name = "GAME";

    private final TopBarPanel topBar;
    private final TurnTimerManager turnTimer;
    private final GameController gameController;

    public GameScreen(MatchRoomManager matchRoom) {
        super(false , true , true);

        gameController = GameController.getInstance();

        // ✅ مهم: قبل از ساخت UI باید بازی رو راه‌اندازی کنیم
        gameController.startNewGame(matchRoom.getPlayer1(), matchRoom.getPlayer2());

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

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(topBar, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);

    }

    private void endTurn() {
        JOptionPane.showMessageDialog(this, "Your turn ended!");

        gameController.nextTurn();
        topBar.updateAll();
        turnTimer.reset(gameController.getGameStatus().getTurnTimeLimit());
    }

    @Override
    public void initializeComponents() {

    }
}
