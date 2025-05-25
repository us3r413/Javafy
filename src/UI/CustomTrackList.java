package src.UI;
import src.concept.SoundTrack;
import src.concept.Music;

import javax.swing.*;
import java.awt.*;

public class CustomTrackList{
    public JPanel thisThing;
    private JPanel control;
    private JPanel musicPanel;
    private JPanel Top;
    private JLabel trackName;
    private Icon deleteIcon = null;
    private Icon trashIcon = null;
    private JButton thisbutt;
    private final int idx;
    private SoundTrack thisTrack;
    CustomTrackList(int idx, SoundTrack track, JButton b, load func, delete deleteFunc, rename renameFunc, deleteMusicfromTrack deleteMusic){
        this.idx = idx;
        this.thisbutt = b;
        this.thisTrack = track;
        thisThing = new JPanel();
        thisThing.setLayout(new BorderLayout());
        control = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        musicPanel = new JPanel();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.Y_AXIS));
        Top = new JPanel(new GridLayout(2,1));
        try {
            deleteIcon = new ImageIcon(SVGToImageConverter.convertSVGToImage("imgs/delete.svg"));
            trashIcon = new ImageIcon(SVGToImageConverter.convertSVGToImage("imgs/trashcan.svg"));
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error converting SVG to image",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        trackName = new JLabel(track.getTrackName());
        trackName.setFont(new Font(trackName.getFont().getName(), Font.BOLD, 40));
        trackName.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton rename = new JButton("Rename");
        rename.addActionListener(k->{
            String newName = renameFunc.rename(track, b);
            if(newName != null) {
                trackName.setText(newName);
            }
        });
        JButton deleteTrack = new JButton(deleteIcon);
        deleteTrack.setContentAreaFilled(false);
        deleteTrack.setBorderPainted(false);
        deleteTrack.setFocusPainted(false);
        deleteTrack.addActionListener(e -> {
            deleteFunc.delete(track, b);
        });
        control.add(rename);
        control.add(deleteTrack);
        Top.add(trackName);
        Top.add(control);
        for(Music musics : track.getMusicTracks()){
            JPanel thisPanel = new JPanel();
            thisPanel.setLayout( new BoxLayout(thisPanel, BoxLayout.X_AXIS));
            JButton button = new JButton(musics.getMusicName());
            JButton delete = new JButton(trashIcon);
            Dimension trashsize = new Dimension(100, 40);
            Dimension btnSize = new Dimension(0, 40);
            button.setPreferredSize(btnSize);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE,40));
            button.setMinimumSize(btnSize);

            delete.setPreferredSize(trashsize);
            delete.setMaximumSize(trashsize);
            delete.setMinimumSize(trashsize);

            delete.addActionListener(e->{
                deleteMusic.deleteMusicfromTrack(track, musics);
                musicPanel.remove(thisPanel);
                musicPanel.revalidate();
                musicPanel.repaint();
            });

            button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            button.setHorizontalAlignment(SwingConstants.LEFT);
            button.addActionListener(e -> {
                func.load(musics, track);
            });
            thisPanel.add(button);
            thisPanel.add(Box.createHorizontalGlue());
            thisPanel.add(delete);
            thisPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            musicPanel.add(thisPanel);
        }
        thisThing.add(Top, BorderLayout.NORTH);
        thisThing.add(musicPanel, BorderLayout.CENTER);
    }
    public int getIdx(){
        return idx;
    }
    public JButton getBut(){
        return thisbutt;
    }
    public SoundTrack getTrack(){
        return thisTrack;
    }
}
