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
@Table(name = "dispositivo")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class DispositivoN {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo_dispositivo;
    private String marca;
    private String modelo;
    private String imei;
    private Integer utils;
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "nota_id")
    private NotaN nota;
}
