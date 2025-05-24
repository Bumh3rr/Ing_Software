package bumh3r.components.card;

import bumh3r.archive.PathResources;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.label.LabelTextArea;
import bumh3r.model.New.EmpleadoN;
import bumh3r.model.New.NotaN;
import bumh3r.model.Nota;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatLabel;
import java.awt.Color;
import java.awt.Cursor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import net.miginfocom.swing.MigLayout;
import raven.extras.AvatarIcon;

public class CardNote extends Card {
    private ButtonDefault view;
    private JLabel status;
    private final NotaN nota;
    private final BiConsumer<NotaN, Runnable> event;

    public CardNote(NotaN nota, BiConsumer<NotaN, Runnable> event) {
        super(nota, event);
        this.nota = nota;
        this.event = event;
        initComponents();
        init();
    }

    private void initComponents() {
        String color = this.nota.getEstado().getBackgroundStatus();
        status = new JLabel(this.nota.getEstado().getValue());
        status.putClientProperty(FlatClientProperties.STYLE,
                "arc:26;"
                        + "border: 6,13,6,13,shade(" + color + ",3%);"
                        + "foreground:shade(" + color + ",3%);"
                        + "background:fade(" + color + ",8%);");
        view = new ButtonDefault("Visualizar");
        view.addActionListener(e -> event.accept(nota,()->{}));
    }

    private void init() {
        setLayout(new MigLayout("fill,insets 5", "fill", "fill"));
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
        AvatarIcon avatarIcon = new AvatarIcon(PathResources.Default.LOGO_DEFAULT_NOTE_IMAGE, 80, 80, 16);
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
        description.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        description.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,0,0,0;"
                + "background:null;"
                + "[light]foreground:tint($Label.foreground,30%);"
                + "[dark]foreground:shade($Label.foreground,30%)");
        description.setText(
                "Recibió: " + nota.getEmpleado().getNombre()
                        + "\nCliente: " + nota.getCliente().getNombre()
                        + "\nDispositivos: " + nota.getDispositivos().size()
        );

        body.add(title);
        body.add(description);
        body.add(status,"gapy 5");
        return body;
    }
}
