package view.screens;

import controller.TurnTimerManager;
import controller.UserManager;
import model.GameStatus;
import view.components.TopBarPanel;

import javax.swing.*;
import java.awt.*;

public class GameScreen extends JPanel {
    private TopBarPanel topBar;
    private GameStatus gameStatus;
    private TurnTimerManager turnTimerManager;

    public GameScreen() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // ساخت وضعیت بازی اولیه
        gameStatus = new GameStatus(1, UserManager.getCurrentUser().getUsername(), Color.RED, 100, 30);

        // ساخت نوار بالا
        topBar = new TopBarPanel(gameStatus);
        add(topBar, BorderLayout.NORTH);

        // راه‌اندازی تایمر
        turnTimerManager = new TurnTimerManager(
                gameStatus.getTurnTimeLimit(),
                this::endTurn,
                seconds -> topBar.updateTimer(seconds)
        );
        turnTimerManager.start();
    }

    private void endTurn() {
        JOptionPane.showMessageDialog(this, "نوبت بازیکن تمام شد!");
        gameStatus.nextTurn();
        topBar.updateTurn(gameStatus.getCurrentTurn());
        turnTimerManager.reset(gameStatus.getTurnTimeLimit());
    }
}
