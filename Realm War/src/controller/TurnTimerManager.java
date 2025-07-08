package controller;

import javax.swing.*;

public class TurnTimerManager {
    private Timer timer;
    private int timeLeft;

    public TurnTimerManager(int seconds, Runnable onTimeout, java.util.function.IntConsumer onTick) {
        this.timeLeft = seconds;

        timer = new Timer(1000, _ -> {
            timeLeft--;
            onTick.accept(timeLeft);
            if (timeLeft <= 0) {
                timer.stop();
                onTimeout.run();
            }
        });
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    public void reset(int seconds) {
        stop();
        this.timeLeft = seconds;
        start();
    }
}
