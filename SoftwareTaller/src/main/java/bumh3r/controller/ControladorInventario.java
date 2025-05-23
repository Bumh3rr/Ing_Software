package bumh3r.controller;

import bumh3r.dao.CategoriaDao;
import bumh3r.dao.ProveedorDao;
import bumh3r.dao.RefaccionDao;
import bumh3r.model.New.CategoriaN;
import bumh3r.model.New.ProveedorN;
import bumh3r.model.New.RefaccionN;
import bumh3r.notifications.Notify;
import bumh3r.request.RefaccionRequest;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.CheckExpression;
import bumh3r.utils.CheckInput;
import bumh3r.view.form.FormInventario;
import bumh3r.view.panel.PanelAddRefaccion;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.toast.ToastPromise;

public class ControladorInventario extends Controller {
    public static String KEY = ControladorInventario.class.getName();
    private FormInventario view;
    private CategoriaDao categoriaDao;
    private ProveedorDao proveedorDao;
    private RefaccionDao refaccionDao;
    private PanelAddRefaccion panelAddRefaccion;

    public ControladorInventario(FormInventario view) {
        this.refaccionDao = getInstance(RefaccionDao.class);
        this.proveedorDao = getInstance(ProveedorDao.class);
        this.categoriaDao = getInstance(CategoriaDao.class);
        this.view = view;
        this.view.setEventFormInit(this::obtenerListaRefaccion);
        this.view.setEventFormRefresh(this::obtenerListaRefaccion);
        this.view.installEventShowPanelAddRefaccion(this::mostrarPantallaAgregarRefaccion);
        this.view.installEventSearch(this::buscarRefaccionPorNombre);
    }

    private void obtenerListaRefaccion() {
        Toast.closeAll();
        if (Toast.checkPromiseId(KEY)) return;
        Notify.showPromise("Obteniendo las Refacciones ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Obteniendo las Refacciones  ...");
                            List<RefaccionN> list = refaccionDao.findAll();
                            if (list.isEmpty()) {
                                callback.done(Toast.Type.WARNING, "No hay Refacciones registradas");
                                return;
                            }
                            view.addAllTable(list);
                            callback.done(Toast.Type.SUCCESS, "Las Refacciones fueron obtenidos correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error al obtener las Refacciones\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    }

    private void obtenerListProveedor() {
        PoolThreads.getInstance().execute(() -> {
            List<ProveedorN> list = Collections.emptyList();
            try {
                list = proveedorDao.findAll();
            } catch (Exception e) {
                Notify.getInstance().showToast(Toast.Type.ERROR, "Error al obtener los proveedores\n" +
                        "Causa: " + e.getLocalizedMessage());
            }
            panelAddRefaccion.setProveedorModel(list);
        });
    }

    private void obtenerListCategoria() {
        PoolThreads.getInstance().execute(() -> {
            List<CategoriaN> list = Collections.emptyList();
            try {
                list = categoriaDao.findAll();
            } catch (Exception e) {
                Notify.getInstance().showToast(Toast.Type.ERROR, "Error al obtener las categorias\n" +
                        "Causa: " + e.getLocalizedMessage());
            }
            panelAddRefaccion.setCategoryModel(list);
        });
    }

    private void mostrarPantallaAgregarRefaccion() {
        if (panelAddRefaccion == null) {
            panelAddRefaccion = (PanelAddRefaccion) PanelsInstances.getInstance().getPanelModal(PanelAddRefaccion.class);
            panelAddRefaccion.installEventAddRefaccion(() -> {
                Toast.closeAll();
                if (Toast.checkPromiseId(KEY)) return;
                RefaccionRequest value = panelAddRefaccion.getValue();
                if (validarDatosRefaccion(value)) return;
                registrarRefaccion(value);
            });
        }
        if (panelAddRefaccion.getCategory().getItemCount() == 1) obtenerListCategoria();
        if (panelAddRefaccion.getProveedor().getItemCount() == 1) obtenerListProveedor();
        showPanel(panelAddRefaccion, "Agregar nueva refacción", "ic_inventario.svg", ID, null, false);
    }

    private void registrarRefaccion(RefaccionRequest value) {
        Notify.showPromise("Registrando Refacción ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Registrando Refacción ...");
                            RefaccionN proveedor = RefaccionN.builder()
                                    .nombre(value.nombre())
                                    .descripcion(value.descripcion())
                                    .stock(value.stock())
                                    .precio_compra(value.precio_compra())
                                    .precio_venta(value.precio_venta())
                                    .proveedor(value.proveedor())
                                    .categoria(value.categoria())
                                    .fecha_registro(LocalDateTime.now())
                                    .build();
                            proveedor = refaccionDao.save(proveedor);
                            view.addOneTable(proveedor);
                            panelAddRefaccion.cleanValue();
                            ModalDialog.closeModal(ID);
                            callback.done(Toast.Type.SUCCESS, "Refacción registrado correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error al registrar el Refacción\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    }

    public void buscarRefaccionPorNombre() {
        List<RefaccionN> list;
        String nombre;
        try {
            nombre = view.getSearch().getText().trim().toLowerCase();
            if (nombre.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "Ingrese un nombre de la refacción para buscar");
                return;
            }
            list = refaccionDao.findByName(nombre);
            if (list.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "No se encontraron resultados");
                return;
            }
            view.addAllTable(list);
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al buscar la refacción\n" + "Causa: " + e.getLocalizedMessage());
        }
    }

    public boolean validarDatosRefaccion(RefaccionRequest value) {
        // Datos requeridos
        Toast.closeAll();
        if (CheckInput.isInvalidInput(value.nombre(), CheckExpression::isNameValid, "Nombre", "solo debe contener letras"))
            return true;
        if (CheckInput.isNullInput(value.categoria(), "Categoria")) return true;
        if (CheckInput.isNullInput(value.stock(), "Stock")) return true;
        if (CheckInput.isNullInput(value.proveedor(), "Proveedor")) return true;
        return false;
    }

}
