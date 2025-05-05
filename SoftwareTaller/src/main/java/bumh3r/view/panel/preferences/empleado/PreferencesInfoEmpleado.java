package bumh3r.view.panel.preferences.empleado;

import bumh3r.archive.PathResources;
import bumh3r.components.input.InputText;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.Empleado;
import bumh3r.system.preferences.Preferences;
import bumh3r.utils.CreatedAvatar;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

public class PreferencesInfoEmpleado extends Preferences {
    private JLabel image,type_employee;
    private InputText id, status, firstname, lastname, email, phone, rfc, sex, state, date_register, date_low, municipality, zip, street, colony;

    public PreferencesInfoEmpleado(Object identifier, String idModal) {
        super(identifier, idModal);
        initComponents();
        init();
    }

    @Override
    public void initPreference() {
        extracted();
    }

    @Override
    public void openPreference() {
        extracted();
    }

    private void extracted() {
        Empleado identifier = (Empleado) getIdentifier();
        if (identifier != null) {
            setValue(identifier);
        }
    }

    @Override
    public String title() {
        return "Información del Empleado";
    }

    public void setValue(Empleado empleado) {
        SwingUtilities.invokeLater(() -> {
            id.setText(empleado.getId().toString());
            type_employee.setText(empleado.getType_employee().getName_type());
            status.setText(empleado.getStatus_activo());
            firstname.setText(empleado.getFirstname());
            lastname.setText(empleado.getLastname());
            sex.setText(empleado.getSex());
            email.setText(empleado.getEmail());
            phone.setText(empleado.getPhone());
            rfc.setText(empleado.getRfc());
            date_register.setText(empleado.getDate_register() == null ? null : empleado.getDate_register().toString());
            date_low.setText(empleado.getDate_low() == null ? null : empleado.getDate_low().toString());
            state.setText(empleado.getState());
            municipality.setText(empleado.getMunicipality());
            zip.setText(empleado.getZip());
            street.setText(empleado.getStreet());
            colony.setText(empleado.getColony());
        });
    }

    private void initComponents() {
        image = new JLabel(CreatedAvatar.created(PathResources.Default.LOGO_DEFAULT_TECHNICIAN_IMAGE, 100, 100, 3.9f));
        type_employee = new JLabel();
        type_employee.setFont(new FontPublicaSans().getFont(FontPublicaSans.FontType.BOLD,14.5f));
        id = getInstance();
        status = getInstance();
        firstname = getInstance();
        lastname = getInstance();
        email = getInstance();
        phone = getInstance();
        sex = getInstance();
        rfc = getInstance();
        date_register = getInstance();
        date_low = getInstance();
        state = getInstance();
        municipality = getInstance();
        zip = getInstance();
        street = getInstance();
        colony = getInstance();
    }

    private InputText getInstance() {
        InputText input = new InputText("campo vació", 300) {
            @Override
            public void setText(String t) {
                if (t == null || t.isEmpty()) {
                    t = "";
                }
                super.setText(t);
            }
        };
        input.setEditable(false);
        return input;
    }

    private void init() {
        setLayout(new MigLayout("wrap 2,fillx,insets 3 3 50 3", "[fill,grow][fill,grow]5"));

        add(image, "span,al center");
        add(type_employee, "span,grow 0,gapy 0 5,al center");

        add(createdGramatical("ID"));
        add(createdGramatical("Estatus"));
        add(id);
        add(status);
        add(createdGramatical("Nombre"));
        add(createdGramatical("Apellido"));
        add(firstname);
        add(lastname);
        add(createdGramatical("Genero"));
        add(createdGramatical("RFC"));
        add(sex);
        add(rfc);
        add(createdGramatical("Fecha de Registro"));
        add(createdGramatical("Fecha de Baja"));
        add(date_register);
        add(date_low);
        add(createdSubtitles("Contacto"), "span,grow 0,gapy 5 5,al center");
        add(createdGramatical("Correo"));
        add(createdGramatical("Teléfono"));
        add(email);
        add(phone);
        add(createdSubtitles("Dirección"), "span,grow 0,gapy 5 5,al center");
        add(createdGramatical("Estado"));
        add(createdGramatical("Municipio"));
        add(state);
        add(municipality);
        add(createdGramatical("Calle"));
        add(createdGramatical("Colonia"));
        add(street);
        add(colony);
        add(createdGramatical("Código Postal"), "wrap");
        add(zip);
    }
}
