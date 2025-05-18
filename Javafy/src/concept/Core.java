package src.concept;

import javax.print.attribute.standard.MediaSize;
import javax.sound.sampled.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class Core {
    private SoundTrack fullTrack;
    private ArrayList<SoundTrack> others = null;
    public Core(){
        this.fullTrack = new SoundTrack("allMusic");
        this.others = new ArrayList<>();
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
        readMusicFromJSON();
    }
    private void readMusicFromJSON(){
        File file = new File("music/music.json");
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("{\"tracks\": []}");
                }
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            JSONObject jsonObject = new JSONObject(content);
            JSONArray jsonArray = jsonObject.getJSONArray("tracks");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject trackObject = jsonArray.getJSONObject(i);
                String trackName = trackObject.getString("trackName");
                SoundTrack track = new SoundTrack(trackName);
                JSONArray musicArray = trackObject.has("musics") ? trackObject.getJSONArray("musics") : new JSONArray();
                for (int j = 0; j < musicArray.length(); j++) {
                    JSONObject musicObject = musicArray.getJSONObject(j);
                    String path = musicObject.getString("path");
                    File musicFile = new File(path);
                    if (musicFile.exists()) {
                        Music music = new Music(
                                musicObject.getString("name"),
                                path,
                                musicObject.getDouble("duration"),
                                musicObject.getString("artist")
                        );
                        track.addMusic(music);
                    }
                }
                others.add(track);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public SoundTrack getFullTrack(){
        return fullTrack;
    }
    public void addEmptyTrack(String name){
        SoundTrack empty = new SoundTrack(name);
        others.add(empty);
    }
    public ArrayList<SoundTrack> getOthers(){
        return this.others;
    }
    public void addOthers(SoundTrack track) {
        if (others == null) {
            others = new ArrayList<>();
        }
    }
    public void writeOtherstoJSON(){
        JSONArray jsonArray = new JSONArray();
        for (SoundTrack track : others) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("trackName", track.trackName);
            for(Music music : track.getMusicTracks()){
                JSONObject musicObject = new JSONObject();
                musicObject.put("name", music.getMusicName());
                musicObject.put("path", music.getFilePath());
                musicObject.put("duration", music.getDuration());
                musicObject.put("artist", music.getArtist());
                jsonObject.append("musics", musicObject);
            }
            jsonArray.put(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("tracks", jsonArray);
        try (FileWriter file = new FileWriter("music/music.json")) {
            file.write(jsonObject.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void deleteTrack(SoundTrack track) {
        if (others != null) {
            others.remove(track);
        }
    }
    public void renameTrack(SoundTrack track, String newName) {
        if (others != null && others.contains(track)) {
            track.trackName = newName;
        }
    }
    public SoundTrack addMusicToTrack(SoundTrack track, Music music) {
        if (others != null && others.contains(track)) {
            track.addMusic(music);
        }
        return track;
    }
}
