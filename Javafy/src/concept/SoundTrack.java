package src.concept;

import java.util.ArrayList;
import java.util.Collections;

public class SoundTrack {
    String trackName;
    public ArrayList <Music> musicTracks;
    private int currentIndex;
    SoundTrack(String nombre){
        this.trackName = nombre;
        this.musicTracks = new ArrayList<>();
        this.currentIndex = -1;
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
    public ArrayList<Music> getMusicTracks(){
        return musicTracks;
    }
    public String getMusicList() {
        String musicList = "";
        int i=1, s = currentIndex+1;
        for (Music music : musicTracks) {
            musicList = musicList.concat(((i==s)?">>":"  ")+i+". "+music.getMusicName() + " by "+ music.getArtist()+"\n");
            i++;
        }
        return musicList;
    }
    public String getTrackName() {
        return trackName;
    }
    public Music lastTrack(){
        if (musicTracks.isEmpty()) {
            return null;
        }
        currentIndex = (currentIndex - 1 + musicTracks.size()) % musicTracks.size(); // Circular decrement
        return musicTracks.get(currentIndex);
    }
    public Music nextTrack() {
        if (musicTracks.isEmpty()) {
            return null;
        }
        currentIndex = (currentIndex + 1) % musicTracks.size(); // Circular increment
        return musicTracks.get(currentIndex);
    }
    public void setIndex(int n){
        if(musicTracks.size()<=n || n < 0){
            System.out.println("Error Message : Invalid Index");
        }else {
            currentIndex = n;
        }
    }
    public Music findMusicByName(String name) {
        for (Music music : musicTracks) {
            if (music.getMusicName().equalsIgnoreCase(name)) {
                return music;
            }
        }
        return null;
    }
    public double getDuration() {
        return musicTracks.get(currentIndex).getDuration();
    }
    public int getIndexfromObj(Music music){
        for (int i = 0; i < musicTracks.size(); i++) {
            if (musicTracks.get(i).equals(music)) {
                return i;
            }
        }
        return -1; // Not found
    }
    public boolean containsMusic(Music music) {
        return musicTracks.contains(music);
    }
}
