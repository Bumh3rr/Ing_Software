package bumh3r.components.card;

import bumh3r.archive.PathResources;
import bumh3r.components.PanelStatus;
import bumh3r.components.button.ButtonDefault;
import bumh3r.model.Empleado;
import bumh3r.model.New.EmpleadoN;
import bumh3r.model.other.Genero;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.CreatedAvatar;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Cursor;
import java.util.function.BiConsumer;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class EmpleadoCard extends Card {

    private final EmpleadoN empleado;
    private final BiConsumer<EmpleadoN, Runnable> event;
    private JLabel icon;
    @Getter
    private Long id;
    private PanelStatus status;
    private JButton button;
    private JTextPane description;

    public EmpleadoCard(EmpleadoN empleado, BiConsumer<EmpleadoN, Runnable> event1) {
        super(empleado, event1);
        this.empleado = empleado;
        this.event = event1;
        init();
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 5", "fill", "fill"));
        putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:30;"
                + "[light]background:darken($Panel.background,3%);"
                + "[dark]background:lighten($Panel.background,3%);");
        add(createHeader(), "grow 0,al trail");
        add(createBody(), "grow 0,al lead");
        updateUI();
        revalidate();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new MigLayout("fill,insets 10 10 0 0", "[fill,center]", "[center]"));
        header.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null");
        icon = new JLabel();
        setImageI();
        header.add(icon);
        return header;
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new MigLayout("wrap", "[150]", "[][]push[]push"));
        body.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null");
        JLabel title = new JLabel("Técnico");
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
        button = new ButtonDefault("Visualizar");
        button.addActionListener(e -> event.accept(empleado, this::refresh));
        status = new PanelStatus();
        setInfo();
        body.add(title);
        body.add(description);
        body.add(status);

        body.add(button, "gapy 10,al trail");
        return body;
    }

    public void setInfo() {
        status.setValue(empleado.getIsActivo(), empleado.getIsActivo() ? Empleado.Status.Activo.name() : Empleado.Status.Inactivo.name());
        description.setText(
                "ID: " + empleado.getId()
                        + "\nNombre: " + empleado.getNombre() + ""
                        + "\nApellidos: " + empleado.getApellido()
                        + "\nTeléfono: " + empleado.getTelefono()
        );
    }

    public void setImageI() {
        PoolThreads.getInstance().execute(() ->
                icon.setIcon(CreatedAvatar.created(Genero.MASCULINO.getNombre().contains(this.empleado.getGenero()) ?
                                PathResources.Default.ICON_DEFAULT_EMPLOYEE_MAN_IMAGE : PathResources.Default.ICON_DEFAULT_EMPLOYEE_WOMAN_IMAGE,
                        80, 80, 3.9f)));
    }

    public void refresh() {
        setInfo();
        setImageI();
    }
}
