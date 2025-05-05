package bumh3r.view.modal.preferences;

import bumh3r.archive.PathResources;
import bumh3r.components.MyScrollPane;
import bumh3r.components.button.ButtonInfoIcon;
import bumh3r.components.label.LabelPublicaSans;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.Empleado;
import bumh3r.system.preferences.Preferences;
import bumh3r.system.preferences.PreferencesInstance;
import bumh3r.thread.PoolThreads;
import bumh3r.view.panel.preferences.empleado.PreferencesInfoEmpleado;
import bumh3r.view.panel.preferences.empleado.PreferencesUpdateInfoEmployee;
import bumh3r.view.panel.preferences.empleado.PreferencesUpdateStatusEmployee;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.EventQueue;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.component.Modal;

public class ModalPreferencesEmpleado extends Modal {
    public static final String ID = ModalPreferencesEmpleado.class.getName();
    private JPanel panelMain;
    private LabelPublicaSans title, titleMain;
    private ButtonInfoIcon infoIcon;
    private JButton generalButton, updateInfoButton, updateStatusButton, closeButton;
    private Empleado empleado;
    private String idModal;
    private int indexSelection = -1;
    private Map<Class<? extends Preferences>, JButton> mapButtons;

    public ModalPreferencesEmpleado(Empleado empleado, String idModal) {
        initComponents();
        initListeners();
        init();
        this.empleado = empleado;
        this.idModal = idModal;
        setSelectionButton(PreferencesInfoEmpleado.class);
    }

    private void initComponents() {
        title = new LabelPublicaSans("Preferences").size(18f).type(FontPublicaSans.FontType.BOLD_BLACK);
        titleMain = new LabelPublicaSans("").size(17f).type(FontPublicaSans.FontType.BOLD_BLACK_ITALIC);

        infoIcon = new ButtonInfoIcon();
        generalButton = getInstanceButton("Información");
        updateInfoButton = getInstanceButton("Actualizar Información");
        panelMain = new JPanel(new MigLayout("wrap,fillx,insets 0", "[fill]", "[fill]"));
        panelMain.putClientProperty(FlatClientProperties.STYLE, "background:null;");

        closeButton = new JButton();
        closeButton.setIcon(new FlatSVGIcon(PathResources.Icon.modal + "ic_close.svg", 0.65f));
        closeButton.putClientProperty(FlatClientProperties.STYLE, "" +
                "margin:5,5,5,5;" +
                getStylesNull() +
                "background:darken(@background,1%);"
        );

        updateStatusButton = new JButton("Actualizar Estado");
        updateStatusButton.putClientProperty(FlatClientProperties.STYLE, "" +
                "foreground: #ffaa00;" +
                "margin:5,20,5,20;" +
                "font:bold 0;" +
                getStylesNull()
        );
        updateStatusButton.setIcon(new FlatSVGIcon(PathResources.Icon.home + "ic_status.svg", 0.65f)
                .setColorFilter(new FlatSVGIcon.ColorFilter((x) -> UIManager.getColor("#ffaa00"))));

        mapButtons = Map.of(
                PreferencesInfoEmpleado.class, generalButton,
                PreferencesUpdateInfoEmployee.class, updateInfoButton,
                PreferencesUpdateStatusEmployee.class, updateStatusButton
        );
    }

    private JButton getInstanceButton(String text) {
        JButton button = new JButton(text);
        button.putClientProperty(FlatClientProperties.STYLE, "" +
                "margin:5,20,5,20;" +
                getStylesNull()
        );
        return button;
    }

    private String getStylesNull() {
        return "background: null;" +
                "arc:16;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "[light]selectedBackground:darken(@background,5%);" +
                "[dark]selectedBackground:lighten(@background,5%);" +
                "innerFocusWidth:0;";
    }

    private void initListeners() {
        mapButtons.forEach((key, value) ->
                value.addActionListener(e -> setSelectionButton(key)));
        closeButton.addActionListener(e -> cleans());
    }

    private void cleans() {
        PreferencesInstance.getInstance().cleanPreferences(idModal);
        ModalDialog.closeModal(idModal);
        PoolThreads.getInstance().close();
    }

    private void init() {
        setLayout(new MigLayout("wrap 2,fillx,insets 10, w 500:750", "[][grow]"));
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

        panel.add(generalButton, "growx");
        panel.add(updateInfoButton, "growx");
        panel.add(updateStatusButton, "growx");
        return panel;
    }

    private void showPanelMain(Class<? extends Preferences> classForm) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            Preferences panel = PreferencesInstance.getInstance().getPreferencesPanel(classForm, empleado, idModal,null);
            titleMain.setText(panel.title());
            panelMain.removeAll();
            panelMain.add(new MyScrollPane(panel));
            panelMain.revalidate();
            panelMain.repaint();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    private void setSelectionButton(Class<? extends Preferences> classPreferences) {
        if (!mapButtons.containsKey(classPreferences)) {
            throw new IllegalArgumentException("No se encontró la clase de preferencias");
        }

        int newIndex = mapButtons.keySet().stream().toList().indexOf(classPreferences);
        if (indexSelection == newIndex) return;

        if (indexSelection != -1) {
            getValueByIndex(mapButtons, indexSelection).setSelected(false);
        }

        indexSelection = newIndex;
        mapButtons.get(classPreferences).setSelected(true);
        showPanelMain(classPreferences);
    }

    public <K, V> V getValueByIndex(Map<K, V> map, int index) {
        if (index < 0 || index >= map.size()) {
            throw new IndexOutOfBoundsException("Índice fuera de los límites del mapa");
        }
        return map.values().toArray((V[]) new Object[0])[index];
    }

}
