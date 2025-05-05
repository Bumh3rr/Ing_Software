package bumh3r.model;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Venta {
    private long id;
    private Nota nota;
    private LocalDate fecha;
    private float precioTotal;
    private Estado estado;

    public static enum Estado {
        PENDIENTE("Pendiente"),
        COMPLETADA("Completada"),
        CANCELADA("Cancelada");

        private String nombre;

        Estado(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }
}
