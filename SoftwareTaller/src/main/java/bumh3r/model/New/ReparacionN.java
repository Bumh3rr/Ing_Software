package bumh3r.model.New;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "reparacion")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReparacionN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reparacion;
    private String categoria;
    private String descripcion;
    private Float precio;
    private Float abono;
    @ManyToOne
    @JoinColumn(name = "dispositivo_id")
    private DispositivoN dispositivo;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private EmpleadoN empleado;
}
