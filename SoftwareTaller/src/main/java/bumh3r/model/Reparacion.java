package bumh3r.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class Reparacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private CategoriaReparacion categoria;
    private String reparacion;
    private String observacion;
    private Float precio;
    private Float abono;
    @Enumerated(EnumType.STRING)
    private EstadoReparacion estado;
    @ManyToOne
    @JoinColumn(name = "dispositivo_id")
    private Dispositivo dispositivo;

    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    public enum EstadoReparacion {
        PENDIENTE("Pendiente"),
        EN_PROCESO("En Proceso"),
        TERMINADO("Terminado"),
        CANCELADO("Cancelado");
        @Getter
        private final String estado;

        EstadoReparacion(String estado) {
            this.estado = estado;
        }

        @Override
        public String toString() {
            return this.estado;
        }
    }

    public enum CategoriaReparacion {
        HARDWARE("Hardware"),
        SOFTWARE("Software"),
        OTRO("Otro");

        @Getter
        private final String nombre;

        CategoriaReparacion(String nombre) {
            this.nombre = nombre;
        }

        @Override
        public String toString() {
            return this.nombre;
        }
    }
}
