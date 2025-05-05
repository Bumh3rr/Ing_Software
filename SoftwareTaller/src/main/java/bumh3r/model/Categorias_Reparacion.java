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
public class Categorias_Reparacion {
    public static final String HARDWARE = "Hardware";
    public static final String SOFTWARE = "Software";
    public static final String OTHER = "Otro";

    //Hardware,Software,Other
    private char id;
    private String name;
    private String nameBaseIcon;

    //constructor
    public Categorias_Reparacion(char id, String categoria, String nameBaseIcon) {
        this.id = id;
        this.name = categoria;
        this.nameBaseIcon = nameBaseIcon;
    }

    @Override
    public String toString() {
        return this.name;
    }


    public static class ListCategorias {

        private static ListCategorias instance;
        private List<Categorias_Reparacion> categorias;
        public static final String KEY = ListCategorias.class.getName();

        public static ListCategorias getInstance() {
            if (instance == null) {
                instance = new ListCategorias();
            }
            return instance;
        }

        public ListCategorias() {
            this.categorias = new ArrayList<>();
        }

        public List<Categorias_Reparacion> getList() throws Exception {
            if (categorias == null || categorias.isEmpty()) {
                return updateCategorias();
            }
            return this.categorias;
        }

        public List<Categorias_Reparacion> updateCategorias() throws Exception {
//            this.categorias = RequestCategoriaReparaciones.getListCategoriaReparaciones();
            return this.categorias;
        }

        public void clean() {
            this.categorias = new ArrayList<>();
        }

        public static void addItemsCategorias(FlatComboBox<Categorias_Reparacion> comboBox) {
            PoolThreads.getInstance().getExecutorService().execute(() -> {
                if (Promiseld.checkPromiseId(KEY)) {
                    return;
                }
                Promiseld.commit(KEY);
                try {
                    List<Categorias_Reparacion> list = getInstance().getList();
                    SwingUtilities.invokeLater(() -> {
                        comboBox.setModel(
                                new DefaultComboBoxModel<>(list.stream().toArray(Categorias_Reparacion[]::new)));
                        comboBox.setSelectedIndex(0);
                        comboBox.updateUI();
                    });

                } catch (Exception e) {
                    Notify.getInstance().showToast(Toast.Type.ERROR, "Conexión inestable, Nose Obtuvieron las Categorías de las Reparaciones");
                } finally {
                    Promiseld.terminate(KEY);
                }
            });
        }
    }

}
