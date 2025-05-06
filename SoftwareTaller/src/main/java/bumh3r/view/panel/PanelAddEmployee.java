package bumh3r.view.panel;

import bumh3r.archive.PathResources;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.comboBox.ComboBoxAddress;
import bumh3r.components.comboBox.ComboBoxGenero;
import bumh3r.components.input.InputText;
import bumh3r.components.input.InputTextCP;
import bumh3r.components.input.InputTextPhone;
import bumh3r.components.label.LabelForDescription;
import bumh3r.dao.TipoEmpleadoDAO;
import bumh3r.model.New.DireccionN;
import bumh3r.model.New.EmpleadoN;
import bumh3r.model.New.TipoEmpleado;
import bumh3r.model.other.EstadosMx;
import bumh3r.notifications.Notify;
import bumh3r.system.panel.Panel;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.CheckExpression;
import bumh3r.utils.CheckInput;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.time.LocalDateTime;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class PanelAddEmployee extends Panel {
    private JTextArea description;
    private InputText firstname, lastname, email, rfc, colony, street;
    private InputTextPhone phone;
    private InputTextCP zip;
    private ComboBoxAddress comboBoxAddress;
    private ComboBoxGenero sex;
    private FlatComboBox<TipoEmpleado> typeEmployee;
    private ButtonDefault buttonAdd;

    @Override
    public void panelInit() {
        addItemsStates();
    }

    public void installEvent(Runnable event) {
        buttonAdd.addActionListener((e) -> event.run());
    }

    public PanelAddEmployee() {
        initComponents();
        init();
    }

    private void initComponents() {
        phone = new InputTextPhone();
        zip = new InputTextCP();
        firstname = new InputText("Ingrese Nombre", 100).setIcon(PathResources.Icon.modal + "ic_name.svg");
        lastname = new InputText("Ingrese Apellido", 100).setIcon(PathResources.Icon.modal + "ic_name.svg");
        email = new InputText("Ingrese Correo", 100).setIcon(PathResources.Icon.modal + "ic_email.svg");
        rfc = new InputText("Ingrese RFC", 13);
        colony = new InputText("Ingrese la Colonia", 45);
        street = new InputText("Ingrese la Calle", 45);
        typeEmployee = new FlatComboBox<>();
        comboBoxAddress = new ComboBoxAddress();
        sex = new ComboBoxGenero();
        buttonAdd = new ButtonDefault("Agregar");
        description = new LabelForDescription("Sistema que gestiona la entrada, reparación y entrega de celulares, reflejando en los tickets información del cliente, detalles del equipo, tipo de reparación, costos y fechas clave.");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 5 45 20 45, w 450:520", "[fill,grow]"));
        add(description);
        add(createdSubTitle("Datos Personales", 15f), "grow 0,gapy 5 5,al center");
        add(createdGramaticalP("Nombre Completo"), "gapy 10");
        add(firstname, "split 2");
        add(lastname);
        add(createdGramaticalP("Correo"));
        add(email);
        add(createdGramaticalP("Teléfono"));
        add(phone);
        add(createdGramaticalP("Tipo de Empleado"));
        add(typeEmployee);
        add(createdGramaticalP("Genero"));
        add(sex);
        add(createdGramaticalP("RFC"));
        add(rfc);
        add(createdSubTitle("Dirección", 15f), "grow 0,gapy 5 5,al center");
        add(createdGramaticalP("Estado"));
        add(comboBoxAddress.getStates());
        add(createdGramaticalP("Municipio"));
        add(comboBoxAddress.getMunicipality());
        add(createdGramaticalP("Colonia"));
        add(colony);
        add(createdGramaticalP("Calle"));
        add(street);
        add(createdGramaticalP("Código Postal"));
        add(zip);
        add(buttonAdd, "grow 0,gapy 10,al trail");
    }

    public EmpleadoN getValue() {
        Toast.closeAll();
        if (checkInputs()) return null;

        String firstnameValue = this.firstname.getText().strip();
        String lastnameValue = this.lastname.getText().strip();
        String phoneValue = this.phone.getValue().toString();
        String sexValue = this.sex.getSelectedItem().toString();
        TipoEmpleado type = ((TipoEmpleado) this.typeEmployee.getSelectedItem());
        String stateValue = EstadosMx.getInstance().getStatesAbbreviation(this.comboBoxAddress.getStates().getSelectedItem().toString());
        String municipalityValue = this.comboBoxAddress.getMunicipality().getSelectedItem().toString();
        String streetValue = this.street.getText().strip();
        String colonyValue = this.colony.getText().strip();
        String zipValue = this.zip.getValue().toString();

        // Datos opcionales que puedes llegar hacer nulos
        String rfcValue = (!rfc.getText().isEmpty()) ? rfc.getText().strip() : null;
        String emailValue = (!email.getText().isEmpty()) ? email.getText().strip() : null;

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
                .fecha_registro(LocalDateTime.now())
                .telefono(phoneValue)
                .tipoEmpleado(type)
                .direccion(direccion)
                .genero(sexValue)
                .rfc(rfcValue)
                .isActivo(true)
                .build();
        return empleado;
    }

    private boolean checkInputs() {
        // Datos requeridos
        if (CheckInput.isInvalidInput(firstname.getText(), CheckExpression::isNameValid, "Nombre", "solo debe contener letras"))
            return true;
        if (CheckInput.isInvalidInput(lastname.getText(), CheckExpression::isNameValid, "Apellidos", "solo debe contener letras"))
            return true;
        if (CheckInput.isInvalidSelection(this.comboBoxAddress.getStates().getSelectedIndex(), "Estado")) return true;
        if (CheckInput.isNullInput(phone.getValue(), "Teléfono")) return true;
        if (CheckInput.isNullInput(sex.getSelectedItem(), "Genero")) return true;
        if (CheckInput.isNullInput(typeEmployee.getSelectedItem(), "Tipo de Empleado")) return true;
        if (CheckInput.isNullInput(zip.getValue(), "Código Postal")) return true;

        // Opcionales
        if (CheckInput.isOptionalInvalidInput(email.getText(), CheckExpression::isValidEmail, "Correo")) return true;
        if (CheckInput.isOptionalInvalidInput(rfc.getText(), CheckExpression::isValidRFC, "RFC")) return true;
        return false;
    }

    private void addItemsStates() {
        PoolThreads.getInstance().execute(() -> {
            try {
                List<TipoEmpleado> typeEmpleadoList = new TipoEmpleadoDAO().getList();
                typeEmployee.setModel(new DefaultComboBoxModel<>(typeEmpleadoList.toArray(new TipoEmpleado[typeEmpleadoList.size()])));
            } catch (Exception e) {
                Notify.getInstance().showToast(Toast.Type.ERROR, "Error al obtener el tipo de empleado" + e.getMessage());
            }
        });
    }

    public void cleanValue() {
        SwingUtilities.invokeLater(() -> {
            this.firstname.setText("");
            this.lastname.setText("");
            this.phone.setValue(null);
            this.email.setText("");
            this.rfc.setText("");
            this.colony.setText("");
            this.street.setText("");
            this.zip.setValue(null);
            this.sex.setSelectedIndex(0);
            this.comboBoxAddress.getStates().setSelectedIndex(0);
            this.comboBoxAddress.getMunicipality().removeAllItems();
            this.typeEmployee.setSelectedIndex(0);
        });
    }
}