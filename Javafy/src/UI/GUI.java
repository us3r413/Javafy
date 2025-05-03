package src.UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import src.concept.MusicPlayer;
import src.concept.Core;
import src.concept.Music;
import java.awt.Image;

public class GUI extends JFrame {
    private static JFrame frame;
    private JButton startPause;
    private JButton next;
    private JButton last;
    private JPanel musiControl;
    private MusicPlayer player;
    private Core core;
    private boolean isPlaying = false;
    public GUI(){
        setTitle("Javafy");
        setSize(800, 600);
        Icon playIcon = resizeIcon("imgs/play.png", 30, 30);
        Icon pauseIcon = resizeIcon("imgs/pause.png", 30, 30);
        core = new Core();
        initializePlayer(core.fullTrack.nextTrack());
        isPlaying = false;
        startPause = new JButton(pauseIcon);
        startPause.setContentAreaFilled(false);
        startPause.setBorderPainted(false);
        startPause.setFocusPainted(false);
        startPause.addActionListener( new AbstractAction("Start/Pause") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if(isPlaying){
                    pause();
                    startPause.setIcon(pauseIcon);
                    isPlaying = false;
                }else{
                    start();
                    startPause.setIcon(playIcon);
                    isPlaying = true;
                }
            }
        });
        next = new JButton(new AbstractAction("Next") {
            @Override
            public void actionPerformed(ActionEvent e) {
                playNextTrack();
                startPause.setIcon(playIcon);
            }
        });
        last = new JButton(new AbstractAction("Last") {
            @Override
            public void actionPerformed(ActionEvent e) {
                playLastTrack();
                startPause.setIcon(playIcon);
            }
        });
        musiControl = new JPanel();
        musiControl.add(last);
        musiControl.add(startPause);
        musiControl.add(next);
        add(musiControl);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    private void start(){
        if(player == null) {
            JOptionPane.showMessageDialog(null,
                    "Music not loaded",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }else player.play();
    }
    private void pause(){
        try {
            player.pause();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null,
                    "Problem pausing music",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private void initializePlayer(Music music) {
        player = new MusicPlayer(music);
        player.setOnMusicEndListener(this::playNextTrack);
    }
    private void playNextTrack() {
        player.terminate();
        initializePlayer(this.core.fullTrack.nextTrack());
        player.play();
        isPlaying = true;
    }
    private void playLastTrack() {
        player.terminate();
        initializePlayer(this.core.fullTrack.lastTrack());
        player.play();
        isPlaying = true;
    }
    private Icon resizeIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
}
