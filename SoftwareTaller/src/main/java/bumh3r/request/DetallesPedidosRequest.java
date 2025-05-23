package bumh3r.request;

import bumh3r.model.New.RefaccionN;

public record DetallesPedidosRequest(
        Integer cantidad,
        RefaccionN refaccionN
) {
}
