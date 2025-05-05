package bumh3r.model;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import lombok.*;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    private Long id;
    private String name;
    private String phone1;
    private String phone2;
    private String address;
    private LocalDate dete_register;
    private LinkedList<Nota> notas;

    public Object[] toArray() {
        return new Object[]{id, name, phone1, phone2,address, dete_register};
    }
}
