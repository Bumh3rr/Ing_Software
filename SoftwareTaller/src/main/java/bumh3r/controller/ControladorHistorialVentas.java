package bumh3r.controller;

import bumh3r.model.DetalleVenta;
import bumh3r.model.Pago;
import bumh3r.model.Reparacion;
import bumh3r.model.Venta;
import bumh3r.notifications.Notify;
import bumh3r.repository.PagoDao;
import bumh3r.repository.VentaDao;
import bumh3r.request.PagoRequest;
import bumh3r.view.form.FormHistorialVentas;
import bumh3r.view.panel.PanelAddPago;
import bumh3r.view.panel.PanelDetallesVenta;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.toast.ToastPromise;


public class ControladorHistorialVentas extends Controller {
    public static String KEY = ControladorHistorialVentas.class.getName();
    private FormHistorialVentas view;
    private PanelDetallesVenta panelDetallesVenta;
    private VentaDao ventaDao;

    public ControladorHistorialVentas(FormHistorialVentas view) {
        this.view = view;
        this.ventaDao = getInstance(VentaDao.class);
        this.view.setEventFormInit(this::obtenerListaVentas);
        this.view.setEventFormRefresh(this::obtenerListaVentas);
        this.view.installEventSearchBuy(this::buscarVentaPorId);
        this.view.installEventFilterByDate(this::buscarVentaPorFecha);
    }

    public Function<Venta, Void> mostrarPantallaDetalleVenta() {
        return venta -> {
            panelDetallesVenta = new PanelDetallesVenta();
            panelDetallesVenta.setData(venta);
            panelDetallesVenta.installEventShowPanelAddPago(() -> mostrarPantallaAgregarPago(venta));
            panelDetallesVenta.installEventCancelarVenta(() -> {
                Toast.closeAll();
                if (Toast.checkPromiseId(KEY)) return;
                if (venta.getEstado() == Venta.Estado.CANCELADA) {
                    Notify.getInstance().showToast(Toast.Type.WARNING, "La venta ya fue cancelada");
                    return;
                }
                int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de cancelar la venta?", "Confirmación", JOptionPane.YES_NO_OPTION);
                if (confirm != JOptionPane.YES_OPTION) return;
                cancelarVenta(venta);
            });
            showPanel(panelDetallesVenta, "Detalles de la Venta", "ic_note.svg", ID, null, false);
            return null;
        };
    }

    private void cancelarVenta(Venta venta) {
        Notify.showPromise("Cancelar Nota ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Cancelado la Venta ...");
                            venta.setEstado(Venta.Estado.CANCELADA);
                            for (DetalleVenta detalleVenta : venta.getDetalles()) {
                                if (detalleVenta.getReparacion() != null) {
                                    detalleVenta.getReparacion().setEstado(Reparacion.EstadoReparacion.LISTO_COBRAR);
                                }
                            }
                            ventaDao.update(venta);
                            SwingUtilities.invokeLater(() -> panelDetallesVenta.setData(venta));
                            view.getTable().update();
                            ModalDialog.popModel(ID);
                            callback.done(Toast.Type.SUCCESS, "La venta de cancelo correctamente correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error al cancelar la venta\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    }

    public void mostrarPantallaAgregarPago(Venta venta) {
        if (venta.getEstado() == Venta.Estado.CANCELADA) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "La venta ya fue cancelada");
            return;
        }
        PanelAddPago panelAddPago = new PanelAddPago();
        SwingUtilities.invokeLater(() -> {
            Float pagos = (float) venta.getPagos().stream().mapToDouble(Pago::getMonto).sum();
            Float monto_pendiente = venta.getTotal() - venta.getAbono() - venta.getDescuento() - pagos;
            panelAddPago.setValue(venta.getTotal(), monto_pendiente);
        });
        panelAddPago.installEventAgregarPago(() -> {
            Toast.closeAll();
            if (Toast.checkPromiseId(KEY)) return;
            PagoRequest value = panelAddPago.getValue();
            if (validarDatosPago(value)) return;
            int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de agregar el pago?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;
            registrarPago(value, venta);
        });
        showPanel(panelAddPago, "Agregar Pago", "ic_pay.svg", ID, () -> ModalDialog.popModel(ID), true);
    }

