package bumh3r.model;


import bumh3r.archive.PathResources;
import bumh3r.notifications.Notify;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.Promiseld;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.awt.Component;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

@Getter
public class MetodoPago {
    private int id;
    private String metodo;

    public MetodoPago(int id, String metodo) {
        this.id = id;
        this.metodo = metodo;
    }

    @Override
    public String toString() {
        return this.metodo;
    }

    public static class ListMetodosPago {
        private static final String KEY = ListMetodosPago.class.getName();
        private static ListMetodosPago instance;
        private List<MetodoPago> list;

        public static ListMetodosPago getInstance() {
            if (instance == null) {
                instance = new ListMetodosPago();
            }
            return instance;
        }

        public List<MetodoPago> getList() throws Exception {
            if (this.list == null || this.list.isEmpty()) {
//                this.list = DaoMetodoPago.getListMetodosPagos();
                this.list = List.of(new MetodoPago(1,"Efectivo"),
                        new MetodoPago(2,"Tarjeta de Crédito"),
                        new MetodoPago(3,"Tarjeta de Débito"),
                        new MetodoPago(4,"Transferencia Bancaria"),
                        new MetodoPago(5,"PayPal"));
                return this.list;
            }
            return this.list;
        }

        public static void addItemsMetodoPago(FlatComboBox<MetodoPago> comboBox) {
            PoolThreads.getInstance().getExecutorService().execute(() -> {
                if (Promiseld.checkPromiseId(KEY)) {
                    return;
                }
                Promiseld.commit(KEY);
                try {
                    List<MetodoPago> list = getInstance().getList();
                    comboBox.removeAllItems();
                    SwingUtilities.invokeLater(() -> {
                        comboBox.setModel(
                                new DefaultComboBoxModel<>(list.stream().toArray(MetodoPago[]::new)));
                        comboBox.setRenderer(new ListCellRenderer<MetodoPago>() {
                            @Override
                            public Component getListCellRendererComponent(JList<? extends MetodoPago> list, MetodoPago value, int index, boolean isSelected, boolean cellHasFocus) {
                                JPanel panel = new JPanel(new MigLayout("wrap 4,insets 4 5 4 5"));
                                panel.putClientProperty(FlatClientProperties.STYLE, "" +
                                        "arc:20;" +
                                        "background:null;");
                                JLabel icon = new JLabel(selectIcon(value.getId()));
                                JLabel name = new JLabel(String.format("%s",value.getMetodo()));
                                panel.add(icon);
                                panel.add(new JSeparator(SwingConstants.VERTICAL), "growy,gapy 2 2");
                                panel.add(name);
                                return panel;
                            }
                            private FlatSVGIcon selectIcon(int id) {
                                switch (id) {
                                    case 1:
                                        return createdIcon("cash.svg");
                                    case 2:
                                        return createdIcon("mastercard.svg");
                                    case 3:
                                        return createdIcon("visacard.svg");
                                    case 4:
                                        return createdIcon("transferbank.svg");
                                    case 5:
                                        return createdIcon("paypal.svg");
                                    default:
                                        return createdIcon("cash.svg");
                                }
                            }
                            private FlatSVGIcon createdIcon(String url) {
                                return new FlatSVGIcon( PathResources.Icon.payment + url, 0.6f);
                            }
                        });
                        comboBox.putClientProperty(FlatClientProperties.STYLE, "" +
                                "padding:3,7,3,7");
                        comboBox.setSelectedIndex(0);
                        comboBox.updateUI();
                    });
                } catch (Exception e) {
                    Notify.getInstance().showToast(Toast.Type.ERROR, "Conexión inestable, Nose Obtuvieron las Formas de Pago" +
                            "\nCausa: " + e.getLocalizedMessage());
                } finally {
                    Promiseld.terminate(KEY);
                }
            });
        }

    }

}
