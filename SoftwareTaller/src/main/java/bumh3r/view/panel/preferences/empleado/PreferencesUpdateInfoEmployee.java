package bumh3r.view.panel.preferences.empleado;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.input.InputTextCP;
import bumh3r.components.input.InputTextPhone;
import bumh3r.model.Empleado;
import bumh3r.model.TypeEmpleado;
import bumh3r.model.other.EstadosMx;
import bumh3r.notifications.Notify;
import bumh3r.system.preferences.Preferences;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.CheckInputs;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class PreferencesUpdateInfoEmployee extends Preferences {
    private ButtonDefault saveButton;
    private InputText firstname, lastname, email, rfc, street, colony;
    private InputTextCP zip;
    private InputTextPhone phone;
    private FlatComboBox sexComboBox, stateComboBox, municipalityComboBox;
    private FlatComboBox typeEmployee;

    @Override
    public String title() {
        return "Actualizar Información del Empleado";
    }

    @Override
    public void initPreference() {
        PoolThreads.getInstance().execute(addItems);
    }

    @Override
    public void openPreference() {
        PoolThreads.getInstance().execute(updateInfo);
    }

    public PreferencesUpdateInfoEmployee(Object id, String key) {
        super(id, key);
        initComponents();
        initListeners();
        init();
    }

    private void initListeners() {
        stateComboBox.addActionListener(x -> {
            if (stateComboBox.getSelectedIndex() != 0) {
                municipalityComboBox.removeAllItems();
                EstadosMx.getInstance().addItemsMunicipality(stateComboBox.getSelectedItem().toString(), municipalityComboBox);
            } else {
                municipalityComboBox.removeAllItems();
            }
        });
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

        sexComboBox = new FlatComboBox();
        sexComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Masculino", "Femenino"}));
        stateComboBox = new FlatComboBox();
        stateComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione el Estado"}));
        municipalityComboBox = new FlatComboBox();
        typeEmployee = new FlatComboBox();

    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 3 3 25 3", "[fill,grow]5"));

        add(createdSubtitles("Información"), "grow 0,gapy 5 5,al center");

        add(createdGramatical("Nombre"), "gapx 6");
        add(firstname);
        add(createdGramatical("Apellidos"), "gapx 6");
        add(lastname);
        add(createdGramatical("Correo"), "gapx 6");
        add(email);
        add(createdGramatical("Teléfono"), "gapx 6");
        add(phone);
        add(createdGramatical("Tipo de Empleado"), "gapx 6");
        add(typeEmployee);
        add(createdGramatical("Genero"), "gapx 6");
        add(sexComboBox);
        add(createdGramatical("RFC"), "gapx 6");
        add(rfc);

        add(createdSubtitles("Dirección"), "grow 0,gapy 5 5,al center");

        add(createdGramatical("Estado"), "gapx 6");
        add(stateComboBox);
        add(createdGramatical("Municipio"), "gapx 6");
        add(municipalityComboBox);
        add(createdGramatical("Colonia"), "gapx 6");
        add(colony);
        add(createdGramatical("Calle"), "gapx 6");
        add(street);
        add(createdGramatical("Código Postal"), "gapx 6");
        add(zip);

        add(saveButton, "grow 0,gapy 10,al trail");
    }

//    public EmployeeRequest getValue() {
//        Toast.closeAll();
//        if (checkInputs()) return null;
//
//        String firstnameValue = firstname.getText().strip();
//        String lastnameValue = lastname.getText().strip();
//        String emailValue = (!email.getText().isEmpty()) ? email.getText().strip() : null;
//        String phoneValue = phone.getValue().toString();
//        String sexValue = sexComboBox.getSelectedItem().toString();
//        TypeEmpleado type = ((TypeEmpleado) this.typeEmployee.getSelectedItem());
//        String rfcValue = (!rfc.getText().isEmpty()) ? rfc.getText().strip() : null;
//        String streetValue = street.getText().strip();
//        String stateValue = stateComboBox.getSelectedItem().toString();
//        String municipalityValue = municipalityComboBox.getSelectedItem().toString();
//        String colonyValue = (!colony.getText().isEmpty()) ? colony.getText().strip() : null;
//        String zipValue = zip.getValue().toString();
//
//        return new EmployeeRequest(
//                firstnameValue,
//                lastnameValue,
//                rfcValue,
//                sexValue,
//                emailValue,
//                phoneValue,
//                stateValue,
//                municipalityValue,
//                colonyValue,
//                streetValue,
//                zipValue,
//                type
//        );
//    }

    private boolean checkInputs() {
        if (firstname.getText().isEmpty() || !CheckInputs.isNameValid(firstname.getText())) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo Nombre y solo debe contener letras");
            return true;
        }
        if (lastname.getText().isEmpty() || !CheckInputs.isNameValid(lastname.getText())) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo Apellidos y solo debe contener letras");
            return true;
        }
        if (phone.getValue() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo Teléfono");
            return true;
        }
        if (stateComboBox.getSelectedIndex() == 0) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo Estado");
            return true;
        }
        if (street.getText().isEmpty() || !CheckInputs.isNameValid(street.getText())) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "El campo Calle solo debe contener letras");
            return true;
        }
        if (zip.getValue() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo Código Postal");
            return true;
        }
        if (sexComboBox.getSelectedItem() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo Genero");
            return true;
        }
        if (typeEmployee.getSelectedItem() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo Tipo de Empleado");
            return true;
        }
        //Datos opcionales
        if (!email.getText().isBlank() && !CheckInputs.isValidEmail(email.getText().strip())) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "El campo Correo debe ser válido");
            return true;
        }
        if (!rfc.getText().isBlank() && !CheckInputs.isValidRFC(rfc.getText().strip())) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "El campo RFC debe ser válido");
            return true;
        }
        if (!colony.getText().isBlank() && !CheckInputs.isNameValid(colony.getText().strip())) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "El campo Colonia solo debe contener letras");
            return true;
        }
        return false;
    }

    private Runnable updateInfo = () ->{
        Empleado empleado = (Empleado) getIdentifier();
        if (empleado != null) {
            setValue(empleado);
        }
    };

    private Runnable addItems = () -> {
        try {
            EstadosMx.getInstance().addItemsStates(stateComboBox);
            List<TypeEmpleado> typeEmpleadoList = List.of(
                    new TypeEmpleado(1,"Tecnico"),
                    new TypeEmpleado(2,"Recepcionista"),
                    new TypeEmpleado(3,"Gerente"),
                    new TypeEmpleado(4,"Administrador")

            );
            typeEmployee.setModel(new DefaultComboBoxModel<>(typeEmpleadoList.toArray(new TypeEmpleado[typeEmpleadoList.size()])));
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al obtener el tipo de empleado" + e.getMessage());
        }
        updateInfo.run();
    };

    public void setValue(Empleado empleado) {
        SwingUtilities.invokeLater(() -> {
            firstname.setText(empleado.getFirstname());
            lastname.setText(empleado.getLastname());
            email.setText(empleado.getEmail());
            phone.setValue(empleado.getPhone());
            sexComboBox.getModel().setSelectedItem(empleado.getSex());
            rfc.setText(empleado.getRfc());
            stateComboBox.getModel().setSelectedItem(empleado.getState());
            municipalityComboBox.getModel().setSelectedItem(empleado.getMunicipality());
            colony.setText(empleado.getColony());
            street.setText(empleado.getStreet());
            zip.setValue(empleado.getZip());
            typeEmployee.getModel().setSelectedItem(empleado.getType_employee());
        });
    }
}
