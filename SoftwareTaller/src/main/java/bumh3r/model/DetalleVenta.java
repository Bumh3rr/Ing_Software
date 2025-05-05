package bumh3r.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {
    private long id;
    private Venta venta;
    private Reparacion_Dispositivo reparacion;
    private Refaccion refaccion;
    private int cantidad;
    private float precio_unitario;
    private float total;
}
