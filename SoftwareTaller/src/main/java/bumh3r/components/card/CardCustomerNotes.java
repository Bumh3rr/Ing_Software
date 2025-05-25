package bumh3r.components.card;

import bumh3r.model.Nota;
import bumh3r.model.Reparacion;
import bumh3r.model.other.DateFull;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Cursor;
import java.util.function.BiConsumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import net.miginfocom.swing.MigLayout;

public class CardCustomerNotes extends Card {
    private Nota nota;
    private BiConsumer<Nota, Runnable> event;

    public CardCustomerNotes(Nota nota, BiConsumer<Nota, Runnable> event) {
        super(nota, event);
        this.nota = nota;
        this.event = event;
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 15", "fill", "fill"));
        putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:30;"
                + "[dark]background:lighten($Panel.background,3%);"
        );
        add(createBody(), "grow 0,al lead");
        updateUI();
    }


    private JPanel createBody() {
        JPanel body = new JPanel(new MigLayout("wrap, insets 0", "[150]", "[][]push[]push"));
        body.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null");
        JLabel title = new JLabel(this.nota.getFolio());
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +1;");
        JTextPane description = new JTextPane();
        description.setEditable(false);
        description.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        description.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,0,0,0;"
                + "background:null;"
                + "[dark]foreground:shade($Label.foreground,30%)"
        );
        description.setText(
                "Fecha registro: " + DateFull.getDateFull(nota.getFecha_registro())
                        + "\nEstatus: " + nota.getEstado().getValue()
                        + "\nRecibido: " + nota.getEmpleado().getNombre() + " " + nota.getEmpleado().getApellido()
                        + "\nDispositivos: " + nota.getDispositivos().size()
                        + "\nReparaciones: " + nota.getDispositivos().stream().mapToInt(value -> value.getReparaciones().size()).sum()
                        + "\nPrecio Total $: " + nota.getDispositivos().stream().mapToDouble(value -> value.getReparaciones().stream().mapToDouble(Reparacion::getPrecio).sum()).sum()
                        + "\nAnticipos $: " + nota.getDispositivos().stream().mapToDouble(value -> value.getReparaciones().stream().mapToDouble(Reparacion::getAbono).sum()).sum()
        );
        body.add(title);
        body.add(description);
        return body;
    }
}