package bumh3r.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Detalles {
    private int id;
    private Boolean entregado;
    private LocalDateTime fecha_entrega;
    private String observaciones;
    private int utils;

    public Detalles( String observaciones, int utils) {
        this.observaciones = observaciones;
        this.utils = utils;
    }



}
