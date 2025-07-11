package model;

import model.kingdom.Kingdom;

import java.awt.*;

public class GameStatus {
    private int currentTurn = 1;                 // شماره واقعی نوبت (مثلاً 1، 2، 3...)
    private Kingdom activeKingdom;               // بازیکن فعال فعلی
    private final int turnTimeLimit;             // محدودیت زمانی هر نوبت بر حسب ثانیه

    public GameStatus(Kingdom kingdom, int timeLimit) {
        this.activeKingdom = kingdom;
        this.turnTimeLimit = timeLimit;
    }

    // شماره واقعی نوبت فعلی
    public int getTurnNumber() {
        return currentTurn;
    }

    // مشخص کردن بازیکن فعلی (1 یا 2)
    public int getCurrentPlayerIndex() {
        return (currentTurn % 2 == 1) ? 1 : 2;
    }

    // افزایش شماره نوبت
    public void nextTurn() {
        currentTurn++;
    }

    // دریافت بازیکن فعال فعلی
    public Kingdom getActiveKingdom() {
        return activeKingdom;
    }

    // تنظیم بازیکن فعال
    public void setActiveKingdom(Kingdom kingdom) {
        this.activeKingdom = kingdom;
    }

    // دریافت محدودیت زمانی نوبت
    public int getTurnTimeLimit() {
        return turnTimeLimit;
    }

    // سایر متدهای کمکی مانند نام یا رنگ
    public void setActiveKingdomName(String name) {
        this.activeKingdom.setName(name);
    }

    public void setActiveKingdomColor(Color color) {
        this.activeKingdom.setColor(color);
    }

    public Color getActiveKingdomColor() {
        return this.activeKingdom.getColor();
    }
}
