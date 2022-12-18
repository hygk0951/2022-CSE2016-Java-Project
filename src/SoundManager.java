import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class SoundManager {
    File bgmFile = new File("sounds/NoCopyrightBGM.wav");
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(bgmFile);
    Clip clip = AudioSystem.getClip();

    public SoundManager() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        clip.open(audioStream);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
