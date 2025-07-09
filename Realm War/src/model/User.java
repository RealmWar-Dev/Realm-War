package model;

import java.awt.*;

public class User {
    private String username;
    private String password;
    private int level;
    private int score;
    private int wins;
    private int losses;
    private Color color;
    /**
     * سازنده اصلی برای ایجاد کاربر جدید
     * @param username نام کاربری
     * @param password رمز عبور
     */
    public User(String username, String password) {
        this(username, password, 1, 0, 0, 0);
    }

    /**
     * سازنده کامل برای بازیابی کاربر از دیتابیس
     * @param username نام کاربری
     * @param password رمز عبور
     * @param level سطح کاربر
     * @param score امتیاز کاربر
     * @param wins تعداد بردها
     * @param losses تعداد باخت‌ها
     */
    public User(String username, String password, int level, int score,
                int wins, int losses) {
        this.username = username;
        this.password = password;
        this.level = level;
        this.score = score;
        this.wins = wins;
        this.losses = losses;
    }

    // ================= متدهای دسترسی (Getters/Setters) =================

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if(username != null && !username.trim().isEmpty()) {
            this.username = username.trim();
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password != null && !password.trim().isEmpty()) {
            this.password = password;
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = Math.max(1, level); // سطح نباید کمتر از ۱ باشد
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = Math.max(0, score); // امتیاز نباید منفی باشد
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = Math.max(0, wins);
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = Math.max(0, losses);
    }

    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    // ================= متدهای کسب امتیاز و پیشرفت =================

    /**
     * افزایش امتیاز کاربر
     * @param amount مقدار امتیازی که اضافه می‌شود
     */
    public void addScore(int amount) {
        if(amount > 0) {
            this.score += amount;
            checkLevelUp();
        }
    }

    /**
     * ثبت برد برای کاربر
     */
    public void addWin() {
        this.wins++;
        addScore(100); // مثلاً ۱۰۰ امتیاز برای هر برد
    }

    /**
     * ثبت باخت برای کاربر
     */
    public void addLoss() {
        this.losses++;
        addScore(10); // مثلاً ۱۰ امتیاز برای هر باخت
    }

    /**
     * بررسی ارتقاء سطح کاربر بر اساس امتیاز
     */
    private void checkLevelUp() {
        int requiredScore = level * 1000; // مثلاً ۱۰۰۰ امتیاز برای هر سطح
        if(score >= requiredScore) {
            level++;
        }
    }

    // ================= متدهای محاسباتی =================

    /**
     * محاسبه امتیاز خالص (بردها منهای باخت‌ها)
     * @return امتیاز خالص
     */
    public int getNetScore() {
        return wins - losses;
    }

    @Override
    public String toString() {
        return String.format("User: %s (Level: %d, Score: %d, Wins: %d, Losses: %d)",
                username, level, score, wins, losses);
    }
}