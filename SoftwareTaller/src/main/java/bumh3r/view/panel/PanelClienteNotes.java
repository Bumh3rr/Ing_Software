package bumh3r.view.panel;

import bumh3r.components.ContainerCards;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.modal.CustomModal;
import bumh3r.model.Cliente;
import bumh3r.model.New.ClienteN;
import bumh3r.model.Nota;
import bumh3r.model.other.DateFull;
import bumh3r.system.panel.Panel;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.view.form.FormCliente;
import bumh3r.view.modal.PanelModalInfoNote;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import static bumh3r.archive.PathResources.Icon.modal;

public class PanelClienteNotes extends Panel {
    private ContainerCards containerCards;
    private JPanel panelNotes;
    private InputText id, name, phone1, phone2, address;
    private ClienteN cliente;

    public PanelClienteNotes(ClienteN cliente) {
        this.cliente = cliente;
        initComponents();
        init();
    }

    private void initComponents() {
        panelNotes = new JPanel(new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 10));
        panelNotes.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%)");
        panelNotes.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:10,10,10,10;");
        containerCards = new ContainerCards(panelNotes);
        id = getInstance();
        name = getInstance();
        phone1 = getInstance();
        phone2 = getInstance();
        address = getInstance();
        id.setText(cliente.getId().toString());
        name.setText(cliente.getNombre());
        phone1.setText(cliente.getTelefono_movil());
        phone2.setText(cliente.getTelefono_fijo());
        address.setText(cliente.getDireccion());
//        refreshPanelNotes(cliente.getNotas());
    }

    private InputText getInstance() {
        InputText input = new InputText("campo vació", 300) {
            @Override
            public void setText(String t) {
                if (t == null || t.isEmpty()) {
                    t = "";
                }
                super.setText(t);
            }
        };
        input.setEditable(false);
        return input;
    }

    private void init() {
        setLayout(new MigLayout("wrap 2,fillx,insets 2 45 20 45,w 350:550", "fill,grow"));

        add(createdSubTitle("Datos del Cliente", 15), "span,grow 0,al center");
        add(createdGramaticalP("ID"), "grow 0,al lead");
        add(createdGramaticalP("Nombre Completo"), "grow 0,al lead");
        add(id);
        add(name);
        add(createdGramaticalP("Teléfono 1"), "grow 0,al lead");
        add(createdGramaticalP("Teléfono 2"), "grow 0,al lead");
        add(phone1);
        add(phone2);
        add(createdGramaticalP("Dirección"), "span,grow 0,al lead");
        add(address, "span,grow");

        add(createdSubTitle("Notas Registradas", 15), "span,grow 0,al center");
        add(containerCards, "span,grow,push");
    }

    public void refreshPanelNotes(List<Nota> list) {
        SwingUtilities.invokeLater(() -> {
            panelNotes.removeAll();
            list.forEach((note) -> {
                panelNotes.add(new CardInfoNote(note, eventViewPreferences));
                panelNotes.add(new CardInfoNote(note, eventViewPreferences));
                panelNotes.add(new CardInfoNote(note, eventViewPreferences));
            });
            panelNotes.updateUI();
            EventQueue.invokeLater(() -> containerCards.getVerticalScrollBar().setValue(0));
            updateUI();
        });
    }

    private Consumer<Nota> eventViewPreferences = e -> {
        ModalDialog.pushModal(
                CustomModal.builder()
                        .component(new PanelModalInfoNote())
                        .icon(modal + "ic_note.svg")
                        .buttonClose(false)
                        .title("Nota")
                        .rollback(()->ModalDialog.popModel(FormCliente.ID))
                        .ID(FormCliente.ID)
                        .build(),
                FormCliente.ID
        );
    };

    private class CardInfoNote extends JPanel {
        private Nota nota;
        private Consumer<Nota> eventView;

        public CardInfoNote(Nota nota, Consumer<Nota> eventView) {
            this.nota = nota;
            this.eventView = eventView;
            init();
        }

        private void init() {
            setLayout(new MigLayout("fill,insets 15", "fill", "fill"));
            putClientProperty(FlatClientProperties.STYLE, ""
                    + "arc:30;"
                    + "[dark]background:lighten($Panel.background,3%);"
            );
            add(createBody(), "grow 0,al lead");
            updateUI();
        }


        private JPanel createBody() {
            JPanel body = new JPanel(new MigLayout("wrap, insets 0", "[150]", "[][]push[]push"));
            body.putClientProperty(FlatClientProperties.STYLE, ""
                    + "background:null");
            JLabel title = new JLabel(this.nota.getFolio());
            title.putClientProperty(FlatClientProperties.STYLE, "font:bold +1;");
            JTextPane description = new JTextPane();
            description.setEditable(false);
            description.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            description.putClientProperty(FlatClientProperties.STYLE, ""
                    + "border:0,0,0,0;"
                    + "background:null;"
                    + "[dark]foreground:shade($Label.foreground,30%)"
            );
            description.setText(
                    "Fecha registro: " + DateFull.getDateFull(nota.getFecha_nota())
                            + "\nEstatus: " + nota.getStatus().getValue()
                            + "\nRecibido: " + nota.getRecido_por().getFirstname() + " " + nota.getRecido_por().getLastname()
                            + "\nDispositivos: " + nota.getDispositivos().size()
            );

            body.add(title);
            body.add(description);
            return body;
        }
    }

}
