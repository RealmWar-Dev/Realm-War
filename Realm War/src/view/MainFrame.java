package view;

import controller.NavigationManager;
import controller.UserManager;
import database.DatabaseManager;
import view.screens.*;
import view.screens.SplashScreen;
import view.utils.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainFrame extends JFrame implements Runnable {
    public static CardLayout cardLayout = new CardLayout();
    public static JPanel crdPanel = new JPanel(cardLayout);
    public static Stack<String> panelHistoryName = new Stack<>();
    Image logo;
    public static MainFrame frame;
    public static String soundPath = "/view/assets/music/background_music.wav";
    public static SoundPlayer soundPlayer = new SoundPlayer(soundPath);

    public MainFrame() {
        frame = this;
        setTitle("Realm War");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (UserManager.isLoggedIn())
                    DatabaseManager.updateUserStats(UserManager.getCurrentUser());

                System.exit(0);
            }
        });

        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/view/assets/image/realm war logo.png"))).getImage();
        setIconImage(logo);

        add(crdPanel, BorderLayout.CENTER);

        // نمایش اولیه با افکت fade in
        NavigationManager.showPanel(SplashScreen.name , false);
    }

    public static void createAndShowPanel(String panelName) {
        JPanel panel;

        switch (panelName) {
            case "SPLASH" -> panel = new SplashScreen();
            case "HOME" -> panel = new HomeScreen();
            case "SETTINGS" -> panel = new SettingsScreen();
            case "HELP" -> panel = new HelpScreen();
            case "LOGIN" -> panel = new LoginScreen();
            case "SIGNUP" -> panel = new SignUpScreen();
            case "USER_PANEL" -> panel = new UserPanelScreen();
            case "MATCH_ROOM" -> panel = new MatchRoomScreen();
            case "GAME" -> panel = new GameScreen();
            default -> {
                System.out.println("پنل ناشناخته: " + panelName);
                return;
            }
        }

        // حذف پنل قدیمی با همین نام (در صورت وجود)
        for (Component comp : crdPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().equals(panelName)) {
                crdPanel.remove(comp);
                break;
            }
        }

        panel.setName(panelName);
        crdPanel.add(panel, panelName);
    }


    @Override
    public void run() {
        setVisible(true);
    }
}