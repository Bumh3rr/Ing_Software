package bumh3r.view.panel.preferences.empleado;

import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.label.LabelForDescription;
import bumh3r.model.Empleado;
import bumh3r.system.preferences.Preferences;
import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

public class PreferencesUpdateStatusEmployee extends Preferences {
    private ButtonAccentBase deleteButton;
    private LabelForDescription description;

    @Override
    public String title() {
        return "Cambiar el Estado del Empleado";
    }

    @Override
    public void initPreference() {
        setData();
    }

    private void setData() {
        Empleado identifier = (Empleado) getIdentifier();
        if (identifier != null) {
            setValue(identifier);
        }
    }

    public PreferencesUpdateStatusEmployee(Object id, String key) {
        super(id, key);
        initComponents();
        init();
    }

    private void initComponents() {
        description = new LabelForDescription("");
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setEditable(false);
        description.setBackground(null);
        deleteButton = new ButtonAccentBase("...", "@accentColor");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 3 3 10 3", "[center]5"));
        add(description, "grow,push");
        add(deleteButton, "grow 0,gapy 5,al trail");
    }

    public void setValue(Empleado empleado) {
        SwingUtilities.invokeLater(() -> {
            if (Empleado.Status.Activo.name().equals(empleado.getStatus_activo())) {
                deleteButton.setText("Dar de Baja Empleado");
                description.setText(String.format("El técnico %s %s se encuentra activo, si desea dar de baja al Empleado, presione el botón Dar de Baja Empleado.\n" +
                        "Nota: Al dar de baja al Empleado, no podrá realizar reparaciones, ni se le asignarán más reparaciones.\n", empleado.getFirstname(), empleado.getLastname()));
                setColorButton("#ed4949");
            } else {
                deleteButton.setText("Dar de Alta Empleado");
                description.setText(String.format("El técnico %s %s se encuentra inactivo, si desea dar de alta al Empleado, presione el botón Dar de Alta Empleado.\n" +
                        "Nota: Al dar de alta al Empleado, podrá realizar reparaciones y se le asignarán reparaciones.\n", empleado.getFirstname(), empleado.getLastname()));
                setColorButton("#38cb6e");
            }
        });
    }

    private void setColorButton(String color) {
        deleteButton.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:" + color + ";"
                + "foreground:#FFFFFF;"
                + "focusedBackground:saturate(" + color + ",5%);"
                + "focusedBorderColor:fade(" + color + ",10%);"
                + "focusColor:fade(" + color + ",35%);"
        );

    }
}
