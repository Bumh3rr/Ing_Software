package bumh3r.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class Taller {
    private long id;
    private String workshop_name;
    private String rfc;
    private String phone;
    private String email;
    private byte[] logo;
    private String opening_hours;
    private Direccion direccion;
    private Licencia licencia;

    @JsonCreator
    public Taller() {
    }
}
