package bumh3r.request;

import bumh3r.model.Proveedor;
import java.util.List;

public record PedidoRequest(
        String observaciones,
        Proveedor proveedor,
        List<DetallesPedidosRequest> detalles
) {
}
