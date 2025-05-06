package bumh3r.view.modal;

import bumh3r.archive.PathResources;
import bumh3r.components.MyScrollPane;
import bumh3r.components.button.ButtonInfoIcon;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.system.preferences.Preferences;
import bumh3r.system.preferences.PreferencesInstance;
import bumh3r.thread.PoolThreads;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.*;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.Modal;
import raven.modal.component.ModalContainer;

public class ModalPreferences<T> extends Modal {
    private JPanel panelMain;
    private LabelPublicaSans title, titleMain;
    private JButton closeButton, infoIcon;
    private T identifier;
    private String ID;
    private int indexSelection = -1;
    private LinkedHashMap<Class<? extends Preferences>, ModalUtils> recurses;

    public ModalPreferences(LinkedHashMap<Class<? extends Preferences>, ModalUtils> recurses, String ID) {
        this.recurses = recurses;
        this.ID = ID;
        initComponents();
        init();
    }

    public ModalPreferences initial(T empleado, Class<? extends Preferences> classPreferences) {
        this.identifier = empleado;
        setSelectionButton(classPreferences);
        return this;
    }

    public ModalPreferences installEventClose(Runnable eventClose) {
        closeButton.addActionListener((x) -> eventClose.run());
        return this;
    }

    public ModalPreferences installEventShowInfo(Runnable eventShowInfo) {
        infoIcon.addActionListener((x) -> eventShowInfo.run());
        return this;
    }

    private void initComponents() {
        title = new LabelPublicaSans("Preferences").size(18f).type(FontPublicaSans.FontType.BOLD_BLACK);
        titleMain = new LabelPublicaSans("").size(17f).type(FontPublicaSans.FontType.BOLD_BLACK_ITALIC);
        infoIcon = new ButtonInfoIcon();
        panelMain = new JPanel(new MigLayout("wrap,fillx,insets 0,h 300:n:n", "[fill]", "[fill]"));
        panelMain.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        closeButton = new JButton();
        closeButton.addActionListener((e) -> {
            PreferencesInstance.getInstance().cleanPreferences(ID);
            ModalDialog.closeModal(ID);
            PoolThreads.getInstance().close();
        });
        closeButton.setIcon(new FlatSVGIcon(PathResources.Icon.modal + "ic_close.svg", 0.65f));
        closeButton.putClientProperty(FlatClientProperties.STYLE, "" +
                "margin:5,5,5,5;" +
                getStylesNull() +
                "background:darken(@background,1%);"
        );
        recurses.forEach((key, value) ->
                value.buttonPreferences.addActionListener(e -> setSelectionButton(key)));
    }

    private static String getStylesNull() {
        return "background: null;" +
                "arc:16;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "[light]selectedBackground:darken(@background,5%);" +
                "[dark]selectedBackground:lighten(@background,5%);" +
                "innerFocusWidth:0;";
    }

    private void init() {
        setLayout(new MigLayout("wrap 2,fillx,insets 10, w 500:750", "[][grow]", ""));
        add(createdPanelOne(), "ay top");
        add(createdPanelMain(), "grow");
    }

    private JComponent createdPanelMain() {
        JButton component = new JButton();
        component.setLayout(new MigLayout("wrap,fillx,insets 10", "[fill]"));
        component.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:36;" +
                "[light]background:lighten($Panel.background,4%);" +
                "[dark]background:@background;" +
                "hoverBackground:null;" +
                "pressedBackground:null;" +
                "pressedSelectedBorderColor:null;" +
                "pressedBorderColor:null;" +
                "hoverBorderColor:null;" +
                "hoverSelectedBorderColor:null;" +
                "focusedSelectedBorderColor:null;" +
                "focusedBorderColor:null;" +
                "selectedBackground:null;" +
                "innerFocusWidth:0;" +
                "innerFocusWidth:0;" +
                "borderWidth:1.8;" +
                "[light]borderColor:#e0e0e0;"
        );
        component.add(titleMain, "grow 0,al lead,split 2");
        component.add(closeButton, "grow 0,al trail");
        component.add(panelMain, "grow,push");
        return component;
    }

    private JComponent createdPanelOne() {
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 10", "[fill]"));
        panel.add(title, "split 2,gapy 0 10,grow 0");
        panel.add(infoIcon, "grow 0");
        panel.add(createdButtons(), "growx");
        return panel;
    }

    private JComponent createdButtons() {
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 3", "[fill]"));
        recurses.values().forEach((utils) -> panel.add(utils.buttonPreferences, "growx"));
        return panel;
    }

    public void showPanelMain(Class<? extends Preferences> classForm) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            ActionListener[] events = this.recurses.get(classForm).getEvents();
            Preferences panel = PreferencesInstance.getInstance().getPreferencesPanel(classForm, identifier, ID,events);
            titleMain.setText(panel.title());
            panelMain.removeAll();
            panelMain.add(new MyScrollPane(panel));
            panelMain.revalidate();
            panelMain.repaint();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    private void updateLayout() {
        Container container = SwingUtilities.getAncestorOfClass(ModalContainer.class, ModalPreferences.this);
        if (container != null) {
            container.revalidate();
        }
    }

    private void setSelectionButton(Class<? extends Preferences> classPreferences) {
        if (!recurses.containsKey(classPreferences)) {
            throw new IllegalArgumentException("No se encontró la clase de preferencias");
        }

        int newIndex = recurses.keySet().stream().toList().indexOf(classPreferences);
        if (indexSelection == newIndex) return;

        if (indexSelection != -1) {
            getValueByIndex(recurses, indexSelection).buttonPreferences
                    .setSelected(false);
        }
        indexSelection = newIndex;
        recurses.get(classPreferences).buttonPreferences.setSelected(true);
        showPanelMain(classPreferences);
        updateLayout();
    }

    private  <K, V> V getValueByIndex(Map<K, V> map, int index) {
        if (index < 0 || index >= map.size()) {
            throw new IndexOutOfBoundsException("Índice fuera de los límites del mapa");
        }
        return map.values().toArray((V[]) new Object[0])[index];
    }


    public static class ButtonPreferences extends JButton {
        public ButtonPreferences(String text) {
            this(text, null);
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "margin:5,20,5,20;" +
                    getStylesNull()
            );
        }

        public ButtonPreferences(String text, String color) {
            this(text, color, null);
        }

        public ButtonPreferences(String text, String color, String icon) {
            super(text);
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "foreground:" + color + ";" +
                    "margin:5,20,5,20;" +
                    "font:bold 0;" +
                    getStylesNull()
            );
            if (icon == null) return;
            setIcon(new FlatSVGIcon(icon, 0.65f)
                    .setColorFilter(new FlatSVGIcon.ColorFilter((x) -> UIManager.getColor(color))));
        }


    }

    public static class ModalUtils{
        private ButtonPreferences buttonPreferences;
        @Getter
        private ActionListener[] events;

        public ModalUtils(ButtonPreferences buttonPreferences, ActionListener... events) {
            this.buttonPreferences = buttonPreferences;
            this.events = events;
        }
    }

}
