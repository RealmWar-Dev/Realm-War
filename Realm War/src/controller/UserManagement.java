package controller;

public class UserManagement {
    private String username;
    private char[] password;
    private int score;
    private int level;
    private int levelsSpace;
    private int wins;
    private int losses;

    public UserManagement(String username, char[] password) {
        setUsername(username);
        setPassword(password);
        setWins(0);
        setLosses(0);
        setScore(0);
        setLevel(0);
        setLevelsSpace(10);
        addToDatabase();
    }

    public String getUsername() {
        return username;
    }

    protected char[] getPassword() {
        return password;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    protected void setPassword(char[] password) {
        this.password = password;
    }

    public int getScore() {
        return score;
    }

    private void setScore(int score) {
        this.score = score;
    }

    public void incScore(int inc) {
        score += inc;
        if (score % levelsSpace == 0 ){
            setLevel(level + 1);
            setLevelsSpace(levelsSpace + 2);
        }
    }

    private void addToDatabase() {
        //TODO
    }

    protected void removeFromDatabase() {
        //TODO
    }

    public static boolean isUserNameExist(String username) {
        //TODO
        return false;
    }

    public int getLevelsSpace() {
        return levelsSpace;
    }

    public void setLevelsSpace(int levelsSpace) {
        this.levelsSpace = levelsSpace;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private void setWins(int wins) {
        this.wins = wins;
    }

    public int getWins() {
        return wins;
    }

    public void incWins() {
        wins++;
    }

    private void setLosses(int losses) {
        this.losses = losses;
    }

    public int getLosses() {
        return losses;
    }

    public void incLosses() {
        losses++;
    }
}
