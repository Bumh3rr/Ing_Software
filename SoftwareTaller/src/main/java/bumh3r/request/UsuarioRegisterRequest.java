package bumh3r.request;

import bumh3r.model.Empleado;

public record UsuarioRegisterRequest(
        String username,
        String password,
        String passwordConfirm,
        Empleado empleado,
        boolean isAdmin
) {
}
