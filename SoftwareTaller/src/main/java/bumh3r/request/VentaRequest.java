package bumh3r.request;

import bumh3r.model.Refaccion;
import bumh3r.model.Reparacion;
import java.util.List;

public record VentaRequest(
        List<Reparacion> reparaciones,
        List<Refaccion> refacciones,
        Float total,
        Float abono,
        Float descuento,
        Float subTotal,
        PagoRequest pago
) {
}
