package bumh3r.request;

public record ReparacionRequest(
        String id,
        String idNota,
        String idDispositivo,
        String idEmpleado,
        String idTipoReparacion,
        String idMarca
) {
}
