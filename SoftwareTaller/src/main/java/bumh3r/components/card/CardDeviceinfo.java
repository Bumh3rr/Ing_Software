package bumh3r.components.card;

import bumh3r.archive.PathResources;
import bumh3r.components.label.LabelTextArea;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.Reparacion_Dispositivo;
import bumh3r.model.TipoReparacion;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Font;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class CardDeviceinfo extends JPanel {
    private String modelo;
    private String urlIcon;
    private List<Reparacion_Dispositivo> listRepairs;

    public CardDeviceinfo(String modelo, String urlIcon, List<Reparacion_Dispositivo> listRepairs) {
        this.modelo = modelo;
        this.urlIcon = urlIcon;
        this.listRepairs = listRepairs;
        this.initCardDevice();
        this.pintCardsRepairs();
    }

    private void initCardDevice() {
        this.setLayout(new MigLayout("wrap,fillx,insets n", "[grow]", "grow 0"));
        this.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:45;" +
                "[light]background:darken(@background,4%);" +
                "[dark]background:lighten(@background,3%);"
        );

        this.add(createdIcon(), "al center");
        this.add(getTitle(this.modelo), "al center");
    }

    protected JLabel createdIcon() {
        JLabel label = new JLabel(new FlatSVGIcon(PathResources.Icon.marcas + this.urlIcon, 0.9f));
        label.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:0,0,0,0;");
        return label;
    }

    private JLabel getTitle(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER) {
            @Override
            public void setFont(Font font) {
                super.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD_BLACK, 13f));
            }
        };
        return label;
    }

    private void pintCardsRepairs() {
        for (Reparacion_Dispositivo reparacion : listRepairs) {
            SwingUtilities.invokeLater(() -> {
                this.add(createdCardRepair(reparacion), "grow");
            });
        }
        this.updateUI();
    }

    private JComponent createdCardRepair(Reparacion_Dispositivo reparacion) {
        JButton component = new JButton();
        component.setLayout(new MigLayout("wrap,fillx,insets 7", "[grow]"));
        component.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:45;" +
                "[light]background:lighten(#efefef,2%);" +
                "[dark]background:lighten(@background,2%);" +
                "hoverBackground:null;" +
                "pressedBackground:null;" +
                "hoverBorderColor:null;" +
                "hoverSelectedBorderColor:null;" +
                "focusedSelectedBorderColor:null;" +
                "focusedBorderColor:null;" +
                "selectedBackground:null;" +
                "innerFocusWidth:0;" +
                "innerFocusWidth:0;" +
                "borderWidth:1.4;" +
                "[light]borderColor:#e0e0e0;");

        component.add(getIcon(reparacion.getTipoReparacion()), "grow 0,al center");
        component.add(getLabelGramatical("Categoria:"),"al center,grow 0,split 2");
        component.add(getTitle(reparacion.getCategoria().getName()), "grow 0");
        component.add(getLabelGramatical("Reparación:"),"al center,grow 0,split 2");
        component.add(getTitle(reparacion.getReparacion().getName()), "grow 0");
        component.add(getLabelGramatical("Precio:"),"al center,grow 0,split 2");
        component.add(getTitle(reparacion.getPrecio().toString()), "grow 0");
        component.add(getLabelGramatical("Anticipo:"),"al center,grow 0,split 2");
        component.add(getTitle(reparacion.getAbono().toString()), "grow 0");
        component.updateUI();
        return component;
    }

    private JComponent getLabelGramatical(String text) {
        JLabel label = LabelTextArea.ForNote.getLabelGramatical(text);
        label.setHorizontalAlignment(JLabel.TRAILING);
        return label;
    }

    private JLabel getIcon(TipoReparacion type) {
        switch (type.getId()) {
            case 1 -> {
                return createdIcon(type.getNombre(), "#ff420a");
            }
            case 2 -> {
                return createdIcon(type.getNombre(), "#5695ff");
            }
            case 3 -> {
                return createdIcon(type.getNombre(), "#ff9300");
            }
            default -> {
                return createdIcon(type.getNombre(), "#aa7942");
            }
        }
    }

    private JLabel createdIcon(String text, String color) {
        JLabel label = new JLabel(text);
        label.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "border:2,6,2,6;"
                + "foreground:shade(" + color + ",3%);"
                + "background:fade(" + color + ",15%);");
        label.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD_BLACK, 13.5f));
        label.updateUI();
        return label;
    }


}
