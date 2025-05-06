package bumh3r.request;

import bumh3r.model.New.TipoEmpleado;
import java.time.LocalDateTime;

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
