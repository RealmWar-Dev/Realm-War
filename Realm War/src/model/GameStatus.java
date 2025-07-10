package model;

import java.awt.*;

public class GameStatus {
    private int currentTurn;                 // شمارنده واقعی نوبت (مثلاً 1، 2، 3...)
    private String activePlayerName;         // نام بازیکن فعال فعلی
    private Color activePlayerColor;         // رنگ بازیکن فعال
    private int gold;                        // مقدار طلا یا منابع
    private final int turnTimeLimit;         // محدودیت زمانی برای هر نوبت (بر حسب ثانیه)

    public GameStatus(int startTurn, String playerName, Color color, int gold, int timeLimit) {
        this.currentTurn = startTurn;
        this.activePlayerName = playerName;
        this.activePlayerColor = color;
        this.gold = gold;
        this.turnTimeLimit = timeLimit;
    }

    // ✅ شماره واقعی نوبت فعلی
    public int getTurnNumber() {
        return currentTurn;
    }

    // ✅ مشخص می‌کنه نوبت بازیکن ۱ هست یا ۲
    public int getCurrentPlayerIndex() {
        return (currentTurn % 2 == 1) ? 1 : 2;
    }

    // ✅ افزایش نوبت (برای endTurn)
    public void nextTurn() {
        currentTurn++;
    }

    // 👤 نام و رنگ بازیکن فعال
    public String getActivePlayerName() {
        return activePlayerName;
    }

    public void setActivePlayerName(String name) {
        this.activePlayerName = name;
    }

    public Color getActivePlayerColor() {
        return activePlayerColor;
    }

    public void setActivePlayerColor(Color color) {
        this.activePlayerColor = color;
    }

    // 💰 طلا یا منابع
    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    // ⏱️ زمان مجاز هر نوبت
    public int getTurnTimeLimit() {
        return turnTimeLimit;
    }
}
