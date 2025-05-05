package bumh3r.components.card;

import bumh3r.archive.PathResources;
import bumh3r.components.button.ButtonDefault;
import bumh3r.model.Nota;
import bumh3r.model.other.DateFull;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Cursor;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;

public class CardNoteSale extends JPanel {
    private Nota nota;
    private Consumer<Nota> onClick;
    private JLabel status;
    private ButtonDefault view;

    public CardNoteSale(Nota nota, Consumer<Nota> onClick) {
        this.nota = nota;
        this.onClick = onClick;
        initComponents();
        init();
    }

    private void initComponents() {
        String color = this.nota.getStatus().getBackgroundStatus();
        status = new JLabel(this.nota.getStatus().getValue());
        status.putClientProperty(FlatClientProperties.STYLE,
                "arc:26;"
                        + "border: 6,13,6,13,shade(" + color + ",3%);"
                        + "foreground:shade(" + color + ",3%);"
                        + "background:fade(" + color + ",8%);");
        view = new ButtonDefault("Seleccionar");
        view.addActionListener(e -> onClick.accept(nota));
        if (this.nota.getStatus() == Nota.StatusNota.CANCELADO){
            view.setEnabled(false);
            view.putClientProperty(FlatClientProperties.STYLE, "background:shade($Panel.background,10%);");
        }
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 5 5 10 5", "fill", "fill"));
        putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:30;"
                + "[light]background:darken($Panel.background,3%);"
                + "[dark]background:lighten($Panel.background,3%);");
        add(createHeader(), "grow 0,al trail");
        add(createBody(), "grow 0,al lead,wrap");
        add(view, "span,grow 0,al center");
        updateUI();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new MigLayout("fill,insets 10 10 0 0", "[fill,center]", "[center]"));
        header.putClientProperty(FlatClientProperties.STYLE, "background:null");
        AvatarIcon avatarIcon = new AvatarIcon(PathResources.Default.LOGO_DEFAULT__SALE_NOTE_IMAGE, 95, 95, 3.5f);
        avatarIcon.setType(AvatarIcon.Type.MASK_SQUIRCLE);
        avatarIcon.setBorder(2, 2);
        avatarIcon.setBorderColor(new AvatarIcon.BorderColor(Color.decode("#6d6d6d"), 0.1f));
        header.add(new JLabel(avatarIcon));
        return header;
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new MigLayout("wrap", "[150]", "[][]push[]push"));
        body.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null");
        JLabel title = new JLabel("Folio: " + nota.getFolio());
        title.putClientProperty(FlatClientProperties.STYLE, "font:bold +1;");
        JTextPane description = new JTextPane();
        description.setEditable(false);
        description.setContentType("text/html");
        description.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        description.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,0,0,0;"
                + "background:null;"
                + "[light]foreground:tint($Label.foreground,30%);"
                + "[dark]foreground:shade($Label.foreground,30%)");
        description.setText(
                                    "<b>Cliente</b>"
                        + "<br>Nombre: " + nota.getCliente().getName()
                        + "<br>Teléfono Móvil: " + nota.getCliente().getPhone1()
                        + "<br>Teléfono Fijo: " + nota.getCliente().getPhone2() +

                                    "<br><br><b>Nota</b>"
                        + "<br>Recibió: " + nota.getRecido_por()
                        + "<br>Fecha Creada: " + DateFull.getDateOnly(nota.getFecha_nota())
                        + "<br>Dispositivos: " + nota.getDispositivos().size()
                        + "<br>Reparaciones: " + nota.getDispositivos().size() * 2
        );

        body.add(title);
        body.add(description);
        body.add(status,"gapy 5");
        return body;
    }

}
