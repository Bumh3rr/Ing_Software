package bumh3r.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class DetallesPedido {
    private Long id;
    private Proveedor proveedor;
    private Refaccion refaccion;
    private int cantidad;

    public Object[] toArray() {
        return new Object[]{id,refaccion.getNombre(), cantidad};
    }


}
