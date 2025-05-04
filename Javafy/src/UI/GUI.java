package src.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;

import src.concept.MusicPlayer;
import src.concept.Core;
import src.concept.Music;

public class GUI extends JFrame {
    private static JFrame frame;
    private JButton startPause;
    private JButton next;
    private JButton last;
    private JPanel musiControl;
    private JSlider progressBar;
    private Timer progressTimer;
    private MusicPlayer player;
    private Core core;
    private boolean latch = false;
    private boolean latch2 = false;
    public GUI(){
        setTitle("Javafy");
        setSize(800, 600);
        Icon playIcon = new ImageIcon("imgs/play.png");
        Icon pauseIcon = new ImageIcon("imgs/pause.png");
        Icon nextIcon = new ImageIcon("imgs/next.png");
        Icon lastIcon = new ImageIcon("imgs/last.png");
        core = new Core();
        progressBar = new JSlider(0,1000,0);
        progressBar.setUI(new CustomSlider(progressBar));
        progressBar.setValue(0);
        progressBar.setEnabled(false);
        progressBar.addChangeListener(e -> {
            progressBar.repaint();
            if (!progressBar.getValueIsAdjusting()) {
                if(latch){
                    player.setPosition(progressBar.getValue() / 1000.0);
                    latch = false;
                    if (!player.isPlaying() && latch2) {
                        player.play();
                        latch2 = true;
                    }
                }
            }
            else{
                player.pause();
                latch = true;
            }
        });
        progressBar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int mouseX = e.getX();
                int progressBarValue = (int) Math.round(((double) mouseX / progressBar.getWidth()) * progressBar.getMaximum());
                progressBar.setValue(progressBarValue);
                player.setPosition(progressBarValue / 1000.0);
            }
        });

        initializePlayer(core.fullTrack.nextTrack());
        startPause = new JButton(pauseIcon);
        startPause.setContentAreaFilled(false);
        startPause.setBorderPainted(false);
        startPause.setFocusPainted(false);
        startPause.addActionListener( new AbstractAction("") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                if(player.isPlaying()){
                    pause();
                    startPause.setIcon(pauseIcon);
                }else{
                    start();
                    startPause.setIcon(playIcon);
                }
            }
        });
        next = new JButton(new AbstractAction("") {
            @Override
            public void actionPerformed(ActionEvent e) {
                playNextTrack();
                startPause.setIcon(playIcon);
            }
        });
        next.setContentAreaFilled(false);
        next.setBorderPainted(false);
        next.setFocusPainted(false);
        next.setIcon(nextIcon);
        last = new JButton(new AbstractAction("") {
            @Override
            public void actionPerformed(ActionEvent e) {
                playLastTrack();
                startPause.setIcon(playIcon);
            }
        });
        last.setContentAreaFilled(false);
        last.setBorderPainted(false);
        last.setFocusPainted(false);
        last.setIcon(lastIcon);
        progressTimer = new Timer(1000, e -> {
            updateProgressBar();
        });
        progressTimer.start();
        JPanel trackControl = new JPanel(new GridLayout(1, 3));
        trackControl.add(last);
        trackControl.add(startPause);
        trackControl.add(next);
        trackControl.setBorder(BorderFactory.createEmptyBorder(0,300,0,300));
        trackControl.setBackground(Color.BLACK);
        progressBar.setBackground(Color.BLACK);
        musiControl = new JPanel(new GridLayout(2,1));
        musiControl.add(trackControl);
        musiControl.add(progressBar);

        JPanel everything = new JPanel(new BorderLayout());
        everything.add(musiControl, BorderLayout.SOUTH);
        add(everything);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    private void start(){
        if(player == null) {
            JOptionPane.showMessageDialog(null,
                    "Music not loaded",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }else {
            player.play();
            latch2 = true;
        }
    }
    private void pause(){
        try {
            player.pause();
            latch2 = false;
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
        progressBar.setEnabled(true);
        progressBar.setValue(0);
    }
    private void playNextTrack() {
        player.terminate();
        initializePlayer(this.core.fullTrack.nextTrack());
        player.play();
    }
    private void playLastTrack() {
        player.terminate();
        initializePlayer(this.core.fullTrack.lastTrack());
        player.play();
    }
    private Icon resizeIcon(String path, int width, int height) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error reading GUI images",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        assert img != null;
        BufferedImage currentImage = img;
        int currentWidth = img.getWidth();
        int currentHeight = img.getHeight();

        while (currentWidth != width && currentHeight != height) {
            currentWidth /= 2;
            currentHeight /= 2;
            if(currentHeight<height) {
                currentHeight = height;
            }
            if(currentWidth<width) {
                currentWidth = width;
            }
            BufferedImage tempImage = new BufferedImage(currentWidth, currentHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = tempImage.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
            g2d.drawImage(currentImage, 0, 0, currentWidth, currentHeight, null);
            g2d.dispose();
            currentImage = tempImage;
        }
        return new ImageIcon(currentImage);
    }
    private void updateProgressBar() {
        if (player != null && player.isPlaying()) {
            double progress = player.getProgress();
            progressBar.setValue((int) (progress * 1000));
        }
    }
}
