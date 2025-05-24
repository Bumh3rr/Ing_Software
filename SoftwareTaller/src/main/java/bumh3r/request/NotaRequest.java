package bumh3r.request;

import bumh3r.model.New.ClienteN;
import bumh3r.model.New.EmpleadoN;
import java.util.List;

public record NotaRequest(
        EmpleadoN empleado,
        ClienteN cliente,
        List<DispositivoRequest> dispositivos
) {
}
