package src.UI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionListener;


import src.AdvConnect.*;
import src.concept.Music;
import src.concept.SoundTrack;

public class ChatBox {
    public JPanel thisThing;
    private JPanel lower;
    private JPanel in;
    private JPanel textArea;
    private JPanel hostArea;
    private JPanel clientArea;
    private JPanel mode;
    private JTextArea display;
    private JScrollPane dScroll;
    private JTextField userType;
    private JButton input;
    private JButton sendMP3;
    private JRadioButton hostMode;
    private JRadioButton clientMode;
    private ButtonGroup modeGroup;
    private boolean isActive = false;
    private boolean isHost = false;
    private SoundTrack musicList;
    private JButton send;
    private Host host;
    private Client client;
    ChatBox(SoundTrack m){
        this.musicList = m;
        thisThing = new JPanel(new BorderLayout());
        hostAreaInitialize();
        clientAreaInitialize();
        display = new JTextArea();
        textArea = new JPanel(new BorderLayout());
        lower = new JPanel(new BorderLayout());
        in = textArea;
        display.setEditable(false);
        dScroll = new JScrollPane(display);
        userType = new JTextField();
        send = new JButton("Send");
        ActionListener sendAction = e -> {
            if(isActive && isHost && host != null) {
                host.sendMessage(userType.getText());
            } else if (isActive && !isHost && client != null) {
                client.sendMessage(userType.getText());
            }
            display.append("You: " + userType.getText()+"\n");
        };
        input = new JButton("Input");
        input.setSelected(true);
        input.addActionListener(e -> {
            modeGroup.clearSelection();
            lower.remove(in);
            in = textArea;
            lower.add(in,BorderLayout.CENTER);
            lower.revalidate();
            lower.repaint();
        });

        sendMP3 = new JButton("Send MP3");
        sendMP3.addActionListener(e -> {
            if(isActive && isHost && host != null) {
                if(host.getClients().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "No clients connected",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                String[] options = new String[musicList.getSize()];
                for(int i = 0; i < musicList.getSize(); i++){
                    options[i] = musicList.getMusic(i).getMusicName();
                }
                String selected = (String) JOptionPane.showInputDialog(
                        null,
                        "Select a music file to send",
                        "Send MP3",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                String[] option2 = new String[host.getClientCount()];
                for(int i = 0; i < host.getClientCount(); i++){
                    option2[i] = host.getClients().get(i).getUsername();
                }
                if(selected != null){
                    String user = (String) JOptionPane.showInputDialog(
                            null,
                            "Select a music file to send",
                            "Send MP3",
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            option2,
                            option2[0]
                    );
                    if(user != null) {
                        Music music = musicList.findMusicByName(selected);
                        int idx = host.getClientIndex(user);
                        if (music != null && idx != -1) {
                            host.sendMP3(idx, music.getFilePath());
                            display.append("You sent: " + selected + " to " + user + "\n");
                        } else {
                            JOptionPane.showMessageDialog(
                                    null,
                                    "Music not found",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }else{
                        JOptionPane.showMessageDialog(
                                null,
                                "No user selected",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            } else if (isActive && !isHost && client != null) {
                if(client.getOtherUsers().isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "You are not connected to any host",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
                String[] options = new String[musicList.getSize()];
                for(int i = 0; i < musicList.getSize(); i++){
                    options[i] = musicList.getMusic(i).getMusicName();
                }
                String selected = (String) JOptionPane.showInputDialog(
                        null,
                        "Select a music file to send",
                        "Send MP3",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]
                );
                if(selected != null){
                    Music music = musicList.findMusicByName(selected);
                    if(music != null) {
                        String user = (String) JOptionPane.showInputDialog(
                                null,
                                "Select a music file to send",
                                "Send MP3",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                client.getOtherUsers().toArray(new String[0]),
                                client.getOtherUsers().getFirst()
                        );
                        if(user != null) {
                            int idx = client.getOtherUsers().indexOf(user);
                            if (idx != -1) {
                                display.append("Sending...\n");
                                client.sendMP3toOthers(idx, music.getFilePath());
                                display.append("You sent " + selected + " to " + user + "\n");
                            } else {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "User not found",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );
                            }
                        }else{
                            JOptionPane.showMessageDialog(
                                    null,
                                    "No user selected",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }else{
                        JOptionPane.showMessageDialog(
                                null,
                                "Music not found",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        });
        hostMode = new JRadioButton("Host Mode");
        hostMode.addActionListener(e -> {
            if(!isActive || isHost) {
                lower.remove(in);
                in = hostArea;
                lower.add(in, BorderLayout.CENTER);
                lower.revalidate();
                lower.repaint();
            }else {
                JOptionPane.showMessageDialog(
                        null,
                        "You are already in active client mode",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                hostMode.setSelected(false);
            }
        });

        clientMode = new JRadioButton("Client Mode");
        clientMode.addActionListener(e -> {
            if(!isActive || !isHost){
                lower.remove(in);
                in = clientArea;
                lower.add(in, BorderLayout.CENTER);
                lower.revalidate();
                lower.repaint();
            }else {
                JOptionPane.showMessageDialog(
                        null,
                        "You are already in active host mode",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                clientMode.setSelected(false);
            }
        });
        modeGroup = new ButtonGroup();
        modeGroup.add(hostMode);
        modeGroup.add(clientMode);
        mode = new JPanel(new FlowLayout(FlowLayout.LEFT));
        mode.add(input);
        mode.add(hostMode);
        mode.add(clientMode);
        display.setBackground(Color.GRAY);
        send.addActionListener(sendAction);
        userType.addActionListener(sendAction);
        userType.setBackground(Color.GRAY);
        userType.setBorder(BorderFactory.createEmptyBorder());
        display.setFont(new Font("Arial", Font.BOLD, 18));
        display.setForeground(Color.WHITE);
        userType.setFont(new Font("Arial", Font.BOLD, 16));
        userType.setForeground(Color.WHITE);
        lower.add(mode,BorderLayout.NORTH);
        textArea.add(userType,BorderLayout.CENTER);
        textArea.add(send,BorderLayout.EAST);
        lower.add(in,BorderLayout.CENTER);
        thisThing.add(dScroll,BorderLayout.CENTER);
        dScroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dScroll.setBackground(Color.BLACK);
        lower.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lower.setBackground(Color.BLACK);
        thisThing.add(lower,BorderLayout.SOUTH);
        thisThing.setBackground(Color.BLACK);
        thisThing.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollBar verticalBar = dScroll.getVerticalScrollBar();
        verticalBar.setUI(new BasicScrollBarUI() {
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(255, 255, 255, 120);
                this.trackColor = Color.GRAY;
            }
        });
    }
    public void hostAreaInitialize(){
        hostArea = new JPanel();
        hostArea.setLayout(new BoxLayout(hostArea, BoxLayout.Y_AXIS));
        JTextField hostIP = new JTextField("Host IP");
        JTextField hostName = new JTextField("Host Name");
        hostName.setText("Default Host Name");
        hostName.setEditable(true);
        hostIP.setEnabled(false);
        Thread hostThread = new Thread(() -> {
            while (isActive) {
                host.listenforClient();
            }
        });
        Thread getMessageThread = new Thread(() -> {
            while (isActive) {
                String message = host.getM();
                if (message != null && !message.isEmpty()) {
                    display.append(message);
                }
            }
        });
        JButton hostButton = new JButton("Host");
        hostButton.addActionListener(e -> {
            if(!isActive){
                clientMode.setEnabled(false);
                host = new Host(hostName.getText());
                isActive = true;
                isHost = true;
                hostButton.setText("Terminate Host");
                hostName.setEditable(false);
                hostIP.setText(host.getIPAddress());
                hostThread.start();
                getMessageThread.start();
                mode.add(sendMP3);
                mode.revalidate();
                mode.repaint();
            }else{
                clientMode.setEnabled(true);
                host.terminateConnection();
                hostThread.interrupt();
                getMessageThread.interrupt();
                isActive = false;
                isHost = false;
                hostIP.setText("Host IP");
                hostButton.setText("Host");
                mode.remove(sendMP3);
                mode.revalidate();
                mode.repaint();
            }
        });
        hostIP.setAlignmentX(Component.LEFT_ALIGNMENT);
        hostName.setAlignmentX(Component.LEFT_ALIGNMENT);
        hostButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        hostArea.add(hostIP);
        hostArea.add(hostName);
        hostArea.add(hostButton);
    }
    public void clientAreaInitialize(){
        clientArea = new JPanel();
        clientArea.setLayout(new BoxLayout(clientArea, BoxLayout.Y_AXIS));
        JTextField hostIP = new JTextField("IP to connect to");
        JTextField hostName = new JTextField("Client Name");
        hostName.setText("Default Client Name");
        hostName.setEditable(true);
        hostIP.setEditable(true);
        JButton clientButton = new JButton("Connect");
        clientButton.addActionListener(e -> {
            if(!isActive){
                hostMode.setEnabled(false);
                if(hostIP.getText().isEmpty() || hostName.getText().isEmpty()){
                    JOptionPane.showMessageDialog(
                            null,
                            "Please enter valid IP and Name",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }else{
                    client = new Client(hostIP.getText(), hostName.getText());
                    isActive = true;
                    isHost = false;
                    clientButton.setText("Disconnect");
                    hostIP.setEditable(false);
                    hostName.setEditable(false);
                    Thread read = new Thread(() -> {
                        while (isActive) {
                            String message = client.getMessage();
                            if (message != null && !message.isEmpty()) {
                                display.append(message + "\n");
                            }
                        }
                    });
                    read.start();
                }
                mode.add(sendMP3);
                mode.revalidate();
                mode.repaint();
            }else{
                hostMode.setEnabled(true);
                client.terminateConnection();
                isActive = false;
                isHost = false;
                hostIP.setEditable(true);
                hostName.setEditable(true);
                clientButton.setText("Connect");
                mode.remove(sendMP3);
                mode.revalidate();
                mode.repaint();
            }
        });
        hostIP.setAlignmentX(Component.LEFT_ALIGNMENT);
        hostName.setAlignmentX(Component.LEFT_ALIGNMENT);
        clientButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        clientArea.add(hostIP);
        clientArea.add(hostName);
        clientArea.add(clientButton);
    }
}
