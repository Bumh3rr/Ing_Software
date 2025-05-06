package bumh3r.view.preferences.empleado;

import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.label.LabelForDescription;
import bumh3r.model.New.EmpleadoN;
import bumh3r.system.preferences.Preferences;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import net.miginfocom.swing.MigLayout;

public class PreferencesUpdateStatusEmployee extends Preferences {
    private ButtonAccentBase buttonChange;
    private JTextArea description,descriptionPreferences;

    public PreferencesUpdateStatusEmployee(Object id, String key, ActionListener... events) {
        super(id, key,events);
        initComponents();
        init();
    }

    @Override
    public String title() {
        return "Cambiar el Estado del Empleado";
    }

    @Override
    public void installEvents() {
        buttonChange.addActionListener(getEvents()[0]);
    }

    @Override
    public void initPreference() {
        setData();
    }

    private void setData() {
        EmpleadoN empleado = (EmpleadoN) getIdentifier();
        setStatus(empleado);
    }

    private void initComponents() {
        descriptionPreferences = new JTextArea();
        descriptionPreferences.setEditable(false);
        descriptionPreferences.setLineWrap(true);
        descriptionPreferences.setWrapStyleWord(true);
        descriptionPreferences.setBorder(BorderFactory.createEmptyBorder());
        descriptionPreferences.setText("Sección para actualizar el estado del empleado en el sistema. Los empleados inactivos no podrán relacionarse en notas o en reparaciones hasta ser habilitados nuevamente.");
        descriptionPreferences.putClientProperty(FlatClientProperties.STYLE, "background:null");
        description = new LabelForDescription("Sección para habilitar o bloquear el acceso de los usuarios al sistema. Si el usuario se encuentra bloqueado, no podrá acceder al sistema.");
        description.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
        buttonChange = new ButtonAccentBase("...", "@accentColor");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 3 10 3", "10[center]10","[][]push[]"));
        add(descriptionPreferences, "grow,gapy 0 6");
        add(description, "grow");
        add(buttonChange, "growx,gapy 160 5,gapx 5 5");
    }

    public void setStatus(EmpleadoN empleado) {
        EventQueue.invokeLater(() -> {
            boolean isActive = empleado.getIsActivo();

            String employeeInfo = String.format("%s %s %s",
                    empleado.getTipoEmpleado().getNombre(),
                    empleado.getNombre(),
                    empleado.getApellido());

            if (isActive) {
                buttonChange.setText("Dar de Baja Empleado");
                description.setText(String.format(
                        "El %s se encuentra activo, si desea dar de baja al Empleado, presione el botón \"Dar de Baja Empleado\".\n\n" +
                                "Importante: Al dar de baja al Empleado, no podrá realizar reparaciones, ni se le asignarán más reparaciones.\n",
                        employeeInfo));
                setColorButton("#ed4949");  // Red for deactivation
            } else {
                buttonChange.setText("Dar de Alta Empleado");
                description.setText(String.format(
                        "El %s se encuentra inactivo, si desea dar de alta al Empleado, presione el botón \"Dar de Alta Empleado\".\n\n" +
                                "Importante : Al dar de alta al Empleado, podrá realizar reparaciones y asignarle notas.\n",
                        employeeInfo));
                setColorButton("#38cb6e");  // Green for activation
            }
            revalidate();
            repaint();
        });
    }

    private void setColorButton(String color) {
        buttonChange.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:" + color + ";"
                + "foreground:#FFFFFF;"
                + "focusedBackground:saturate(" + color + ",5%);"
                + "focusedBorderColor:fade(" + color + ",10%);"
                + "focusColor:fade(" + color + ",35%);"
        );
    }
}