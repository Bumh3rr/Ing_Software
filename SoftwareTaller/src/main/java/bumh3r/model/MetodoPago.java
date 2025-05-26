package bumh3r.model;

import bumh3r.notifications.Notify;
import bumh3r.repository.MetodoPagoDao;
import bumh3r.utils.PathResources;
import bumh3r.utils.Promiseld;
import bumh3r.utils.thread.PoolThreads;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "metodo_pago")
public class MetodoPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @Override
    public String toString() {
        return String.format("%s", nombre);
    }


    public static void addItemsMetodoPago(FlatComboBox<MetodoPago> comboBox) {
        PoolThreads.getInstance().getExecutorService().execute(() -> {
            try {
                List<MetodoPago> list = new MetodoPagoDao().findAll();
                comboBox.removeAllItems();
                SwingUtilities.invokeLater(() -> {
                    comboBox.setModel(
                            new DefaultComboBoxModel<>(list.stream().toArray(MetodoPago[]::new)));
                    comboBox.setRenderer(new ListCellRenderer<>() {
                        @Override
                        public Component getListCellRendererComponent(JList<? extends MetodoPago> list, MetodoPago value, int index, boolean isSelected, boolean cellHasFocus) {
                            JPanel panel = new JPanel(new MigLayout("wrap 4,insets 4 5 4 5"));
                            panel.putClientProperty(FlatClientProperties.STYLE, "" +
                                    "arc:20;" +
                                    "background:null;");
                            JLabel icon = new JLabel(selectIcon(value.getId()));
                            JLabel name = new JLabel(String.format("%s", value.getNombre()));
                            panel.add(icon);
                            panel.add(new JSeparator(SwingConstants.VERTICAL), "growy,gapy 2 2");
                            panel.add(name);
                            return panel;
                        }

                        private FlatSVGIcon selectIcon(Long id) {
                            if (id == 1l) {
                                return createdIcon("cash.svg");
                            } else if (id == 2) {
                                return createdIcon("mastercard.svg");
                            } else if (id == 3) {
                                return createdIcon("visacard.svg");
                            } else if (id == 4) {
                                return createdIcon("transferbank.svg");
                            } else if (id == 5) {
                                return createdIcon("paypal.svg");
                            }
                            return createdIcon("cash.svg");
                        }

                        private FlatSVGIcon createdIcon(String url) {
                            return new FlatSVGIcon(PathResources.Icon.payment + url, 0.6f);
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
            }
        });
    }

}
