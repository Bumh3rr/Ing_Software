package bumh3r.request;

import bumh3r.model.New.EmpleadoN;
import bumh3r.model.New.ReparacionN;

public record ReparacionRequest(
        String reparacion,
        ReparacionN.CategoriaReparacion categoria,
        String observacion,
        Float precio,
        Float abono,
        EmpleadoN tecnico
) {
}
