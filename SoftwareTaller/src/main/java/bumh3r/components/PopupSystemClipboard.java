package bumh3r.components;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

public class PopupSystemClipboard extends JPopupMenu {

    private final JMenuItem copyItem = new JMenuItem("Copiar");
    private final JMenuItem cutItem = new JMenuItem("Cortar");
    private final JMenuItem pasteItem = new JMenuItem("Pegar");

    public PopupSystemClipboard(JTextComponent textComponent) {
        copyItem.addActionListener((x) -> {
            String textoParaCopiar = textComponent.getSelectedText();
            StringSelection stringSelection = new StringSelection(textoParaCopiar);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });
        pasteItem.addActionListener((e) -> {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            try {
                if (clipboard.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                    String textoPegado = (String) clipboard.getData(DataFlavor.stringFlavor);
                    textComponent.setText(textComponent.getText().concat(textoPegado));
                }
            } catch (IOException | UnsupportedFlavorException ex) {
                textComponent.setText(textComponent.getText().concat(""));
            }
        });
        cutItem.addActionListener((e) -> {
            String textoParaCortar = textComponent.getSelectedText();
            if (textoParaCortar != null) {
                StringSelection stringSelection = new StringSelection(textoParaCortar);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                textComponent.replaceSelection("");
            }
        });

        add(copyItem);
        add(cutItem);
        add(pasteItem);
    }

}
