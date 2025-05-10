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
import bumh3r.view.panel.PanelAddUsuario;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.DefaultComboBoxModel;
import lombok.extern.slf4j.Slf4j;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.toast.ToastPromise;

@Slf4j
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
        Notify.showPromise("Obteniendo los usuarios ...",
                new ToastPromise(KEY) {
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
                            callback.done(Toast.Type.ERROR, "Error al obtener los usuarios\n" +
                                    "Causa: " + ex.getLocalizedMessage());
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
        if (panelAddUsuario.getComboBoxEmpleados().getSelectedItem() == null)
            actualizarListEmpleados();
        showPanel(panelAddUsuario, "Agregar nuevo Usuario", "ic_add-user.svg", ID, null, false);
    }

    private void registrarUsuario(UsuarioRegisterRequest value) {
        Notify.showPromise("Registrando el usuario ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Registrando el usuario ...");
                            Usuario newUser = Usuario.builder()
                                    .username(value.username())
                                    .password(value.password())
                                    .empleado(value.empleado())
                                    .fecha_registro(LocalDateTime.now())
                                    .isAdmin(false)
                                    .build();
                            newUser = usuarioDao.save(newUser);
                            actualizarListEmpleados(); // <- Actualizar la lista de empleados
                            panelAddUsuario.cleanValue(); // <- Limpiar los campos
                            view.eventAddUsuario.accept(newUser); // <- Agregar el empleado ala lista
                            ModalDialog.closeModal(ID);
                            callback.done(Toast.Type.SUCCESS, "Usuario registrado correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error al registrar el usuario\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
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

    private void actualizarListEmpleados() {
        PoolThreads.getInstance().execute(() -> {
            List<EmpleadoN> empleados;
            try {
                empleados = empleadoDAO.findAllNoUser();
            } catch (Exception e) {
                Notify.getInstance().showToast(Toast.Type.ERROR, "Error al obtener los empleados");
                return;
            }
            panelAddUsuario.getComboBoxEmpleados().setModel(new DefaultComboBoxModel<>(empleados.toArray(new EmpleadoN[0])));
        });
    }

    public BiConsumer<Usuario, Runnable> eventChangePassword = (x, t) -> {};

    public BiConsumer<Usuario, Runnable> eventDelete = (x, t) -> {};

}
