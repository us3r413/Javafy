package src.UI;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatBox {
    public JPanel thisThing;
    private JTextArea display;
    private JScrollPane dScroll;
    private JTextField userType;
    private JButton send;
    ChatBox(){
        thisThing = new JPanel(new BorderLayout());
        display = new JTextArea();
        display.setEditable(false);
        dScroll = new JScrollPane(display);
        userType = new JTextField();
        send = new JButton("Send");
        ActionListener sendAction = e -> {
            display.append(userType.getText()+"\n");
        };
        display.setBackground(Color.GRAY);
        send.addActionListener(sendAction);
        userType.addActionListener(sendAction);
        userType.setBackground(Color.GRAY);
        userType.setBorder(BorderFactory.createEmptyBorder());
        display.setFont(new Font("Arial", Font.BOLD, 18));
        display.setForeground(Color.WHITE);
        userType.setFont(new Font("Arial", Font.BOLD, 16));
        userType.setForeground(Color.WHITE);
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(userType,BorderLayout.CENTER);
        bottom.add(send,BorderLayout.EAST);
        thisThing.add(dScroll,BorderLayout.CENTER);
        dScroll.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dScroll.setBackground(Color.BLACK);
        bottom.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottom.setBackground(Color.BLACK);
        thisThing.add(bottom,BorderLayout.SOUTH);
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
}
