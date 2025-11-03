package cs.main;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Objects;

public class BackgroundMusic {
    private Clip clip;
    public final FloatControl volControl;
    public float volume = 0.5f;

    public BackgroundMusic(String filePath) {
        try {
            if (filePath != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(filePath)));
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
        assert clip != null;
        volControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    }

    public void changeSong(String filePath) {
        try {
            stop();
            if (filePath != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(Objects.requireNonNull(getClass().getResource(filePath)));
                clip = AudioSystem.getClip();
                clip.open(audioStream);
            }
            play();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Loop continuously
            System.out.println("Starting Clip");
            clip.start();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
        }
    }

    public void changeVolume(double newVolume) {
        volControl.setValue((float) newVolume);
        volume = (float) newVolume;
    }
}

