package bumh3r.view.form;

import bumh3r.components.button.ButtonAccentBase;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardNote;
import bumh3r.components.card.ContainerCards;
import bumh3r.components.input.InputText;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.controller.ControladorNota;
import bumh3r.modal.Config;
import bumh3r.model.*;
import bumh3r.model.New.NotaN;
import bumh3r.system.form.Form;
import bumh3r.thread.PoolThreads;
import bumh3r.view.modal.PanelModalInfoDevice;
import bumh3r.view.modal.PanelModalInfoNote;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.datetime.DatePicker;
import raven.modal.ModalDialog;

public class FormNotes extends Form {
    private InputText search;
    private JFormattedTextField inputDate;
    private DatePicker datePicker;
    private ButtonDefault button_createNote;
    private ButtonAccentBase buttonSearch;
    private ContainerCards<NotaN> containerCards;
    private ControladorNota controladorNota;

    @Override
    public void installController() {
        this.controladorNota = new ControladorNota(this);
    }

    @Override
    public void formInit() {
        containerCards.installDependent1(controladorNota.eventViewDetailsNote);
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
    }

    public FormNotes() {
        initComponents();
        init();
    }

    private void initComponents() {
        containerCards = new ContainerCards<>(CardNote.class, new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 10));
        containerCards.setLongitud(6);

        search = new InputText("Buscar Folio ...", 6);
        datePicker = new DatePicker();
        inputDate = new JFormattedTextField();
        datePicker.setEditor(inputDate);
        datePicker.setSelectedDate(LocalDate.now());
        datePicker.setCloseAfterSelected(true);
        datePicker.setUsePanelOption(true);
        button_createNote = new ButtonDefault("Crear Nota");
        buttonSearch = new ButtonAccentBase("Buscar");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0 n 0 n", "[fill]", "[grow 0]10[fill]"));
        add(createHeader("Notas", "En este apartado puedes visualizar las notas por fecha y visualizar el contenido de cada una de ellas", 1));
        add(createBody(), "grow,push,gapx 20 20");
    }

     private JComponent createBody() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap 2,insets 5", "[]push[]", "[]10[]"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null;");
        panel.add(search, "w 180!,al lead,split 2");
        panel.add(buttonSearch, "growx 0");
        panel.add(button_createNote, "w 100!,split 2");
        panel.add(inputDate, "w 160!");
        panel.add(containerCards, "span,grow,push");
        return panel;
    }

    public Consumer<NotaN> addOneCard = (nota) -> containerCards.addItemOne(nota);

    public Consumer<List<NotaN>> addAllCards = (list) -> containerCards.addItemsAll(list);

    public void cleanCards() {
        containerCards.cleanCards();
    }

}