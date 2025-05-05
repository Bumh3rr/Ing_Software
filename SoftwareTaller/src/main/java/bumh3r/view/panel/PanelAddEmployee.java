package bumh3r.view.panel;

import bumh3r.archive.PathResources;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.input.InputTextCP;
import bumh3r.components.input.InputTextPhone;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.Empleado;
import bumh3r.model.other.EstadosMx;
import bumh3r.notifications.Notify;
import bumh3r.request.TecnicoRequest;
import bumh3r.system.panel.Panel;
import bumh3r.utils.CheckInputs;
import bumh3r.utils.Genero;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.awt.event.ActionListener;
import java.util.function.Consumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

public class PanelAddEmployee extends Panel {
    public static final String ID = PanelAddEmployee.class.getName();

    private JTextArea description;
    private InputText firstname, lastname, email, rfc, colony, street;
    private InputTextPhone phone;
    private InputTextCP zip;
    private FlatComboBox state, municipality, sex,typeEmployee;
    private ButtonDefault buttonAdd;
    public Consumer<Empleado> callBack_success;

    @Override
    public void panelInit() {
        addItemsStates();
    }

    @Override
    public void panelRefresh() {
    }

//    @Override
//    public void installController() {
//        new PanelAddTechnicianController(this);
//    }

    public PanelAddEmployee setEventCallBack(Consumer<Empleado> callBack) {
        this.callBack_success = callBack;
        return this;
    }

    public void setEventAddTechnician(ActionListener e) {
        buttonAdd.addActionListener(e);
    }

    public PanelAddEmployee() {
        initComponents();
        initListeners();
        init();
    }

    private void initComponents() {
        phone = new InputTextPhone();
        zip = new InputTextCP();
        firstname = new InputText("Ingrese Nombre",100).setIcon(PathResources.Icon.modal + "ic_name.svg");
        lastname = new InputText("Ingrese Apellido",100).setIcon(PathResources.Icon.modal + "ic_name.svg");
        email = new InputText("Ingrese Correo",100).setIcon(PathResources.Icon.modal + "ic_email.svg");
        rfc = new InputText("Ingrese RFC",13);
        colony = new InputText("Ingrese la Colonia",45);
        street = new InputText("Ingrese la Calle",45);

        state = new FlatComboBox();
        municipality = new FlatComboBox();
        sex = new FlatComboBox();
        typeEmployee = new FlatComboBox();
        typeEmployee.setModel(new DefaultComboBoxModel(new String[]{"Seleccione el tipo de empleado"}));
        state.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione el Estado"}));
        sex.setModel(Genero.getDefaultModelComBox());

        buttonAdd = new ButtonDefault("Agregar");
        description = new JTextArea("En esta sección puedes agregar un nuevo Empleado a la base de datos.\n"
                + "Por favor, llena los campos con la información solicitada. \n"
                + "Recuerda que los campos marcados con * son obligatorios.") {
            @Override
            public void updateUI() {
                setWrapStyleWord(true);
                setEditable(false);
                setBorder(BorderFactory.createEmptyBorder());
                putClientProperty(FlatClientProperties.STYLE, ""
                        + "[light]foreground:lighten(@foreground,30%);"
                        + "[dark]foreground:darken(@foreground,30%);"
                        + "background:null");
                super.updateUI();
            }
        };
    }

    private void initListeners() {
        state.addActionListener(x -> {
            if (state.getSelectedIndex() != 0) {
                municipality.removeAllItems();
                EstadosMx.getInstance().addItemsMunicipality(state.getSelectedItem().toString(), municipality);
            } else {
                municipality.removeAllItems();
            }
        });
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 5 45 20 45, w 400", "fill,grow"));
        add(description);

        add(createdSubTitle("Datos Personales"), "grow 0,gapy 5 5,al center");

        add(createdGramatical("Nombre Completo"), "gapy 10");
        add(firstname, "split 2");
        add(lastname);
        add(createdGramatical("Correo"));
        add(email);
        add(createdGramatical("Teléfono"));
        add(phone);
        add(createdGramatical("Genero"));
        add(sex);
        add(createdGramatical("RFC"));
        add(rfc);
        add(createdGramatical("Tipo de Empleado"));
        add(typeEmployee);

        add(createdSubTitle("Dirección"), "grow 0,gapy 5 5,al center");

        add(createdGramatical("Estado"));
        add(state);
        add(createdGramatical("Municipio"));
        add(municipality);
        add(createdGramatical("Colonia"));
        add(colony);
        add(createdGramatical("Calle"));
        add(street);
        add(createdGramatical("Código Postal"));
        add(zip);

        add(buttonAdd, "grow 0,gapy 10,al trail");
    }

    public TecnicoRequest getValue() {
        Toast.closeAll();
        if (checkInputs()) return null;

        String firstnameValue = firstname.getText().strip();
        String lastnameValue = lastname.getText().strip();
        String emailValue = email.getText().strip();
        String phoneValue = phone.getValue().toString();
        String sexValue = sex.getSelectedItem().toString();
        String rfcValue = rfc.getText().strip().strip();
        String streetValue = street.getText().strip();
        String stateValue = state.getSelectedItem().toString();
        String municipalityValue = municipality.getSelectedItem().toString();
        String colonyValue = colony.getText().strip();
        String zipValue = zip.getText().strip();

        return new TecnicoRequest(
                firstnameValue,
                lastnameValue,
                rfcValue,
                sexValue,
                emailValue,
                phoneValue,
                stateValue,
                municipalityValue,
                colonyValue,
                streetValue,
                zipValue);
    }

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
        if (state.getSelectedIndex() == 0) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo Estado");
            return true;
        }
        if (street.getText().isEmpty() || !CheckInputs.isNameValid(street.getText())) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "El campo Calle solo debe contener letras");
            return true;
        }
        if (zip.getValue() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo Teléfono");
            return true;
        }
        if (sex.getSelectedItem() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo Genero");
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

    private JComponent createdGramatical(String title) {
        JLabel label = new JLabel(title);
        label.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:lighten(@foreground,30%);"
                + "[dark]foreground:darken(@foreground,30%);"
                + "font:12;"
        );
        return label;
    }

    private JComponent createdSubTitle(String str) {
        return new LabelPublicaSans(str).size(15f).type(FontPublicaSans.FontType.BOLD_BLACK);
    }

    private void addItemsStates() {
        EstadosMx.getInstance().addItemsStates(state);
    }

    public Runnable eventClean = () -> {
        firstname.setText("");
        lastname.setText("");
        phone.setValue(null);
        email.setText("");
        rfc.setText("");
        colony.setText("");
        street.setText("");
        zip.setValue(null);
        sex.setSelectedIndex(0);
        state.setSelectedIndex(0);
        municipality.removeAllItems();
    };
}
