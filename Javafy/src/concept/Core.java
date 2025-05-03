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
            double durationInSeconds = 0.0;
//            try {
//                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
//                AudioFormat format = audioInputStream.getFormat();
//                long frames = audioInputStream.getFrameLength();
//                durationInSeconds = (frames + 0.0) / format.getFrameRate();
//            } catch (Exception e) {
//                System.err.println("Error reading file: " + file.getAbsolutePath());
//                durationInSeconds = 0.0;
//            }
            Music music = new Music(
                    file.getName().replace(".mp3", ""),
                    file.getAbsolutePath(),
                    durationInSeconds,
                    "N/A"
            );
            this.fullTrack.addMusic(music);
        }
    }
    public void loadCustomTrack(SoundTrack track){
        this.customTrack = track;
    }
}
