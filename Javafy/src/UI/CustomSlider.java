package src.UI;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomSlider extends BasicSliderUI {
    private final Color played = Color.WHITE;
    private final Color unplayed = Color.GRAY;
    private final Color hoveredPlay = Color.CYAN;
    private final BasicStroke stroke = new BasicStroke(6f);
    private boolean hovered = false;

    CustomSlider(JSlider b) {
        super(b);
        installHoverListener(b);
    }
    public static ComponentUI createUI(JComponent c) {
        return new CustomSlider((JSlider)c);
    }
    private void installHoverListener(JSlider slider) {
        slider.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                slider.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                slider.repaint();
            }
        });
    }

    @Override
    public void paintTrack(Graphics g) {
        Color play = (hovered?hoveredPlay:played);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setStroke(stroke);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int trackStart, trackEnd, trackY;
        if (slider.getOrientation() == JSlider.HORIZONTAL) {
            trackStart = trackRect.x;
            trackEnd = trackRect.x + trackRect.width;
            trackY = trackRect.y + trackRect.height / 2;
            int thumbPos = thumbRect.x + thumbRect.width / 2;
            g2.setColor(play);
            g2.drawLine(trackStart, trackY, thumbPos, trackY);
            g2.setColor(unplayed);
            g2.drawLine(thumbPos, trackY, trackEnd, trackY);
        } else {
            trackStart = trackRect.y;
            trackEnd = trackRect.y + trackRect.height;
            int trackX = trackRect.x + trackRect.width / 2;
            int thumbPos = thumbRect.y + thumbRect.height / 2;
            g2.setColor(play);
            g2.drawLine(trackX, trackStart, trackX, thumbPos);
            g2.setColor(unplayed);
            g2.drawLine(trackX, thumbPos, trackX, trackEnd);
        }
        g2.dispose();
    }

    @Override
    public void paintThumb(Graphics g) {
        if (!hovered) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);

        int size = 12;
        int x = thumbRect.x + (thumbRect.width - size) / 2;
        int y = thumbRect.y + (thumbRect.height - size) / 2;

        g2.fillOval(x, y, size, size);
        g2.dispose();
    }
    @Override
    public void paintFocus(Graphics g) {

    }
}
