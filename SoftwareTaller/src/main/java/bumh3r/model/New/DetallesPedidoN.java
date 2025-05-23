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
@Table(name = "detalles_pedido")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DetallesPedidoN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer cantidad;
    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private PedidoN pedido;
    @ManyToOne
    @JoinColumn(name = "refaccion_id")
    private RefaccionN refaccion;
}
