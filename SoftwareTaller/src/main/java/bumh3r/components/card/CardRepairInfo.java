package bumh3r.components.card;

import bumh3r.utils.PathResources;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.model.Reparacion;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatTextArea;
import java.awt.Color;
import java.awt.Cursor;
import java.util.function.BiConsumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;

public class CardRepairInfo extends Card {
    private final Reparacion reparacion;
    private final BiConsumer<Reparacion, Runnable> event;
    private JLabel icon;
    private FlatTextArea description;

    public CardRepairInfo(Reparacion reparacion, BiConsumer<Reparacion, Runnable> event) {
        super(reparacion, event);
        this.reparacion = reparacion;
        this.event = event;
        init();
    }

    private void init() {
        icon = new JLabel();
        icon.setIcon(createdIcon());
        putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:30;" +
                "[light]background:darken($Panel.background,3%);" +
                "[dark]background:lighten($Panel.background,3%);");

        setLayout(new MigLayout("wrap 2,insets 5", "[fill,center][]", "[grow 0,center]"));
        add(icon, "gapx 15");
        add(createBody());
        revalidate();
        updateUI();
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new MigLayout("wrap,insets n", "[200]", "[][][]push"));
        body.putClientProperty(FlatClientProperties.STYLE, "background:null");
        JLabel title = new JLabel(this.reparacion.getCategoria().getNombre());
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +1;");
        description = new FlatTextArea();
        description.setEditable(false);
        description.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,0,0,0;" +
                "background:null;" +
                "[light]foreground:tint($Label.foreground,30%);" +
                "[dark]foreground:shade($Label.foreground,30%)");
        setValue();
        ButtonAccentBase buttonAccentBase = new ButtonAccentBase("Actualizar Estado", "#EF9D30");
        buttonAccentBase.addActionListener((x) -> this.event.accept(this.reparacion, this::setValue));
        body.add(title);
        body.add(description);
        body.add(buttonAccentBase, "al center,gapy 0 5");
        return body;
    }

    private void setValue() {
        String text = String.format("Reparación: %s\nDescripción: %s\nPrecio: $%.2f\nAnticipo: $%.2f\nTécnico Encargado:\n%s\nEstado:\n%s",
                reparacion.getReparacion(),
                reparacion.getObservacion() == null || reparacion.getObservacion().isEmpty() ? "campo vacío" : reparacion.getObservacion(),
                reparacion.getPrecio(),
                reparacion.getAbono(),
                String.format("%s %s", reparacion.getEmpleado().getNombre(), reparacion.getEmpleado().getApellido()),
                reparacion.getEstado().getEstado()
        );
        description.setText(text);
    }

    private AvatarIcon createdIcon() {
        switch (reparacion.getCategoria()) {
            case SOFTWARE -> {
                return createIcon("software.png");
            }
            case HARDWARE -> {
                return createIcon("hardware.png");
            }
            default -> {
                return createIcon("otros.png");
            }
        }
    }

    private AvatarIcon createIcon(String url) {
        AvatarIcon icon = new AvatarIcon(CardRepairInfo.class.getResource(PathResources.Img.categorydevice + url), 70, 70, 3.9f);
        icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        icon.setBorder(2, 2);
        icon.setBorderColor(new AvatarIcon.BorderColor(Color.decode("#6d6d6d"), 0.1f));
        return icon;
    }

}
