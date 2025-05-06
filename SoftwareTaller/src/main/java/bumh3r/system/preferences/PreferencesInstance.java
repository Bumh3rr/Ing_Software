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

    public Preferences getPreferencesPanel(Class<? extends Preferences> cls, Object objeto, String key, ActionListener... actionListeners) {
        if (formsMap.containsKey(cls)) {
            Preferences preferences = formsMap.get(cls);
            preferencesOpen(preferences);
            return preferences;
        }

        try {
            Preferences preferences = cls.getDeclaredConstructor(Object.class, String.class, ActionListener[].class).newInstance(objeto, key, actionListeners);
            preferences.installEvents();
            preferenceInit(preferences);
            formsMap.put(cls, preferences);
            return preferences;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Preferences getInstancePreferences(Class<? extends Preferences> cls){
        if (formsMap.containsKey(cls)) {
            return formsMap.get(cls);
        }
        return null;
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
