package bumh3r.model;

import java.time.LocalDate;
import java.util.LinkedList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class Pedido {
    private Long id;
    private Proveedor proveedor;
    private LocalDate fecha;
    private Estado estado;
    private String observaciones;
    private LinkedList<DetallesPedido> detallesPedidos;

    public String[] toArray() {
        return new String[]{id.toString(), proveedor.getNombre(), fecha.toString(), estado.toString(), observaciones};
    }

    public enum Estado {
        CANCELADO,
        PENDIENTE,
        ENTREGADO
    }
}
