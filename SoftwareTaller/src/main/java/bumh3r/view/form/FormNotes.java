package bumh3r.view.form;

import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardNote;
import bumh3r.components.card.ContainerCards;
import bumh3r.components.input.InputText;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.controller.ControladorNota;
import bumh3r.model.Nota;
import bumh3r.system.form.Form;
import bumh3r.utils.thread.PoolThreads;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import raven.datetime.PanelDateOptionLabel;

@Slf4j
public class FormNotes extends Form {
    @Getter
    private InputText search;
    private JFormattedTextField inputDate;
    @Getter
    private DatePicker datePicker;
    private ButtonDefault button_createNote;
    private ButtonAccentBase buttonSearch;
    private ContainerCards<Nota> containerCards;
    private ControladorNota controladorNota;

    @Override
    public void installController() {
        this.controladorNota = new ControladorNota(this);
    }

    @Override
    public void formInit() {
        containerCards.installDependent1(controladorNota.mostrarPantallaVisualizarNota());
        PoolThreads.getInstance().execute(getEventFormInit());
    }

    @Override
    public void formRefresh() {
        cleanCards();
        PoolThreads.getInstance().execute(getEventFormRefresh());
    }

    public void installEventCreateNote(Runnable event) {
        button_createNote.addActionListener(e -> event.run());
    }

    public void installEventSearchNote(Runnable event) {
        buttonSearch.addActionListener(e -> event.run());
        search.addActionListener(e -> event.run());
    }

    public void installEventFilterByDate(Runnable event) {
        datePicker.addDateSelectionListener((x) -> event.run());
    }

    public FormNotes() {
        initComponents();
        init();
    }

    private void initComponents() {
        containerCards = new ContainerCards<>(CardNote.class, new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(330, -1), 10, 10));
        containerCards.setLongitud(1000);
        search = new InputText("Buscar Folio ...", 10);
        inputDate = new JFormattedTextField();
        datePicker = new DatePicker();
        datePicker.setEditor(inputDate);
        datePicker.setSelectedDate(LocalDate.now());
        datePicker.setCloseAfterSelected(false);
        datePicker.setPanelDateOptionLabel(createDefaultPanelDateOptionLabel());
        datePicker.setUsePanelOption(true);
        button_createNote = new ButtonDefault("Crear Nota");
        buttonSearch = new ButtonAccentBase("Buscar");
    }

    private PanelDateOptionLabel createDefaultPanelDateOptionLabel() {
        PanelDateOptionLabel defaultPanelDateOptionLabel = new PanelDateOptionLabel();
        defaultPanelDateOptionLabel.add("Hoy", PanelDateOptionLabel.LabelCallback.TODAY);
        defaultPanelDateOptionLabel.add("Ayer", PanelDateOptionLabel.LabelCallback.YESTERDAY);
        defaultPanelDateOptionLabel.add("Últimos 7 días", PanelDateOptionLabel.LabelCallback.LAST_7_DAYS);
        defaultPanelDateOptionLabel.add("Últimos 30 días", PanelDateOptionLabel.LabelCallback.LAST_30_DAYS);
        defaultPanelDateOptionLabel.add("Este mes", PanelDateOptionLabel.LabelCallback.THIS_MONTH);
        defaultPanelDateOptionLabel.add("Mes pasado", PanelDateOptionLabel.LabelCallback.LAST_MONTH);
        defaultPanelDateOptionLabel.add("El año pasado", PanelDateOptionLabel.LabelCallback.LAST_YEAR);
        defaultPanelDateOptionLabel.add("Personalizar", PanelDateOptionLabel.LabelCallback.CUSTOM);
        return defaultPanelDateOptionLabel;
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n 0 n", "[fill]", "[grow 0]10[fill]"));
        add(createHeader("Notas", "En este apartado puedes visualizar las notas por fecha y visualizar el contenido de cada una de ellas", 1));
        add(createBody(), "grow,push,gapx 20 20");
    }

    private JComponent createBody() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap 2,insets 5", "[]push[]", "[]10[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        panel.add(search, "w 180!,al lead,split 2");
        panel.add(buttonSearch, "growx 0");
        panel.add(button_createNote, "w 100!,split 2");
        panel.add(inputDate, "w 160!");
        panel.add(containerCards, "span,grow,push");
        return panel;
    }

    public Consumer<Nota> addOneCard = (nota) -> containerCards.addItemOne(nota);

    public Consumer<List<Nota>> addAllCards = (list) -> containerCards.addItemsAll(list);

    public void cleanCards() {
        containerCards.cleanCards();
    }

}