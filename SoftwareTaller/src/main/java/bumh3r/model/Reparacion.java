package bumh3r.model;

import java.util.HashMap;
import java.util.List;
import lombok.Getter;

@Getter
public class Reparacion {
    //Cambio de Display, Centro de Carga, Bateria
    private int id;
    private String name;
    private String nameBaseIcon;

    //Constructor
    public Reparacion(int id, String reparacion, String nameBaseIcon) {
        this.id = id;
        this.name = reparacion;
        this.nameBaseIcon = nameBaseIcon;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static class ListReparaciones {
        private static ListReparaciones instance;
        private HashMap<Categorias_Reparacion, List<Reparacion>> mapReparaciones;

        public static ListReparaciones getInstance() {
            if (instance == null) {
                instance = new ListReparaciones();
            }
            return instance;
        }

        public ListReparaciones() {
            mapReparaciones = new HashMap<>();
        }

        public List<Reparacion> getList(Categorias_Reparacion tipo) throws Exception {
            if (mapReparaciones.containsKey(tipo)) {
                return mapReparaciones.get(tipo);
            }
//            List<Reparacion> list = RequestReparacion.getListReparaciones(tipo.getId());
//            mapReparaciones.put(tipo, list);
//            return list;
            return null;
        }
    }
}

