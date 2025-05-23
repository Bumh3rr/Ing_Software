package bumh3r.model.New;


import bumh3r.model.Proveedor;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "refaccion")
public class RefaccionN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String descripcion;
    private int stock;
    private double precio_venta;
    private double precio_compra;
    @CreationTimestamp
    private LocalDateTime fecha_registro;
    @ManyToOne
    @JoinColumn(name = "proveedor_id")
    private ProveedorN proveedor;
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaN categoria;

    @Override
    public String toString() {
        return String.format("%d | %s", id, nombre);
    }
}
