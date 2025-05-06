package bumh3r.view.form;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardUsuario;
import bumh3r.components.card.ContainerCards;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.modal.Config;
import bumh3r.modal.CustomModal;
import bumh3r.model.Usuario;
import bumh3r.model.other.DateFull;
import bumh3r.system.form.Form;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.view.modal.ModalToas;
import bumh3r.view.panel.PanelChangePasswordUsuario;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import static bumh3r.archive.PathResources.Icon.modal;

public class FormControlUsuario extends Form {
    public static String ID = FormControlUsuario.class.getName();
    private ContainerCards<Usuario> containerCards;
    private ButtonDefault buttonAddUsuario;
    private ControlUsuarioViewController controller;

    @Override
    public void formInit() {
        containerCards.installDependent1(controller.eventDelete);
        containerCards.installDependent2(controller.eventChangePassword);
        getEventFormInit().run();
    }

    @Override
    public void installController() {
        controller = new ControlUsuarioViewController(this);
    }

    public FormControlUsuario() {
        initComponents();
        initListeners();
        init();
    }

    private void initListeners() {
        buttonAddUsuario.addActionListener((e) -> PanelsInstances.getInstance().showPanelAddUsuario());
    }

    private void initComponents() {
        containerCards = new ContainerCards<>(CardUsuario.class, new ResponsiveLayout(ResponsiveLayout.JustifyContent.SPACE_AROUND, new Dimension(-1, -1), 10, 10));
        buttonAddUsuario = new ButtonDefault("Agregar Usuario");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,insets 0 n 0 n", "[fill]", "[grow 0][fill]"));
        add(createHeader("Control de Usuario", "En este apartado podrás gestionar a tus usuarios", 1));
        add(body());
        updateUI();
    }

    private JComponent body() {
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 0", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null");
        panel.add(buttonAddUsuario, "grow 0,al trail");
        panel.add(containerCards, "gapx 0 2,grow,push");
        panel.add(containerCards.getPanelPaginacion(), "grow 0,al center");
        return panel;
    }

    private Consumer<Usuario> eventChangePassword = e -> {
        ModalDialog.showModal(SwingUtilities.windowForComponent(this),
                CustomModal.builder()
                        .component(PanelsInstances.getInstance().getPanelModal(PanelChangePasswordUsuario.class))
                        .title("Cambiar Contraseña")
                        .icon(modal + "ic_update.svg")
                        .buttonClose(true)
                        .ID(ID)
                        .build(),
                Config.getModelShowModalFromNote(),
                ID);
    };

    private Consumer<Usuario> eventDelete = e -> {
        ModalDialog.showModal(SwingUtilities.windowForComponent(this),
                new ModalToas(ModalToas.Type.WARNING, "Eliminar Usuario", "¿Estás seguro de eliminar a " + e.getUsername() + "?",
                        (modal, action) -> {
                        }),
                Config.getModelShowModalFromNote(),
                ID);
    };

    public Consumer<Usuario> eventAddUsuario = (employee) ->
            SwingUtilities.invokeLater(() -> containerCards.addItemOne(employee));

    public Consumer<List<Usuario>> eventAddUsuarioCard = (list) ->
            containerCards.addItemsAll(list);

}

