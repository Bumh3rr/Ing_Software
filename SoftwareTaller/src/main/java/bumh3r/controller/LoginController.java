package bumh3r.controller;

import bumh3r.components.drawer.MyDrawerTallerBuilder;
import bumh3r.model.Refaccion;
import bumh3r.model.Usuario;
import bumh3r.notifications.Notify;
import bumh3r.repository.UsuarioDao;
import bumh3r.request.UsuarioLogin;
import bumh3r.system.form.FormsManager;
import bumh3r.system.form.LoginContext;
import bumh3r.view.form.FormHistorialVentas;
import bumh3r.view.form.FormInicio;
import bumh3r.view.form.FormLogin;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.prefs.Preferences;
import raven.modal.Drawer;
import raven.modal.Toast;
import raven.modal.toast.ToastPromise;

public class LoginController extends Controller {
    public static final String KEY = LoginController.class.getName();
    private final FormLogin view;
    private UsuarioDao usuarioDao;

    public LoginController(FormLogin view) {
        this.view = view;
        this.usuarioDao = getInstance(UsuarioDao.class);
        this.view.installEventLogin(this::iniciarSesion);
    }

    private void iniciarSesion() {
        Toast.closeAll();
        if (Toast.checkPromiseId(KEY)) return;
        UsuarioLogin value = view.getValue();
        if (validarCredenciales(value)) return;
        validarUsuario(value);
    }

    private void validarUsuario(UsuarioLogin value) {
        Notify.showPromise("Iniciando Sesión..",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Iniciando Sesión ...");
                            Usuario usuario;
                            try {
                                usuario = usuarioDao.verificarUsuario(value.username(), value.password());
                            } catch (Exception e) {
                                callback.done(Toast.Type.ERROR, "Usuario o contraseña incorrectos");
                                return;
                            }
                            if (usuario == null) {
                                callback.done(Toast.Type.ERROR, "Usuario o contraseña incorrectos");
                                return;
                            }

                            guardarCredenciales(value);
                            LoginContext.getInstance().login(usuario);
                            Drawer.installDrawer(FormsManager.getFrame(), new MyDrawerTallerBuilder());
                            FormsManager.login(FormInicio.class);
                            callback.done(Toast.Type.SUCCESS, "Las Refacciones fueron obtenidos correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error al obtener las Refacciones\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    }

    private void guardarCredenciales(UsuarioLogin value) {
        if (value.remember()){
            Preferences.userRoot().put("username", value.username());
            Preferences.userRoot().put("password", value.password());
        }else {
            Preferences.userRoot().remove("username");
            Preferences.userRoot().remove("password");
        }
        Preferences.userRoot().putBoolean("remember", value.remember());
    }

    private boolean validarCredenciales(UsuarioLogin value) {
        if (value.username().isEmpty() || value.password().isEmpty()) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Se requiere ingresar el usuario y la contraseña");
            return true;
        }
        return false;
    }


}
