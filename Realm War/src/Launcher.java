import database.DatabaseManager;
import view.MainFrame;

public static void main(String[] args) {
    new Thread(new MainFrame()).start();
    new DatabaseManager();
}