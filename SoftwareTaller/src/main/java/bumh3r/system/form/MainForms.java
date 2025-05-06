package bumh3r.system.form;

import bumh3r.components.RefreshLine;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import net.miginfocom.swing.MigLayout;
import raven.modal.Drawer;

public class MainForms extends JPanel {

    private JPanel mainPanel;
    private RefreshLine refreshLine;

    private JButton buttonUndo;
    private JButton buttonRedo;
    private JButton buttonRefresh;

    private final String basePathIcon = "icon/svg/drawer/";

    public MainForms() {
        init();
    }

    private void init() {
        setLayout(new MigLayout("fillx,wrap,insets 0,gap 0", "[fill]", "[][][fill,grow]"));
        add(createHeader());
        add(createRefreshLine(), "height 3!");
        add(createMain());
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new MigLayout("insets 3", "[]push[]push", "[fill]"));

        JToolBar toolBar = new JToolBar();
        toolBar.putClientProperty(FlatClientProperties.STYLE,"background:null;");
        JButton buttonDrawer = new JButton(new FlatSVGIcon(basePathIcon + "menu.svg", 0.5f));
        buttonUndo = new JButton(new FlatSVGIcon(basePathIcon + "undo.svg", 0.5f));
        buttonRedo = new JButton(new FlatSVGIcon(basePathIcon + "redo.svg", 0.5f));
        buttonRefresh = new JButton(new FlatSVGIcon(basePathIcon + "refresh.svg", 0.5f));
        buttonDrawer.addActionListener(e -> {
            if (Drawer.isOpen()) {
                Drawer.showDrawer();
            } else {
                Drawer.toggleMenuOpenMode();
//                    Drawer.closeDrawer();
            }
        });
        buttonUndo.addActionListener(e -> FormsManager.undo());
        buttonRedo.addActionListener(e -> FormsManager.redo());
        buttonRefresh.addActionListener(e -> FormsManager.refresh());

        toolBar.add(buttonDrawer);
        toolBar.add(buttonUndo);
        toolBar.add(buttonRedo);
        toolBar.add(buttonRefresh);
        panel.add(toolBar);
        return panel;
    }

    private JPanel createRefreshLine() {
        refreshLine = new RefreshLine();
        return refreshLine;
    }

    private Component createMain() {
        mainPanel = new JPanel(new BorderLayout());
        return mainPanel;
    }

    public void setForm(Form form) {
        mainPanel.removeAll();
        mainPanel.add(form);
        mainPanel.repaint();
        mainPanel.revalidate();

        buttonUndo.setEnabled(FormsManager.FORMS.isUndoAble());
        buttonRedo.setEnabled(FormsManager.FORMS.isRedoAble());
    }

    public void refresh() {
        //refreshLine.refresh();
    }
}
