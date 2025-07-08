package view.components;

import model.GameStatus;

import javax.swing.*;
import java.awt.*;

public class TopBarPanel extends JPanel {
    private JLabel turnLabel;
    private JLabel playerLabel;
    private JLabel resourceLabel;
    private JLabel timerLabel;

    private GameStatus gameStatus;

    public TopBarPanel(GameStatus gameStatus) {
        this.gameStatus = gameStatus;

        setLayout(new GridLayout(1, 4));
        setBackground(new Color(20, 20, 20));
        setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY));
        setPreferredSize(new Dimension(800, 50));

        // ساخت لیبل‌ها
        turnLabel = createLabel("");
        playerLabel = createLabel("");
        resourceLabel = createLabel("");
        timerLabel = createLabel("");

        add(turnLabel);
        add(playerLabel);
        add(resourceLabel);
        add(timerLabel);

        updateAll(); // مقداردهی اولیه
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Consolas", Font.BOLD, 16));
        return label;
    }

    public void updateAll() {
        updateTurn(gameStatus.getCurrentTurn());
        updatePlayer(gameStatus.getActivePlayerName(), gameStatus.getActivePlayerColor());
        updateResources(gameStatus.getGold());
        updateTimer(gameStatus.getTurnTimeLimit());
    }

    public void updateTurn(int turn) {
        turnLabel.setText("Turn: " + turn);
    }

    public void updatePlayer(String name, Color color) {
        playerLabel.setText("Player: " + name);
        playerLabel.setForeground(color); // مثلا "#FF0000"
    }

    public void updateResources(int gold) {
        resourceLabel.setText("Gold: " + gold);
    }

    public void updateTimer(int seconds) {
        timerLabel.setText("Time Left: " + seconds + "s");
    }
}
