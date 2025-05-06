package bumh3r.model;

import bumh3r.model.New.EmpleadoN;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "usuario")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @CreationTimestamp
    private LocalDateTime fecha_registro;
    private Boolean admin;
    @ManyToOne
    @JoinColumn(name = "E_id")
    private EmpleadoN empleado;
}
