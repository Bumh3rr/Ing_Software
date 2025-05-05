package bumh3r.components.card;

import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.model.Usuario;
import bumh3r.model.other.DateFull;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Cursor;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import net.miginfocom.swing.MigLayout;

public class CardUsuario extends JPanel {

    private final Usuario usuario;
    private final Consumer<Usuario> eventChangePassword;
    private final Consumer<Usuario> eventDelete;
    private ButtonDefault buttonChangePassword;
    private ButtonAccentBase buttonDelete;
    private JTextPane description;

    public CardUsuario(Usuario usuario, Consumer<Usuario> eventChangePassword, Consumer<Usuario> eventDelete) {
        this.usuario = usuario;
        this.eventChangePassword = eventChangePassword;
        this.eventDelete = eventDelete;
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 5", "fill", "fill"));
        putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:30;"
                + "[light]background:darken($Panel.background,3%);"
                + "[dark]background:lighten($Panel.background,3%);");
        add(createBody(), "grow 0,al lead");
        updateUI();
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new MigLayout("wrap", "[150]", "[][]push[]push"));
        body.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null");
        JLabel title = new JLabel(this.usuario.getType());
        title.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +1;");
        description = new JTextPane();
        description.setEditable(false);
        description.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        description.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,0,0,0;"
                + "background:null;"
                + "[light]foreground:tint($Label.foreground,30%);"
                + "[dark]foreground:shade($Label.foreground,30%)");
        description.setText(
                "ID: " + usuario.getId()
                        + "\nUsuario: " + usuario.getLastname()
                        + "\nFecha registro: " + DateFull.getDateFull(usuario.getDate_register())
        );

        buttonChangePassword = new ButtonDefault("Cambiar Contraseña");
        buttonDelete = new ButtonAccentBase("Eliminar", "#ff2600");
        buttonDelete.addActionListener(e -> eventDelete.accept(usuario));
        buttonChangePassword.addActionListener(e -> eventChangePassword.accept(usuario));

        body.add(title);
        body.add(description);
        body.add(buttonDelete, "gapy 10,al trail");
        body.add(buttonChangePassword, "gapy 5 5,al trail");
        return body;
    }

}
