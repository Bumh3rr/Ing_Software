package bumh3r.request;

import bumh3r.model.New.ProveedorN;
import java.util.List;

public record PedidoRequest(
        String descripcion,
        ProveedorN proveedor,
        List<DetallesPedidosRequest> detalles
) {
}
