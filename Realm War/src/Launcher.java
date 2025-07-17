import database.DatabaseManager;
import view.MainFrame;

public static void main(String[] args) {
    new DatabaseManager();
    new Thread(new MainFrame()).start();
}