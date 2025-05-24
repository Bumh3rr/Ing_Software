package bumh3r.controller;

import bumh3r.dao.PedidoDao;
import bumh3r.dao.ProveedorDao;
import bumh3r.dao.RefaccionDao;
import bumh3r.model.New.DetallesPedidoN;
import bumh3r.model.New.PedidoN;
import bumh3r.model.New.ProveedorN;
import bumh3r.model.New.RefaccionN;
import bumh3r.notifications.Notify;
import bumh3r.request.DetallesPedidosRequest;
import bumh3r.request.PedidoRequest;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.CheckInput;
import bumh3r.view.form.FormPedidos;
import bumh3r.view.panel.PanelAddPedido;
import bumh3r.view.panel.PanelDetailsPedido;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import javax.swing.SwingUtilities;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.toast.ToastPromise;

public class ControladorPedido extends Controller {
    public static String KEY = ControladorPedido.class.getName();
    private FormPedidos view;
    private PedidoDao pedidoDao;
    private ProveedorDao proveedorDao;
    private RefaccionDao refaccionDao;
    private PanelAddPedido panelAddPedido;

    public ControladorPedido(FormPedidos view) {
        this.view = view;
        this.proveedorDao = getInstance(ProveedorDao.class);
        this.refaccionDao = getInstance(RefaccionDao.class);
        this.pedidoDao = getInstance(PedidoDao.class);
        this.view.setEventFormInit(this::obtenerListaPedidos);
        this.view.setEventFormRefresh(this::obtenerListaPedidos);
        this.view.installEventAddPedido(this::mostrarPantallaAgregarPedido);
    }

    private void obtenerListaPedidos() {
        Toast.closeAll();
        if (Toast.checkPromiseId(KEY)) return;
        Notify.showPromise("Obteniendo los pedidos ...", new ToastPromise(KEY) {
            @Override
            public void execute(PromiseCallback callback) {
                try {
                    callback.update("Obteniendo los pedidos  ...");
                    List<PedidoN> list = pedidoDao.findAll();
                    if (list.isEmpty()) {
                        callback.done(Toast.Type.WARNING, "No hay pedidos registrados");
                        return;
                    }
                    view.addAllTable(list);
                    callback.done(Toast.Type.SUCCESS, "Los pedidos fueron obtenidos correctamente");
                } catch (Exception ex) {
                    callback.done(Toast.Type.ERROR, "Error al obtener los pedidos\n" + "Causa: " + ex.getLocalizedMessage());
                }
            }
        });
    }

    private void mostrarPantallaAgregarPedido() {
        if (panelAddPedido == null) {
            panelAddPedido = (PanelAddPedido) PanelsInstances.getInstance().getPanelModal(PanelAddPedido.class);
            panelAddPedido.installEventAddPedido(() -> {
                Toast.closeAll();
                if (Toast.checkPromiseId(KEY)) return;
                PedidoRequest value = panelAddPedido.getValue();
                if (validarDatosPedido(value)) return;
                registrarPedido(value);
            });
            panelAddPedido.installEventAddDetalle(() -> {
                DetallesPedidosRequest detalle = panelAddPedido.getValueDetail();
                if (validarDatosDetallePedido(detalle)) return;
                panelAddPedido.addOneTable(detalle);
                panelAddPedido.cleanValueDetail(); // <- Limpiar los campos
                Notify.getInstance().showToast(Toast.Type.SUCCESS, "Detalle agregado correctamente");
            });
            panelAddPedido.installEventRemoveDetalle((detalle) -> {
                panelAddPedido.getTable().getDataList().remove(detalle);
                panelAddPedido.getTable().update();
                Notify.getInstance().showToast(Toast.Type.SUCCESS, "Detalle eliminado correctamente");
                return null;
            });
        }
        if (panelAddPedido.getRefaccion().getItemCount() == 1) obtenerListRefacciones();
        if (panelAddPedido.getProveedor().getItemCount() == 1) obtenerListProveedor();
        showPanel(panelAddPedido, "Agregar nuevo pedido", "ic_inventario.svg", ID, null, false);
    }

