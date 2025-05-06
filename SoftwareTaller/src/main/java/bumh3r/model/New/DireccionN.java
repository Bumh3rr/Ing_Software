package bumh3r.model.New;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "direccion")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class DireccionN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String estado;
    private String municipio;
    private String colonia;
    private String calle;
    private String codigo_postal;
}
