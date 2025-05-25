package bumh3r.controller;

import bumh3r.repository.ClienteDao;
import bumh3r.repository.EmpleadoDAO;
import bumh3r.repository.NotaDao;
import bumh3r.model.Cliente;
import bumh3r.model.Dispositivo;
import bumh3r.model.Empleado;
import bumh3r.model.Nota;
import bumh3r.model.Reparacion;
import bumh3r.notifications.Notify;
import bumh3r.request.DispositivoRequest;
import bumh3r.request.NotaRequest;
import bumh3r.request.ReparacionRequest;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.utils.thread.PoolThreads;
import bumh3r.utils.CheckExpression;
import bumh3r.utils.CheckInput;
import bumh3r.view.form.FormNotes;
import bumh3r.view.modal.PanelModalInfoDevice;
import bumh3r.view.modal.PanelModalInfoNote;
import bumh3r.view.modal.PanelModalInfoReparacion;
import bumh3r.view.panel.PanelAddNota;
import bumh3r.view.panel.PanelAddDispositivo;
import bumh3r.view.panel.PanelAddReparacion;
import bumh3r.view.panel.PanelSearchCliente;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.JOptionPane;
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
        this.view.installEventCreateNote(this::mostrarPantallaNuevaNota);
        this.view.installEventSearchNote(this::buscarNotaPorFolio);
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
            view.addAllCards.accept(list);
            Notify.getInstance().showToast(Toast.Type.SUCCESS, "Notas encontradas");
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
                            Nota newNote = notaDao.findById(id);
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
            List<Nota> list = notaDao.findByFolio(folio);
            if (list == null || list.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "No se encontraron resultados");
                return;
            }
            view.addAllCards.accept(list);
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
                    view.getSearch().setText("");
                    List<Nota> list = notaDao.findByDate(LocalDate.now());
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

    public BiConsumer<Nota, Runnable> mostrarPantallaVisualizarNota() {
        return (notaN, runnable) -> {
            PanelModalInfoNote panelModalInfoNote = new PanelModalInfoNote(notaN, () -> {
                Nota.EstadoNota estado = (Nota.EstadoNota)
                        JOptionPane.showInputDialog(null,
                                "Seleccione el nuevo estado",
                                "Actualizar Estado",
                                JOptionPane.QUESTION_MESSAGE,
                                null, Nota.EstadoNota.values(), notaN.getEstado());
                if (estado == null) return;
                notaN.setEstado(estado);
                actualizarNota(notaN);
                runnable.run();
            });
            panelModalInfoNote.installEventDetailsDevice((dispositivo, runnableDetail) -> mostrarPantallaVisualizarDispositivo(dispositivo));
            panelModalInfoNote.installEventClose(() -> ModalDialog.closeModal(ID));
            panelModalInfoNote.setValue();
            showPanel(panelModalInfoNote, "Detalles de la Nota", "ic_note.svg", ID, null, false);
        };
    }

    private void mostrarPantallaVisualizarDispositivo(Dispositivo e) {
        PanelModalInfoDevice panelModalInfoDevice = new PanelModalInfoDevice(e);
        panelModalInfoDevice.setValue();
        panelModalInfoDevice.installEventMostrarDetallesReparacion(() -> {
            mostrarPantallaVisualizarReparaciones(e.getReparaciones());
        });
        showPanel(panelModalInfoDevice, "Detalles del dispositivo", "ic_device.svg", ID, () -> ModalDialog.popModel(ID), true);
    }

    private void mostrarPantallaVisualizarReparaciones(List<Reparacion> reparaciones) {
        PanelModalInfoReparacion panelDetailRepair = new PanelModalInfoReparacion();
        panelDetailRepair.installEventUpdateStatusCardRepair((reparacion, refresh) -> {
            Reparacion.EstadoReparacion estado = (Reparacion.EstadoReparacion)
                    JOptionPane.showInputDialog(panelDetailRepair,
                            "Seleccione el nuevo estado",
                            "Actualizar Estado",
                            JOptionPane.QUESTION_MESSAGE,
                            null, Reparacion.EstadoReparacion.values(), reparacion.getEstado());
            if (estado == null) return;
            reparacion.setEstado(estado);
            actualizarReparacion(reparacion);
            refresh.run();
        });
        panelDetailRepair.setValue(reparaciones);
        showPanel(panelDetailRepair, "Reparaciones del Dispositivo", "ic_repair.svg", ID, () -> ModalDialog.popModel(ID), true);
    }

    public void actualizarReparacion(Reparacion reparacion) {
        try {
            notaDao.update(reparacion);
            Notify.getInstance().showToast(Toast.Type.SUCCESS, "Estado de la reparación actualizado");
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al actualizar el estado de la reparación\n" + "Causa: " + e.getLocalizedMessage());
        }
    }

    private void actualizarNota(Nota nota) {
        try {
            notaDao.update(nota);
            Notify.getInstance().showToast(Toast.Type.SUCCESS, "Estado de la nota actualizado");
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al actualizar el estado de la nota\n" + "Causa: " + e.getLocalizedMessage());
        }
    }

    public void mostrarPantallaNuevaNota() {
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
            panelAddNota.installEventAddDevice(this::mostrarPantallaAgregarNuevoDispositivo);
            panelAddNota.installEventDeleteDevice((dispositivo, runnable) -> panelAddNota.deleteCardDevice(dispositivo));

        }
        obtenerListEmpleados();
        showPanel(panelAddNota, "Agregar nueva nota", "ic_newNote.svg", ID, null, false);
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

    public void mostrarPantallaAgregarNuevoDispositivo() {
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
            panelAddDispositivo.installEventAddReparaciones(this::mostrarPantallaAgregarReparaciones);
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

    public void mostrarPantallaAgregarReparaciones() {
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
            List<Empleado> list = Collections.emptyList();
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
            List<Empleado> list = Collections.emptyList();
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

    public void buscarClientePorNombre() {
        List<Cliente> list;
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
