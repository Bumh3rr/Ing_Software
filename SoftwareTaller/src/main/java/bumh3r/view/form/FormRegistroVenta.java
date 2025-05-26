package bumh3r.view.form;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardNoteSale;
import bumh3r.components.card.ContainerCards;
import bumh3r.components.input.InputText;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.controller.ControladorVentas;
import bumh3r.components.modal.Config;
import bumh3r.components.modal.CustomModal;
import bumh3r.model.*;
import bumh3r.system.form.Form;
import bumh3r.utils.thread.PoolThreads;
import bumh3r.view.panel.PanelRegisterSale;
import com.fasterxml.jackson.databind.introspect.DefaultAccessorNamingStrategy;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import raven.modal.ModalDialog;

import static bumh3r.utils.PathResources.Icon.modal;

public class FormRegistroVenta extends Form {
    public static final String ID = FormRegistroVenta.class.getName();
    private ContainerCards<Nota> containerCards;
    @Getter
    private InputText input_search;
    private ButtonDefault search;
    private JFormattedTextField inputDate;
    @Getter
    private DatePicker datePicker;
    private ControladorVentas controladorVentas;

    @Override
    public void installController() {
        this.controladorVentas = new ControladorVentas(this);
    }

    @Override
    public void formInit() {
        containerCards.installDependent1(this.controladorVentas.mostrarPantallaRegistroDeVenta());
        PoolThreads.getInstance().execute(getEventFormInit());
    }

    @Override
    public void formRefresh() {
        PoolThreads.getInstance().execute(getEventFormRefresh());
    }

    public void installEventSearch(Runnable event) {
        search.addActionListener((x) -> event.run());
        input_search.addActionListener((x) -> event.run());
    }

    public void installEventFilterByDate(Runnable event) {
        datePicker.addDateSelectionListener((x) -> event.run());
    }

    public FormRegistroVenta() {
        initComponents();
        init();
    }

    private void initComponents() {
        containerCards = new ContainerCards<>(CardNoteSale.class, new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 10));
        search = new ButtonDefault("Buscar Nota");
        input_search = new InputText("Ingrese el folio de la nota", 100);
        search = new ButtonDefault("Buscar");
        datePicker = new DatePicker();
        inputDate = new JFormattedTextField();
        datePicker.setEditor(inputDate);
        datePicker.setSelectedDate(LocalDate.now());
        datePicker.setCloseAfterSelected(true);
        datePicker.setUsePanelOption(true);
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n 0 n", "[fill]"));
        add(createHeader("Registro de Venta por Nota de Reparación", "En este apartado podrás buscar una nota de reparación existente y seleccionarla para continuar con el registro de venta.\n" +
                "Una vez seleccionada, podrás elegir las reparaciones realizadas, las refacciones utilizadas y registrar el pago del cliente.", 1));
        add(createBody());
    }

    private JComponent createBody() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap 2", "[grow][grow,trail]", "[]10[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        panel.add(input_search, "w 200!,al lead,split 2");
        panel.add(search, "growx 0");
        panel.add(inputDate, "w 160!");
        panel.add(containerCards, "span,grow,push");
        return panel;
    }

    public void addAllCards(List<Nota> list) {
        containerCards.addItemsAll(list);
    }


}
