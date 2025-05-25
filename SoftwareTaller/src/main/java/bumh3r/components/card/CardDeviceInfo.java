package bumh3r.components.card;

import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.label.LabelTextArea;
import bumh3r.model.Dispositivo;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Cursor;
import java.util.function.BiConsumer;
import javax.swing.JButton;
import javax.swing.JComponent;
import net.miginfocom.swing.MigLayout;

import static bumh3r.utils.PathResources.Icon.home;

public class CardDeviceInfo extends Card {
    private Dispositivo dispositivo;
    private BiConsumer<Dispositivo, Runnable> event;

    public CardDeviceInfo(Dispositivo dispositivo, BiConsumer<Dispositivo, Runnable> event) {
        super(dispositivo, event);
        this.dispositivo = dispositivo;
        this.event = event;
        init();
    }

    private void init() {
        putClientProperty(FlatClientProperties.STYLE, "" +
                "background:@background;" +
                "arc:16;");
        setLayout(new MigLayout("wrap 3,fill,insets 10 20 10 20,w 450!,", "[][]0[grow 0]"));
        add(LabelTextArea.ForNote.getLabelGramatical("Dispositivo:"), "span 2,grow 0,al center,split 2");
        add(getLabelCard(this.dispositivo.getTipo_dispositivo().getNombre()));
        add(getIconAngle(), "span 1 2,ax trail,ay center,wrap,growx 0,growy 0,gapx 0 1");
        add(LabelTextArea.ForNote.getLabelGramatical("Marca:"), "span 1,grow 0,al trail,split 2");
        add(getLabelCard(this.dispositivo.getMarca().getNombre()), "grow 0");
        add(LabelTextArea.ForNote.getLabelGramatical("Modelo:"), "span 1,grow 0,al lead,split 2");
        add(getLabelCard(this.dispositivo.getModelo()), "grow 0");
    }

    private JButton getIconAngle() {
        JButton icon = new JButton();
        icon.setCursor(Cursor.getDefaultCursor());
        icon.setIcon(new FlatSVGIcon(home + "ic_angle_right.svg"));
        icon.addActionListener((x)->event.accept(this.dispositivo, () -> {}));
        icon.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:999;"
                + "borderWidth:0;"
                + "focusWidth:0;"
                + "innerFocusWidth:0;"
                + "margin:5,5,5,5;"
                + "background:null");

        return icon;
    }

    private JComponent getLabelCard(String title) {
        return new LabelPublicaSans(title).size(14f);
    }


}
