package bumh3r.request;

import bumh3r.model.New.ProveedorN;
import java.util.List;

public record PedidoRequest(
        String observaciones,
        ProveedorN proveedor,
        List<DetallesPedidosRequest> detalles
) {
}
