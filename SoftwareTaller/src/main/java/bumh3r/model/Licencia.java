package bumh3r.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class Licencia {
    private long id;
    private String key_licence;
    private String status_licence;
    private LocalDate date_expired;
    private LocalDate date_register;

    @JsonCreator
    public Licencia() {
    }
}
