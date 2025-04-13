package src.concept;
import org.json.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MusicPlayer {
    SoundTrack track;
    public void updateTrack() {
        JSONObject obj = new JSONObject("../../resource/music.json");
        this.track = new SoundTrack("track1");
        JSONArray trackArray = obj.getJSONArray("track1");
        for (int i = 0; i < trackArray.length(); i++) {
            JSONObject trackObj = trackArray.getJSONObject(i);
            Music newMusic = new Music(
                    trackObj.getString("musicName"),
                    trackObj.getString("filePath"),
                    trackObj.getInt("duration"),
                    trackObj.getString("artist")
            );
            this.track.addMusic(newMusic);
        }
    }
    public void updateJson(){
        File folder = new File("../../resource/");
        File[] listOfFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".mp3"));
        if (listOfFiles == null) {
            System.err.println("No MP3 files found or path invalid.");
            return;
        }
        JSONArray trackList = new JSONArray();
        for(File file:listOfFiles){
            JSONObject track = new JSONObject();
            String[] parts = file.getName().replace(".mp3", "").split("_");
            track.put("musicName", parts[0]);
            track.put("filePath", file.getAbsolutePath());
            track.put("duration", file.length());
            track.put("artist", parts[1]);
            trackList.put(track);
        }
        JSONObject musicData = new JSONObject();
        musicData.put("track1", trackList);
        try (FileWriter fileWriter = new FileWriter("../../resource/music.json")) {
            fileWriter.write(musicData.toString(4)); // Indent with 4 spaces
            System.out.println("JSON file updated successfully.");
        } catch (IOException e) {
            System.err.println("Error writing to music.json: " + e.getMessage());
        }
    }
    public void play(){

    }
}
