package src.concept;
import javafx.scene.media.*;
import java.io.File;


public class MusicPlayer {
    private MediaPlayer mp3Player;
    private final Music music;
    private OnMusicFinishedListener listener;
    public interface OnMusicFinishedListener {
        void onMusicEnd();
    }
    public MusicPlayer(Music music) {
        this.music = music;
        File file = new File(music.getFilePath());
        try {
            Media media = new Media(file.toURI().toString());
            mp3Player = new MediaPlayer(media);
            mp3Player.setOnEndOfMedia(() -> {
                System.out.println("Music finished playing");
                terminate();
                if(listener!= null){
                    listener.onMusicEnd();
                }
            });
        }catch (Exception e) {
            System.out.println("Problem loading music " + e.getMessage());
        }
    }
    public void play(){
        try {
            mp3Player.play();
            System.out.println("Playing -> " + music.getMusicName());
        } catch (MediaException e) {
            System.out.println("Problem playing music " + music.getMusicName() + ": " + e.getMessage());
        }
    }
    public void pause(){
        try {
            mp3Player.pause();
            System.out.println("Paused -> " + music.getMusicName());
        }catch (Exception e){
            System.out.println("Problem pausing music " + music.getMusicName()+' '+ e);
        }
    }
    public void terminate(){
        mp3Player.stop();
    }
    public double getProgress() {
        return mp3Player.getCurrentTime().toMillis() / mp3Player.getTotalDuration().toMillis();
    }
    public double getCurrentTime(){
        return mp3Player.getCurrentTime().toMillis();
    }
    public void setPosition(double progress){
        try {
            mp3Player.seek(mp3Player.getTotalDuration().multiply(progress));
        }catch (Exception e){
            System.out.println("Problem setting position " + music.getMusicName() + ": " + e.getMessage());
        }
    }
    public Double getTotalDuration() {
        return mp3Player.getTotalDuration().toMillis();
    }
    public void setOnMusicEndListener(OnMusicFinishedListener listener) {
        this.listener = listener;
    }
    public Boolean isPlaying() {
        return mp3Player.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
