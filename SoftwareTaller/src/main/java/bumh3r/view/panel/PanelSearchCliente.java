package bumh3r.view.panel;

import bumh3r.components.Table;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelForDescription;
import bumh3r.model.Cliente;
import bumh3r.system.panel.Panel;
import net.miginfocom.swing.MigLayout;
import static bumh3r.archive.PathResources.Icon.modal;

public class PanelSearchCliente extends Panel {
    private LabelForDescription description;
    private InputText input;
    private ButtonDefault buttonSearch;
    private Table<Cliente> table;

    public PanelSearchCliente() {
        initComponents();
        init();
    }

    private void initComponents() {
        input = new InputText("Introduce el numero teléfono").setIcon(modal + "ic_search.svg");
        buttonSearch = new ButtonDefault("Buscar");
        table = new Table<>(new String[]{"ID", "Nombre","Teléfono 1", "Teléfono 2", "Dirección", "Fecha de registro"});
        description = new LabelForDescription("En este panel puedes buscar al cliente por su número de teléfono y seleccionarlo.");
    }

    private void init() {
        setLayout(new MigLayout("wrap,ins 0 n n n, w 600:680","[grow]","[grow 0][]"));
        add(description, "grow,gapx 10 10,gapy 1 15");
        add(input, "w 200!,grow 0,al lead,split 2");
        add(buttonSearch, "grow 0");
        add(table, "h 300!,growx,gapy 5 0");
    }
}
