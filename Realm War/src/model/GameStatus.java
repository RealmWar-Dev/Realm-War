package model;

import model.kingdom.Kingdom;

import java.awt.*;

public class GameStatus {
    private int currentTurn = 1;                 // شمارنده واقعی نوبت (مثلاً 1، 2، 3...)
    private Kingdom activeKingdom;         // نام بازیکن فعال فعلی
    private final int turnTimeLimit;         // محدودیت زمانی برای هر نوبت (بر حسب ثانیه)

    public GameStatus(Kingdom kingdom, int timeLimit) {
        this.activeKingdom = kingdom;
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
    public Kingdom getActiveKingdom() {
        return activeKingdom;
    }

    public void setActiveKingdom(String name) {
        this.activeKingdom.setName(name);
    }

    public Color getActiveKingdomColor() {
        return activeKingdom.getColor();
    }

    public void setActiveKingdomColor(Color color) {
        this.activeKingdom.setColor(color);
    }

    public void setActiveKingdom(Kingdom kingdom) {
        this.activeKingdom = kingdom;
    }


    // ⏱️ زمان مجاز هر نوبت
    public int getTurnTimeLimit() {
        return turnTimeLimit;
    }
}
