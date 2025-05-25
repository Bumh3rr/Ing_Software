package bumh3r.controller;

import bumh3r.model.Nota;
import bumh3r.notifications.Notify;
import bumh3r.repository.NotaDao;
import bumh3r.view.form.FormRegistroVenta;
import java.time.LocalDate;
import java.util.List;
import raven.modal.Toast;
import raven.modal.toast.ToastPromise;

public class ControladorVentas extends Controller {
    public static String KEY = ControladorVentas.class.getName();
    private FormRegistroVenta view;
    private final NotaDao notaDao;

    public ControladorVentas(FormRegistroVenta view) {
        this.view = view;
        this.notaDao = getInstance(NotaDao.class);
        this.view.setEventFormInit(this::obtenerListaNotas);
        this.view.setEventFormRefresh(this::obtenerListaNotas);
        this.view.installEventSearch(this::buscarNotaPorFolio);
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



//    .component(new PanelRegisterSale(e))
//            .title("Registro de Venta")
//                        .buttonClose(true)
//                        .icon(modal + "ic_sale.svg")
}
