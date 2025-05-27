package bumh3r.controller;

import bumh3r.model.Nota;
import bumh3r.model.Refaccion;
import bumh3r.model.Reparacion;
import bumh3r.notifications.Notify;
import bumh3r.repository.NotaDao;
import bumh3r.repository.RefaccionDao;
import bumh3r.repository.VentaDao;
import bumh3r.request.VentaRequest;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.view.form.FormRegistroVenta;
import bumh3r.view.panel.PanelRegisterSale;
import bumh3r.view.panel.PanelSelectRefacciones;
import bumh3r.view.panel.PanelSelectReparacion;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.toast.ToastPromise;

public class ControladorVentas extends Controller {
    public static String KEY = ControladorVentas.class.getName();
    private FormRegistroVenta view;
    private final NotaDao notaDao;
    private final VentaDao ventaDao;
    private final RefaccionDao refaccionDao;
    private PanelSelectReparacion panelSelectReparacion;
    private PanelSelectRefacciones panelSelectRefacciones;
    private PanelRegisterSale panelRegisterSale;

    public ControladorVentas(FormRegistroVenta view) {
        this.view = view;
        this.notaDao = getInstance(NotaDao.class);
        this.refaccionDao = getInstance(RefaccionDao.class);
        this.ventaDao = getInstance(VentaDao.class);
        this.view.setEventFormInit(this::obtenerListaNotas);
        this.view.setEventFormRefresh(this::obtenerListaNotas);
        this.view.installEventSearch(this::buscarNotaPorFolio);
        this.view.installEventFilterByDate(this::buscarNotasPorFecha);
    }

