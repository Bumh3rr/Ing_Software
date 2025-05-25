package bumh3r.request;

import bumh3r.model.Refaccion;

public record DetallesPedidosRequest(
        Integer cantidad,
        Refaccion refaccion
) {
}
