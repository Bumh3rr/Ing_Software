package bumh3r.model;

import bumh3r.notifications.Notify;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.Promiseld;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingUtilities;
import lombok.Getter;
import raven.modal.Toast;

@Getter
public class TipoReparacion {
    private int id;
    private String nombre;

    public TipoReparacion(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return this.nombre;
    }


    public static class ListTiposReparaciones {

        public static final String KEY = ListTiposReparaciones.class.getName();
        private static ListTiposReparaciones instance;
        public List<TipoReparacion> list;

        public static ListTiposReparaciones getInstance() {
            if (instance == null) {
                instance = new ListTiposReparaciones();
            }
            return instance;
        }

        public ListTiposReparaciones() {
            this.list = new ArrayList<>();
        }

        public List<TipoReparacion> getList()throws Exception {
            if (this.list == null || this.list.isEmpty()) {
//                this.list = RequestTipoReparacion.getListTiposReparaciones();
                return this.list;
            }
            return this.list;
        }

        public void clean(){
            this.list = new ArrayList<>();
        }


        public static void addItemsTipoReparaciones(FlatComboBox<TipoReparacion> comboBox) {
            PoolThreads.getInstance().getExecutorService().execute(() -> {
                if (Promiseld.checkPromiseId(KEY)) {
                    return;
                }
                Promiseld.commit(KEY);
                try {
                    List<TipoReparacion> list = getInstance().getList();
                    comboBox.removeAllItems();
                    SwingUtilities.invokeLater(() -> {
                        comboBox.setModel(
                                new DefaultComboBoxModel<>(list.stream().toArray(TipoReparacion[]::new)));
                        comboBox.setSelectedIndex(0);
                        comboBox.updateUI();
                    });
                } catch (Exception e) {
                    Notify.getInstance().showToast(Toast.Type.ERROR, "Conexión inestable, Nose Obtuvieron los tipos de Reparaciones" +
                            "\nCausa: " + e.getLocalizedMessage());
                } finally {
                    Promiseld.terminate(KEY);
                }
            });
        }

    }


}
