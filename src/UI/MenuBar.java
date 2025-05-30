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
import src.UI.SVGToImageConverter;
public class MenuBar extends JMenuBar {
    private JButton closeButton;
    private JPanel mainPanel;
    private ImageIcon closeBeforeIcon;
    private ImageIcon closeAfterIcon;
    private JButton minimizeButton;
    private ImageIcon minimizeBeforeIcon;
    private ImageIcon minimizeAfterIcon;
    private JButton maximizeButton;
    private ImageIcon maximizeBeforeIcon;
    private ImageIcon maximizeAfterIcon;
    private JButton reloadButton;
    private ImageIcon reloadBeforeIcon;
    private ImageIcon reloadAfterIcon;
    Point mouseClickPoint = new Point();

    public MenuBar(reload reloadAction) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        mainPanel.setOpaque(false);
        setBackground(Color.BLACK);
        closeButton = new JButton();
        maximizeButton = new JButton();
        minimizeButton = new JButton();
        try {
            Image scaledIcon;
            BufferedImage bufferedImage;
            bufferedImage = ImageIO.read(new File("imgs/closeafter.png"));
            scaledIcon = bufferedImage.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
            closeBeforeIcon = new ImageIcon(scaledIcon);
            bufferedImage = ImageIO.read(new File("imgs/closebefore.png"));
            scaledIcon = bufferedImage.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
            closeAfterIcon = new ImageIcon(scaledIcon);
            closeButton.setIcon(closeAfterIcon);
            bufferedImage = ImageIO.read(new File("imgs/maximizebefore.png"));
            scaledIcon = bufferedImage.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
            maximizeBeforeIcon = new ImageIcon(scaledIcon);
            bufferedImage = ImageIO.read(new File("imgs/maximizeafter.png"));
            scaledIcon = bufferedImage.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
            maximizeAfterIcon = new ImageIcon(scaledIcon);
            maximizeButton.setIcon(maximizeAfterIcon);
            bufferedImage = ImageIO.read(new File("imgs/minimizebefore.png"));
            scaledIcon = bufferedImage.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
            minimizeBeforeIcon = new ImageIcon(scaledIcon);
            bufferedImage = ImageIO.read(new File("imgs/minimizeafter.png"));
            scaledIcon = bufferedImage.getScaledInstance(12, 12, Image.SCALE_SMOOTH);
            minimizeAfterIcon = new ImageIcon(scaledIcon);
            minimizeButton.setIcon(minimizeAfterIcon);
            SVGToImageConverter converter = new SVGToImageConverter();
            reloadBeforeIcon = new ImageIcon();
            reloadBeforeIcon = converter.convertSVGToImage("imgs/reloadBefore.svg", 20, 20);
            reloadAfterIcon = new ImageIcon();
            reloadAfterIcon = converter.convertSVGToImage("imgs/reloadAfter.svg", 20, 20);
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
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

        reloadButton = new JButton();
        reloadButton.setIcon(reloadAfterIcon);
        reloadButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        reloadButton.setContentAreaFilled(false);
        reloadButton.setBorderPainted(false);
        reloadButton.addActionListener(e -> {
            reloadAction.reload();
        });
        reloadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                reloadButton.setIcon(reloadBeforeIcon);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                reloadButton.setIcon(reloadAfterIcon);
            }
        });
        mainPanel.add(closeButton);
        mainPanel.add(maximizeButton);
        mainPanel.add(minimizeButton);
        mainPanel.setMaximumSize(mainPanel.getPreferredSize());
        add(mainPanel);
    }

    public void addReloadButton() {
        add(Box.createHorizontalGlue());
        add(reloadButton);
        revalidate();
        repaint();
    }
    public void removeReloadButton() {
        remove(reloadButton);
        revalidate();
        repaint();
    }
    private class closeButtonAction extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent e) {
            closeButton.setIcon(closeBeforeIcon);
        }

        public void mouseExited(MouseEvent e) {
            closeButton.setIcon(closeAfterIcon);
        }
    }

    private class maximizeButtonAction extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent e) {
            maximizeButton.setIcon(maximizeBeforeIcon);
        }

        public void mouseExited(MouseEvent e) {
            maximizeButton.setIcon(maximizeAfterIcon);
        }
    }
    private class minimizeButtonAction extends MouseAdapter{
        @Override
        public void mouseEntered(MouseEvent e) {
            minimizeButton.setIcon(minimizeBeforeIcon);
        }

        public void mouseExited(MouseEvent e) {
            minimizeButton.setIcon(minimizeAfterIcon);
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