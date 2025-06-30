package view.utils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

import static javax.sound.sampled.AudioSystem.getAudioInputStream;

public class SoundPlayer implements Runnable {
    private Clip clip;

    @Override
    public void run() {
        playLoop();
    }

    public SoundPlayer(String path) {
        try {
            URL soundURL = getClass().getResource(path);
            if (soundURL == null) {
                System.err.println("Sound file not found: " + path);
                return;
            }
            AudioInputStream audioStream = getAudioInputStream(soundURL);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    public void playLoop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.stop();
        }
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void start() {
        if (clip != null && !clip.isRunning()) {
            clip.start();
        }
    }

    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }
}
