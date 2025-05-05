package bumh3r.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pago {
    private long id;
    private Venta venta;
    private MetodoPago metodoPago;
    private float monto;
    private LocalDate fecha;
}
