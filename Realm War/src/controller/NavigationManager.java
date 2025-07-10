package controller;

import view.MainFrame;
import view.screens.GameScreen;
import view.screens.HomeScreen;
import view.styles.Fade;

import javax.swing.*;

public class NavigationManager {

    // نگهداری نام پنل فعلی
    private static String currentPanelName;

    /**
     * نمایش پنل جدید و ذخیره در تاریخچه
     */
    public static void showPanel(String panelName) {
        showPanel(panelName, true);
    }

    /**
     * نمایش پنل جدید با امکان کنترل ذخیره در تاریخچه
     */
    public static void showPanel(String panelName, boolean saveToHistory) {
        if (panelName == null || panelName.isEmpty() || panelName.equals(currentPanelName)) {
            return;
        }

        // ساخت و نمایش پنل همراه با افکت
        applyPanelTransition(panelName);

        // ذخیره در تاریخچه در صورت نیاز
        if (saveToHistory) {
            MainFrame.panelHistoryName.push(panelName);
        }

        // به‌روزرسانی پنل فعلی
        currentPanelName = panelName;
    }

    /**
     * بازگشت به پنل قبلی در تاریخچه
     */
    public static void goBack() {
        if (MainFrame.panelHistoryName.size() <= 1) {
            // اگر تاریخچه یا تنها یک پنل بود، برو به صفحه اصلی
            MainFrame.panelHistoryName.clear();
            navigateToHome();
            return;
        }

        if (currentPanelName.equals(GameScreen.name)) {
            int result = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to exit the game?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.NO_OPTION) {
                return;
            }
        }

        // حذف پنل فعلی
        MainFrame.panelHistoryName.pop();

        // گرفتن نام پنل قبلی
        String previousPanel = MainFrame.panelHistoryName.peek();

        // نمایش بدون ذخیره مجدد در تاریخچه
        showPanel(previousPanel, false);
    }

    /**
     * اعمال افکت مناسب هنگام نمایش پنل
     */
    private static void applyPanelTransition(String panelName) {
        switch (panelName) {
            case "SPLASH" -> {
                MainFrame.createAndShowPanel(panelName);
                new Fade().fadeInOnly(panelName);
            }
            case "LAST_SCREEN" -> new Fade().fadeOutOnly(() -> System.exit(0));
            default -> {
                MainFrame.createAndShowPanel(panelName);
                new Fade(panelName);
            }
        }
    }

    /**
     * نمایش صفحه اصلی
     */
    private static void navigateToHome() {
        showPanel(HomeScreen.name);
    }


    public static void startGame(MatchRoomManager matchRoom) {
        MainFrame.createAndShowStartPanel(matchRoom);
        MainFrame.panelHistoryName.push(GameScreen.name);
        currentPanelName = GameScreen.name;
        new Fade(GameScreen.name);
    }
}
