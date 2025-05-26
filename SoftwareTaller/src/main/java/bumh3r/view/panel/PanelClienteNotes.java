package bumh3r.view.panel;

import bumh3r.components.card.CardCustomerNotes;
import bumh3r.components.card.ContainerCards;
import bumh3r.components.input.InputText;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.model.Cliente;
import bumh3r.model.Nota;
import bumh3r.system.panel.Panel;
import java.awt.Dimension;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

@Slf4j
public class PanelClienteNotes extends Panel {
    private ContainerCards<Nota> containerCards;
    private InputText id, name, phone1, phone2, address;
    private Cliente cliente;

    public PanelClienteNotes(Cliente cliente) {
        this.cliente = cliente;
        initComponents();
        init();
    }

    private void initComponents() {
        containerCards = new ContainerCards<>(CardCustomerNotes.class, new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 10));
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
        addNoteAll();
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

    private void addNoteAll(){
        containerCards.addItemsAll(cliente.getNotas());
    }

}
