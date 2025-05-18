package src.concept;

import java.util.Objects;

public class Music {
    private final String musicName;
    private final String filePath;
    private final double duration;
    private final String artist;
    Music(String nombre,String path,double duration,String artist) {
        this.musicName = nombre;
        this.filePath = path;
        this.duration = duration;
        this.artist = artist;
    }

    public double getDuration() {
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return music.getFilePath().equals(filePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePath);
    }
}
