package bumh3r.controller;

import bumh3r.dao.ClienteDao;
import bumh3r.dao.EmpleadoDAO;
import bumh3r.dao.NotaDao;
import bumh3r.model.New.ClienteN;
import bumh3r.model.New.EmpleadoN;
import bumh3r.model.New.NotaN;
import bumh3r.notifications.Notify;
import bumh3r.request.DispositivoRequest;
import bumh3r.request.NotaRequest;
import bumh3r.request.ReparacionRequest;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.CheckExpression;
import bumh3r.utils.CheckInput;
import bumh3r.view.form.FormNotes;
import bumh3r.view.panel.PanelAddNota;
import bumh3r.view.panel.PanelAddDispositivo;
import bumh3r.view.panel.PanelAddReparacion;
import bumh3r.view.panel.PanelSearchCliente;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.toast.ToastPromise;

@Slf4j
public class ControladorNota extends Controller {
    public static String KEY = ControladorNota.class.getName();
    private final FormNotes view;
    private final NotaDao notaDao;
    private final ClienteDao clienteDao;
    private final EmpleadoDAO empleadoDao;
    private PanelAddNota panelAddNota;
    private PanelAddDispositivo panelAddDispositivo;
    private PanelAddReparacion panelAddReparacion;
    private PanelSearchCliente panelSearchCliente;

    public ControladorNota(FormNotes view) {
        this.view = view;
        this.notaDao = getInstance(NotaDao.class);
        this.empleadoDao = getInstance(EmpleadoDAO.class);
        this.clienteDao = getInstance(ClienteDao.class);
        this.view.setEventFormInit(this::obtenerListaNotas);
        this.view.setEventFormRefresh(this::obtenerListaNotas);
        this.view.installEventCreateNote(this::mostrarPantallaAgregarNota);
        this.view.installEventSearchNote(this::buscarNotaPorFolio);
        this.view.installEventFilterByDate(this::buscarNotasPorFecha);
    }

