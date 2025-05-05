package bumh3r.model.other;

import bumh3r.archive.PathResources;
import bumh3r.notifications.Notify;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import lombok.Cleanup;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import raven.modal.Toast;

public class EstadosMx {

    private static EstadosMx instance;
    private HashMap<String, String> states;

    public static EstadosMx getInstance() {
        if (instance == null) {
            instance = new EstadosMx();
        }
        return instance;
    }

    public EstadosMx() {
        states = obtenerEstados();
    }

    public String getStateName(String abbreviation) {
        for (Map.Entry<String, String> entry : instance.states.entrySet()) {
            if (entry.getValue().contains(abbreviation)) {
                return entry.getKey();
            }
        }
        return "";
    }

    public void addItemsMunicipality(String stateSelect, FlatComboBox<String> boxMunicipality) {
        try {
            String[] municipios = obtenerMunicipiosPorEstado(stateSelect);
            Arrays.sort(municipios);
            for (String municipio : municipios) {
                boxMunicipality.addItem(municipio);
            }
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Algo Salio Mal:("
                    + "\nFavor de Contactar al Soporte"
                    + "\nCausa: " + e.getLocalizedMessage());
        }
    }

    public void addItemsStates(FlatComboBox<String> boxStates) {
        try {
            SwingUtilities.invokeLater(()->{
                for (Map.Entry<String, String> entry : instance.states.entrySet()) {
                    boxStates.addItem(entry.getKey());
                }
            });
        } catch (Exception e) {
            Notify.getInstance().showToast(Toast.Type.ERROR, "Algo Salio Mal:("
                    + "\nFavor de Contactar al Soporte"
                    + "\nCausa: " + e.getLocalizedMessage());
        }
    }

    public String getStatesAbbreviation(String stateName) {
        if (instance.states.containsKey(stateName)) {
            return instance.states.get(stateName);
        }
        return null;
    }

    public static String[] obtenerMunicipiosPorEstado(String searchEstado) throws Exception {
        String[] estados = null;
        @Cleanup
        InputStream resourceAsStream = EstadosMx.class.getClassLoader().getResourceAsStream(PathResources.Json.url +"estados-municipios.json");
        JSONObject estadosObject = new JSONObject(new JSONTokener(resourceAsStream));

        if (estadosObject.has(searchEstado)) {
            JSONArray jsonArray = estadosObject.getJSONArray(searchEstado);

            List<String> municipiosList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                municipiosList.add(jsonArray.getString(i));
            }
            estados = new String[municipiosList.size()];
            return municipiosList.toArray(estados);
        }
        return estados;
    }

    public static HashMap<String, String> obtenerEstados() {
        try {
            @Cleanup
            InputStream resourceAsStream = EstadosMx.class.getClassLoader().getResourceAsStream(PathResources.Json.url + "estados.json");
            JSONArray estadosArray = new JSONArray(new JSONTokener(resourceAsStream));
            HashMap<String, String> list = new LinkedHashMap<>();

            // Recorrer el array JSON y extraer los nombres de los estados
            for (int i = 0; i < estadosArray.length(); i++) {
                JSONObject estado = estadosArray.getJSONObject(i);
                list.put(estado.getString("nombre"), estado.getString("clave"));
            }
            return list;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}
