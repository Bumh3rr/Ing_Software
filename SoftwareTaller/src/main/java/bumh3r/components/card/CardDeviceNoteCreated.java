package bumh3r.components.card;

import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.label.LabelTextArea;
import bumh3r.interfaces.DeviceCallback;
import bumh3r.model.Dispositivo;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Cursor;
import java.util.function.Consumer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import static bumh3r.archive.PathResources.Icon.home;
import static bumh3r.archive.PathResources.Icon.modal;

public class CardDeviceNoteCreated extends JPanel {

    private Dispositivo dispositivo;
    private int index;
    private Consumer<Integer> delete;
    private DeviceCallback view;

    public CardDeviceNoteCreated(Dispositivo dispositivoCard, int index, Consumer<Integer> delete, DeviceCallback view) {
        this.dispositivo = dispositivoCard;
        this.index = index;
        this.view = view;
        this.delete = delete;
        initCardDevice();
    }

    private void initCardDevice() {
        JButton icon_delete = getIcon(modal + "ic_delete.svg");
        icon_delete.addActionListener((e) -> delete.accept(this.index));
        JButton icon_view = getIcon(home + "ic_angle_right.svg");
        icon_view.addActionListener((e) -> view.action(CardDeviceNoteCreated.this.dispositivo, CardDeviceNoteCreated.this.index));

        putClientProperty(FlatClientProperties.STYLE, "" +
                "background:@background;" +
                "arc:16;");
        setLayout(new MigLayout("wrap 4,fill,insets 10 20 10 20", "[grow 0][][][grow 0]"));

        add(icon_delete, "span 1 2");

        add(LabelTextArea.ForNote.getLabelGramatical("Dispositivo:"), "span 2,grow 0,al center,split 2");
        add(getLabelCard(this.dispositivo.getType().getName()));

        add(icon_view, "span 1 2");

        add(LabelTextArea.ForNote.getLabelGramatical("Marca:"), "span 1,grow 0,al trail,split 2");
        add(getLabelCard(this.dispositivo.getBrand().getNombre_marca()), "grow 0");
        add(LabelTextArea.ForNote.getLabelGramatical("Modelo:"), "span 1,grow 0,al lead,split 2");
        add(getLabelCard(this.dispositivo.getModel()), "grow 0");

        updateUI();
    }

    private JButton getIcon(String iconUrl) {
        return new JButton() {
            @Override
            public void updateUI() {
                setCursor(Cursor.getDefaultCursor());
                setIcon(new FlatSVGIcon(iconUrl));
                putClientProperty(FlatClientProperties.STYLE, ""
                        + "arc:999;"
                        + "borderWidth:0;"
                        + "focusWidth:0;"
                        + "innerFocusWidth:0;"
                        + "margin:5,20,5,20;"
                        + "background:null");
                super.updateUI();
            }
        };
    }

    private JComponent getLabelCard(String title) {
        return new LabelPublicaSans(title).size(14f);
    }
}
