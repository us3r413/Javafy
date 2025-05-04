package src.concept;

import javax.sound.sampled.*;
import java.io.File;

public class Core {
    public SoundTrack fullTrack;
    public SoundTrack customTrack = null;
    public Core(){
        this.fullTrack = new SoundTrack("allMusic");
        File folder = new File("music/");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
        if (listOfFiles == null) {
            System.err.println("No MP3 files found or path invalid.");
        }
        assert listOfFiles != null;
        for(File file:listOfFiles){
            Music music = new Music(
                    file.getName().replace(".mp3", ""),
                    file.getAbsolutePath(),
                    0.0,
                    "N/A"
            );
            this.fullTrack.addMusic(music);
        }
    }
    public void loadCustomTrack(SoundTrack track){
        this.customTrack = track;
    }
}
