package bumh3r.components.button;

import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class LabelButton extends JLabel {
    public LabelButton(String text) {
        super("<html><a href=\"#\">" + text + "</a></html>");
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public LabelButton() {
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public void addOnClick(Consumer event) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    event.accept(null);
                }
            }
        });
    }
}
