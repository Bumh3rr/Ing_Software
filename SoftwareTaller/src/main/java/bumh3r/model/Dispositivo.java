package bumh3r.model;

import java.util.LinkedList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dispositivo {

    private int id;
    private TipoDispositivo type;
    private Marca brand;
    private String model;
    private String serial;
    private Detalles detalles;
    private LinkedList<Reparacion_Dispositivo> listReparaciones;

    public Dispositivo(TipoDispositivo type,Marca brand, String model) {
        this(type, brand, model, null, null, null);
    }

    public Dispositivo(TipoDispositivo type, Marca brand, String model, String serial, Detalles detalles, LinkedList<Reparacion_Dispositivo> reparacionDispositivos) {
        this.type = type;
        this.brand = brand;
        this.model = model;
        this.serial = serial;
        this.detalles = detalles;
        this.listReparaciones = reparacionDispositivos;
    }

}
