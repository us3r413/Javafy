package src.UI;
import src.concept.SoundTrack;
import src.concept.Music;
import javax.swing.*;
import java.awt.*;

public class MusicList {
    JPanel thisThing;
    MusicList(SoundTrack track, load func, addtoTrack addCustom){
        thisThing = new JPanel();
        thisThing.setLayout(new BoxLayout(thisThing, BoxLayout.Y_AXIS));
        for(Music musics : track.getMusicTracks()){
            JPanel thisMusic = new JPanel();
            thisMusic.setLayout( new BoxLayout(thisMusic, BoxLayout.X_AXIS));
            JButton button = new JButton(musics.getMusicName());
            JButton addtoTrack = new JButton("Add to Track");
            addtoTrack.addActionListener(e -> {
                addCustom.addtoTrack(musics, track);
            });
            button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            addtoTrack.setMaximumSize(new Dimension(120,40));
            addtoTrack.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            button.addActionListener(e -> {
                func.load(musics, track);
            });
            thisMusic.add(button);
            thisMusic.add(Box.createHorizontalGlue());
            thisMusic.add(addtoTrack);
            thisMusic.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            thisThing.add(thisMusic);
        }
    }
}
