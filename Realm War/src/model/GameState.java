package model;

public class GameState {
    private boolean isAITurn;
    private boolean gameOver;

    public GameState() {
        isAITurn = false;
        gameOver = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void endGame() {
        gameOver = true;
    }

    public boolean isAITurn() {
        return isAITurn;
    }

    public void switchTurn() {
        isAITurn = !isAITurn;
    }
}
