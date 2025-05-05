package bumh3r.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Empleado {
    private Long id;
    private String firstname;
    private String lastname;
    private String rfc;
    private String phone;
    private String sex;
    private String email;
    private String state;
    private String municipality;
    private String colony;
    private String street;
    private String zip;
    private LocalDate date_register;
    private LocalDate date_low;
    private String status_activo;
    private TypeEmpleado type_employee;

    @Override
    public String toString() {
        return String.format("%s %s", firstname, lastname);
    }

    public static enum Status {
        Activo, Inactivo
    }

}
