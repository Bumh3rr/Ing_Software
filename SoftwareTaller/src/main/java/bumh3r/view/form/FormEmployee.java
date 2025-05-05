package bumh3r.view.form;

import bumh3r.archive.PathResources;
import bumh3r.components.ContainerCards;
import bumh3r.components.button.ButtonDefault;
import bumh3r.components.card.CardTechnician;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.modal.Config;
import bumh3r.modal.CustomModal;
import bumh3r.model.Taller;
import bumh3r.model.Empleado;
import bumh3r.model.TypeEmpleado;
import bumh3r.system.form.Form;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.view.modal.preferences.ModalPreferencesEmpleado;
import bumh3r.view.panel.PanelAddEmployee;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;

public class FormEmployee extends Form {
    private JPanel panelEmployee;
    private ContainerCards containerCards;
    private ButtonDefault buttonAddEmployee;

    @Override
    public void formInit() {
        List<Empleado> empleados = new ArrayList<>();
        Empleado empleado = new Empleado(
                1L,
                "Juan",
                "Perez",
                "RFC123456",
                "747-232-3232",
                "M",
                "juan.perez@example.com",
                "Jalisco",
                "Acatic",
                "Colony",
                "Street",
                "12345",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                "Activo",
                new TypeEmpleado(1,"Tecnico")
        );
        Empleado empleado2 = new Empleado(
                2L,
                "Juan",
                "Perez",
                "RFC123456",
                "747-232-3232",
                "M",
                "juan.perez@example.com",
                "Jalisco",
                "Acatic",
                "Colony",
                "Street",
                "12345",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                "Activo",
                new TypeEmpleado(1,"Tecnico")
        );
        Empleado empleado3 = new Empleado(
                3L,
                "Juan",
                "Perez",
                "RFC123456",
                "747-232-3232",
                "M",
                "juan.perez@example.com",
                "Jalisco",
                "Acatic",
                "Colony",
                "Street",
                "12345",
                LocalDate.now(),
                LocalDate.now().plusDays(30),
                "Activo",
                new TypeEmpleado(1,"Tecnico")
        );
        empleados.add(empleado);
        empleados.add(empleado2);
        empleados.add(empleado3);
        refreshPanelEmployee(empleados);
    }

    public FormEmployee() {
        initComponents();
        init();
        buttonAddEmployee.addActionListener((x) -> {
            ModalDialog.showModal(SwingUtilities.windowForComponent(this),
                    CustomModal.builder()
                            .component(PanelsInstances.getInstance().getPanelModal(PanelAddEmployee.class))
                            .title("Agregar Empleados")
                            .icon(PathResources.Icon.modal + "ic_add-user.svg")
                            .buttonClose(true)
                            .ID(PanelAddEmployee.ID)
                            .build(),
                    Config.getModelShowModalFromNote(),
                    PanelAddEmployee.ID
            );
        });
    }

    private void initComponents() {
        panelEmployee = new JPanel(new ResponsiveLayout(ResponsiveLayout.JustifyContent.FIT_CONTENT, new Dimension(-1, -1), 10, 10));
        panelEmployee.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:darken(@background,3%);"
                + "[dark]background:lighten(@background,3%)");
        panelEmployee.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:10,10,10,10;");
        buttonAddEmployee = new ButtonDefault("Agregar Empleado");
        containerCards = new ContainerCards(panelEmployee);
    }

    private void init() {
        setLayout(new MigLayout("wrap,fill,insets 0 n 0 n", "[fill]", "[grow 0][fill]"));
        add(createHeader("Empleados", "En el apartado de Empleados puedes gestionar tus empleados, las cuales son agregar, actualizar y visualizar.", 1));
        add(body());
        updateUI();
    }

    private JComponent body() {
        JPanel panel = new JPanel(new MigLayout("fillx,wrap", "[fill]", "[][fill]"));
        panel.add(buttonAddEmployee, "grow 0,al trail");
        panel.add(containerCards, "gapx 0 2");
        return panel;
    }

    public void refreshPanelEmployee(List<Empleado> list) {
        SwingUtilities.invokeLater(() -> {
            panelEmployee.removeAll();
            list.forEach((tecnico) -> {
                panelEmployee.add(new CardTechnician(tecnico, eventViewPreferences));
            });
            panelEmployee.updateUI();
            EventQueue.invokeLater(() -> containerCards.getVerticalScrollBar().setValue(0));
            updateUI();
        });
    }

    private Consumer<Empleado> eventViewPreferences = e -> {
        ModalDialog.showModal(SwingUtilities.windowForComponent(this),
                new ModalPreferencesEmpleado(e, ModalPreferencesEmpleado.ID),
                Config.getModelShowModalFromNote(),
                ModalPreferencesEmpleado.ID
        );
    };
}
