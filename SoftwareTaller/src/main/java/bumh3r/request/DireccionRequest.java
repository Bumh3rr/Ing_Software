package bumh3r.request;

public record DireccionRequest(
        String state,
        String city,
        String municipality,
        String colony,
        String street,
        String zip,
        String references_domicile
) {
}
