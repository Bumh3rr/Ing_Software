package bumh3r.request;

import bumh3r.model.Empleado;
import bumh3r.model.Reparacion;

public record ReparacionRequest(
        String reparacion,
        Reparacion.CategoriaReparacion categoria,
        String observacion,
        Float precio,
        Float abono,
        Empleado tecnico
) {
}
