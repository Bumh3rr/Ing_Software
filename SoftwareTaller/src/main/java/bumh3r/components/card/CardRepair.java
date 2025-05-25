
package bumh3r.components.card;

import bumh3r.components.input.InputFormattedDecimal;
import bumh3r.request.ReparacionRequest;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Cursor;
import java.util.function.BiConsumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;

import static bumh3r.utils.PathResources.Img.categorydevice;

public class CardRepair extends Card {
    private final ReparacionRequest reparacion;
    private final BiConsumer<ReparacionRequest, Runnable> event;

    public CardRepair(ReparacionRequest reparacion, BiConsumer<ReparacionRequest, Runnable> event) {
        super(reparacion, event);
        this.reparacion = reparacion;
        this.event = event;
        init();
    }

    private void init() {
        putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:30;" +
                "[light]background:darken($Panel.background,3%);" +
                "[dark]background:lighten($Panel.background,3%);");

        setLayout(new MigLayout("wrap 2,insets 5", "[fill,center][]", "[grow 0,center]"));
        add(createIcon(), "gapx 15");
        add(createBody());
        revalidate();
        updateUI();
    }

    private JComponent createIcon() {
        AvatarIcon icon = new AvatarIcon(createdIcon(), 100, 100, 3.9f);
        icon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        icon.setBorder(2, 2);
        icon.setBorderColor(new AvatarIcon.BorderColor(Color.decode("#6d6d6d"), 0.2f));
        return new JLabel(icon);
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new MigLayout("wrap", "[200]", "[][][]push"));
        body.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null");
        JLabel title = new JLabel(reparacion.categoria().toString());
        title.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +1;");
        JTextPane description = new JTextPane();
        description.setEditable(false);
        description.setCursor(new Cursor(Cursor.TEXT_CURSOR));
        description.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,0,0,0;" +
                "background:null;" +
                "[light]foreground:tint($Label.foreground,30%);" +
                "[dark]foreground:shade($Label.foreground,30%)");
        description.setText(
                "Reparación: " + reparacion.reparacion()
                        + "\nDescripción: " + (reparacion.observacion() == null || reparacion.observacion().isEmpty() ?
                        "campo vacío" : reparacion.observacion())
                        + "\nPrecio: $" + InputFormattedDecimal.decimalFormat.format(reparacion.precio())
                        + "\nAnticipo: $" + InputFormattedDecimal.decimalFormat.format(reparacion.abono())
                        + "\nTécnico Encargado:\n " + reparacion.tecnico().toString());

        JButton button = new JButton();
        button.setText("Eliminar");
        button.addActionListener(e -> event.accept(reparacion, null));
        button.setBackground(new Color(255, 51, 102));
        button.setForeground(new Color(255, 255, 255));

        body.add(title);
        body.add(description);
        body.add(button, "al center");
        return body;
    }

    private ImageIcon createdIcon() {
        switch (reparacion.categoria()) {
            case SOFTWARE -> {
                return new ImageIcon(CardRepair.class.getResource(categorydevice + "software.png"));
            }
            case HARDWARE -> {
                return new ImageIcon(CardRepair.class.getResource(categorydevice + "hardware.png"));
            }
            default -> {
                return new ImageIcon(CardRepair.class.getResource(categorydevice + "otros.png"));
            }
        }
    }

}
