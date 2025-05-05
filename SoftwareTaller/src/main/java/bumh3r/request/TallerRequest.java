package bumh3r.request;

public record TallerRequest(
        String workshop_name,
        String rfc,
        String phone,
        String email,
        String opening_hours,
        DireccionRequest direccion,
        LicenciaRequest licencia
) {
}
