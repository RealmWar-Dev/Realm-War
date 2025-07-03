package view.components;

import controller.NavigationManager;
import controller.UserManager;
import view.MainFrame;
import view.screens.HelpScreen;
import view.screens.LoginScreen;
import view.screens.UserPanelScreen;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * پنل آیکون‌های بالایی صفحه با قابلیت‌های:
 * - کنترل صدا
 * - راهنمای برنامه
 * - ذخیره بازی (در حالت بازی)
 * - بازگشت به صفحه قبلی
 * - مدیریت حساب کاربری
 */
public class TopIconPanel extends JPanel {
    // ثابت‌های مربوط به مسیر آیکون‌ها
    private static final String HELP_ICON_PATH = "/view/assets/image/icons/help.png";
    private static final String RETURN_ICON_PATH = "/view/assets/image/icons/return-arrow.png";
    private static final String SAVE_ICON_PATH = "/view/assets/image/icons/save.png";
    private static final String SOUND_ON_ICON_PATH = "/view/assets/image/icons/sound-on.png";
    private static final String SOUND_OFF_ICON_PATH = "/view/assets/image/icons/sound-off.png";
    public static final String USER_LOGIN_ICON_PATH = "/view/assets/image/icons/user_login.png";
    private static final String USER_LOGOUT_ICON_PATH = "/view/assets/image/icons/user_logout.png";

    // تنظیمات فاصله‌ها
    private static final int HORIZONTAL_GAP = 20;
    private static final int VERTICAL_GAP = 10;

    // دکمه‌ها
    private final JButton soundButton = new Buttons.IconButton(SOUND_OFF_ICON_PATH, this::handleSound);
    private final JButton helpButton = new Buttons.IconButton(HELP_ICON_PATH, _ -> NavigationManager.showPanel(HelpScreen.name));
    private final JButton saveButton = new Buttons.IconButton(SAVE_ICON_PATH, this::handleSave);
    private final JButton backButton = new Buttons.IconButton(RETURN_ICON_PATH, _ -> NavigationManager.goBack());
    private final JButton userButton = new Buttons.IconButton(USER_LOGOUT_ICON_PATH, this::handleUser);

    private final boolean isMenu;
    private final boolean isInGame;

    public TopIconPanel(boolean isMenu, boolean isInGame) {
        this.isMenu = isMenu;
        this.isInGame = isInGame;
        setupPanel();
    }

    private void setupPanel() {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(VERTICAL_GAP, HORIZONTAL_GAP ,VERTICAL_GAP, HORIZONTAL_GAP));

        add(createLeftPanel(), BorderLayout.WEST);
        add(createRightPanel(), BorderLayout.EAST);

        updateButtonsVisibility();
        updateSoundButtonIcon();
        updateUserButtonIcon();
    }

    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, HORIZONTAL_GAP, 0));
        panel.setOpaque(false);

        panel.add(soundButton);
        panel.add(helpButton);
        panel.add(saveButton);

        return panel;
    }

    private JPanel createRightPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, HORIZONTAL_GAP, 0));
        panel.setOpaque(false);

        panel.add(userButton);
        panel.add(backButton);

        return panel;
    }

    private void updateButtonsVisibility() {
        saveButton.setVisible(isInGame);
        userButton.setVisible(isMenu);
        backButton.setVisible(!isMenu);
    }

    private void handleSound(ActionEvent e) {
        if (MainFrame.soundPlayer.isPlaying()) {
            MainFrame.soundPlayer.pause();
            Buttons.setBackgroundButton(SOUND_OFF_ICON_PATH, soundButton);
        } else {
            MainFrame.soundPlayer.start();
            Buttons.setBackgroundButton(SOUND_ON_ICON_PATH, soundButton);
        }
    }

    private void handleSave(ActionEvent e) {
        // TODO: پیاده‌سازی منطق ذخیره بازی
    }

    private void handleUser(ActionEvent e) {
        String targetPanel = UserManager.isLoggedIn() ? UserPanelScreen.name : LoginScreen.name;
        NavigationManager.showPanel(targetPanel , false);
    }

    public void updateUserButtonIcon() {
        String iconPath = UserManager.isLoggedIn() ? USER_LOGIN_ICON_PATH : USER_LOGOUT_ICON_PATH;
        Buttons.setBackgroundButton(iconPath, userButton);
    }

    private void updateSoundButtonIcon() {
        String iconPath = MainFrame.soundPlayer.isPlaying() ? SOUND_ON_ICON_PATH : SOUND_OFF_ICON_PATH;
        Buttons.setBackgroundButton(iconPath, soundButton);
    }
}