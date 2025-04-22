package src.concept;
import javafx.scene.media.*;
import java.io.File;


public class MusicPlayer {
    private MediaPlayer mp3Player;
    private final Music music;
    MusicPlayer(Music music) {
        this.music = music;
        File file = new File(music.getFilePath());
        try {
            Media media = new Media(file.toURI().toString());
            mp3Player = new MediaPlayer(media);
            mp3Player.setOnEndOfMedia(() -> {
                System.out.println("Music finished playing.");
                terminate();
            });
        }catch (Exception e) {
            System.out.println("Problem loading music " + e.getMessage());
        }
    }
    public void play(){
        try {
            mp3Player.play();
            System.out.println("Playing: " + music.getMusicName());
        } catch (MediaException e) {
            System.out.println("Problem playing music " + music.getMusicName() + ": " + e.getMessage());
        }
    }
    public void pause(){
        try {
            mp3Player.pause();
            System.out.println("Paused: " + music.getMusicName());
        }catch (Exception e){
            System.out.println("Problem pausing music " + music.getMusicName()+' '+ e);
        }
    }
    public void terminate(){
        mp3Player.stop();
    }
}
