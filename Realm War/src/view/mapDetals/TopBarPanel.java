package view.mapDetals;

import controller.GameController;
import model.GameStatus;

import javax.swing.*;
import java.awt.*;

public class TopBarPanel extends JPanel {
    private final JLabel turnLabel;
    private final JLabel playerLabel;
    private final JLabel resourceLabel;
    private final JLabel timerLabel;

    private final GameController gameController;

    public TopBarPanel() {
        this.gameController = GameController.getInstance();

        setLayout(new GridLayout(1, 4));
        setBackground(new Color(20, 20, 20));
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
        setPreferredSize(new Dimension(800, 50));

        turnLabel = createLabel();
        playerLabel = createLabel();
        resourceLabel = createLabel();
        timerLabel = createLabel();

        add(turnLabel);
        add(playerLabel);
        add(resourceLabel);
        add(timerLabel);

        updateAll();
    }

    private JLabel createLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Consolas", Font.BOLD, 14));
        return label;
    }

    public void updateAll() {
        GameStatus status = gameController.getGameStatus();
        if (status == null) {
            turnLabel.setText("Waiting for game...");
            playerLabel.setText("-");
            resourceLabel.setText("-");
            timerLabel.setText("-");
            return;
        }

        updateTurn(status.getTurnNumber(),
                gameController.getPlayer1TurnCount(),
                gameController.getPlayer2TurnCount());

        updatePlayer(status.getActivePlayerName(), status.getActivePlayerColor() , status.getCurrentPlayerIndex());
        updateResources(status.getGold());
        updateTimer(status.getTurnTimeLimit());
    }

    public void updateTurn(int turnNumber, int p1, int p2) {
        turnLabel.setText("Turn #" + turnNumber + " | P1: " + p1 + " | P2: " + p2);
    }

    public void updatePlayer(String name, Color color, int number) {
        playerLabel.setText("Player " + number + " : " + name);
        playerLabel.setForeground(color);
    }

    public void updateResources(int gold) {
        resourceLabel.setText("Gold: " + gold);
    }

    public void updateTimer(int seconds) {
        timerLabel.setText("Time Left: " + seconds + "s");
    }
}
