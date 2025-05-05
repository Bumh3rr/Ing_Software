package bumh3r.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class Direccion {
    private Long id;
    private String state;
    private String city;
    private String municipality;
    private String colony;
    private String street;
    private String zip;
    private String references_domicile;

    @JsonCreator
    public Direccion() {
    }
}
