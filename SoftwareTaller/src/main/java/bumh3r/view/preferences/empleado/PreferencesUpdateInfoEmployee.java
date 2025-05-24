package bumh3r.view.preferences.empleado;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.comboBox.ComboBoxAddress;
import bumh3r.components.comboBox.ComboBoxGenero;
import bumh3r.components.input.InputText;
import bumh3r.components.input.InputTextCP;
import bumh3r.components.input.InputTextPhone;
import bumh3r.dao.TipoEmpleadoDAO;
import bumh3r.model.New.DireccionN;
import bumh3r.model.New.EmpleadoN;
import bumh3r.model.New.TipoEmpleado;
import bumh3r.model.other.EstadosMx;
import bumh3r.notifications.Notify;
import bumh3r.request.DireccionRequest;
import bumh3r.request.EmpleadoRequest;
import bumh3r.system.preferences.Preferences;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.CheckExpression;
import bumh3r.utils.CheckInput;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class PreferencesUpdateInfoEmployee extends Preferences {
    private JButton saveButton;
    private InputText firstname, lastname, email, rfc, street, colony;
    private InputTextCP zip;
    private InputTextPhone phone;
    private ComboBoxGenero sex;
    private ComboBoxAddress comboBoxAddress;
    private FlatComboBox<TipoEmpleado> typeEmployee;

    public PreferencesUpdateInfoEmployee(Object id, String key, ActionListener... events) {
        super(id, key,events);
        initComponents();
        init();
    }

    @Override
    public String title() {
        return "Actualizar Información";
    }

    @Override
    public void installEvents() {
        saveButton.addActionListener(getEvents()[0]);
    }

    @Override
    public void initPreference() {
        PoolThreads.getInstance().execute(this::addItems);
        PoolThreads.getInstance().execute(this::setValue);
    }

    @Override
    public void openPreference() {
        PoolThreads.getInstance().execute(this::setValue);
    }

    public void setValue(EmpleadoN empleado) {
        SwingUtilities.invokeLater(() -> {
            firstname.setText(empleado.getNombre());
            lastname.setText(empleado.getApellido());
            email.setText(empleado.getCorreo());
            phone.setValue(empleado.getTelefono());
            sex.getModel().setSelectedItem(empleado.getGenero());
            rfc.setText(empleado.getRfc());
            comboBoxAddress.getStates().getModel().setSelectedItem(EstadosMx.getInstance().getStateName(empleado.getDireccion().getEstado()));
            comboBoxAddress.getMunicipality().getModel().setSelectedItem(empleado.getDireccion().getMunicipio());
            colony.setText(empleado.getDireccion().getColonia());
            street.setText(empleado.getDireccion().getCalle());
            zip.setValue(empleado.getDireccion().getCodigo_postal());
            typeEmployee.getModel().setSelectedItem(empleado.getTipoEmpleado());
        });
    }

    public void setValue() {
        EmpleadoN empleado = (EmpleadoN) getIdentifier();
        if (empleado != null) {
            setValue(empleado);
        }
    }

    public void addItems() {
        try {
            List<TipoEmpleado> typeEmpleadoList = new TipoEmpleadoDAO().getList();
            typeEmployee.setModel(new DefaultComboBoxModel<>(typeEmpleadoList.toArray(new TipoEmpleado[0])));
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al obtener el tipo de empleado" + e.getMessage());
        }
    }

    private void initComponents() {
        firstname = new InputText("Nombre", 45);
        lastname = new InputText("Apellido", 45);
        email = new InputText("Correo", 45);
        phone = new InputTextPhone();
        rfc = new InputText("RFC", 45);
        zip = new InputTextCP();
        street = new InputText("Calle", 45);
        colony = new InputText("Colonia", 45);
        saveButton = new ButtonDefault("Actualizar");

        sex = new ComboBoxGenero();
        comboBoxAddress = new ComboBoxAddress();
        typeEmployee = new FlatComboBox<>();
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 3 3 25 3", "[fill,grow]5"));
        add(createdSubtitles("Información"), "grow 0,gapy 5 5,al center");
        add(createdGramaticalP("Nombre"), "gapx 6");
        add(firstname);
        add(createdGramaticalP("Apellidos"), "gapx 6");
        add(lastname);
        add(createdGramaticalP("Correo"), "gapx 6");
        add(email);
        add(createdGramaticalP("Teléfono"), "gapx 6");
        add(phone);
        add(createdGramaticalP("Tipo de Empleado"), "gapx 6");
        add(typeEmployee);
        add(createdGramaticalP("Genero"), "gapx 6");
        add(sex);
        add(createdGramaticalP("RFC"), "gapx 6");
        add(rfc);
        add(createdSubtitles("Dirección"), "grow 0,gapy 5 5,al center");
        add(createdGramaticalP("Estado"), "gapx 6");
        add(comboBoxAddress.getStates());
        add(createdGramaticalP("Municipio"), "gapx 6");
        add(comboBoxAddress.getMunicipality());
        add(createdGramaticalP("Colonia"), "gapx 6");
        add(colony);
        add(createdGramaticalP("Calle"), "gapx 6");
        add(street);
        add(createdGramaticalP("Código Postal"), "gapx 6");
        add(zip);
        add(saveButton, "grow 0,gapy 10,al trail");
    }

    public EmpleadoN getValue() {
        String firstnameValue = !this.firstname.getText().isEmpty() ? this.firstname.getText().strip() : null;
        String lastnameValue = !this.lastname.getText().isEmpty() ? this.lastname.getText().strip() : null;
        String phoneValue = this.phone.getValue() != null ? this.phone.getValue().toString() : null;
        String sexValue = this.sex.getSelectedItem() != null ? this.sex.getSelectedItem().toString() : null;
        TipoEmpleado type = this.typeEmployee.getSelectedItem() != null ? ((TipoEmpleado) this.typeEmployee.getSelectedItem()) : null;
        String stateValue = this.comboBoxAddress.getStates().getSelectedItem() != null ? EstadosMx.getInstance().getStatesAbbreviation(this.comboBoxAddress.getStates().getSelectedItem().toString()) : null;

        // Datos opcionales que puedes llegar hacer nulos
        String municipalityValue = this.comboBoxAddress.getMunicipality().getSelectedItem() != null ? this.comboBoxAddress.getMunicipality().getSelectedItem().toString() : null;
        String streetValue = !this.street.getText().isEmpty() ? this.street.getText().strip() : null;
        String colonyValue = !this.colony.getText().isEmpty() ? this.colony.getText().strip() : null;
        String zipValue = this.zip.getValue() != null ? this.zip.getValue().toString() : null;
        // Datos personales opcionales
        String rfcValue = (!this.rfc.getText().isEmpty()) ? this.rfc.getText().strip() : null;
        String emailValue = (!this.email.getText().isEmpty()) ? this.email.getText().strip() : null;

        DireccionN direccion = DireccionN.builder()
                .estado(stateValue)
                .municipio(municipalityValue)
                .colonia(colonyValue)
                .calle(streetValue)
                .codigo_postal(zipValue)
                .build();
        EmpleadoN empleado = EmpleadoN.builder()
                .nombre(firstnameValue)
                .apellido(lastnameValue)
                .correo(emailValue)
                .telefono(phoneValue)
                .tipoEmpleado(type)
                .direccion(direccion)
                .genero(sexValue)
                .rfc(rfcValue)
                .build();
        return empleado;
    }
}
