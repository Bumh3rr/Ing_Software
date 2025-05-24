package bumh3r.model;

import bumh3r.archive.PathResources;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.notifications.Notify;
import bumh3r.thread.PoolThreads;
import bumh3r.utils.Promiseld;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.awt.Color;
import java.awt.Component;
import java.time.LocalDateTime;
import java.util.LinkedList;
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
import lombok.Data;
import net.miginfocom.swing.MigLayout;
import raven.modal.Toast;

@Data
@AllArgsConstructor
public class Nota {
    private int id;
    private String folio;
    private LocalDateTime fecha_nota;
    private StatusNota status;
    private Empleado recido_por;
    private Cliente cliente;
    private List<Dispositivo> dispositivos;


    public Nota(int id, String folio, LocalDateTime fecha_nota, StatusNota status, Empleado recido_por, Cliente cliente, double precioTotal, double anticipoTotal, double cantidadPendiente, List<Dispositivo> dispositivos) {
        this.id = id;
        this.folio = folio;
        this.fecha_nota = fecha_nota;
        this.status = status;
        this.recido_por = recido_por;
        this.cliente = cliente;
        this.dispositivos = dispositivos;
    }

    public Nota(LocalDateTime fecha_nota, StatusNota status, Empleado recido_por, Cliente cliente, double precioTotal, double anticipoTotal, List<Dispositivo> dispositivos) {
        this(0,null,fecha_nota,status,recido_por,cliente,precioTotal,anticipoTotal,(precioTotal-anticipoTotal),dispositivos);
    }

    public Nota() {
    }





    public enum StatusNota{

        EN_PROCESO('P',"En Proceso"),
        TERMINADO('T',"Terminado"),
        CANCELADO('C',"Cancelado");

        private final char id;
        private final String value;
        public static final String KEY = StatusNota.class.getName();

        StatusNota(char id, String value){
            this.id = id;
            this.value = value;
        }

        public String getValue(){
            return value;
        }
        public char getId(){
            return id;
        }

        @Override
        public String toString() {
            return value;
        }

        public static StatusNota fromStatus(char key) {
            for (StatusNota status : StatusNota.values()) {
                if (status.getId() == key) {
                    return status;
                }
            }
            return null;
        }


        public static void addItemsStatusNote(FlatComboBox<StatusNota> comboBox) {
            PoolThreads.getInstance().getExecutorService().execute(() -> {
                if (Promiseld.checkPromiseId(KEY)) {
                    return;
                }
                Promiseld.commit(KEY);
                try {
                    comboBox.removeAllItems();
                    SwingUtilities.invokeLater(() -> {
                        comboBox.setModel(
                                new DefaultComboBoxModel<>(StatusNota.values()));
                        comboBox.setRenderer(new ListCellRenderer<StatusNota>() {
                            @Override
                            public Component getListCellRendererComponent(JList<? extends StatusNota> list, StatusNota value, int index, boolean isSelected, boolean cellHasFocus) {
                                JPanel panel = new JPanel(new MigLayout("wrap 4,insets 4 5 4 5"));
                                panel.putClientProperty(FlatClientProperties.STYLE, "" +
                                        "arc:20;" +
                                        "background:null;");

                                JLabel icon = new JLabel(selectIcon(value));
                                JLabel label = new JLabel(value.getValue());
                                label.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD_BLACK, 13f));
                                label.putClientProperty(FlatClientProperties.STYLE, ""
                                        + "arc:16;"
                                        + "border: 2,6,2,6,shade(" + value.getBackgroundStatus() + ",3%);"
                                        + "foreground:shade(" + value.getBackgroundStatus() + ",3%);"
                                        + "background:fade(" + value.getBackgroundStatus() + ",8%);");

                                panel.add(icon);
                                panel.add(new JSeparator(SwingConstants.VERTICAL), "growy,gapy 2 2");
                                panel.add(label);
                                return panel;
                            }
                            private FlatSVGIcon selectIcon(StatusNota value) {
                                switch (value) {
                                    case EN_PROCESO:
                                        return createdIcon("ic_inprogress.svg",value.getBackgroundStatus());
                                    case TERMINADO:
                                        return createdIcon("ic_finish.svg",value.getBackgroundStatus());
                                    case CANCELADO:
                                        return createdIcon("ic_error.svg",value.getBackgroundStatus());
                                    default:
                                        return createdIcon("ic_inprogress.svg",value.getBackgroundStatus());
                                }
                            }

                            private FlatSVGIcon createdIcon(String url,String color) {
                                return new FlatSVGIcon( PathResources.Icon.modal + url, 0.6f).setColorFilter(new FlatSVGIcon.ColorFilter((x)->Color.decode(color)));
                            }
                        });
                        comboBox.putClientProperty(FlatClientProperties.STYLE, "" +
                                "padding:1,3,1,1;" +
                                "[light]background:darken(@background,2%);"
                        );
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



        public String getBackgroundStatus() {
            switch (this) {
                case EN_PROCESO:
                    return "#ff9300";
                case TERMINADO:
                    return "#1aad2c";
                case CANCELADO:
                    return "#ff420a";
            }
            return "#ff9300";
        }



    }




}
