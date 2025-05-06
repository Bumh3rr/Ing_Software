package bumh3r.components.card;

import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.model.Usuario;
import bumh3r.model.other.DateFull;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Cursor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import net.miginfocom.swing.MigLayout;

public class CardUsuario extends Card {

    private final Usuario usuario;
    private ButtonDefault buttonChangePassword;
    private ButtonAccentBase buttonDelete;
    private BiConsumer<Usuario, Runnable> event1;
    private BiConsumer<Usuario, Runnable> event2;
    private JTextPane description;

    public CardUsuario(Usuario usuario, BiConsumer<Usuario, Runnable> event1, BiConsumer<Usuario, Runnable> event2) {
        super(usuario, event1, event2);
        this.usuario = usuario;
        this.event1 = event1;
        this.event2 = event2;
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 15", "fill", "fill"));
        putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:30;"
                + "[light]background:darken($Panel.background,3%);"
                + "[dark]background:lighten($Panel.background,3%);");
        add(createBody(), "grow 0,al lead");
        updateUI();
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new MigLayout("wrap", "[150]", "[][]push[]push"));
        body.putClientProperty(FlatClientProperties.STYLE, "background:null");
        JLabel title = new JLabel(this.usuario.getEmpleado().getTipoEmpleado().getNombre());
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +1;");
        description = new JTextPane();
        description.setEditable(false);
        description.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        description.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,0,0,0;"
                + "background:null;"
                + "[dark]foreground:shade($Label.foreground,30%)");

        String format = String.format("Usuario: %s \nID Empleado: %d \nNombre Empleado: \n%s %s \nFecha registro: \n%s",
                usuario.getUsername(),
                usuario.getEmpleado().getId(),
                usuario.getEmpleado().getNombre(),
                usuario.getEmpleado().getApellido(),
                DateFull.getDateFull(usuario.getFecha_registro())
        );
        description.setText(format);

        buttonChangePassword = new ButtonDefault("Cambiar Contraseña");
        buttonDelete = new ButtonAccentBase("Eliminar", "#ff2600");
        buttonDelete.addActionListener(e -> event1.accept(usuario,()->{}));
        buttonChangePassword.addActionListener(e -> event2.accept(usuario,()->{}));
        body.add(title);
        body.add(description);
        body.add(buttonDelete, "grow,gapy 10,al trail");
        body.add(buttonChangePassword, "grow,gapy 5 5,al trail");
        return body;
    }

}