    private boolean validarDatosPago(PagoRequest value) {
        if (value.monto() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Ingrese un monto");
            return true;
        }
        if (value.metodoPago() == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Seleccione un método de pago");
            return true;
        }
        if (value.monto() <= 0) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "El monto debe ser mayor a 0");
            return true;
        }
        return false;
    }

    private void registrarPago(PagoRequest pagoRequest, Venta venta) {
        Notify.showPromise("Agregando Pago ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Registrando Pago ...");
                            Pago pago = Pago.builder()
                                    .fecha(LocalDateTime.now())
                                    .metodoPago(pagoRequest.metodoPago())
                                    .monto(pagoRequest.monto())
                                    .venta(venta)
                                    .build();

                            Float pagos = (float) venta.getPagos().stream().mapToDouble(Pago::getMonto).sum();
                            Float monto_pendiente = venta.getTotal() - venta.getAbono() - venta.getDescuento() - pagos;
                            if (Objects.equals(pagoRequest.monto(), monto_pendiente))
                                venta.setEstado(Venta.Estado.COMPLETADA);

                            venta.getPagos().add(pago);
                            ventaDao.update(venta);
                            SwingUtilities.invokeLater(() -> panelDetallesVenta.setData(venta));
                            view.getTable().update();
                            ModalDialog.popModel(ID);
                            callback.done(Toast.Type.SUCCESS, "El pago fue agregado correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error registrar el pago\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    }

    private void obtenerListaVentas() {
        Toast.closeAll();
        if (Toast.checkPromiseId(KEY)) return;
        Notify.showPromise("Obteniendo las Ventas ...", new ToastPromise(KEY) {
            @Override
            public void execute(PromiseCallback callback) {
                try {
                    callback.update("Obteniendo las Ventas  ...");
                    view.getDatePicker().setSelectedDate(LocalDate.now());
                    view.getInput_search().setValue(null);
                    List<Venta> list = ventaDao.findByDate(LocalDate.now());
                    if (list.isEmpty()) {
                        callback.done(Toast.Type.WARNING, "No hay ventas registradas");
                        return;
                    }
                    view.addAllTable(list);
                    callback.done(Toast.Type.SUCCESS, "Las ventas fueron obtenidas correctamente");
                } catch (Exception ex) {
                    callback.done(Toast.Type.ERROR, "Error al obtener las ventas\n" + "Causa: " + ex.getLocalizedMessage());
                }
            }
        });
    }

    private void buscarVentaPorId() {
        Toast.closeAll();
        Long id = view.getInput_search().getValue() == null ? null : Long.valueOf(view.getInput_search().getValue().toString());
        if (id == null) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Ingrese un id para buscar");
            return;
        }
        try {
            List<Venta> list = ventaDao.findById(id);
            if (list == null || list.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "No se encontraron resultados");
                return;
            }
            view.addAllTable(list);
            Notify.getInstance().showToast(Toast.Type.SUCCESS, null);
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al buscar la venta\n" + "Causa: " + e.getLocalizedMessage());
        }
    }

    private void buscarVentaPorFecha() {
        Toast.closeAll();
        if (view.getDatePicker().getSelectedDate() == null) {
            view.getDatePicker().setSelectedDate(LocalDate.now());
        }
        try {
            List<Venta> list = ventaDao.findByDate(view.getDatePicker().getSelectedDate());
            if (list.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "No se encontraron resultados");
                return;
            }
            view.addAllTable(list);
            Notify.getInstance().showToast(Toast.Type.SUCCESS, "Ventas encontradas");
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al buscar las ventas por fecha\n" + "Causa: " + e.getLocalizedMessage());
        }
    }

}
