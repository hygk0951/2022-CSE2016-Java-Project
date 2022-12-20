import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class SoundManager {
    String bgm = "NoCopyrightBGM.wav";
    String shuffle = "shuffle.wav";
    String draw = "draw.wav";

    public static Clip bgmClip, shuffleClip, drawClip;
    public SoundManager() throws URISyntaxException {

    }

    public Clip playClip(String filename, boolean loop, float gain) throws Exception {
        try {
            URL url = SoundManager.class.getResource(filename);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.setFramePosition(0);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(gain);

            if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
            else clip.start();
            return clip;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void playSound(String sound) throws LineUnavailableException, IOException {
        try {
            switch (sound) {
                case "bgm":
                    bgmClip = playClip(bgm, true, -10.0f);
                    break;
                case "shuffle":
                    shuffleClip = playClip(shuffle, false, -10.0f);
                    break;
                case "draw":
                    drawClip = playClip(draw, false, -10.0f);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopSound() {
        if (bgmClip != null) bgmClip.stop();
        if (shuffleClip != null) shuffleClip.stop();
        if (drawClip != null) drawClip.stop();
    }
}
