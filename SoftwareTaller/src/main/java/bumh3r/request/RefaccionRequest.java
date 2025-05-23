package bumh3r.request;

import bumh3r.model.New.CategoriaN;
import bumh3r.model.New.ProveedorN;

public record RefaccionRequest(
        String nombre,
        String descripcion,
        Integer stock,
        Float precio_venta,
        Float precio_compra,
        ProveedorN proveedor,
        CategoriaN categoria
) {
}
