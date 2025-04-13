package src.concept;

import java.util.ArrayList;
import java.util.Collections;

public class SoundTrack {
    String trackName;
    ArrayList <Music> musicTracks;
    SoundTrack(String nombre){
        this.trackName = nombre;
        this.musicTracks = new ArrayList<>();
    }
    public void addMusic(Music music){
        musicTracks.add(music);
    }
    public void removeMusic(Music music){
        musicTracks.remove(music);
    }
    public void shuffleMusic(){
        Collections.shuffle(musicTracks);
    }
    public String getMusicList() {
        String musicList = "";
        int i=1;
        for (Music music : musicTracks) {
            musicList = musicList.concat(i+". "+music.getMusicName() + "by "+ music.getArtist()+"\n");
            i++;
        }
        return musicList;
    }
    public Music getMusic(){
        return musicTracks.getFirst();
    }
}
