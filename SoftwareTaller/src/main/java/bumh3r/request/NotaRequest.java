package bumh3r.request;

import bumh3r.model.Cliente;
import bumh3r.model.Empleado;
import java.util.List;

public record NotaRequest(
        Empleado empleado,
        Cliente cliente,
        List<DispositivoRequest> dispositivos
) {
}
