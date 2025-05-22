package bumh3r.controller;

import bumh3r.dao.ProveedorDao;
import bumh3r.model.New.ProveedorN;
import bumh3r.notifications.Notify;
import bumh3r.request.ProveedorRequest;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.utils.CheckExpression;
import bumh3r.utils.CheckInput;
import bumh3r.view.form.FormProveedor;
import bumh3r.view.panel.PanelAddProveedor;
import java.time.LocalDateTime;
import java.util.List;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.toast.ToastPromise;

public class ControladorProveedor extends Controller {
    public static String KEY = ControladorProveedor.class.getName();
    private FormProveedor view;
    private ProveedorDao proveedorDao;
    private PanelAddProveedor panelAddProveedor;

    public ControladorProveedor(FormProveedor view) {
        this.view = view;
        this.proveedorDao = getInstance(ProveedorDao.class);
        this.view.setEventFormInit(this::obtenerListaProveedor);
        this.view.setEventFormRefresh(this::obtenerListaProveedor);
        this.view.installEventAddCliente(this::mostrarPantallaAgregarProveedor);
    }

    private void obtenerListaProveedor() {
        Toast.closeAll();
        if (Toast.checkPromiseId(KEY)) return;
        Notify.showPromise("Obteniendo los Proveedores ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Obteniendo los Proveedores  ...");
                            List<ProveedorN> list = proveedorDao.findAll();
                            if (list.isEmpty()) {
                                callback.done(Toast.Type.WARNING, "No hay Proveedores registrados");
                                return;
                            }
                            view.addAllTable(list);
                            callback.done(Toast.Type.SUCCESS, "Los Proveedores fueron obtenidos correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error al obtener los Proveedores\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    }

    private void mostrarPantallaAgregarProveedor() {
        if (panelAddProveedor == null) {
            panelAddProveedor = (PanelAddProveedor) PanelsInstances.getInstance().getPanelModal(PanelAddProveedor.class);
            panelAddProveedor.installEventAddProveedor(() -> {
                Toast.closeAll();
                if (Toast.checkPromiseId(KEY)) return;
                ProveedorRequest value = panelAddProveedor.getValue();
                if (validarDatosProveedor(value)) return;
                registrarProveedor(value);
            });
        }
        showPanel(panelAddProveedor, "Agregar Nuevo Proveedor", "ic_add-user.svg", ID, null, false);
    }

    private void registrarProveedor(ProveedorRequest value) {
        Notify.showPromise("Registrando Proveedor ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Registrando Proveedor  ...");
                            ProveedorN proveedor = ProveedorN.builder()
                                    .nombre(value.nombre())
                                    .telefono(value.telefono())
                                    .correo(value.correo())
                                    .direccion(value.direccion())
                                    .fecha_registro(LocalDateTime.now())
                                    .build();
                            proveedor = proveedorDao.save(proveedor);
                            view.addOneTable(proveedor);
                            panelAddProveedor.cleanValue();
                            ModalDialog.closeModal(ID);
                            callback.done(Toast.Type.SUCCESS, "Proveedor registrado correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error al registrar el Proveedor\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    }

    public boolean validarDatosProveedor(ProveedorRequest value) {
        Toast.closeAll();
        // Datos requeridos
        if (CheckInput.isInvalidInput(value.nombre(), CheckExpression::isNameValid, "Nombre", "solo debe contener letras"))
            return true;
        if (CheckInput.isNullInput(value.telefono(), "Teléfono")) return true;

        // Opcionales
        if (CheckInput.isOptionalInvalidInput(value.correo(), CheckExpression::isValidEmail, "Correo"))
            return true;
        if (CheckInput.isOptionalInvalidInput(value.direccion(), CheckExpression::isValidAddress, "Dirección"))
            return true;
        return false;
    }

}
