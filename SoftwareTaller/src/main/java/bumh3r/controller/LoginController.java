package bumh3r.controller;

import bumh3r.drawer.MyDrawerTallerBuilder;
import bumh3r.system.form.FormsManager;
import bumh3r.view.form.FormHistorialVentas;
import bumh3r.view.form.FormLogin;
import bumh3r.view.form.FormRegistroVenta;
import java.awt.event.ActionListener;
import raven.modal.Drawer;

public class LoginController {
    private FormLogin view;

    public LoginController(FormLogin view) {
        this.view = view;
        this.view.getLoginButton().addActionListener(eventLogin);
    }

    private ActionListener eventLogin = (event) -> {
        Drawer.installDrawer(FormsManager.getFrame(), new MyDrawerTallerBuilder());
        FormsManager.login(FormHistorialVentas.class);
    };

}
