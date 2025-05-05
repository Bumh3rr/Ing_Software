package bumh3r.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;


@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TypeEmpleado {
    private long id;
    private String name_type;

    @JsonCreator
    public TypeEmpleado() {
    }

    @Override
    public String toString() {
        return String.format("%s", name_type);
    }
}
