package bumh3r.controller;

import bumh3r.archive.PathResources;
import bumh3r.dao.EmpleadoDAO;
import bumh3r.model.New.EmpleadoN;
import bumh3r.notifications.Notify;
import bumh3r.request.EmpleadoRequest;
import bumh3r.system.panel.PanelsInstances;
import bumh3r.system.preferences.Preferences;
import bumh3r.system.preferences.PreferencesInstance;
import bumh3r.view.form.FormEmployee;
import bumh3r.view.modal.ModalPreferences;
import bumh3r.view.panel.PanelAddEmployee;
import bumh3r.view.preferences.empleado.PreferencesGeneralEmpleado;
import bumh3r.view.preferences.empleado.PreferencesUpdateInfoEmployee;
import bumh3r.view.preferences.empleado.PreferencesUpdateStatusEmployee;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.BiConsumer;
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
        this.view.setEventFormInit(this::showEmployeeAll);
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
                            List<EmpleadoN> list = empleadoDAO.findAll();
                            if (list.isEmpty()) {
                                callback.done(Toast.Type.WARNING, "No hay empleados registrados");
                                view.eventCleanUser();
                                return;
                            }
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
                                    EmpleadoN empleado = empleadoDAO.save(value);
                                    view.eventAddUsuario.accept(empleado);
                                    panelAddEmployee.cleanValue();
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

    private ActionListener eventGetEmployee = (x) -> {
        Toast.closeAll();
        PreferencesGeneralEmpleado panel = (PreferencesGeneralEmpleado) PreferencesInstance.getInstance().getInstancePreferences(PreferencesGeneralEmpleado.class);
        EmpleadoN value = (EmpleadoN) panel.getIdentifier();
        if (panel == null || value == null || Toast.checkPromiseId(KEY)) return;
        Notify.showPromise("Obteniendo ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Obteniendo Empleado ...");
                            EmpleadoN empleado = empleadoDAO.findById(value.getId())
                                    .orElseThrow(() -> new RuntimeException("El empleado no existe"));
                            setInfoEmployee(empleado, value);
                            panel.setValue(empleado);
                            callback.done(Toast.Type.SUCCESS, "El empleado fue obtenido correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error agregar el empleado\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    };

    private ActionListener eventUpdateEmployee = (x) -> {
        Toast.closeAll();
        PreferencesUpdateInfoEmployee panel = (PreferencesUpdateInfoEmployee) PreferencesInstance.getInstance().getInstancePreferences(PreferencesUpdateInfoEmployee.class);
        EmpleadoRequest value = panel.getValue();
        if (value == null || Toast.checkPromiseId(KEY)) return;
        Notify.showPromise("Actualizando ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Actualizando Empleado ...");
                            EmpleadoN empleadoOrigin = (EmpleadoN) panel.getIdentifier();
                            EmpleadoN empleado = empleadoDAO.update(empleadoOrigin.getId(), value);
                            setInfoEmployee(empleado, empleadoOrigin);
                            callback.done(Toast.Type.SUCCESS, "El empleado fue actualizado correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error actualizar el empleado\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    };

    private ActionListener eventChangeStatusEmployee = (x) -> {
        Toast.closeAll();
        if (Toast.checkPromiseId(KEY)) return;
        PreferencesUpdateStatusEmployee panel = (PreferencesUpdateStatusEmployee) PreferencesInstance.getInstance().getInstancePreferences(PreferencesUpdateStatusEmployee.class);
        EmpleadoN empleadoOrigin = (EmpleadoN) panel.getIdentifier();
        Notify.showPromise("Actualizando ...",
                new ToastPromise(KEY) {
                    @Override
                    public void execute(PromiseCallback callback) {
                        try {
                            callback.update("Actualizando Empleado ...");
                            EmpleadoN empleadoSearch = empleadoDAO.findById(empleadoOrigin.getId()).orElseThrow(() -> new RuntimeException("El empleado no existe"));

                            boolean isActive = !empleadoOrigin.getIsActivo();
                            empleadoSearch.setIsActivo(isActive);
                            empleadoDAO.update(empleadoSearch);

                            empleadoOrigin.setIsActivo(isActive);
                            panel.setStatus(empleadoSearch);
                            callback.done(Toast.Type.SUCCESS, "El empleado fue actualizado correctamente");
                        } catch (Exception ex) {
                            callback.done(Toast.Type.ERROR, "Error actualizar el estado del empleado\n" +
                                    "Causa: " + ex.getLocalizedMessage());
                        }
                    }
                });
    };



    public BiConsumer<EmpleadoN, Runnable> eventShowPreferences = (empleado, refresh) -> {
        LinkedHashMap<Class<? extends Preferences>, ModalPreferences.ModalUtils> mapActions = new LinkedHashMap<>();
        mapActions.put(PreferencesGeneralEmpleado.class, new ModalPreferences.ModalUtils(new ModalPreferences.ButtonPreferences("General"), eventGetEmployee));
        mapActions.put(PreferencesUpdateInfoEmployee.class, new ModalPreferences.ModalUtils(new ModalPreferences.ButtonPreferences("Actualizar Información"), eventUpdateEmployee));
        mapActions.put(PreferencesUpdateStatusEmployee.class, new ModalPreferences.ModalUtils(new ModalPreferences.ButtonPreferences("Actualizar Estado", "#ffaa00", PathResources.Icon.home + "ic_status.svg"),eventChangeStatusEmployee));

        showPanel(new ModalPreferences<EmpleadoN>(mapActions, ID)
                        .initial(empleado, PreferencesGeneralEmpleado.class)
                        .installEventShowInfo(() -> {})
                        .installEventClose(refresh),
                null, null, ID, null, false);
    };

    public void setInfoEmployee(EmpleadoN getEmpleado, EmpleadoN empleadoOrigin) {
        empleadoOrigin.setTipoEmpleado(getEmpleado.getTipoEmpleado());
        empleadoOrigin.setNombre(getEmpleado.getNombre());
        empleadoOrigin.setApellido(getEmpleado.getApellido());
        empleadoOrigin.setCorreo(getEmpleado.getCorreo());
        empleadoOrigin.setTelefono(getEmpleado.getTelefono());
        empleadoOrigin.setRfc(getEmpleado.getRfc());
        empleadoOrigin.setGenero(getEmpleado.getGenero());
        empleadoOrigin.setDireccion(getEmpleado.getDireccion());
        empleadoOrigin.setIsActivo(getEmpleado.getIsActivo());
        empleadoOrigin.setTipoEmpleado(getEmpleado.getTipoEmpleado());
        empleadoOrigin.getDireccion().setEstado(getEmpleado.getDireccion().getEstado());
        empleadoOrigin.getDireccion().setMunicipio(getEmpleado.getDireccion().getMunicipio());
        empleadoOrigin.getDireccion().setCodigo_postal(getEmpleado.getDireccion().getCodigo_postal());
        empleadoOrigin.getDireccion().setCalle(getEmpleado.getDireccion().getCalle());
        empleadoOrigin.getDireccion().setColonia(getEmpleado.getDireccion().getColonia());
    }


}
