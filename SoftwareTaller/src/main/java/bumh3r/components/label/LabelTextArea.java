package bumh3r.components.label;

import bumh3r.fonts.FontPublicaSans;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Cursor;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class LabelTextArea extends JTextArea {

    private final JPopupMenu popupMenu = new JPopupMenu();
    private final JMenuItem copyItem = new JMenuItem("Copiar");

    public LabelTextArea(String title) {
        this();
        if (title == null || title.isEmpty()) {
            super.setText("el campo esta vació");
            putClientProperty(FlatClientProperties.STYLE, ""
                    + "background:null;"
                    + "[light]foreground:lighten(@foreground,30%);"
                    + "[dark]foreground:darken(@foreground,30%);"
                    + "font:bold +3");
        } else {
            super.setText(title);
            putClientProperty(FlatClientProperties.STYLE, ""
                    + "background:null;"
                    + "font:bold +3");
        }
    }

    public LabelTextArea() {
        setEditable(false);
        setBorder(BorderFactory.createEmptyBorder());
        setCursor(new Cursor(Cursor.TEXT_CURSOR));
        addPopMenu();
        putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;"
                + "font:bold +3");
    }

    @Override
    public void setText(String t) {
        if (t == null || t.isEmpty()) {
            super.setText("el campo esta vació");
            putClientProperty(FlatClientProperties.STYLE, ""
                    + "background:null;"
                    + "[light]foreground:lighten(@foreground,30%);"
                    + "[dark]foreground:darken(@foreground,30%);"
                    + "font:bold +3");
        } else {
            putClientProperty(FlatClientProperties.STYLE, ""
                    + "background:null;"
                    + "font:bold +3");
            super.setText(t);
        }
    }

    private void addPopMenu() {
        popupMenu.add(copyItem);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }

            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {  // Detecta si es click derecho
                    popupMenu.show(e.getComponent(), e.getX(), e.getY()); // Muestra el menú en la posición del click
                }
            }
        });

        copyItem.addActionListener(x -> {
            String textoParaCopiar = getSelectedText();

            // Crear un objeto StringSelection con el texto
            StringSelection stringSelection = new StringSelection(textoParaCopiar);

            // Obtener el portapapeles del sistema
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            // Poner el texto en el portapapeles
            clipboard.setContents(stringSelection, null);
        });
    }


    public static class ForNote extends LabelTextArea {

        public ForNote(String title) {
            super(title);
            putClientProperty(FlatClientProperties.STYLE, ""
                    + "background:null;" +
                    "font:bold +1");
            setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD_BLACK, 14f));
        }

        public ForNote() {
            this("");
        }

        public ForNote getStyleWrap() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setColumns(35);
            return this;
        }


        public static JLabel getLabelTitle(String title, Float size) {
            return new JLabel(title) {
                @Override
                public void updateUI() {
                    putClientProperty(FlatClientProperties.STYLE, "" +
                            "[light]foreground:lighten(@foreground,15%);"
                            + "[dark]foreground:darken(@foreground,15%);"
                            + "");
                    setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD_BLACK, size));
                    super.updateUI();
                }
            };
        }

        public static JLabel getLabelTitleOnly(String title, Float size) {
            return new LabelPublicaSans(title).type(FontPublicaSans.FontType.BOLD_BLACK).size(size);
        }


        public static JLabel getLabelGramatical(String title) {
            JLabel label = new JLabel(title) {
                @Override
                public void updateUI() {
                    putClientProperty(FlatClientProperties.STYLE, ""
                            + "[light]foreground:lighten(@foreground,35%);"
                            + "[dark]foreground:darken(@foreground,30%);"
                            + "");
                    setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD_BLACK, 13f));
                    super.updateUI();
                }
            };
            return label;
        }


    }


}
