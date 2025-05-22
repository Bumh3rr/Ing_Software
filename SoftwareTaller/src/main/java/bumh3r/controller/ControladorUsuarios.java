package bumh3r.controller;

import bumh3r.dao.EmpleadoDAO;
import bumh3r.dao.UsuarioDao;
import bumh3r.model.New.EmpleadoN;
import bumh3r.model.Usuario;
import bumh3r.notifications.Notify;
import bumh3r.request.UsuarioRegisterRequest;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.CheckExpression;
import bumh3r.utils.CheckInput;
import bumh3r.view.form.FormControlUsuario;
import bumh3r.view.modal.ModalToas;
import bumh3r.view.panel.PanelAddUsuario;
import bumh3r.view.panel.PanelChangePasswordUsuario;
import java.awt.EventQueue;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.listener.ModalController;
import raven.modal.toast.ToastPromise;

public class ControladorUsuarios extends Controller {
    private static final String KEY = ControladorUsuarios.class.getName();
    private final FormControlUsuario view;
    private final UsuarioDao usuarioDao;
    private final EmpleadoDAO empleadoDAO;
    private PanelAddUsuario panelAddUsuario;

    public ControladorUsuarios(FormControlUsuario view) {
        this.view = view;
        this.view.setEventFormInit(this::obtenerListaUsuarios);
        this.view.setEventFormRefresh(this::obtenerListaUsuarios);
        this.view.installEventShowPanelAddUsuario(this::mostrarPantallaAgregarUsuario);
        this.usuarioDao = getInstance(UsuarioDao.class);
        this.empleadoDAO = getInstance(EmpleadoDAO.class);
    }

    private void obtenerListaUsuarios() {
        Toast.closeAll();
        if (Toast.checkPromiseId(KEY)) return;
        Notify.showPromise("Obteniendo los usuarios ...", new ToastPromise(KEY) {
            @Override
            public void execute(PromiseCallback callback) {
                try {
                    callback.update("Guardando ...");
                    List<Usuario> list = usuarioDao.getList();
                    if (list.isEmpty()) {
                        callback.done(Toast.Type.WARNING, "No hay usuarios registrados");
                        view.cleanCards();
                        return;
                    }
                    view.eventAddUsuarioCard.accept(list);
                    callback.done(Toast.Type.SUCCESS, "Los usuarios fueron obtenidos correctamente");
                } catch (Exception ex) {
                    callback.done(Toast.Type.ERROR, "Error al obtener los usuarios\n" + "Causa: " + ex.getLocalizedMessage());
                }
            }
        });
        view.eventAddUsuarioCard.accept(this.usuarioDao.getList());
    }

    private void mostrarPantallaAgregarUsuario() {
        if (panelAddUsuario == null) {
            panelAddUsuario = (PanelAddUsuario) PanelsInstances.getInstance().getPanelModal(PanelAddUsuario.class);
            panelAddUsuario.installEventAdd(() -> {
                Toast.closeAll();
                if (Toast.checkPromiseId(KEY)) return;
                UsuarioRegisterRequest value = panelAddUsuario.getValue();
                if (value == null || validarDatos(value)) return;
                registrarUsuario(value);
            });
        }
        if (panelAddUsuario.getComboBoxEmpleados().getSelectedItem() == null) actualizarListEmpleados();
        showPanel(panelAddUsuario, "Agregar nuevo Usuario", "ic_add-user.svg", ID, null, false);
    }

    private void registrarUsuario(UsuarioRegisterRequest value) {
        Notify.showPromise("Registrando el usuario ...", new ToastPromise("KEYddd") {
            @Override
            public void execute(PromiseCallback callback) {
                try {
                    callback.update("Registrando el usuario ...");
                    Usuario newUser = Usuario.builder().username(value.username()).password(value.password()).empleado(value.empleado()).fecha_registro(LocalDateTime.now()).isAdmin(false).build();
                    newUser = usuarioDao.save(newUser);
                    actualizarListEmpleados(); // <- Actualizar la lista de empleados
                    panelAddUsuario.cleanValue(); // <- Limpiar los campos
                    view.eventAddUsuario.accept(newUser); // <- Agregar el empleado ala lista
                    SwingUtilities.invokeLater(() -> ModalDialog.closeModal(ID));
                    callback.done(Toast.Type.SUCCESS, "Usuario registrado correctamente");
                } catch (Exception ex) {
                    callback.done(Toast.Type.ERROR, "Error al registrar el usuario\n" + "Causa: " + ex.getLocalizedMessage());
                }
            }
        });
    }