    private void buscarNotasPorFecha() {
        Toast.closeAll();
        if (view.getDatePicker().getSelectedDate() == null) {
            view.getDatePicker().setSelectedDate(LocalDate.now());
        }
        try {
            List<Nota> list = notaDao.findByDate(view.getDatePicker().getSelectedDate());
            if (list.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "No se encontraron resultados");
                return;
            }
            view.addAllCards(list);
            Notify.getInstance().showToast(Toast.Type.SUCCESS, "Notas encontradas");
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al buscar las notas por fecha\n" + "Causa: " + e.getLocalizedMessage());
        }
    }

    private void buscarNotaPorFolio() {
        Toast.closeAll();
        String folio = view.getInput_search().getText().trim();
        if (folio.isEmpty()) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Ingrese un folio para buscar");
            return;
        }
        try {
            List<Nota> list = notaDao.findByFolio(folio);
            if (list == null || list.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "No se encontraron resultados");
                return;
            }
            view.addAllCards(list);
            Notify.getInstance().showToast(Toast.Type.SUCCESS, null);
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al buscar la nota\n" + "Causa: " + e.getLocalizedMessage());
        }
    }

    private void obtenerListaNotas() {
        Toast.closeAll();
        if (Toast.checkPromiseId(KEY)) return;
        Notify.showPromise("Obteniendo los Notas ...", new ToastPromise(KEY) {
            @Override
            public void execute(PromiseCallback callback) {
                try {
                    callback.update("Obteniendo los Nota  ...");
                    view.getDatePicker().setSelectedDate(LocalDate.now());
                    view.getInput_search().setText("");
                    List<Nota> list = notaDao.findByDate(LocalDate.now());
                    if (list.isEmpty()) {
                        callback.done(Toast.Type.WARNING, "No hay notas registradas");
                        return;
                    }
                    view.addAllCards(list);
                    callback.done(Toast.Type.SUCCESS, "Las notas fueron obtenidas correctamente");
                } catch (Exception ex) {
                    callback.done(Toast.Type.ERROR, "Error al obtener las notas\n" + "Causa: " + ex.getLocalizedMessage());
                }
            }
        });
    }

    private void registrarVeta(VentaRequest value, Nota nota) {
        Notify.showPromise("Registrando el Cliente ...", new ToastPromise(KEY) {
            @Override
            public void execute(PromiseCallback callback) {
                try {
                    callback.update("Registrando el Cliente ...");
                    ventaDao.save(value,nota);
                    ModalDialog.closeModal(ID); // <- Cerrar la pantallaAgregarCliente
                    callback.done(Toast.Type.SUCCESS, "La venta se generó correctamente");
                } catch (Exception ex) {
                    callback.done(Toast.Type.ERROR, "Error al generar la venta\n" + "Causa: " + ex.getLocalizedMessage());
                }
            }
        });
    }

    public BiConsumer<Nota, Runnable> mostrarPantallaRegistroDeVenta() {
        return (nota, runnable) -> {
            panelRegisterSale = new PanelRegisterSale();
            panelRegisterSale.installEventEliminarRefaccion((refaccion) -> {
                panelRegisterSale.removeOneRefaccion(refaccion);
                panelRegisterSale.setPresupuesto();
                return null;
            });
            panelRegisterSale.installEventEliminarReparacion((reparacion) -> {
                panelRegisterSale.removeOneReparacion(reparacion);
                panelRegisterSale.setPresupuesto();
                return null;
            });
            panelRegisterSale.installEventShowSelectRefaccion(this::mostrarPantallaSeleccionarRefacciones);
            panelRegisterSale.installEventShowSelectReparacion(() -> {
                List<Reparacion> allReparaciones = new ArrayList<>();
                nota.getDispositivos().forEach((dispositivo -> allReparaciones.addAll(dispositivo.getReparaciones())));
                mostrarPantallaSeleccionarReparaciones(allReparaciones);
            });
            panelRegisterSale.installEventButtonGenerarVenta(() -> {
                Toast.closeAll();
                if (Toast.checkPromiseId(KEY)) return;
                VentaRequest value = panelRegisterSale.getValue();
                if (value == null || validarDatosVenta(value)) return;
                registrarVeta(value,nota);
            });
            showPanel(panelRegisterSale, "Registro de Venta", "ic_sale.svg", ID, null, false);
        };
    }

    private boolean validarDatosVenta(VentaRequest value) {
        // Por lo menos una reparacion o una refaccion
        if (value.refacciones().isEmpty()) {
            if (value.reparaciones().isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "Seleccione al menos una refacción o una reparación");
                return true;
            }
            return false;
        }
        if (value.pago().monto() > value.subTotal()) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "El monto del pago no puede ser mayor al subtotal");
            return true;
        }
        return false;
    }

    public void mostrarPantallaSeleccionarReparaciones(List<Reparacion> reparaciones) {
        if (panelSelectReparacion == null) {
            panelSelectReparacion = (PanelSelectReparacion) PanelsInstances.getInstance().getPanelModal(PanelSelectReparacion.class);
            panelSelectReparacion.installEventSelect((reparacion) -> {
                if (panelRegisterSale.getListReparacion().stream().anyMatch(rep -> rep.getId().equals(reparacion.getId()))) {
                    Notify.getInstance().showToast(Toast.Type.WARNING, "La reparación ya fue seleccionada");
                    return null;
                }
                if (reparacion.getEstado() == Reparacion.EstadoReparacion.COBRADO || reparacion.getEstado() == Reparacion.EstadoReparacion.CANCELADO) {
                    Notify.getInstance().showToast(Toast.Type.WARNING, "La reparación se encuentra como " + reparacion.getEstado());
                    return null;
                }
                panelRegisterSale.addOneReparacion(reparacion);
                panelRegisterSale.setPresupuesto();
                ModalDialog.popModel(ID);
                return null;
            });
        }
        panelSelectReparacion.addData(reparaciones);
        showPanel(panelSelectReparacion, "Seleccionar Reparaciones", "ic_repair.svg", ID, () -> ModalDialog.popModel(ID), true);
    }

    public void mostrarPantallaSeleccionarRefacciones() {
        if (panelSelectRefacciones == null) {
            panelSelectRefacciones = (PanelSelectRefacciones) PanelsInstances.getInstance().getPanelModal(PanelSelectRefacciones.class);
            panelSelectRefacciones.installEventSearch(this::buscarRefaccionesPorNombre);
            panelSelectRefacciones.installEventSelect((refaccion) -> {
                if (refaccion.getStock() <= 0) {
                    Notify.getInstance().showToast(Toast.Type.WARNING, "No hay stock disponible");
                    return null;
                }
                panelRegisterSale.addOneRefaccion(refaccion);
                panelRegisterSale.setPresupuesto();
                ModalDialog.popModel(ID);
                return null;
            });
        }
        showPanel(panelSelectRefacciones, "Seleccionar Refacciones", "ic_select.svg", ID, () -> ModalDialog.popModel(ID), true);
    }

    public void buscarRefaccionesPorNombre() {
        List<Refaccion> list;
        String nombre;
        try {
            nombre = panelSelectRefacciones.getInput().getText().trim().toLowerCase();
            if (nombre.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "Ingrese un nombre para buscar");
                return;
            }
            list = refaccionDao.findByName(nombre);
            if (list.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "No se encontraron resultados");
                return;
            }
            panelSelectRefacciones.getTable().addAll(list);
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al buscar las refacciones\n" + "Causa: " + e.getLocalizedMessage());
        }
    }


}
