package controller;

import model.User;

import java.util.Objects;

public class MatchRoomManager {
    private final User player1;
    private User player2;

    public MatchRoomManager(User player1) {
        this.player1 = player1;
    }

    public boolean join(User player2) {
        if (this.player2 == null && !Objects.equals(this.player1.getUsername(), player2.getUsername())) {
            this.player2 = player2;
            return true;
        }
        return false;
    }

    public boolean isReady() {
        return player1 != null && player2 != null;
    }

    public User getOpponent(User user) {
        return user.equals(player1) ? player2 : player1;
    }

    public User getPlayer1() {
        return player1;
    }

    public User getPlayer2() {
        return player2;
    }
}
