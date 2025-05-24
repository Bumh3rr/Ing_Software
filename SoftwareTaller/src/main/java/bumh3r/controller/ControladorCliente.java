package bumh3r.controller;

import bumh3r.dao.ClienteDao;
import bumh3r.model.New.ClienteN;
import bumh3r.model.New.EmpleadoN;
import bumh3r.notifications.Notify;
import bumh3r.request.ClienteRequest;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.utils.CheckExpression;
import bumh3r.utils.CheckInput;
import bumh3r.view.form.FormCliente;
import bumh3r.view.panel.PanelAddCliente;
import bumh3r.view.panel.PanelClienteNotes;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.toast.ToastPromise;

public class ControladorCliente extends Controller {
    public static String KEY = ControladorCliente.class.getName();
    private FormCliente view;
    private ClienteDao clienteDao;
    private PanelAddCliente panelAddCliente;

    public ControladorCliente(FormCliente view) {
        this.clienteDao = getInstance(ClienteDao.class);
        this.view = view;
        this.view.setEventFormInit(this::obtenerListaCliente);
        this.view.setEventFormRefresh(this::obtenerListaCliente);
        this.view.installEventAddCliente(this::mostrarPantallaAgregarCliente);
        this.view.installEventSearchCliente(this::buscarClientePorNombre);
    }

    private void obtenerListaCliente() {
        Toast.closeAll();
        if (Toast.checkPromiseId(KEY)) return;
        Notify.showPromise("Obteniendo los Clientes ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Obteniendo los Clientes  ...");
                            List<ClienteN> list = clienteDao.findAll();
                            if (list.isEmpty()) {
                                callback.done(Toast.Type.WARNING, "No hay clientes registrados");
                                return;
                            }
                            view.addAllTable(list);
                            callback.done(Toast.Type.SUCCESS, "Los empleados fueron obtenidos correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error al obtener los empleados\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    }

    public void mostrarPantallaAgregarCliente() {
        if (panelAddCliente == null) {
            panelAddCliente = (PanelAddCliente) PanelsInstances.getInstance().getPanelModal(PanelAddCliente.class);
            panelAddCliente.installEventAddCustomer(() -> {
                Toast.closeAll();
                if (Toast.checkPromiseId(KEY)) return;
                ClienteRequest value = panelAddCliente.getValue();
                if (value == null || validarDatosCliente(value)) return;
                registrarCliente(value);
            });
        }
        showPanel(panelAddCliente, "Agregar nuevo Cliente", "ic_add-user.svg", ID, null, false);
    }

    private void registrarCliente(ClienteRequest value) {
        Notify.showPromise("Registrando el Cliente ...", new ToastPromise(KEY) {
            @Override
            public void execute(PromiseCallback callback) {
                try {
                    callback.update("Registrando el Cliente ...");
                    ClienteN newCustomer = ClienteN.builder()
                            .nombre(value.nombre())
                            .telefono_movil(value.telefono_movil())
                            .telefono_fijo(value.telefono_fijo())
                            .direccion(value.direccion())
                            .fecha_registro(LocalDateTime.now())
                            .build();

                    newCustomer = clienteDao.save(newCustomer);
                    view.addOneTable(newCustomer);
                    panelAddCliente.cleanValue(); // <- Limpiar los campos
                    ModalDialog.closeModal(ID); // <- Cerrar la pantallaAgregarCliente
                    callback.done(Toast.Type.SUCCESS, "Cliente agregado correctamente");
                } catch (Exception ex) {
                    callback.done(Toast.Type.ERROR, "Error al registrar el Cliente\n" + "Causa: " + ex.getLocalizedMessage());
                }
            }
        });
    }

    public Function<ClienteN, Void> mostrarNotasCliente = (cliente) -> {
        PanelClienteNotes panelClienteNotes = new PanelClienteNotes(cliente);
        panelClienteNotes.panelCheckUI();
        showPanel(panelClienteNotes, "Notas del Cliente", "ic_note.svg", ID, null, false);
        return null;
    };

    public void buscarClientePorNombre() {
        List<ClienteN> list;
        String nombre;
        try {
            nombre = view.getSearch().getText().trim().toLowerCase();
            if (nombre.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "Ingrese un nombre para buscar");
                return;
            }
            list = clienteDao.findByName(nombre);
            if (list.isEmpty()) {
                Notify.getInstance().showToast(Toast.Type.WARNING, "No se encontraron resultados");
                return;
            }
            view.addAllTable(list);
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Error al buscar el cliente\n" + "Causa: " + e.getLocalizedMessage());
        }
    }

    public boolean validarDatosCliente(ClienteRequest value) {
        Toast.closeAll();
        // Datos requeridos
        if (CheckInput.isInvalidInput(value.nombre(), CheckExpression::isNameValid, "Nombre", "solo debe contener letras"))
            return true;
        if (CheckInput.isNullInput(value.telefono_movil(), "Teléfono Móvil")) return true;
        return false;
    }

}
