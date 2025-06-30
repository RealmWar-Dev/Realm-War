package model;

public class User {
    private String username;
    private String password;
    private int level;
    private int score;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.level = 1;   // شروع از سطح ۱
        this.score = 0;
    }

    // گتر و سترها
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    // متدهای کمکی (مثلا افزایش امتیاز)
    public void addScore(int amount) {
        this.score += amount;
    }
}
