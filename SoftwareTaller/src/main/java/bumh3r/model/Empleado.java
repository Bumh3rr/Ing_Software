package bumh3r.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "empleado")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private String genero;
    private String rfc;
    @CreationTimestamp
    private LocalDateTime fecha_registro;
    @CreationTimestamp
    private LocalDateTime fecha_baja;
    private Boolean isActivo;
    @OneToOne
    @JoinColumn(name = "direccion_id")
    private Direccion direccion;
    @ManyToOne
    @JoinColumn(name = "tipo_empleado_id")
    private TipoEmpleado tipoEmpleado;

    @Override
    public String toString() {
        return String.format("ID: %d | %s %s",id,nombre,apellido);
    }
}
