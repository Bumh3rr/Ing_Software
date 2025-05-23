package bumh3r.components.swingButton;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class SwitchButton extends JComponent {

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        timer.start();
        runEvent();
    }

    private Timer timer;
    private float location;
    private boolean selected;
    private boolean mouseOver;
    private float speed = 0.1f;
    private List<EventSwitchSelected> events;

    public SwitchButton() {
        setBackground(new Color(0, 174, 255));
        setPreferredSize(new Dimension(45, 25));
        setForeground(Color.WHITE);
        events = new ArrayList<>();
        location = 2;
        timer = new Timer(1, (e) -> {
            if (isSelected()) {
                int endLocation = getWidth() - getHeight() + 2;
                if (location < endLocation) {
                    location += speed;
                    repaint();
                } else {
                    timer.stop();
                    location = endLocation;
                    repaint();
                }
            } else {
                int endLocation = 2;
                if (location > endLocation) {
                    location -= speed;
                    repaint();
                } else {
                    timer.stop();
                    location = endLocation;
                    repaint();
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                mouseOver = true;
            }

            @Override
            public void mouseExited(MouseEvent me) {
                mouseOver = false;
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                EventQueue.invokeLater(()->{
                    if (SwingUtilities.isLeftMouseButton(me)) {
                        if (mouseOver) {
                            selected = !selected;
                            timer.start();
                            runEvent();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void paint(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int width = getWidth();
        int height = getHeight();
        float alpha = getAlpha();
        if (alpha < 1) {
            g2.setColor(Color.GRAY);
            g2.fillRoundRect(0, 0, width, height, 25, 25);
        }
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, width, height, 25, 25);
        g2.setColor(getForeground());
        g2.setComposite(AlphaComposite.SrcOver);
        g2.fillOval((int) location, 2, height - 4, height - 4);
        super.paint(grphcs);
    }

    private float getAlpha() {
        float width = getWidth() - getHeight();
        float alpha = (location - 2) / width;
        if (alpha < 0) {
            alpha = 0;
        }
        if (alpha > 1) {
            alpha = 1;
        }
        return alpha;
    }

    private void runEvent() {
        for (EventSwitchSelected event : events) {
            event.onSelected(selected);
        }
    }

    public void addEventSelected(EventSwitchSelected event) {
        events.add(event);
    }
}
