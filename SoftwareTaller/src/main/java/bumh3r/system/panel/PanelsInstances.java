package bumh3r.system.panel;

import bumh3r.modal.Config;
import bumh3r.modal.CustomModal;
//import bumh3r.view.panel.*;
import bumh3r.view.panel.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import lombok.Getter;
import raven.modal.ModalDialog;

import static bumh3r.archive.PathResources.Icon.modal;

public class PanelsInstances {

    private static PanelsInstances instance;
    private Map<Class<? extends Panel>, Panel> formsMap;
    @Getter
    private static JFrame frame;

    private PanelsInstances() {
        formsMap = new HashMap<>();
    }

    public void install(JFrame application) {
        frame = application;
    }

    public static PanelsInstances getInstance() {
        if (instance == null) {
            instance = new PanelsInstances();
        }
        return instance;
    }

    public void showPanelCreateNote() {
        if (ModalDialog.isIdExist(PanelCreateNote.ID)) {
            return;
        }
        showModal(PanelCreateNote.class, "Nueva Nota", "ic_newNote.svg", PanelCreateNote.ID);
    }
    public void showPanelAddPedido() {
        showModal(PanelAddPedido.class, "Agregar nuevo pedido", "ic_inventario.svg", PanelAddPedido.ID);
    }

    private void showModal(Class<? extends Panel> panelClass, String title, String icon, String id) {
        ModalDialog.showModal(this.frame,
                CustomModal.builder()
                        .component(getPanelModal(panelClass))
                        .title(title)
                        .icon(modal + icon)
                        .buttonClose(true)
                        .ID(id)
                        .build(),
                Config.getModelShowModalFromNote(),
                id);
    }

    public Panel getPanelModal(Class<? extends Panel> cls) {
        if (getInstance().formsMap.containsKey(cls)) {
            Panel panel = getInstance().formsMap.get(cls);
            panel.panelCheckUI();
            formOpen(panel);
            return panel;
        }
        try {
            Panel panel = cls.getDeclaredConstructor().newInstance();
            getInstance().formsMap.put(cls, panel);
            panel.installController();
            formInit(panel);
            return panel;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void formInit(Panel form) {
        SwingUtilities.invokeLater(form::panelInit);
    }

    private void formOpen(Panel form) {
        SwingUtilities.invokeLater(form::panelOpen);
    }

}
