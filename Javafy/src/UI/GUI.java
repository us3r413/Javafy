package src.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;


import com.formdev.flatlaf.FlatDarkLaf;
import src.concept.MusicPlayer;
import src.concept.Core;
import src.concept.Music;
import src.concept.SoundTrack;


public class GUI extends JFrame {
    public static JFrame frame;
    private JLabel name;
    private JButton startPause;
    private JButton next;
    private JButton last;
    private JButton addTrack;
    private JPanel musiControl;
    private JPanel sideSelctor;
    private JPanel middle;
    private JPanel everything;
    private JSlider progressBar;
    private JSlider volumeBar;
    private Timer progressTimer;
    private MusicPlayer player;
    private Core core;
    private ChatBox chat;
    private SoundTrack currentTrack;
    private MusicList mainList;
    private ArrayList<CustomTrackList> otherTrack = null;
    private Icon playIcon = null;
    private Icon pauseIcon = null;
    private Icon nextIcon = null;
    private Icon lastIcon = null;
    private Icon addIcon = null;
    private boolean latch = false;
    private boolean latch2 = false;
    private volatile long lastInternalChange = 0;
    public GUI(){
        frame = new JFrame();
        frame.setTitle("Javafy");
        frame.setSize(800, 600);
        FlatDarkLaf.setup();
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.getRootPane().putClientProperty("JRootPane.titleBarBackground", Color.black);
        frame.getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.white);
        try {
            playIcon = new ImageIcon(SVGToImageConverter.convertSVGToImage("imgs/play.svg"));
            pauseIcon = new ImageIcon(SVGToImageConverter.convertSVGToImage("imgs/pause.svg"));
            nextIcon = new ImageIcon(SVGToImageConverter.convertSVGToImage("imgs/next.svg"));
            lastIcon = new ImageIcon(SVGToImageConverter.convertSVGToImage("imgs/last.svg"));
            addIcon = new ImageIcon(SVGToImageConverter.convertSVGToImage("imgs/add.svg"));
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error converting SVG to image",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        name = new JLabel("");
        name.setPreferredSize(new Dimension(200,30));
        name.setOpaque(true);
        name.setBackground(Color.BLACK);
        name.setBorder(BorderFactory.createEmptyBorder(0,40,0,40));
        chat = new ChatBox();
        core = new Core();
        otherTrack = new ArrayList<>();
        currentTrack = core.getFullTrack();
        middle = chat.thisThing;
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
                int progressBarValue = (int) Math.round(((double) mouseX / progressBar.getWidth())*1000);
                progressBar.setValue(progressBarValue);
                player.setPosition(progressBarValue / 1000.0);
            }
        });

        volumeBar = new JSlider(0, 1000, 0);
        volumeBar.setUI(new CustomSlider(volumeBar));
        volumeBar.setValue(500);
        volumeBar.setEnabled(true);
        updateVolumeBar();
        volumeBar.addChangeListener(e -> {
            volumeBar.repaint();
            updateVolumeBar();
        });
        volumeBar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int volumeBarValue = (int) Math.round(((double) e.getX() / volumeBar.getWidth()) * 1000);
                volumeBar.setValue(volumeBarValue);
                if (player != null) {
                    player.setVolume(volumeBarValue / 1000.0);
                }
            }
        });
        volumeBar.setBackground(Color.BLACK);
        volumeBar.setPreferredSize(new Dimension(200,30));

        initializePlayer(currentTrack.nextTrack());
        mainList = new MusicList(currentTrack, this::loadMusic, this::addtoTrack);
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
        JPanel controlBar = new JPanel(new BorderLayout());
        trackControl.add(last);
        trackControl.add(startPause);
        trackControl.add(next);


        trackControl.setBackground(Color.BLACK);
        controlBar.add(name, BorderLayout.WEST);
        controlBar.add(trackControl, BorderLayout.CENTER);
        controlBar.add(volumeBar, BorderLayout.EAST);
        progressBar.setBackground(Color.BLACK);
        musiControl = new JPanel(new GridLayout(2,1));
        musiControl.add(controlBar);
        musiControl.add(progressBar);

        Dimension size = new Dimension(150, 30);
        addTrack = new JButton(addIcon);
        addTrack.setContentAreaFilled(false);
        addTrack.setBorderPainted(false);
        addTrack.setFocusPainted(false);
        JButton chatMode = new JButton(new AbstractAction("chat") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (middle != null && middle.getParent() != null) {
                    middle.getParent().remove(middle);
                }
                middle = chat.thisThing;
                everything.add(middle, BorderLayout.CENTER);
                everything.revalidate();
                everything.repaint();
            }
        });
        JButton allMusic = new JButton(new AbstractAction("music") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (middle != null && middle.getParent() != null) {
                    middle.getParent().remove(middle);
                }
                middle = mainList.thisThing;
                everything.add(middle, BorderLayout.CENTER);
                everything.revalidate();
                everything.repaint();
            }
        });
        setSize(chatMode,size);
        setSize(allMusic,size);
        setSize(addTrack,size);
        addTrack.addActionListener(e -> {
            String trackName = JOptionPane.showInputDialog(
                    frame, "Please Input a Name", "New Track", JOptionPane.PLAIN_MESSAGE);
            if(trackName == null) {
                return;
            }
            if(trackName.isEmpty()){
                trackName = "Unnamed Track";
            }
            JButton newButton = new JButton(trackName);
            core.addEmptyTrack(trackName);
            CustomTrackList newList = new CustomTrackList(
                    otherTrack.size(), core.getOthers().getLast(), newButton, this::loadMusic, this::deleteTrack, this::renameTrack
            );
            otherTrack.add(newList);
            int idx = otherTrack.size() - 1;
            newButton.addActionListener(k ->{
                if (middle != null && middle.getParent() != null) {
                    middle.getParent().remove(middle);
                }
                middle = otherTrack.get(idx).thisThing;
                everything.add(middle, BorderLayout.CENTER);
                everything.revalidate();
                everything.repaint();
            });
            markChangeAsInternal();
            core.writeOtherstoJSON();
            setSize(newButton,size);
            sideSelctor.add(newButton, sideSelctor.getComponentZOrder(addTrack));
            sideSelctor.revalidate();
            sideSelctor.repaint();
        });
        sideSelctor = new JPanel();
        sideSelctor.setBackground(Color.BLACK);
        sideSelctor.setLayout(new BoxLayout(sideSelctor, BoxLayout.Y_AXIS));
        sideSelctor.add(chatMode);
        sideSelctor.add(allMusic);
        for(SoundTrack track : core.getOthers()){
            JButton newButton = new JButton(track.getTrackName());
            int idx = otherTrack.size();
            CustomTrackList newList = new CustomTrackList(idx, track, newButton, this::loadMusic,this::deleteTrack, this::renameTrack);
            otherTrack.add(newList);
            newButton.addActionListener(k ->{
                if (middle != null && middle.getParent() != null) {
                    middle.getParent().remove(middle);
                }
                middle = otherTrack.get(idx).thisThing;
                everything.add(middle, BorderLayout.CENTER);
                everything.revalidate();
                everything.repaint();
            });
            setSize(newButton,size);
            sideSelctor.add(newButton);
        }
        sideSelctor.add(addTrack);
        sideSelctor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        everything = new JPanel(new BorderLayout());
        everything.add(musiControl, BorderLayout.SOUTH);
        everything.add(middle, BorderLayout.CENTER);
        everything.add(sideSelctor,BorderLayout.WEST);
        frame.add(everything);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
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
        player.setVolume(volumeBar.getValue() / 1000.0);
        player.setOnMusicEndListener(this::playNextTrack);
        updateName();
        progressBar.setEnabled(true);
        progressBar.setValue(0);
    }
    private void playNextTrack() {
        player.terminate();
        initializePlayer(this.currentTrack.nextTrack());
        player.play();
    }
    private void playLastTrack() {
        player.terminate();
        initializePlayer(this.currentTrack.lastTrack());
        player.play();
    }
    public void loadMusic(Music music, SoundTrack track){
        player.terminate();
        initializePlayer(music);
        startPause.setIcon(playIcon);
        this.currentTrack = track;
        this.currentTrack.setIndex(this.currentTrack.musicTracks.indexOf(music));
        player.play();
    }
    private Icon resizeIcon(String path, int width, int height) {
        BufferedImage img = null;
        try {
            if (path.endsWith(".svg")) {
                try {
                    img = SVGToImageConverter.convertSVGToImage(path);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Error converting SVG to image",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                img = ImageIO.read(new File(path));
            }
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
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            //g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
            g2d.drawImage(currentImage, 0, 0, currentWidth, currentHeight, null);
            g2d.dispose();
            currentImage = tempImage;
        }
        return new ImageIcon(currentImage);
    }
    private void setSize(JButton thisThing,Dimension size){
        thisThing.setPreferredSize(size);
        thisThing.setMinimumSize(size);
        thisThing.setMaximumSize(size);
    }
    private void updateProgressBar() {
        if (player != null && player.isPlaying()) {
            double progress = player.getProgress();
            progressBar.setValue((int) (progress * 1000));
        }
    }
    private void updateVolumeBar() {
        if (player != null) {
            if (volumeBar.getValueIsAdjusting()) {
                double volume = volumeBar.getValue() / 1000.0;
                player.setVolume(volume); // Set player's volume when adjusting
            } else {
                double volume = player.getVolume();
                volumeBar.setValue((int) (volume * 1000)); // Update slider value
            }
        }
    }
    private void updateName(){
        if (player != null) {
            name.setText(this.player.getMusicName());
        } else {
            name.setText("No music loaded");
        }
    }
    private void deleteTrack(SoundTrack track, JButton target){
        int response = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to delete this track?",
                "Delete Track",
                JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            markChangeAsInternal();
            core.deleteTrack(track);
            core.writeOtherstoJSON();
            if (middle != null && middle.getParent() != null) {
                middle.getParent().remove(middle);
            }
            middle = mainList.thisThing;
            everything.add(middle, BorderLayout.CENTER);
            everything.revalidate();
            everything.repaint();
            sideSelctor.remove(target);
            sideSelctor.revalidate();
            sideSelctor.repaint();
        }
    }
    private String renameTrack(SoundTrack track, JButton target){
        String newName = JOptionPane.showInputDialog(
                frame, "New name for track:", "Rename Track", JOptionPane.PLAIN_MESSAGE);
        if(newName == null) {
            return null;
        }
        if(newName.isEmpty()){
            newName = "Unnamed";
        }
        markChangeAsInternal();
        core.renameTrack(track, newName);
        core.writeOtherstoJSON();
        target.setText(newName);
        return newName;
    }
    private void addtoTrack(Music music, SoundTrack target){
        String[] trackNames = core.getOthers().stream()
                .map(SoundTrack::getTrackName)
                .toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(
                frame,
                "Please select a track:",
                "Add to Track",
                JOptionPane.PLAIN_MESSAGE,
                null,
                trackNames,
                trackNames.length > 0 ? trackNames[0] : null
        );
        if (selected != null) {
            SoundTrack chosenTrack = core.getOthers().stream()
                    .filter(t -> t.getTrackName().equals(selected))
                    .findFirst()
                    .orElse(null);
            if (chosenTrack != null) {
                if(chosenTrack.containsMusic(music)){
                    JOptionPane.showMessageDialog(null,
                            "Music already in track",
                            "Duplicate Detected",
                            JOptionPane.PLAIN_MESSAGE);
                    return;
                }
                int idx = -1;
                for (int i = 0; i < otherTrack.size(); i++) {
                    if (otherTrack.get(i).getTrack().equals(chosenTrack)) {
                        idx = i;
                        break;
                    }
                }
                if(idx == -1){
                    JOptionPane.showMessageDialog(null,
                            "Error: Track not found",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                SoundTrack newTrack = core.addMusicToTrack(chosenTrack, music);
                markChangeAsInternal();
                core.writeOtherstoJSON();
                CustomTrackList updated = new CustomTrackList(
                        idx,newTrack,otherTrack.get(idx).getBut(), this::loadMusic, this::deleteTrack, this::renameTrack
                );
                otherTrack.set(idx, updated);
            }
        }
    }
    public void markChangeAsInternal() {
        lastInternalChange = System.currentTimeMillis();
    }
    public boolean isInternalChange() {
        return System.currentTimeMillis() - lastInternalChange < 1000;
    }
}