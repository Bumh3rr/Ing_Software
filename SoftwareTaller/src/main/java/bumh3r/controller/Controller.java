package bumh3r.controller;

import bumh3r.modal.Config;
import bumh3r.modal.CustomModal;
import bumh3r.system.form.FormsManager;
import bumh3r.view.modal.ModalToas;
import java.util.HashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import raven.modal.ModalDialog;
import raven.modal.component.Modal;
import static bumh3r.archive.PathResources.Icon.modal;

public class Controller {
    public final String ID = Controller.class.getName();
    private static Controller instance;
    private HashMap<Class<?>, Object> controllers;

    private Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
            instance.controllers = new HashMap<>();
        }
        return instance;
    }

    public <T> T getInstance(Class<T> clazz) {
        Object controller = getInstance().controllers.get(clazz);
        if (controller != null) {
            return clazz.cast(controller);
        }

        try {
            T newInstance = clazz.getDeclaredConstructor().newInstance();
            getInstance().controllers.put(clazz, newInstance);
            return newInstance;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear instancia de " + clazz.getName(), e);
        }
    }

    public void showPanel(JComponent component, String title, String icon, String id, Runnable callback, boolean pushModal) {
        if (component == null) {
            throw new IllegalArgumentException("El componente no puede ser nulo");
        }
        Modal modalConfig;
        if (component instanceof Modal) {
            modalConfig = (Modal) component;
        } else {
            modalConfig = CustomModal.builder()
                    .component(component)
                    .title(title)
                    .buttonClose(!pushModal)
                    .ID((id == null) ? ID : id)
                    .rollback(callback)
                    .icon(modal + icon)
                    .build();
        }

        if (pushModal) {
            ModalDialog.pushModal(modalConfig, (id == null) ? ID : id);
        } else {
            ModalDialog.showModal(
                    FormsManager.getFrame(),
                    modalConfig,
                    Config.getModelShowModalFromNote(),
                    (id == null) ? ID : id
            );
        }
    }

    public Consumer<String> eventFail = (message) -> {
        if (!ModalDialog.isIdExist(ID)) return;
        showPanel(new ModalToas(ModalToas.Type.ERROR, "Se ha producido un error :(", message, (controller, action) -> {
            controller.consume();
            if (action == ModalToas.ACCEPT_OPTION || action == ModalToas.CLOSE_OPTION) {
                ModalDialog.popModel(ID);
            }
        }), null, null, null, () -> ModalDialog.popModel(ID), ModalDialog.isIdExist(ID));
    };


    public BiConsumer<String, String> eventSuccess = (title, description) -> {
        ModalDialog.pushModal(new ModalToas(ModalToas.Type.SUCCESS, title, description,
                (controller, accion) -> {
                    controller.close();
                }), ID);
    };

    public int getConfirmation(String message) {
        return JOptionPane.showConfirmDialog(SwingUtilities.windowForComponent(FormsManager.getFrame()),
                message, "Confirmación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }
}