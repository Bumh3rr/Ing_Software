package bumh3r.components.card;

import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.label.LabelTextArea;
import bumh3r.request.DispositivoRequest;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Cursor;
import java.util.function.BiConsumer;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import net.miginfocom.swing.MigLayout;

import static bumh3r.archive.PathResources.Icon.modal;

public class CardDevice extends Card {
    private DispositivoRequest dispositivo;
    private BiConsumer<DispositivoRequest, Runnable> event;

    public CardDevice(DispositivoRequest dispositivo, BiConsumer<DispositivoRequest, Runnable> event) {
        super(dispositivo, event);
        this.dispositivo = dispositivo;
        this.event = event;
        initCardDevice();
    }

    private void initCardDevice() {
        JButton icon_delete = getIcon(modal + "ic_delete.svg");
        icon_delete.addActionListener((e) -> event.accept(dispositivo, () -> {}));
        putClientProperty(FlatClientProperties.STYLE, "" +
                "background:@background;" +
                "arc:16;");
        setLayout(new MigLayout("wrap 4,fill,insets 10 20 10 20", "[grow 0][][][grow 0]"));
        add(icon_delete, "span 1 2");
        add(LabelTextArea.ForNote.getLabelGramatical("Dispositivo:"), "span 2,grow 0,al center,split 2");
        add(getLabelCard(this.dispositivo.getTipo_dispositivo().getNombre()));
        add(Box.createHorizontalBox(), "span 1 2");
        add(LabelTextArea.ForNote.getLabelGramatical("Marca:"), "span 1,grow 0,al trail,split 2");
        add(getLabelCard(this.dispositivo.getMarca().getNombre()), "grow 0");
        add(LabelTextArea.ForNote.getLabelGramatical("Modelo:"), "span 1,grow 0,al lead,split 2");
        add(getLabelCard(this.dispositivo.getModelo()), "grow 0");
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
