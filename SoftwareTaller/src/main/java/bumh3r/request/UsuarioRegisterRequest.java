package bumh3r.request;

import bumh3r.model.New.EmpleadoN;

public record UsuarioRegisterRequest(
        String username,
        String password,
        String passwordConfirm,
        EmpleadoN empleado,
        boolean isAdmin
) {
}
