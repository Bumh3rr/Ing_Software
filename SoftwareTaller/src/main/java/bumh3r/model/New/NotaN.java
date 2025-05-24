package bumh3r.model.New;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "nota")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class NotaN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String folio;
    @Enumerated(EnumType.STRING)
    private EstadoNota estado;
    @CreationTimestamp
    private String fecha_registro;
    @ManyToOne
    @JoinColumn(name = "empleado_id")
    private EmpleadoN empleado;
    @ManyToOne
    @JoinColumn(name = " cliente_id")
    private ClienteN cliente;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "nota_id")
    private List<DispositivoN> dispositivos;

    public enum EstadoNota {
        EN_PROCESO("En Proceso"),
        CANCELADO("Cancelado"),
        TERMINADO("Terminado");

        @Getter
        private final String value;
        EstadoNota(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String getBackgroundStatus() {
            switch (this) {
                case EN_PROCESO:
                    return "#ff9300";
                case TERMINADO:
                    return "#1aad2c";
                case CANCELADO:
                    return "#ff420a";
            }
            return "#ff9300";
        }
    }
}
