package bumh3r.request;

import bumh3r.model.TipoEmpleado;

public record EmpleadoRequest(
        String nombre,
        String apellido,
        String telefono,
        String correo,
        String genero,
        String rfc,
        DireccionRequest direccion,
        TipoEmpleado tipoEmpleado
) {
}