    private boolean validarDatosDetallePedido(DetallesPedidosRequest detalle) {
        if (CheckInput.isNullInput(detalle.cantidad(), "Unidades")) return true;
        if (CheckInput.isNullInput(detalle.refaccionN(), "Refacción")) return true;
        return false;
    }

    private void registrarPedido(PedidoRequest value) {
        Notify.showPromise("Registrando el Pedido ...", new ToastPromise(KEY) {
            @Override
            public void execute(PromiseCallback callback) {
                try {
                    callback.update("Registrando el Pedido ...");
                    PedidoN newPedido = PedidoN.builder().proveedor(value.proveedor()).observaciones(value.observaciones()).fecha_pedido(LocalDateTime.now()).estado(PedidoN.EstadoPedido.PENDIENTE).build();
                    newPedido = pedidoDao.save(newPedido, value.detalles());
                    view.addOneTable(newPedido);
                    panelAddPedido.cleanValue(); // <- Limpiar los campos
                    ModalDialog.closeModal(ID); // <- Cerrar la pantallaAgregarCliente
                    callback.done(Toast.Type.SUCCESS, "Pedido agregado correctamente");
                } catch (Exception ex) {
                    callback.done(Toast.Type.ERROR, "Error al registrar el Pedido\n" + "Causa: " + ex.getLocalizedMessage());
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
                Notify.getInstance().showToast(Toast.Type.ERROR, "Error al obtener los proveedores\n" + "Causa: " + e.getLocalizedMessage());
            }
            panelAddPedido.setProveedorModel(list);
        });
    }

    private void obtenerListRefacciones() {
        PoolThreads.getInstance().execute(() -> {
            List<RefaccionN> list = Collections.emptyList();
            try {
                list = refaccionDao.findAll();
            } catch (Exception e) {
                Notify.getInstance().showToast(Toast.Type.ERROR, "Error al obtener las refacciones\n" + "Causa: " + e.getLocalizedMessage());
            }
            panelAddPedido.setRefaccionModel(list);
        });
    }

    public Function<PedidoN, Void> mostrarDetallesPedido = (pedido) -> {
        PanelDetailsPedido panelClienteNotes = new PanelDetailsPedido();
        panelClienteNotes.setPedido(pedido);
        PoolThreads.getInstance().execute(() -> {
            List<DetallesPedidoN> detallesPedido = getDetallesPedido(pedido.getId());
            SwingUtilities.invokeLater(() -> panelClienteNotes.setDetails(detallesPedido));
        });
        panelClienteNotes.installEventUpdate(() -> {
            PedidoN.EstadoPedido estado = (PedidoN.EstadoPedido) panelClienteNotes.getStatus().getSelectedItem();
            actualizarEstadoPedido(estado, pedido);
        });
        showPanel(panelClienteNotes, "Detalles del Pedido", "ic_inventario.svg", ID, null, false);
        return null;
    };

    public List<DetallesPedidoN> getDetallesPedido(Long idPedido) {
        List<DetallesPedidoN> detalles = Collections.emptyList();
        try {
            detalles = pedidoDao.findAllDetalles(idPedido);
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al obtener los detalles del pedido\n" + "Causa: " + e.getLocalizedMessage());
        }
        return detalles;
    }

    public void actualizarEstadoPedido(PedidoN.EstadoPedido estado, PedidoN pedido) {
        Notify.showPromise("Actualizando el estado del Pedido ...", new ToastPromise(KEY) {
            @Override
            public void execute(PromiseCallback callback) {
                try {
                    callback.update("Actualizando el estado del Pedido ...");
                    pedido.setEstado(estado);
                    pedidoDao.update(pedido);
                    ControladorPedido.this.view.getTable().update(); // <- Actualizar la tabla
                    callback.done(Toast.Type.SUCCESS, "Estado actualizado correctamente");
                } catch (Exception ex) {
                    callback.done(Toast.Type.ERROR, "Error al actualizar el estado del Pedido\n" + "Causa: " + ex.getLocalizedMessage());
                }
            }
        });
    }

    private boolean validarDatosPedido(PedidoRequest value) {
        if (CheckInput.isNullInput(value.proveedor(), "Proveedor")) return true;
        if (value.detalles().isEmpty()) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Se requiere al menos un detalle");
            return true;
        }
        return false;
    }

}