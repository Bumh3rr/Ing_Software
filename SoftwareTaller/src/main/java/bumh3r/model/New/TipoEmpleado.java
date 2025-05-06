package bumh3r.model.New;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_empleado")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TipoEmpleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;

    @Override
    public String toString() {
        return String.format("%s", nombre);
    }
}
