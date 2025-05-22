package bumh3r.view.form;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardUsuario;
import bumh3r.components.card.ContainerCards;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.controller.ControladorUsuarios;
import bumh3r.model.Usuario;
import bumh3r.system.form.Form;
import bumh3r.thread.PoolThreads;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class FormControlUsuario extends Form {
    public static String ID = FormControlUsuario.class.getName();
    private ContainerCards<Usuario> containerCards;
    private ButtonDefault buttonAddUsuario;
    private ControladorUsuarios controller;

    public void installEventShowPanelAddUsuario(Runnable event) {
        buttonAddUsuario.addActionListener((e) -> event.run());
    }

    @Override
    public void formInit() {
        containerCards.installDependent1(controller.eventMostrarPanelEliminarUsuario);
        containerCards.installDependent2(controller.eventMostrarPanelCambiarContraseñaUsuario);
        PoolThreads.getInstance().execute(getEventFormInit());
    }

    @Override
    public void formRefresh() {
        getEventFormRefresh().run();
    }

    @Override
    public void installController() {
        controller = new ControladorUsuarios(this);
    }

    public FormControlUsuario() {
        initComponents();
        init();
    }

    private void initComponents() {
        containerCards = new ContainerCards<>(CardUsuario.class, new ResponsiveLayout(ResponsiveLayout.JustifyContent.SPACE_AROUND, new Dimension(-1, -1), 10, 10));
        containerCards.setLongitud(6);
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
        panel.putClientProperty(FlatClientProperties.STYLE, "" + "background:null");
        panel.add(buttonAddUsuario, "grow 0,al trail");
        panel.add(containerCards, "gapx 0 2,grow,push");
        panel.add(containerCards.getPanelPaginacion(), "grow 0,gapy 0 5,al center");
        return panel;
    }

    public Consumer<Usuario> eventAddUsuario = (employee) -> containerCards.addItemOne(employee);

    public Consumer<List<Usuario>> eventAddUsuarioCard = (list) -> containerCards.addItemsAll(list);

    public Consumer<Usuario> eventDeleteUsuario = (usuario) -> containerCards.delete(usuario);

    public void cleanCards() {
        containerCards.cleanCards();
    }
}

