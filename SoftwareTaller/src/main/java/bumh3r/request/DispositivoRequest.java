package bumh3r.request;

public record DispositivoRequest(
        String tipo_dispositivo,
        String marca,
        String modelo,
        String imei,
        Integer utils,
        String observaciones
) {
}
