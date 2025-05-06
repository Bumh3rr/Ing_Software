package bumh3r.system.form;

import bumh3r.system.panel.PanelsInstances;
import bumh3r.utils.UndoRedo;
import bumh3r.view.form.FormLogin;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import java.awt.EventQueue;
import javax.swing.JFrame;
import lombok.Getter;
import raven.modal.Drawer;
import raven.modal.Toast;

public class FormsManager {
    protected static final UndoRedo<Form> FORMS = new UndoRedo<>();
    @Getter
    private static JFrame frame;
    private static MainForms mainForm;
    private static FormsManager instance;

    public static FormsManager getInstance() {
        if (instance == null) {
            instance = new FormsManager();
        }
        return instance;
    }

    public void initFrame(JFrame application) {
        this.frame = application;
        PanelsInstances.getInstance().install(application);
        init();
    }

    public static void showFormMain(Form form) {
        if (form != FORMS.getCurrent()) {
            FORMS.add(form);
            form.formCheckUI();

            mainForm.setForm(form);
            mainForm.refresh();
            Drawer.setSelectedItemClass(form.getClass());
        }
    }

    public static void undo() {
        if (FORMS.isUndoAble()) {
            Form form = FORMS.undo();
            form.formCheckUI();
            form.formOpen();
            Toast.closeAll();
            mainForm.setForm(form);
            Drawer.setSelectedItemClass(form.getClass());
        }
    }

    public static void redo() {
        if (FORMS.isRedoAble()) {
            Form form = FORMS.redo();
            form.formCheckUI();
            form.formOpen();
            Toast.closeAll();
            mainForm.setForm(form);
            Drawer.setSelectedItemClass(form.getClass());
        }
    }

    public static void refresh() {
        if (FORMS.getCurrent() != null) {
            FORMS.getCurrent().formRefresh();
            mainForm.refresh();
        }
    }

    public static void login(Class<? extends Form> classForm) {
        EventQueue.invokeLater(() -> {
            FlatAnimatedLafChange.showSnapshot();
            Drawer.setVisible(true);
            frame.getContentPane().removeAll();
            frame.getContentPane().add(getMainForm());
            Drawer.setSelectedItemClass(classForm);
            frame.repaint();
            frame.revalidate();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    public static void logout(boolean clean) {
        EventQueue.invokeLater(() -> {
            Drawer.setVisible(false);
            FORMS.clear();
            mainForm = null;
            FlatAnimatedLafChange.showSnapshot();
            FormLogin login = (FormLogin) AllFormsMain.getForm(FormLogin.class);
            if (clean) login.cleanFields();
            login.formCheckUI();
            frame.getContentPane().removeAll();
            frame.getContentPane().add(login);
            frame.repaint();
            frame.revalidate();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    public static void init() {
        EventQueue.invokeLater(() -> {
            Form form = AllFormsMain.getForm(FormLogin.class);
            form.formCheckUI();
            frame.getContentPane().removeAll();
            frame.getContentPane().add(form);
            FORMS.clear();
            frame.repaint();
            frame.revalidate();
            FlatAnimatedLafChange.hideSnapshotWithAnimation();
        });
    }

    private static MainForms getMainForm() {
        if (mainForm == null) {
            mainForm = new MainForms();
        }
        return mainForm;
    }
}
