package bumh3r.request;

import bumh3r.model.MetodoPago;

public record PagoRequest(
        MetodoPago metodoPago,
        Float monto
) {
}
