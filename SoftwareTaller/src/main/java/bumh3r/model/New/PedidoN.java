package bumh3r.model.New;

import jakarta.persistence.Entity;
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
public class PedidoN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime fecha_pedido;
    private EstadoPedido estado;
    private String observaciones;
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private ProveedorN proveedor;

    public enum EstadoPedido {
        PENDIENTE,
        EN_PROCESO,
        COMPLETADO,
        CANCELADO
    }
}
