import database.DatabaseManager;
import view.MainFrame;

public static void main(String[] ignoredArgs) {
    new Thread(new MainFrame()).start();
    new DatabaseManager();
}
