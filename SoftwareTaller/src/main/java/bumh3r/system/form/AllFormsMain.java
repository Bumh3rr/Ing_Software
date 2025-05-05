package bumh3r.system.form;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;

public class AllFormsMain {
    private static AllFormsMain instance;
    private Map<Class<? extends Form>, Form> formsMap;

    public static void closeSession() {
        instance = null;
    }
    private static AllFormsMain getInstance() {
        if (instance == null) {
            instance = new AllFormsMain();
        }
        return instance;
    }

    private AllFormsMain() {
        formsMap = new HashMap<>();
    }

    public static Form getForm(Class<? extends Form> cls) {
        if (getInstance().formsMap.containsKey(cls)) {
            Form form = getInstance().formsMap.get(cls);
            formOpen(form);
            return form;
        }
        try {
            Form form = cls.getDeclaredConstructor().newInstance();
            form.installController();
            getInstance().formsMap.put(cls, form);
            formInit(form);
            return form;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void formInit(Form form) {
        SwingUtilities.invokeLater(() -> form.formInit());
    }

    public static void formOpen(Form form) {
        SwingUtilities.invokeLater(() -> form.formOpen());
    }
}
