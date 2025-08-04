package bumh3r.system.form;

import bumh3r.components.custom.RefreshLine;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.util.SystemInfo;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.BorderFactory;
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
    private JPanel panelControl;

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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!SystemInfo.isMacOS) return;
        if (Drawer.isOpen()){
            panelControl.setBorder(BorderFactory.createEmptyBorder(0,60,0,0));
        }else{
            panelControl.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
        }
    }

    private JPanel createHeader() {
        String insets = (SystemInfo.isWindows) ? "insets 0 3 3 3" : "insets 3";
        panelControl = new JPanel(new MigLayout(insets, "[]push[]push", "[fill]"));

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
        //toolBar.add(buttonUndo);
        //toolBar.add(buttonRedo);
        toolBar.add(buttonRefresh);
        panelControl.add(toolBar);
        return panelControl;
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
