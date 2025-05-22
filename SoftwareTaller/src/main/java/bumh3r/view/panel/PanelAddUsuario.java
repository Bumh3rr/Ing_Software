package bumh3r.view.panel;

import bumh3r.archive.PathResources;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.New.EmpleadoN;
import bumh3r.request.UsuarioRegisterRequest;
import bumh3r.system.panel.Panel;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.CheckExpression;
import bumh3r.utils.CheckInput;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

public class PanelAddUsuario extends Panel {
    public static final String ID = PanelAddCliente.class.getName();
    private JTextArea description;
    private InputText usuario, password, confirmPassword;
    @Getter
    private FlatComboBox<EmpleadoN> comboBoxEmpleados;
    private ButtonDefault buttonAdd;

    public PanelAddUsuario() {
        initComponents();
        init();
    }

    public void installEventAdd(Runnable event) {
        buttonAdd.addActionListener((e) -> event.run());
    }

    private void initComponents() {
        usuario = new InputText("Ingrese el username", 100).setIcon(PathResources.Icon.modal + "ic_name.svg");
        password = new InputText("Ingrese la contraseña", 100);
        confirmPassword = new InputText("Confirme la contraseña", 100);
        comboBoxEmpleados = new FlatComboBox<>();
        buttonAdd = new ButtonDefault("Agregar");
        description = new JTextArea("En este apartado podrás registrar los usuarios en el sistema, asegurate de ingresar los datos correctos.");
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setBorder(BorderFactory.createEmptyBorder());
        description.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]foreground:lighten(@foreground,30%);"
                + "[dark]foreground:darken(@foreground,30%);"
                + "background:null");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 5 45 20 45, w 250:450", "fill,grow"));
        add(description);

        add(createdSubTitle("Datos del Usuario Nuevo"), "grow 0,gapy 5 5,al center");
        add(createdGramaticalP("Usuario"), "gapy 10");
        add(usuario);
        add(createdGramaticalP("Contraseña"));
        add(password);
        add(createdGramaticalP("Confirmar Contraseña"));
        add(confirmPassword);
        add(createdGramaticalP("Empleado"));
        add(comboBoxEmpleados);

        add(buttonAdd, "grow 0,gapy 15,al trail");
    }

    private JComponent createdSubTitle(String str) {
        return new LabelPublicaSans(str).size(15f).type(FontPublicaSans.FontType.BOLD_BLACK);
    }

    public UsuarioRegisterRequest getValue() {
        if (checkInputs()) return null;
        return new UsuarioRegisterRequest(
                usuario.getText(),
                password.getText(),
                confirmPassword.getText(),
                (EmpleadoN) comboBoxEmpleados.getSelectedItem(),
                false
        );
    }

    private boolean checkInputs() {
        if (CheckInput.isEmptyInput(usuario.getText(), "Usuario")) return true;
        if (CheckInput.isEmptyInput(password.getText(), "Contraseña")) return true;
        if (CheckInput.isEmptyInput(confirmPassword.getText(), "Confirmar Contraseña")) return true;
        if (CheckInput.isNullInput(comboBoxEmpleados.getSelectedItem(), "Empleado")) return true;
        return false;
    }

    public void cleanValue() {
        SwingUtilities.invokeLater(() -> {
            usuario.setText("");
            password.setText("");
            confirmPassword.setText("");
        });
    }

}
