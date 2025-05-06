package bumh3r.request;

public record DireccionRequest(
        String estado,
        String municipio,
        String colonia,
        String calle,
        String codigo_postal
) {
}
