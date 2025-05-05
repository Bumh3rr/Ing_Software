package bumh3r.request;

public record UsuarioLogin(
        String username,
        String password,
        boolean remember
) {
}
