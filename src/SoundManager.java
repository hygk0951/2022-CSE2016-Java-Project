import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class SoundManager {
    File bgmFile = new File("C:\\Users\\stonl\\Documents\\gitPractice\\Cloning\\2022-CSE2016-Java-Project\\sounds\\NoCopyrightBGM.wav");
    AudioInputStream audioStream = AudioSystem.getAudioInputStream(bgmFile);
    Clip clip = AudioSystem.getClip();

    public SoundManager() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        clip.open(audioStream);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
