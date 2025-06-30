package view.styles;

import view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class Fade {
    private final JPanel overlayPanel;
    private final int fadeDuration = 1400;
    private final int frameRate = 60;
    private final int frameDelay = 100 / frameRate;
    private float currentAlpha = 0f;

    // سازنده عمومی برای استفاده از fadeIn یا fadeOut
    public Fade() {
        overlayPanel = createOverlayPanel();
        setupOverlayPanel();
    }

    // سازنده مخصوص ترنزیشن بین پنل‌ها
    public Fade(String targetPanelName) {
        overlayPanel = createOverlayPanel();
        setupOverlayPanel();
        startFadeTransition(targetPanelName);
    }

    // ساخت پنل overlay
    private JPanel createOverlayPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, currentAlpha));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        };
    }

    // تنظیمات اولیه overlayPanel
    private void setupOverlayPanel() {
        overlayPanel.setOpaque(false);
        overlayPanel.setBounds(0, 0, MainFrame.frame.getWidth(), MainFrame.frame.getHeight());
        MainFrame.frame.getLayeredPane().add(overlayPanel, JLayeredPane.MODAL_LAYER);

        MainFrame.frame.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                overlayPanel.setSize(MainFrame.frame.getSize());
            }
        });
    }

    // متد اصلی برای fade همراه با تغییر پنل
    private void startFadeTransition(String targetPanelName) {
        final long startTime = System.currentTimeMillis();
        final boolean[] switched = {false};
        Timer timer = new Timer(frameDelay, null);

        timer.addActionListener(_ -> {
            float progress = (System.currentTimeMillis() - startTime) / (float) fadeDuration;
            progress = Math.min(progress, 1.0f);

            if (progress < 0.5f) {
                currentAlpha = easeInOutQuad(progress * 2f);
            } else {
                if (!switched[0]) {
                    currentAlpha = 1f;
                    overlayPanel.repaint();
                    MainFrame.cardLayout.show(MainFrame.crdPanel, targetPanelName);
                    switched[0] = true;
                }
                float fadeInProgress = (progress - 0.5f) * 2f;
                currentAlpha = easeInOutQuad(1 - fadeInProgress);
            }

            overlayPanel.repaint();

            if (progress >= 1.0f) {
                overlayPanel.setVisible(false);
                timer.stop();
            }
        });

        overlayPanel.setVisible(true);
        timer.start();
    }

    // 🌑 فقط Fade In (برای نمایش صفحه Welcome)
    public void fadeInOnly(String panelName) {
        // ابتدا overlay را کاملاً مشکی کنید
        currentAlpha = 1f;
        overlayPanel.setVisible(true);
        overlayPanel.repaint();

        // 🔁 مرحله اول: چند میلی‌ثانیه تأخیر قبل از نشان دادن پنل جدید
        Timer delayTimer = new Timer(50, null); // تأخیر 50 میلی‌ثانیه
        delayTimer.setRepeats(false);
        delayTimer.addActionListener(__ -> {
            // بعد از تأخیر، پنل مورد نظر را نمایش بده
            MainFrame.cardLayout.show(MainFrame.crdPanel, panelName);

            // حالا fade شروع شود
            Timer fadeTimer = new Timer(frameDelay, null);
            long start = System.currentTimeMillis();

            fadeTimer.addActionListener(_ -> {
                float progress = (System.currentTimeMillis() - start) / (float) fadeDuration / 2;
                progress = Math.min(progress, 1.0f);

                // کاهش تدریجی آلفا از 1 به 0 (مشکی به شفاف)
                currentAlpha = 1f - easeInOutQuad(progress);
                overlayPanel.repaint();

                if (progress >= 1.0f) {
                    overlayPanel.setVisible(false);
                    fadeTimer.stop();
                }
            });

            fadeTimer.start();
        });

        delayTimer.start();
    }


    // 🌒 فقط Fade Out با اجرای کد بعد از پایان
    public void fadeOutOnly(Runnable afterFade) {
        currentAlpha = 0f;
        overlayPanel.setVisible(true);

        Timer timer = new Timer(frameDelay, null);
        long start = System.currentTimeMillis();

        timer.addActionListener(_ -> {
            float progress = (System.currentTimeMillis() - start) / (float) fadeDuration;
            progress = Math.min(progress, 1.0f);
            currentAlpha = (float) Math.sin(progress * Math.PI / 2);
            overlayPanel.repaint();

            if (progress >= 1.0f) {
                overlayPanel.setVisible(false);
                timer.stop();
                if (afterFade != null) afterFade.run();
            }
        });

        timer.start();
    }

    // تابع easing برای حرکت نرم
    private float easeInOutQuad(float t) {
        return t < 0.5f ? 2 * t * t : 1 - (float) Math.pow(-2 * t + 2, 2) / 2;
    }
}
