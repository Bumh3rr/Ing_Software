package bumh3r.view.panel;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputPassword;
import bumh3r.components.label.LabelForDescription;
import bumh3r.system.panel.Panel;
import bumh3r.utils.PasswordStrengthStatus;
import net.miginfocom.swing.MigLayout;

public class PanelChangePasswordUsuario extends Panel {
    private InputPassword password, confirmPassword;
    private PasswordStrengthStatus statusPassword, statusConfirmPassword;
    private ButtonDefault changePassword;
    private LabelForDescription description;

    public PanelChangePasswordUsuario() {
        initComponents();
        init();
    }

    private void initComponents() {
        description = new LabelForDescription("En este apartado podrás cambiar la contraseña de tu cuenta, asegurate de ingresar los datos correctos.");
        password = new InputPassword("Ingrese la nueva contraseña", 45);
        confirmPassword = new InputPassword("Confirme la nueva contraseña", 45);

        statusPassword = new PasswordStrengthStatus();
        statusConfirmPassword = new PasswordStrengthStatus();
        statusConfirmPassword.initPasswordField(password,true);
        statusConfirmPassword.initPasswordField(confirmPassword,true);

        changePassword = new ButtonDefault("Cambiar Contraseña");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 5 45 20 45, w 250:450", "fill,grow"));
        add(description,"grow");
        add(createdGramaticalP("Nueva Contraseña"), "grow 0,al center");
        add(password, "grow");
        add(statusPassword, "grow");
        add(createdGramaticalP("Confirmar Contraseña"), "grow 0,al lead");
        add(confirmPassword, "grow");
        add(statusConfirmPassword, "grow");
        add(changePassword, "gapy 10,grow 0,al trail");
    }

}
