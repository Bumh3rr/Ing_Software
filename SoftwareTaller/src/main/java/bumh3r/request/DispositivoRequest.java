package bumh3r.request;

import bumh3r.model.New.DispositivoN;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DispositivoRequest{
    private DispositivoN.TipoDispositivo tipo_dispositivo;
    private DispositivoN.Marca marca;
    private String modelo;
    private String imei;
    private Integer utils;
    private String observaciones;
    private List<ReparacionRequest> reparaciones;
}
