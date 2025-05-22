package bumh3r.controller;

import bumh3r.dao.CategoriaDao;
import bumh3r.request.ClienteRequest;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.view.form.FormInventario;
import bumh3r.view.panel.PanelAddCliente;
import bumh3r.view.panel.PanelAddRefaccion;
import raven.modal.Toast;

public class ControladorInventario extends Controller {
    public static String KEY = ControladorInventario.class.getName();
    private FormInventario view;
    private CategoriaDao categoriaDao;
    private PanelAddRefaccion panelAddRefaccion;

    public ControladorInventario(FormInventario view) {
        this.categoriaDao = getInstance(CategoriaDao.class);
        this.view = view;
        this.view.setEventFormInit(this::obtenerListaRefaccion);
        this.view.setEventFormRefresh(this::obtenerListaRefaccion);
        this.view.installEventShowPanelAddRefaccion(this::mostrarPantallaAgregarRefaccion);
        //showModal(PanelAddRefaccion.class, "Agregar nueva refacción", "ic_inventario.svg", PanelAddRefaccion.ID);

    }

    private void mostrarPantallaAgregarRefaccion() {
        if (panelAddRefaccion == null) {
            panelAddRefaccion = (PanelAddRefaccion) PanelsInstances.getInstance().getPanelModal(PanelAddRefaccion.class);
//            panelAddRefaccion.installEventAddRefaccion(() -> {
//                Toast.closeAll();
//                if (Toast.checkPromiseId(KEY)) return;
//                ClienteRequest value = panelAddRefaccion.getValue();
//                if (value == null || validarDatosCliente(value)) return;
//                registrarCliente(value);
//            });
        }
        showPanel(panelAddRefaccion, "Agregar nuevo Cliente", "ic_add-user.svg", ID, null, false);
    }
    private void obtenerListaRefaccion() {

    }
}
