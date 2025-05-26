package bumh3r.view.form;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputPassword;
import bumh3r.components.input.InputText;
import bumh3r.controller.LoginController;
import bumh3r.notifications.Notify;
import bumh3r.request.UsuarioLogin;
import bumh3r.system.form.Form;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import javax.swing.*;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

import static bumh3r.utils.PathResources.Icon.modal;

public class FormLogin extends Form {
    private InputText userField;
    private JPasswordField passwordField;
    @Getter
    private JCheckBox rememberBox;
    @Getter
    private ButtonDefault loginButton;
    private JLabel lbTitle;
    private JLabel description;


    @Override
    public void installController() {
        new LoginController(this);
    }

    public FormLogin() {
        initComponents();
        init();
    }

    private void initComponents() {
        userField = new InputText("Ingresa tu Usuario", 45).setIcon(modal + "user_login.svg");
        passwordField = new InputPassword("Ingresa tu Contraseña", 45).setIcon(modal + "password.svg");

        rememberBox = new JCheckBox("Recordar");
        lbTitle = new JLabel("Inicio de Sesión");
        lbTitle.putClientProperty(FlatClientProperties.STYLE, ""
                + "font:bold +15");
        description = new JLabel("Por favor inicia sesión para acceder a tu cuenta") {
            @Override
            public void updateUI() {
                putClientProperty(FlatClientProperties.STYLE, ""
                        + "[light]foreground:lighten(@foreground,30%);"
                        + "[dark]foreground:darken(@foreground,30%)");
                super.updateUI();
            }
        };
        loginButton = new ButtonDefault("Iniciar Sesión");
        loginButton.setHorizontalTextPosition(JButton.LEADING);
        loginButton.setIcon(new FlatSVGIcon(modal + "next.svg"));
        loginButton.putClientProperty(FlatClientProperties.STYLE, ""
                + "foreground:#FFFFFF;"
                + "iconTextGap:10;");
    }

    public UsuarioLogin getDataUsuarioLogin() {
        Toast.closeAll();
        if (verifyNotEmpty()) return null;

        String userName = userField.getText().strip();
        String password = String.valueOf(passwordField.getPassword());
        boolean selected = rememberBox.isSelected();

        return new UsuarioLogin(userName, password, selected);
    }

    private boolean verifyNotEmpty() {
        if (userField.getText().isEmpty()) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo Usuario");
            return true;
        }
        if (passwordField.getPassword().length == 0) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Es requerido el campo Contraseña");
            return true;
        }
        return false;
    }

    private void init() {
        setLayout(new MigLayout("al center center"));
        putClientProperty(FlatClientProperties.STYLE, ""
                + "[dark]background:lighten(@background,1%);");

        JPanel panel = new JPanel(new MigLayout("wrap,fillx,ins 35 45 35 45", "[fill]", "fill"));
        panel.putClientProperty(FlatClientProperties.STYLE, "arc:36;");
        panel.add(lbTitle, "grow 0, al center");
        panel.add(description);
        panel.add(new JLabel("Usuario"), "gapy 10 5");
        panel.add(userField);
        panel.add(new JLabel("Contraseña"), "gapy 10 5");
        panel.add(passwordField);
        panel.add(rememberBox);
        panel.add(loginButton, "gapy 0 15");
        add(panel);
        updateUI();
    }

    public void cleanFields() {
        userField.setText("");
        passwordField.setText("");
        rememberBox.setSelected(false);
    }
}
