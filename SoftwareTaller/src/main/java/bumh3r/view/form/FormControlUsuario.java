package bumh3r.view.form;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardUsuario;
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
    private ButtonDefault buttonAddUsuario;
    private ResponsiveLayout responsiveLayout;
    private JPanel panelControlUsuario;
    private JScrollPane scrollPane;

    @Override
    public void formInit() {
        Usuario usuario = new Usuario(1L, "Usuario 1", "description", "Example", "Exmaple", LocalDateTime.now(), "Recepcionista", null);
        Usuario usuario1 = new Usuario(2L, "Usuario 2", "description", "Example", "Exmaple", LocalDateTime.now(), "Técnico", null);
        Usuario usuario2 = new Usuario(3L, "Usuario 3", "description", "Example", "Exmaple", LocalDateTime.now(), "Gerente", null);
        Usuario usuario3 = new Usuario(4L, "Usuario 4", "description", "Example", "Exmaple", LocalDateTime.now(), "Administrador", null);

        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(usuario);
        usuarios.add(usuario1);
        usuarios.add(usuario2);
        usuarios.add(usuario3);
        refreshPanelEmployee(usuarios);
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
        buttonAddUsuario = new ButtonDefault("Agregar Usuario");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,insets 0 n 0 n", "[fill]", "[grow 0][fill]"));
        add(createHeader("Control de Usuario", "En este apartado podrás gestionar a tus usuarios", 1));
        add(body());
        updateUI();
    }

    private JComponent body() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]", "[][fill]"));
        panel.add(buttonAddUsuario, "grow 0,al trail");
        panel.add(createEmployeeContainers(), "grow,push,gapy 5 0,gapx 0 2");
        return panel;
    }

    private JComponent createEmployeeContainers() {
        responsiveLayout = new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 10);
        panelControlUsuario = new JPanel(responsiveLayout);
        panelControlUsuario.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%)");
        panelControlUsuario.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:10,10,10,10;");
        scrollPane = new JScrollPane(panelControlUsuario);
        scrollPane.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%);");
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.getHorizontalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:$ScrollBar.thumbArc;"
                + "thumbInsets:0,0,0,0;"
                + "width:5;");
        scrollPane.getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "trackArc:$ScrollBar.thumbArc;"
                + "thumbInsets:0,0,0,0;"
                + "width:5;");
        return scrollPane;
    }

    public void refreshPanelEmployee(List<Usuario> list) {
        SwingUtilities.invokeLater(() -> {
            panelControlUsuario.removeAll();
            list.forEach((tecnico) -> {
                panelControlUsuario.add(new CardUsuario(tecnico, eventChangePassword, eventDelete));
            });
            panelControlUsuario.updateUI();
            EventQueue.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(0));
            updateUI();
        });
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
}

