package bumh3r.view.form;

import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.EmpleadoCard;
import bumh3r.components.card.ContainerCards;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.controller.ControladorEmpleado;
import bumh3r.model.New.EmpleadoN;
import bumh3r.system.form.Form;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class FormEmployee extends Form {
    private ContainerCards<EmpleadoN> containerCards;
    private ButtonDefault buttonAddEmployee;
    private ControladorEmpleado controller;

    @Override
    public void formInit() {
        containerCards.installDependent1(controller.eventShowPreferences);
        getEventFormInit().run();
    }

    @Override
    public void installController() {
        this.controller = new ControladorEmpleado(this);
    }

    @Override
    public void formRefresh() {
        eventCleanUser();
        getEventFormRefresh().run();
    }

    public void installEventShowAddEmployee(Runnable event) {
        buttonAddEmployee.addActionListener((e) -> event.run());
    }

    public FormEmployee() {
        initComponents();
        init();
    }

    private void initComponents() {
        containerCards = new ContainerCards<>(EmpleadoCard.class, new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(400, -1), 10, 10));
        containerCards.setLongitud(6);
        buttonAddEmployee = new ButtonDefault("Agregar Empleado");
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,insets 0 n 0 n", "[fill]", "[grow 0][fill]"));
        add(createHeader("Empleados", "En el apartado de Empleados puedes gestionar tus empleados, las cuales son agregar, actualizar y visualizar.", 1));
        add(body());
        updateUI();
    }

    private JComponent body() {
        JPanel panel = new JPanel(new MigLayout("wrap,fillx,insets 0", "[fill]"));
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null");
        panel.add(buttonAddEmployee, "grow 0,al trail");
        panel.add(containerCards, "gapx 0 2,grow,push");
        panel.add(containerCards.getPanelPaginacion(), "grow 0,gapy 0 5,al center");
        return panel;
    }
    public Consumer<EmpleadoN> eventAddUsuario = (employee) ->
            SwingUtilities.invokeLater(() -> containerCards.addItemOne(employee));

    public Consumer<List<EmpleadoN>> eventAddUsuarioCard = (list) ->
            containerCards.addItemsAll(list);

    public void eventCleanUser() {
        containerCards.cleanCards();
    }
}
