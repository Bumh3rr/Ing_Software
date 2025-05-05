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
public class TipoDispositivo {
    //Telefono,Tablet,Computadora
    private int id;
    private String name;
    private String baseUrlIcon;

    //Constructor
    public TipoDispositivo(int id, String categoria, String baseUrlIcon) {
        this.id = id;
        this.name = categoria;
        this.baseUrlIcon = baseUrlIcon;
    }

    @Override
    public String toString() {
        return this.name;
    }

    //Clase para obtener una lita de "Tipo de dispositivo"
    public static class ListTiposDispositivos {

        public static final String KEY = ListTiposDispositivos.class.getName();
        private static ListTiposDispositivos instance;
        public List<TipoDispositivo> list;

        public static ListTiposDispositivos getInstance() {
            if (instance == null) {
                instance = new ListTiposDispositivos();
            }
            return instance;
        }

        public ListTiposDispositivos() {
            this.list = new ArrayList<>();
        }

        public List<TipoDispositivo> getList()throws Exception {
            if (this.list == null || this.list.isEmpty()) {
//                this.list = RequestTipoDispositivo.getListDispositivo();
                return this.list;
            }
            return this.list;
        }

        public void clean(){
            this.list = new ArrayList<>();
        }


        public static void addItemsTipoDispositivo(FlatComboBox<TipoDispositivo> comboBox) {
            PoolThreads.getInstance().getExecutorService().execute(() -> {
                if (Promiseld.checkPromiseId(KEY)) {
                    return;
                }
                Promiseld.commit(KEY);
                try {
                    List<TipoDispositivo> list = getInstance().getList();
                    comboBox.removeAllItems();
                    SwingUtilities.invokeLater(() -> {
                        comboBox.setModel(
                                new DefaultComboBoxModel<>(list.stream().toArray(TipoDispositivo[]::new)));
                        comboBox.setSelectedIndex(0);
                        comboBox.updateUI();
                    });
                } catch (Exception e) {
                    Notify.getInstance().showToast(Toast.Type.ERROR, "Conexión inestable, Nose Obtuvieron los tipos de Dispositivos" +
                            "\nCausa: " + e.getLocalizedMessage());
                } finally {
                    Promiseld.terminate(KEY);
                }
            });
        }

    }

}
