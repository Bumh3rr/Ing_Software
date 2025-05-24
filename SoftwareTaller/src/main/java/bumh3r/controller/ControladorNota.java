package bumh3r.controller;

import bumh3r.dao.ClienteDao;
import bumh3r.dao.EmpleadoDAO;
import bumh3r.dao.NotaDao;
import bumh3r.model.New.ClienteN;
import bumh3r.model.New.EmpleadoN;
import bumh3r.model.New.NotaN;
import bumh3r.notifications.Notify;
import bumh3r.request.NotaRequest;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.thread.PoolThreads;
import bumh3r.view.form.FormNotes;
import bumh3r.view.panel.PanelAddNota;
import bumh3r.view.panel.PanelAddDispositivo;
import bumh3r.view.panel.PanelAddReparacion;
import bumh3r.view.panel.PanelSearchCliente;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.toast.ToastPromise;

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
        this.view.installEventCreateNote(this::mostrarPantallaCrearNota);
        this.view.installEventSearchNote(this::buscarNotaPorFolio);
    }

    private void mostrarPantallaCrearNota() {
        showPanel(getPanelAddNota(), "Agregar nueva nota", "ic_newNote.svg", ID, null, false);
    }

    private void mostrarPantallaAgregarDispositivo() {
        showPanel(getPanelAddDispositivo(), "Agregar nuevo dispositivo", "ic_device.svg", ID, () -> ModalDialog.popModel(ID), true);
    }

    private void mostrarPantallaAgregarReparacion() {
        showPanel(getPanelAddReparacion(), "Agregar Reparaciones", "ic_repair.svg", ID, () -> ModalDialog.popModel(ID), true);

    }

    private void registrarNota(NotaRequest value) {

    }

    private boolean validarDatosNota(NotaRequest value) {
        return false;
    }

    private void buscarNotaPorFolio() {
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

    public void buscarClientePorNombre() {
        List<ClienteN> list;
        String nombre;
        try {
            nombre = getPanelSearchCliente().getInput().getText().trim().toLowerCase();
            if (nombre.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "Ingrese un nombre para buscar");
                return;
            }
            list = clienteDao.findByName(nombre);
            if (list.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "No se encontraron resultados");
                return;
            }
            getPanelSearchCliente().getTable().addAll(list);
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al buscar el cliente\n" + "Causa: " + e.getLocalizedMessage());
        }
    }

    public PanelAddDispositivo getPanelAddDispositivo() {
        if (panelAddDispositivo == null) {
            panelAddDispositivo = (PanelAddDispositivo) PanelsInstances.getInstance().getPanelModal(PanelAddDispositivo.class);
            panelAddDispositivo.installEventCancel(()->{
                ModalDialog.popModel(ID);
                panelAddDispositivo.cleanValue();
            });
            panelAddDispositivo.installEventAddReparaciones(this::mostrarPantallaAgregarReparacion);

        }
        return panelAddDispositivo;
    }


    public PanelAddReparacion getPanelAddReparacion() {
        if (panelAddReparacion == null) {
            panelAddReparacion = (PanelAddReparacion) PanelsInstances.getInstance().getPanelModal(PanelAddReparacion.class);
        }
        return panelAddReparacion;
    }

    public PanelSearchCliente getPanelSearchCliente() {
        if (panelSearchCliente == null) {
            panelSearchCliente = (PanelSearchCliente) PanelsInstances.getInstance().getPanelModal(PanelSearchCliente.class);
            panelSearchCliente.installEventSearch(this::buscarClientePorNombre);
            panelSearchCliente.installEventSelect((cliente) -> {
                getPanelAddNota().setCustomerModel(cliente);
                ModalDialog.popModel(ID);
                return null;
            });
        }
        return panelSearchCliente;
    }

    public PanelAddNota getPanelAddNota() {
        if (panelAddNota == null) {
            panelAddNota = (PanelAddNota) PanelsInstances.getInstance().getPanelModal(PanelAddNota.class);
            panelAddNota.installEventAddNota(() -> {
                Toast.closeAll();
                if (Toast.checkPromiseId(KEY)) return;
                NotaRequest value = panelAddNota.getValue();
                if (validarDatosNota(value)) return;
                registrarNota(value);
            });
            panelAddNota.installEventSearchCustomer(() -> {
                showPanel(getPanelSearchCliente(), "Buscar Cliente", "ic_search_panel.svg", ID, () -> {
                    ModalDialog.popModel(ID);
                }, true);
            });
            panelAddNota.installEventAddDevice(this::mostrarPantallaAgregarDispositivo);
        }
        if (panelAddNota.getEmployee().getItemCount() == 1) obtenerListEmpleados();
        return panelAddNota;
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

//    private Consumer<Nota> eventViewPreferences = e -> {
//        ModalDialog.showModal(SwingUtilities.windowForComponent(this),
//                new PanelModalInfoNote().setData(e),
//                Config.getModelShowModalFromNote(),
//                PanelModalInfoDevice.ID
//        );
//    };
}
