package bumh3r.request;

import bumh3r.model.Dispositivo;
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
    private Dispositivo.TipoDispositivo tipo_dispositivo;
    private Dispositivo.Marca marca;
    private String modelo;
    private String imei;
    private Integer utils;
    private String observaciones;
    private List<ReparacionRequest> reparaciones;
}
