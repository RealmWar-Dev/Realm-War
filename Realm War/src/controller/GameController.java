package controller;

import model.GameStatus;
import model.User;

import java.awt.*;

public class GameController {
    private static GameController instance;

    private GameStatus gameStatus;
    private User player1;
    private User player2;
    private User activePlayer;

    private int player1TurnCount;
    private int player2TurnCount;

    private GameController() {
    }

    public static GameController getInstance() {
        if (instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    public void startNewGame(User p1, User p2) {
        this.player1 = p1;
        this.player2 = p2;
        this.activePlayer = p1;

        this.player1TurnCount = 1;
        this.player2TurnCount = 0;

        player1.setColor(Color.red);
        player2.setColor(Color.blue);

        this.gameStatus = new GameStatus(
                1,
                p1.getUsername(),
                p1.getColor(),
                100,
                30
        );
    }

    public void nextTurn() {
        gameStatus.nextTurn();

        // اول activePlayer را عوض می‌کنیم
        if (activePlayer.equals(player1)) {
            activePlayer = player2;
            player2TurnCount++;
        } else {
            activePlayer = player1;
            player1TurnCount++;
        }

        // بعد اطلاعات بازیکن فعال را به gameStatus می‌دهیم
        gameStatus.setActivePlayerName(activePlayer.getUsername());
        gameStatus.setActivePlayerColor(activePlayer.getColor());
    }


    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public int getPlayer1TurnCount() {
        return player1TurnCount;
    }

    public int getPlayer2TurnCount() {
        return player2TurnCount;
    }

    public void buildUnitAt(int row, int col) {
        System.out.println("✅ Building unit at: " + row + "," + col);
        // بعداً اینجا منطق واقعی ساخت نیرو رو پیاده‌سازی کن
    }

    public void upgradeAt(int row, int col) {
        System.out.println("🔼 Upgrading at: " + row + "," + col);
        // بعداً اینجا منطق ارتقاء نیرو یا ساختمان رو پیاده‌سازی کن
    }

}
