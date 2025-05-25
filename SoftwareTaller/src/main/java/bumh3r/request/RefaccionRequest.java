package bumh3r.request;

import bumh3r.model.Categoria;
import bumh3r.model.Proveedor;

public record RefaccionRequest(
        String nombre,
        String descripcion,
        Integer stock,
        Float precio_venta,
        Float precio_compra,
        Proveedor proveedor,
        Categoria categoria
) {
}
