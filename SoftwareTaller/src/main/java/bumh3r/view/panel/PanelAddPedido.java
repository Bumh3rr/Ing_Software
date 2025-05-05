package bumh3r.view.panel;

import bumh3r.components.Table;
import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.input.InputText;
import bumh3r.components.label.LabelForDescription;
import bumh3r.modal.CustomModal;
import bumh3r.system.panel.Panel;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.utils.LimitTextDocument;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import static bumh3r.archive.PathResources.Icon.modal;

public class PanelAddPedido extends Panel {
    public static final String ID = PanelAddPedido.class.getName();
    private InputText nombre;
    private LabelForDescription description;
    private JTextArea descripcionText;
    private FlatComboBox refaccion, proveedor;
    private ButtonDefault buttonAdd;
    private ButtonAccentBase buttonAddDetalle;
    private Table table;

    public PanelAddPedido() {
        initComponents();
        init();
    }

    private void initComponents() {
        table = new Table<>(new String[]{"Unidades", "Refacción"});
        table.setNameAccion("Eliminar");

        buttonAddDetalle = new ButtonAccentBase("Agregar Detalle");
        nombre = new InputText("Ingrese Nombre de la refacción",100);

        refaccion = new FlatComboBox();
        proveedor = new FlatComboBox();
        proveedor.setModel(new DefaultComboBoxModel(new String[]{"Seleccione el proveedor"}));
        refaccion.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione la refacción a pedir"}));

        buttonAdd = new ButtonDefault("Agregar pedido");
        description = new LabelForDescription("En este apartado podrás registrar los productos a solicitar al pedido, asegurate de ingresar los datos correctos.");
        descripcionText = new JTextArea();
        descripcionText.setDocument(new LimitTextDocument(250));
        descripcionText.setLineWrap(true);
        descripcionText.setWrapStyleWord(true);
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 5 45 20 45, w 450:550", "fill,grow"));
        add(description);
        add(createdSubTitle("Proveedor",15), "grow 0,gapy 5 0,al center");
        add(createdGramaticalP("Proveedor"));
        add(proveedor);
        add(createdGramaticalP("Observaciones"));
        add(createInputObservaciones());

        add(createdSubTitle("Detalles del Pedido",15),"grow 0,gapy 5 0,al center");
        add(createdGramaticalP("Unidades"));
        add(nombre);
        add(createdGramaticalP("Refacción"));
        add(refaccion);
        add(buttonAddDetalle, "grow 0,gapy 3,al trail");

        add(createdSubTitle("Detalles",15), "grow 0,gapy 5 0,al center");
        add(table, "grow,push,gapy 5 0,h 130!");
        add(buttonAdd, "grow 0,gapy 5,al trail");
    }


    private JComponent createInputObservaciones() {
        JPanel panel = new JPanel(new MigLayout("fill,insets 1"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "arc:10;"
                + "[light]background:darken(@background,4%);"
                + "[dark]background:lighten(@background,4%);");
        descripcionText.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,4%);"
                + "[dark]background:lighten(@background,4%);");
        panel.add(descripcionText, "grow,push");
        return panel;
    }

}