package bumh3r.components.card;

import bumh3r.components.button.ButtonShadow;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.components.label.LabelTextArea;
import bumh3r.model.Dispositivo;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.util.function.Consumer;
import javax.swing.JComponent;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;
import static bumh3r.archive.PathResources.Icon.home;

public class CardDeviceNote extends ButtonShadow {
    private Dispositivo dispositivo;

    public CardDeviceNote(Dispositivo dispositivoCard, Consumer<Dispositivo> consumerCard) {
        this.dispositivo = dispositivoCard;
        addActionListener(e -> consumerCard.accept(dispositivoCard));
        initCardDevice();
    }

    private void initCardDevice() {
        putClientProperty(FlatClientProperties.STYLE, "" + "background:@background;");

        setLayout(new MigLayout("wrap 3,fill,insets 3 0 9 0,w 450!,h 50!", "[][]0[grow 0]"));

        add(LabelTextArea.ForNote.getLabelGramatical("Dispositivo:"), "span 2,grow 0,al center,split 2");
        add(getLabelCard(this.dispositivo.getType().getName()));

        add(getIconAngle(), "span 1 2,grow 0,ax trail,ay center,wrap");

        add(LabelTextArea.ForNote.getLabelGramatical("Marca:"), "span 1,grow 0,al trail,split 2");
        add(getLabelCard(this.dispositivo.getBrand().getNombre_marca()), "grow 0");
        add(LabelTextArea.ForNote.getLabelGramatical("Modelo:"), "span 1,grow 0,al lead,split 2");
        add(getLabelCard(this.dispositivo.getModel()), "grow 0");

        updateUI();
    }

    private JComponent getIconAngle() {
        return new JLabel(new FlatSVGIcon(home +"ic_angle_right.svg"));
    }

    private JComponent getLabelCard(String title) {
        return new LabelPublicaSans(title).size(14f);
    }


}
