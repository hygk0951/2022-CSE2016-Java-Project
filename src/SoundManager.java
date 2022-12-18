import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundManager {
    String bgm = "assets/NoCopyrightBGM.wav";
    String shuffle = "assets/shuffle.wav";
    String draw = "assets/draw.wav";
    Clip bgmClip, shuffleClip, drawClip;

    public SoundManager() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        try {
            bgmClip = playClip(bgm, true, -10.0f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Clip playClip(String filename, boolean loop, float gain) throws Exception {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename));
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.setFramePosition(0);

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(gain);

        if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
        else clip.start();
        return clip;
    }

    public void playSound(String sound) throws LineUnavailableException, IOException {
        try {
            switch (sound) {
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
}
