package src.concept;
import org.json.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MusicPlayer {
    SoundTrack track;
    public void loadTrack(SoundTrack track){
        this.track = track;
    }
    public void play(){
        
    }
    public void shuffle(){
        this.track.shuffleMusic();
    }
}
