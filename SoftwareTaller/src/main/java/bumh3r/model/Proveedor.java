package bumh3r.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
public class Proveedor {
    private Long id;
    private String nombre;
    private String telefono;
    private String correo;
    private String direccion;
    private String fechaRegistro;

    public Proveedor(Long id, String nombre, String telefono, String correo, String direccion, String fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.fechaRegistro = fechaRegistro;
    }

    public String[] toArray() {
        return new String[]{id.toString(), nombre, telefono, correo, direccion, fechaRegistro};
    }
}