    private void actualizarListEmpleados() {
        SwingUtilities.invokeLater(() -> {
            List<EmpleadoN> empleados;
            try {
                empleados = empleadoDAO.findAllNoUser();
                if (empleados == null || empleados.isEmpty()) {
                    panelAddUsuario.getComboBoxEmpleados().removeAllItems();
                    Notify.getInstance().showToast(Toast.Type.WARNING, "No hay empleados disponibles");
                    return;
                }
                EventQueue.invokeLater(() -> panelAddUsuario.getComboBoxEmpleados().setModel(new DefaultComboBoxModel<>(empleados.toArray(new EmpleadoN[0]))));
            } catch (Exception e) {
                Notify.getInstance().showToast(Toast.Type.ERROR, "Error al obtener los empleados" + e.getMessage());
            }
        });
    }

    public BiConsumer<Usuario, Runnable> eventMostrarPanelCambiarContraseñaUsuario = (x, t) -> {
        PanelChangePasswordUsuario panel = new PanelChangePasswordUsuario();
        panel.installEventChangePassword(() -> {
            Toast.closeAll();
            if (Toast.checkPromiseId(KEY)) return;

            String password = String.valueOf(panel.getPassword().getPassword());
            String confirmPassword = String.valueOf(panel.getConfirmPassword().getPassword());
            if (!validPassword(password, confirmPassword)) return;

            Notify.showPromise("Cambiando la contraseña ...", new ToastPromise(KEY) {
                @Override
                public void execute(PromiseCallback callback) {
                    try {
                        callback.update("Cambiando la contraseña ...");
                        x.setPassword(password);
                        usuarioDao.update(x);
                        callback.done(Toast.Type.SUCCESS, "Contraseña cambiada correctamente");
                        ModalDialog.closeModal(ID);
                    } catch (Exception ex) {
                        callback.done(Toast.Type.ERROR, "Error al cambiar la contraseña\n" + "Causa: " + ex.getLocalizedMessage());
                    }
                }
            });
        });

        showPanel(panel, "Cambiar Contraseña", "ic_update.svg", ID, null, false);
        PanelsInstances.getInstance().getPanelModal(PanelChangePasswordUsuario.class);
    };

    public BiConsumer<Usuario, Runnable> eventMostrarPanelEliminarUsuario = (usuario, t) -> {
        showPanel(new ModalToas(ModalToas.Type.WARNING, "Eliminar Usuario", "¿Estás seguro de eliminar a " + usuario.getUsername() + "?", (modal, action) -> {
            modal.consume();
            if (action == ModalToas.ACCEPT_OPTION) {
                eliminarUsuario(usuario);
            } else {
                modal.close();
            }
        }), null, null, ID, null, false);

    };

    private void eliminarUsuario(Usuario usuario) {
        Toast.closeAll();
        if (Toast.checkPromiseId(KEY)) return;
        Notify.showPromise("Eliminando el usuario ...", new ToastPromise(KEY) {
            @Override
            public void execute(PromiseCallback callback) {
                try {
                    callback.update("Eliminando el usuario ...");
                    usuarioDao.delete(usuario);
                    view.eventDeleteUsuario.accept(usuario);
                    ModalDialog.closeModal(ID);
                    callback.done(Toast.Type.SUCCESS, "Usuario eliminado correctamente");
                } catch (Exception ex) {
                    callback.done(Toast.Type.ERROR, "Error al eliminar el usuario\n" + "Causa: " + ex.getLocalizedMessage());
                }
            }
        });
    }

    public boolean validPassword(String password, String confirmPassword) {
        if (CheckInput.isNullInput(password, "Contraseña")) return false;
        if (CheckInput.isNullInput(confirmPassword, "Confirmar Contraseña")) return false;
        if (password.trim().length() < 4 || confirmPassword.trim().length() < 4) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "La contraseña debe tener al menos 4 caracteres");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Las contraseñas no coinciden");
            return false;
        }
        return true;
    }

    public boolean validarDatos(UsuarioRegisterRequest value) {
        if (CheckInput.isInvalidInput(value.username(), CheckExpression::isValidUsername, "Username", "debe ser valido con 3-20 caracteres"))
            return true;
        if (value.password().trim().length() < 4 || value.passwordConfirm().trim().length() < 4) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "La contraseña debe tener al menos 4 caracteres");
            return true;
        }
        if (!value.password().equals(value.passwordConfirm())) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Las contraseñas no coinciden");
            return true;
        }
        return false;
    }

}
