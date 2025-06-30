import view.MainFrame;

public class Launcher {
    public static void main(String[] args) {
        MainFrame mainFrame = new MainFrame();
        Thread frameThread = new Thread(mainFrame);
        frameThread.start();
    }
}
