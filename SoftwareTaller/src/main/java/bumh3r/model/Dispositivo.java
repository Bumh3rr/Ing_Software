package bumh3r.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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

@Entity
@Table(name = "dispositivo")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Dispositivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private TipoDispositivo tipo_dispositivo;
    @Enumerated(EnumType.STRING)
    private Marca marca;
    private String modelo;
    private String imei;
    private Integer utils;
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "nota_id")
    private Nota nota;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "dispositivo_id")
    private List<Reparacion> reparaciones;

    public enum TipoDispositivo {
        CELULAR("Celular"),
        TABLET("Tablet"),
        COMPUTADORA("Computadora"),
        OTRO("Otro");

        private final String nombre;

        TipoDispositivo(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }

    public enum Marca {
        APPLE("Apple"),
        SAMSUNG("Samsung"),
        HUAWEI("Huawei"),
        XIAOMI("Xiaomi"),
        MOTOROLA("Motorola"),
        LG("LG"),
        SONY("Sony"),
        ASUS("Asus"),
        LENOVO("Lenovo"),
        DELL("Dell"),
        HP("HP"),
        ACER("Acer"),
        TOSHIBA("Toshiba"),
        MSI("MSI"),
        RAZER("Razer"),
        OTRO("Otro");

        private final String nombre;

        Marca(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }
}
