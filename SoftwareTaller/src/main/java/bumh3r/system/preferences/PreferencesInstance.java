package bumh3r.system.preferences;

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingUtilities;

public class PreferencesInstance {
    private static PreferencesInstance instance;
    private Map<Class<? extends Preferences>, Preferences> formsMap;

    private PreferencesInstance() {
        formsMap = new HashMap<>();
    }

    public static void closeSession() {
        instance = null;
    }

    public static PreferencesInstance getInstance() {
        if (instance == null) {
            instance = new PreferencesInstance();
        }
        return instance;
    }

    public Preferences getPreferences(Class<? extends Preferences> cls, Object objeto, String key, ActionListener actionListener) {
        return getPreferencesPanel(cls, objeto, key, actionListener);
    }

    public Preferences getPreferences(Class<? extends Preferences> cls) {
        return formsMap.get(cls);
    }

    public Preferences getPreferencesPanel(Class<? extends Preferences> cls, Object objeto, String key, ActionListener actionListener) {
        if (formsMap.containsKey(cls)) {
            Preferences preferences = formsMap.get(cls);
            preferencesOpen(preferences);
            return formsMap.get(cls);
        }

        try {
            Preferences preferences = cls.getDeclaredConstructor(Object.class, String.class).newInstance(objeto, key);
            formsMap.put(cls, preferences);
            preferences.setEventButton(actionListener);
            preferences.installController();
            preferenceInit(preferences);
            return preferences;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Actualiza el objeto de preferencias
    public void updatePreferencesPanel(Object objeto, String key) {
        for (Map.Entry<Class<? extends Preferences>, Preferences> entry : formsMap.entrySet()) {
            if (entry.getValue().getIdModal().equals(key)) {
                entry.getValue().setIdentifier(objeto);
            }
        }
    }

    private static void preferencesOpen(Preferences preferences) {
        SwingUtilities.invokeLater(preferences::openPreference);
    }

    public static void preferenceInit(Preferences preferences) {
        SwingUtilities.invokeLater(preferences::initPreference);
    }

    public void cleanPreferences(String key) {
        formsMap.entrySet().removeIf(entry -> entry.getValue().getIdModal().equals(key));
    }
}
