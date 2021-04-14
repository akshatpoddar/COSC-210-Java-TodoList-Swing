package ui.views;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public interface SoundPlayer {

    static void playErrorSound(){
        try{
            File sound = new File("/Users/akshat/Desktop/Courses/Cosc 210/Project/resources/error.wav");
            Clip c = AudioSystem.getClip();
            c.open(AudioSystem.getAudioInputStream(sound));
            c.start();
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException lineUnavailableException) {
            lineUnavailableException.printStackTrace();
        }
    }

}
