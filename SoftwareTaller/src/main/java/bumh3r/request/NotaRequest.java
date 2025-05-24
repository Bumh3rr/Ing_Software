package bumh3r.request;

import bumh3r.model.New.ClienteN;
import bumh3r.model.New.DispositivoN;
import java.util.List;

public record NotaRequest(
        ClienteN cliente,
        List<DispositivoN> dispositivos
) {
}
