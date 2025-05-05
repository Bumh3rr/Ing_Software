package bumh3r.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@ToString
@Setter
public class Refaccion {
    private Long id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private int stock;
    private double precioVenta;
    private double precioCompra;
    private LocalDate fecha_registro;
    private Proveedor proveedor;

    public String[] toArray() {
        return new String[]{id.toString(), nombre, descripcion, categoria, String.valueOf(stock), String.valueOf(precioVenta), String.valueOf(precioCompra), fecha_registro.toString(), proveedor.getNombre()};
    }
}
