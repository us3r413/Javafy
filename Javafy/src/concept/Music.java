package src.concept;

public class Music {
    private final String musicName;
    private final String filePath;
    private final int duration;
    private final String artist;
    Music(String nombre,String path,int duration,String artist) {
        this.musicName = nombre;
        this.filePath = path;
        this.duration = duration;
        this.artist = artist;
    }

    public int getDuration() {
        return duration;
    }

    public String getArtist() {
        return artist;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getMusicName() {
        return musicName;
    }
}
