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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pedido")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha_pedido;
    @Enumerated(EnumType.STRING)
    private EstadoPedido estado;
    private String observaciones;
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private Proveedor proveedor;

    public enum EstadoPedido {
        PENDIENTE("Pendiente"),
        EN_PROCESO("En Proceso"),
        COMPLETADO("Completado"),
        CANCELADO("Cancelado");

        @Getter
        private final String value;
        EstadoPedido(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
}
