package bumh3r.controller;

import bumh3r.archive.PathResources;
import bumh3r.dao.EmpleadoDAO;
import bumh3r.model.Empleado;
import bumh3r.model.New.EmpleadoN;
import bumh3r.notifications.Notify;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.system.preferences.Preferences;
import bumh3r.system.preferences.PreferencesInstance;
import bumh3r.view.form.FormEmployee;
import bumh3r.view.modal.ModalPreferences;
import bumh3r.view.panel.PanelAddEmployee;
import bumh3r.view.panel.preferences.empleado.PreferencesGeneralEmpleado;
import bumh3r.view.panel.preferences.empleado.PreferencesUpdateInfoEmployee;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.Getter;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.toast.ToastPromise;

public class EmpleadoViewController extends Controller {
    public static String KEY = EmpleadoViewController.class.getName();
    private final FormEmployee view;
    private final EmpleadoDAO empleadoDAO;
    private PanelAddEmployee panelAddEmployee;

    public EmpleadoViewController(FormEmployee view) {
        this.view = view;
        this.empleadoDAO = getInstance(EmpleadoDAO.class);
        this.view.setEventFormRefresh(this::showEmployeeAll);
        this.view.installEventShowAddEmployee(this::showPanelAddEmployee);
    }

    private void showEmployeeAll() {
        Toast.closeAll();
        if (Toast.checkPromiseId(KEY)) return;
        Notify.showPromise("Obteniendo los empleados ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Guardando ...");
                            List<EmpleadoN> list = empleadoDAO.getList();
                            view.eventAddUsuarioCard.accept(list);
                            callback.done(Toast.Type.SUCCESS, "Los empleados fueron obtenidos correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error al obtener los empleados\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    }

    private void showPanelAddEmployee() {
        if (panelAddEmployee == null) {
            panelAddEmployee = (PanelAddEmployee) PanelsInstances.getInstance().getPanelModal(PanelAddEmployee.class);
            panelAddEmployee.installEvent(() -> {
                EmpleadoN value = panelAddEmployee.getValue();
                if (value == null || Toast.checkPromiseId(KEY)) return;
                Notify.showPromise("Guardando ...",
                        new ToastPromise(KEY) {
                            @Override
                            public void execute(PromiseCallback callback) {
                                try {
                                    callback.update("Guardando ...");
                                    EmpleadoN empleado = empleadoDAO.guardar(value);
                                    view.eventAddUsuario.accept(empleado);
                                    if (ModalDialog.isIdExist(ID)) ModalDialog.closeModal(ID);
                                    callback.done(Toast.Type.SUCCESS, "El empleado fue agregado correctamente");
                                } catch (Exception ex) {
                                    callback.done(Toast.Type.ERROR, "Error agregar el empleado\n" +
                                            "Causa: " + ex.getLocalizedMessage());
                                }
                            }
                        });
            });
        }
        showPanel(panelAddEmployee, "Agregar Empleado", "ic_add-user.svg", ID, null, false);
    }

    private ActionListener eventGetEmployee = (x)->{
        PreferencesGeneralEmpleado panel = (PreferencesGeneralEmpleado)PreferencesInstance.getInstance().getInstancePreferences(PreferencesGeneralEmpleado.class);
        panel.get
    };

    public BiConsumer<EmpleadoN, Runnable> eventShowPreferences = (empleado, refresh) -> {
        LinkedHashMap<Class<? extends Preferences>, ModalPreferences.ModalUtils> mapActions = new LinkedHashMap<>();
        mapActions.put(PreferencesGeneralEmpleado.class, new ModalPreferences.ModalUtils(new ModalPreferences.ButtonPreferences("General"),eventGetEmployee));
        mapActions.put(PreferencesUpdateInfoEmployee.class, new ModalPreferences.ModalUtils(new ModalPreferences.ButtonPreferences("Actualizar Información"),(x)->{}));
//        mapActions.put(PreferencesUpdateStatusEmployee.class, new ModalPreferences.ButtonPreferences("Actualizar Estado", "#ffaa00", PathResources.Icon.home + "ic_status.svg"));

        showPanel(new ModalPreferences<EmpleadoN>(mapActions, ID)
                        .initial(empleado, PreferencesGeneralEmpleado.class)
                        .installEventShowInfo(() -> {
                        })
                        .installEventClose(refresh),
                null, null, ID, null, false);
    };




}
