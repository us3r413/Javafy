package src.UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseAdapter;

public class MenuBar extends JPanel {
    private JButton closeButton;
    private ImageIcon closeBeforeIcon;
    private ImageIcon closeAfterIcon;
    private JButton minimizeButton;
    private ImageIcon minimizeBeforeIcon;
    private ImageIcon minimizeAfterIcon;
    private JButton maximizeButton;
    private ImageIcon maximizeBeforeIcon;
    private ImageIcon maximizeAfterIcon;
    Point mouseClickPoint = new Point();

    public MenuBar() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(Color.BLACK);
        try {
            Image scaledIcon;
            BufferedImage bufferedImage;
            bufferedImage = ImageIO.read(new File("imgs/closeafter.png"));
            scaledIcon = bufferedImage.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            closeBeforeIcon = new ImageIcon(scaledIcon);
            bufferedImage = ImageIO.read(new File("imgs/closebefore.png"));
            scaledIcon = bufferedImage.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            closeAfterIcon = new ImageIcon(scaledIcon);
            closeButton = new JButton(closeBeforeIcon);

            bufferedImage = ImageIO.read(new File("imgs/maximizebefore.png"));
            scaledIcon = bufferedImage.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            maximizeBeforeIcon = new ImageIcon(scaledIcon);
            bufferedImage = ImageIO.read(new File("imgs/maximizeafter.png"));
            scaledIcon = bufferedImage.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            maximizeAfterIcon = new ImageIcon(scaledIcon);
            maximizeButton = new JButton(maximizeBeforeIcon);

            bufferedImage = ImageIO.read(new File("imgs/minimizebefore.png"));
            scaledIcon = bufferedImage.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            minimizeBeforeIcon = new ImageIcon(scaledIcon);
            bufferedImage = ImageIO.read(new File("imgs/minimizeafter.png"));
            scaledIcon = bufferedImage.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            minimizeAfterIcon = new ImageIcon(scaledIcon);
            minimizeButton = new JButton(minimizeBeforeIcon);

            minimizeButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            minimizeButton.setBackground(Color.BLACK);
            minimizeButton.addMouseListener(new minimizeButtonAction());
            minimizeButton.addActionListener(e->{
                    GUI.frame.setState(JFrame.ICONIFIED);
            });
            maximizeButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            maximizeButton.setBackground(Color.BLACK);
            maximizeButton.addMouseListener(new maximizeButtonAction());
            maximizeButton.addActionListener(e->{
                    if(GUI.frame.getExtendedState() == JFrame.MAXIMIZED_BOTH){
                        GUI.frame.setExtendedState(JFrame.NORMAL);
                    } else {
                        GUI.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    }
            });
            closeButton.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            closeButton.setBackground(Color.BLACK);
            closeButton.addActionListener( e -> {
                GUI.frame.dispose();
                GUI.frame.setVisible(false);
                GUI.frame = null;
                System.exit(0);
            });
            closeButton.addMouseListener(new closeButtonAction());
            addMouseMotionListener(new draggableBarListener());
            addMouseListener(new draggableBarListener());
            add(closeButton);
            add(maximizeButton);
            add(minimizeButton);
        } catch (IOException e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
    }


    private class closeButtonAction extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent e) {
            closeButton.setIcon(closeAfterIcon);
        }

        public void mouseExited(MouseEvent e) {
            closeButton.setIcon(closeBeforeIcon);
        }
    }

    private class maximizeButtonAction extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent e) {
            maximizeButton.setIcon(maximizeAfterIcon);
        }

        public void mouseExited(MouseEvent e) {
            maximizeButton.setIcon(maximizeBeforeIcon);
        }
    }

    private class minimizeButtonAction extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent e) {
            minimizeButton.setIcon(minimizeAfterIcon);
        }

        public void mouseExited(MouseEvent e) {
            minimizeButton.setIcon(minimizeBeforeIcon);
        }
    }
    private class draggableBarListener extends MouseAdapter implements MouseMotionListener {
        public void mousePressed(MouseEvent e) {
            mouseClickPoint.setLocation(e.getPoint());
        }
        public void mouseDragged(MouseEvent e) {
            int x = GUI.frame.getLocation().x - mouseClickPoint.x+e.getX();
            int y = GUI.frame.getLocation().y - mouseClickPoint.y+e.getY();
            GUI.frame.setLocation(x, y);
        }
    }

}