    private void buscarNotasPorFecha() {
        Toast.closeAll();
        LocalDate fecha = view.getDatePicker().getSelectedDate();
        if (fecha == null) {
            fecha = LocalDate.now();
            view.getDatePicker().setSelectedDate(fecha);
        }
        try {
            List<NotaN> list = notaDao.findByDate(fecha);
            if (list.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "No se encontraron resultados");
                return;
            }
            view.addAllCards.accept(list);
            Notify.getInstance().showToast(Toast.Type.SUCCESS,"Notas encontradas");
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al buscar las notas por fecha\n" + "Causa: " + e.getLocalizedMessage());
        }
    }

    private void registrarNota(NotaRequest value) {
        Notify.showPromise("Agregando Nota ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Agregando Nota ...");
                            Long id = notaDao.save(value);
                            NotaN newNote = notaDao.findById(id);
                            view.addOneCard.accept(newNote);
                            panelAddNota.cleanValue();
                            ModalDialog.closeModal(ID);
                            callback.done(Toast.Type.SUCCESS, "La nota fue agregado correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error agregar la nota\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    }

    private void buscarNotaPorFolio() {
        Toast.closeAll();
        String folio = view.getSearch().getText().trim();
        if (folio.isEmpty()) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Ingrese un folio para buscar");
            return;
        }
        try {
            List<NotaN> list = notaDao.findByFolio(folio);
            if (list == null || list.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "No se encontraron resultados");
                return;
            }
            view.addAllCards.accept(list);
            Notify.getInstance().showToast(Toast.Type.SUCCESS,null);
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
                    List<NotaN> list = notaDao.findAll();
                    if (list.isEmpty()) {
                        callback.done(Toast.Type.WARNING, "No hay notas registradas");
                        return;
                    }
                    view.addAllCards.accept(list);
                    callback.done(Toast.Type.SUCCESS, "Las notas fueron obtenidas correctamente");
                } catch (Exception ex) {
                    callback.done(Toast.Type.ERROR, "Error al obtener las notas\n" + "Causa: " + ex.getLocalizedMessage());
                }
            }
        });
    }

    public BiConsumer<NotaN, Runnable> eventViewDetailsNote = (notaN, runnable) -> {
        runnable.run();
    };

    public void mostrarPantallaAgregarNota() {
        if (panelAddNota == null) {
            panelAddNota = (PanelAddNota) PanelsInstances.getInstance().getPanelModal(PanelAddNota.class);
            panelAddNota.installEventAddNota(() -> {
                Toast.closeAll();
                if (Toast.checkPromiseId(KEY)) return;
                NotaRequest value = panelAddNota.getValue();
                if (validarDatosNota(value)) return;
                registrarNota(value);
            });
            panelAddNota.installEventSearchCustomer(this::mostrarPantallaBuscarCliente);
            panelAddNota.installEventAddDevice(this::mostrarPantallaAgregarDispositivo);
            panelAddNota.installEventDeleteDevice((dispositivo, runnable) -> panelAddNota.deleteCardDevice(dispositivo));

        }
        obtenerListEmpleados();
        showPanel(panelAddNota, "Agregar nueva nota", "ic_newNote.svg", ID, null, false);
    }

    public void mostrarPantallaAgregarDispositivo() {
        if (panelAddDispositivo == null) {
            panelAddDispositivo = (PanelAddDispositivo) PanelsInstances.getInstance().getPanelModal(PanelAddDispositivo.class);
            panelAddDispositivo.installEventCancel(() -> {
                panelAddDispositivo.cleanValue();
                if (panelAddReparacion != null) {
                    panelAddReparacion.cleanValue();
                    panelAddReparacion.cleanCards();
                }
                ModalDialog.popModel(ID);
            });
            panelAddDispositivo.installEventAddReparaciones(this::mostrarPantallaAgregarReparacion);
            panelAddDispositivo.installEventAddDispositivo(() -> {
                DispositivoRequest value = panelAddDispositivo.getValue();
                if (validarDatosDispositivo(value)) return;
                if (panelAddReparacion == null || panelAddReparacion.getRepairs().isEmpty()) {
                    Notify.getInstance().showToast(Toast.Type.WARNING, "Agregue al menos una reparación");
                    return;
                }
                log.info("Reparaciones: {}", panelAddReparacion.getRepairs());
                value.setReparaciones(panelAddReparacion.getRepairs());
                panelAddNota.addCardDevice(value);
                panelAddDispositivo.cleanValue();
                panelAddReparacion.cleanValue();
                panelAddReparacion.cleanCards();
                ModalDialog.popModel(ID);
            });
        }
        showPanel(panelAddDispositivo, "Agregar nuevo dispositivo", "ic_device.svg", ID, () -> ModalDialog.popModel(ID), true);
    }

    public void mostrarPantallaAgregarReparacion() {
        if (panelAddReparacion == null) {
            panelAddReparacion = (PanelAddReparacion) PanelsInstances.getInstance().getPanelModal(PanelAddReparacion.class);
            panelAddReparacion.installEventAddRepair(() -> {
                ReparacionRequest value = panelAddReparacion.getValue();
                if (validarDatosReparacion(value)) return;
                panelAddReparacion.addCardOne(value);
                panelAddReparacion.cleanValue();
            });
            panelAddReparacion.installEventDeleteCardRepair((reparacion, runnable) -> {
                panelAddReparacion.deleteCardOne(reparacion);
            });
        }
        obtenerListTecnicos();
        showPanel(panelAddReparacion, "Agregar Reparaciones", "ic_repair.svg", ID, () -> {
            List<ReparacionRequest> repairs = panelAddReparacion.getRepairs();
            panelAddDispositivo.setPresupuesto(repairs);
            ModalDialog.popModel(ID);
        }, true);
    }

    private void obtenerListEmpleados() {
        PoolThreads.getInstance().execute(() -> {
            List<EmpleadoN> list = Collections.emptyList();
            try {
                list = this.empleadoDao.findAll();
            } catch (Exception e) {
                Notify.getInstance().showToast(Toast.Type.ERROR, "Error al obtener las empleados\n" + "Causa: " + e.getLocalizedMessage());
            }
            panelAddNota.setEmployeeModel(list);
        });
    }

    private void obtenerListTecnicos() {
        PoolThreads.getInstance().execute(() -> {
            List<EmpleadoN> list = Collections.emptyList();
            try {
                list = this.empleadoDao.findAllTechnician();
            } catch (Exception e) {
                Notify.getInstance().showToast(Toast.Type.ERROR, "Error al obtener las Técnicos\n" + "Causa: " + e.getLocalizedMessage());
            }
            panelAddReparacion.setTechnicianModel(list);
        });
    }

    public boolean validarDatosReparacion(ReparacionRequest value) {
        // Datos requeridos
        Toast.closeAll();
        if (CheckInput.isInvalidInput(value.reparacion(), CheckExpression::isNameValid, "Reparación", "solo debe contener letras"))
            return true;
        if (CheckInput.isNullInput(value.categoria(), "Categoria")) return true;
        if (CheckInput.isNullInput(value.tecnico(), "Técnico")) return true;
        return false;
    }

    public boolean validarDatosDispositivo(DispositivoRequest value) {
        // Datos requeridos
        Toast.closeAll();
        if (CheckInput.isNullInput(value.getTipo_dispositivo(), "Tipo de Dispositivo")) return true;
        if (CheckInput.isNullInput(value.getMarca(), "Marca")) return true;
        if (CheckInput.isNullInput(value.getModelo(), "Modelo")) return true;
        return false;
    }

    private boolean validarDatosNota(NotaRequest value) {
        // Datos requeridos
        Toast.closeAll();
        if (CheckInput.isNullInput(value.empleado(), "Empleado")) return true;
        if (CheckInput.isNullInput(value.cliente(), "Cliente")) return true;
        if (value.dispositivos() == null || value.dispositivos().isEmpty()) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Agregue al menos un dispositivo");
            return true;
        }
        return false;
    }

    public void mostrarPantallaBuscarCliente() {
        if (panelSearchCliente == null) {
            panelSearchCliente = (PanelSearchCliente) PanelsInstances.getInstance().getPanelModal(PanelSearchCliente.class);
            panelSearchCliente.installEventSearch(this::buscarClientePorNombre);
            panelSearchCliente.installEventSelect((cliente) -> {
                panelAddNota.setCustomerModel(cliente);
                ModalDialog.popModel(ID);
                return null;
            });
        }
        showPanel(panelSearchCliente, "Buscar Cliente", "ic_search_panel.svg", ID, () -> ModalDialog.popModel(ID), true);
    }

    public void buscarClientePorNombre() {
        List<ClienteN> list;
        String nombre;
        try {
            nombre = panelSearchCliente.getInput().getText().trim().toLowerCase();
            if (nombre.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "Ingrese un nombre para buscar");
                return;
            }
            list = clienteDao.findByName(nombre);
            if (list.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "No se encontraron resultados");
                return;
            }
            panelSearchCliente.getTable().addAll(list);
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al buscar el cliente\n" + "Causa: " + e.getLocalizedMessage());
        }
    }

}
