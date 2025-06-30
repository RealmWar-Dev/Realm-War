package view.screens;

import controller.NavigationManager;
import view.MainFrame;
import view.styles.GameStyle;
import view.utils.ImageUtils;
import view.utils.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SplashScreen extends JPanel {
    public static final int TRANSITION_DELAY = 6000; // 6 ثانیه
    public static String name = "SPLASH";

    public SplashScreen() {
        setLayout(new BorderLayout());
        setOpaque(false);

        // ایجاد پنل اصلی با پس‌زمینه
        JPanel mainPanel = createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // شروع تایمر انتقال
        startTransitionTimer();
    }

    private JPanel createMainPanel() {
        // پنل پس‌زمینه
        JPanel backgroundPanel = GameStyle.createBackGroundPanel("/view/assets/image/splash background.png");
        backgroundPanel.setBackground(Color.BLACK);
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setOpaque(true);

        // اضافه کردن لوگو
        backgroundPanel.add(createLogoPanel(), BorderLayout.CENTER);

        return backgroundPanel;
    }

    private JPanel createLogoPanel() {
        Image logoImage = new ImageIcon(Objects.requireNonNull(
                getClass().getResource("/view/assets/image/logo in splash.png"))).getImage();



        JLabel logoLabel = new JLabel();
        ImageUtils.resizeLabelImage(logoLabel , logoImage , 400 , 400 );
        logoLabel.setOpaque(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(logoLabel, BorderLayout.CENTER);
        return panel;
    }



    private void startTransitionTimer() {
        Timer timer = new Timer(TRANSITION_DELAY , _ -> {
            NavigationManager.showPanel(HomeScreen.name);
            MainFrame.soundPlayer = new SoundPlayer(MainFrame.soundPath);
        });

        timer.setRepeats(false);
        timer.start();
    }
}