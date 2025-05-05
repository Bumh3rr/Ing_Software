package bumh3r.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
@Getter
@ToString
@AllArgsConstructor
public class Reparacion_Dispositivo {
    private int id;
    private TipoReparacion tipoReparacion;
    private Categorias_Reparacion categoria;
    private Reparacion reparacion;
    private String descripcion;
    private Double precio;
    private Double abono;
    private Double ganancia;
    private Boolean garantia;
    private String expirationGarantia;
    private Status status;

    private Empleado empleado;
    private JSON json;

    public Reparacion_Dispositivo(TipoReparacion tipoReparacion, Categorias_Reparacion categoria, Reparacion reparacion, String descripcion, Double precio, Double abono, Empleado empleado, Status status){
        this.tipoReparacion = tipoReparacion;
        this.categoria = categoria;
        this.reparacion = reparacion;
        this.descripcion = descripcion;
        this.precio = precio;
        this.abono = abono;
        this.empleado = empleado;
        this.status = status;
        this.json = new JSON(tipoReparacion.getId(),categoria.getId(),reparacion.getId(),descripcion,precio,abono,0.0,0,null,status.getKey(), empleado.getId());
    }

    public String generatedJsonCreatedNote(){
//        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
//                .append("idTipoReparacion", this.tipoReparacion.getId())
//                .append("idCategoria", this.categoria.getId())
//                .append("idReparacion", this.reparacion.getId())
//                .append("descripcion", descripcion)
//                .append("precio", precio)
//                .append("abono", abono)
//                .append("idStatus", status.getKey())
//                .append("idTecnico", tecnico.getId())
//                .toString();
        return "";
    }

    @Getter
    @AllArgsConstructor
    public class JSON{
        private int idTipoReparacion;
        private char idCategoria;
        private int idReparacion;
        private String descripcion;
        private double precio;
        private double abono;
        private double ganancia;
        private int garantia;
        private String expirationGarantia;
        private String idStatus;
        private Long idTecnico;
    }

    @Getter
    public enum Status {
        PENDIENTE("P","Pendiente"),
        EN_REPARACION("R","En Reparación"),
        LISTO("L","Listo"),
        NO_REPARADO("N","No Reparado");

        private final String key;
        private final String value;

        Status(String key,String value) {
            this.key = key;
            this.value = value;
        }


        public static Status fromValue(String key) {
            for (Status status : Status.values()) {
                if (status.getKey().equals(key)) {
                    return status;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return value;
        }
    }

}
