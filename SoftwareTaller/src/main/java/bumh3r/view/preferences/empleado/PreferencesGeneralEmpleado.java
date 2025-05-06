package bumh3r.view.preferences.empleado;

import bumh3r.archive.PathResources;
import bumh3r.components.button.ButtonRefreshIcon;
import bumh3r.components.input.InputText;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.Empleado;
import bumh3r.model.New.EmpleadoN;
import bumh3r.model.other.Genero;
import bumh3r.system.preferences.Preferences;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.CreatedAvatar;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;

public class PreferencesGeneralEmpleado<T> extends Preferences {
    private JLabel image, type_employee;
    private InputText id, status, firstname, lastname, email, phone, rfc, sex, state, date_register, date_low, municipality, zip, street, colony;
    private ButtonRefreshIcon refreshButton;

    public PreferencesGeneralEmpleado(T identifier, String idModal, ActionListener... events) {
        super(identifier, idModal,events);
        initComponents();
        init();
    }

    @Override
    public void installEvents() {
        refreshButton.addActionListener(getEvents()[0]);
    }

    @Override
    public void initPreference() {
        setValue();
    }

    @Override
    public void openPreference() {
        setValue();
    }

    @Override
    public String title() {
        return "Información del Empleado";
    }

    private void setValue() {
        EmpleadoN identifier = (EmpleadoN) getIdentifier();
        if (identifier != null) {
            setValue(identifier);
        }
    }

    public void setValue(EmpleadoN empleado) {
        SwingUtilities.invokeLater(() -> {
            id.setText(empleado.getId().toString());
            type_employee.setText(empleado.getTipoEmpleado().getNombre());
            status.setText(empleado.getIsActivo() ? Empleado.Status.Activo.name() : Empleado.Status.Inactivo.name());
            firstname.setText(empleado.getNombre());
            lastname.setText(empleado.getApellido());
            sex.setText(empleado.getGenero());
            email.setText(empleado.getCorreo());
            phone.setText(empleado.getTelefono());
            rfc.setText(empleado.getRfc());
            date_register.setText(empleado.getFecha_registro() == null ? null : empleado.getFecha_registro().toString());
            date_low.setText(empleado.getFecha_baja() == null ? null : empleado.getFecha_baja().toString());
            state.setText(empleado.getDireccion().getEstado());
            municipality.setText(empleado.getDireccion().getMunicipio());
            zip.setText(empleado.getDireccion().getCodigo_postal());
            street.setText(empleado.getDireccion().getCalle());
            colony.setText(empleado.getDireccion().getColonia());
            PoolThreads.getInstance().execute(() ->
                    image.setIcon(CreatedAvatar.created(Genero.MASCULINO.getNombre().contains(empleado.getGenero()) ?
                                    PathResources.Default.ICON_DEFAULT_EMPLOYEE_MAN_IMAGE : PathResources.Default.ICON_DEFAULT_EMPLOYEE_WOMAN_IMAGE,
                            80, 80, 3.9f)));
        });
    }

    private void initComponents() {
        image = new JLabel();
        type_employee = new JLabel();
        type_employee.setFont(new FontPublicaSans().getFont(FontPublicaSans.FontType.BOLD, 14.5f));
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
        refreshButton = new ButtonRefreshIcon(0.65f);
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

        add(refreshButton, "span,grow 0,al lead");
        add(image, "span,grow 0,al center");
        add(type_employee, "span,grow 0,gapy 0 5,al center");

        add(createdGramaticalP("ID"));
        add(createdGramaticalP("Estatus"));
        add(id);
        add(status);
        add(createdGramaticalP("Nombre"));
        add(createdGramaticalP("Apellido"));
        add(firstname);
        add(lastname);
        add(createdGramaticalP("Genero"));
        add(createdGramaticalP("RFC"));
        add(sex);
        add(rfc);
        add(createdGramaticalP("Fecha de Registro"));
        add(createdGramaticalP("Fecha de Baja"));
        add(date_register);
        add(date_low);
        add(createdSubtitles("Contacto"), "span,grow 0,gapy 5 5,al center");
        add(createdGramaticalP("Correo"));
        add(createdGramaticalP("Teléfono"));
        add(email);
        add(phone);
        add(createdSubtitles("Dirección"), "span,grow 0,gapy 5 5,al center");
        add(createdGramaticalP("Estado"));
        add(createdGramaticalP("Municipio"));
        add(state);
        add(municipality);
        add(createdGramaticalP("Calle"));
        add(createdGramaticalP("Colonia"));
        add(street);
        add(colony);
        add(createdGramaticalP("Código Postal"), "wrap");
        add(zip, "wrap");
    }

}
