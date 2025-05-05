package bumh3r.model;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class Cobro {
    private int id;
    private double monto_abonado;
    private LocalDateTime fecha;
    private MetodoPago metodoPago;
    private TipoCobro tipoCobro;

    public Cobro(int id, double monto_abonado, LocalDateTime fecha, MetodoPago metodoPago,TipoCobro tipoCobro) {
        this.id = id;
        this.monto_abonado = monto_abonado;
        this.fecha = fecha;
        this.metodoPago = metodoPago;
        this.tipoCobro = tipoCobro;
    }

    public Cobro(double monto_abonado, LocalDateTime fecha,MetodoPago metodoPago, TipoCobro tipoCobro) {
        this(0, monto_abonado, fecha, metodoPago,tipoCobro);
    }


    public enum TipoCobro {
        ABONO("A", "Abono"),
        PAGO_TOTAL("PT", "Pago Total");

        private final String key;
        private final String name;

        public String getKey() {
            return key;
        }

        TipoCobro(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public static TipoCobro fromTipoCobro(String key) {
            for (TipoCobro tipoCobro : TipoCobro.values()) {
                if (tipoCobro.key.equals(key)) {
                    return tipoCobro;
                }
            }
            return null;
        }
    }

}
