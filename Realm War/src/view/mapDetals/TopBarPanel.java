package view.mapDetals;

import controller.GameController;
import model.GameStatus;
import model.kingdom.Kingdom;

import javax.swing.*;
import java.awt.*;

public class TopBarPanel extends JPanel {
    private final JLabel turnLabel;
    private final JLabel foodLabel;
    private final JLabel playerLabel;
    private final JLabel resourceLabel;
    private final JLabel timerLabel;

    private final GameController gameController;

    public TopBarPanel() {
        this.gameController = GameController.getInstance();

        setLayout(new GridLayout(1, 5));
        setBackground(new Color(20, 20, 20));
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
        setPreferredSize(new Dimension(800, 50));

        turnLabel = createLabel();
        playerLabel = createLabel();
        resourceLabel = createLabel();
        foodLabel = createLabel();
        timerLabel = createLabel();

        add(turnLabel);
        add(playerLabel);
        add(resourceLabel);
        add(foodLabel);
        add(timerLabel);

        updateAll();
    }

    private JLabel createLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Consolas", Font.BOLD, 12));
        return label;
    }

    public void updateAll() {
        GameStatus status = gameController.getGameStatus();
        if (status == null) {
            turnLabel.setText("Waiting for game...");
            playerLabel.setText("-");
            resourceLabel.setText("-");
            foodLabel.setText("-");
            timerLabel.setText("-");
            return;
        }

        updateTurn(
                status.getTurnNumber(),
                gameController.getPlayer1TurnCount(),
                gameController.getKingdom2TurnCount()
        );

        Kingdom active = status.getActiveKingdom();
        if (active != null) {
            updatePlayer(active, status.getCurrentPlayerIndex());
            updateResources(active.getGold());
            updateFood(active.getFood()); // ← اضافه‌شده
        }
        updateTimer(status.getTurnTimeLimit());
    }


    public void updateTurn(int turnNumber, int p1, int p2) {
        turnLabel.setText("Turn #" + turnNumber + " | P1: " + p1 + " | P2: " + p2);
    }

    public void updateFood(int food) {
        foodLabel.setText("Food: " + food);
    }

    public void updatePlayer(Kingdom kingdom, int number) {
        playerLabel.setText("Player " + number + " : " + kingdom.getName());
        playerLabel.setForeground(kingdom.getColor());
    }

    public void updateResources(int gold) {
        resourceLabel.setText("Gold: " + gold);
    }

    public void updateTimer(int seconds) {
        timerLabel.setText("Time Left: " + seconds + "s");
    }
}
