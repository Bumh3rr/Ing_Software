package bumh3r.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Getter;

@Getter
public class Marca {
    private int id;
    private String nombre_marca;
    private String url_icono;

    public Marca(int id, String nombre_marca, String url_icono) {
        this.id = id;
        this.nombre_marca = nombre_marca;
        this.url_icono = url_icono;
    }

    @Override
    public String toString() {
        return this.nombre_marca;
    }

    public static class ListMarcas {
        private static ListMarcas instance;
        private HashMap<TipoDispositivo, List<Marca>> mapTiposReparaciones;
        private List<Marca> marcasAll;

        public static ListMarcas getInstance() {
            if (instance == null) {
                instance = new ListMarcas();
            }
            return instance;
        }

        public ListMarcas() {
            mapTiposReparaciones = new HashMap<>();
            marcasAll = new ArrayList<>();
        }

        public List<Marca> getList(TipoDispositivo tipo) throws Exception {
            if (mapTiposReparaciones.containsKey(tipo)) {
                List<Marca> marcas = mapTiposReparaciones.get(tipo);
                if (marcas != null || !marcas.isEmpty()) {
                    return marcas;
                }
                mapTiposReparaciones.remove(tipo);
            }
//            List<Marca> list = RequestMarcas.getListMarcas(tipo.getId());
//            mapTiposReparaciones.put(tipo, list);
//            return list;
            return null;
        }

        public List<Marca> getListAll() throws Exception {
            if(!marcasAll.isEmpty()){
                return marcasAll;
            }
//            marcasAll = RequestMarcas.getListMarcasAll();
            return marcasAll;
        }
    }
}
