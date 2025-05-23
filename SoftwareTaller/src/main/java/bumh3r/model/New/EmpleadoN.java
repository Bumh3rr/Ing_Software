package bumh3r.model.New;

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
public class EmpleadoN {
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
    @JoinColumn(name = "D_id")
    private DireccionN direccion;
    @ManyToOne
    @JoinColumn(name = "TE_id")
    private TipoEmpleado tipoEmpleado;

    @Override
    public String toString() {
        return String.format("ID: %d | %s %s",id,nombre,apellido);
    }
}
