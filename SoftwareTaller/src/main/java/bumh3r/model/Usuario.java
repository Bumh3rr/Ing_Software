package bumh3r.model;

import java.time.LocalDateTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class Usuario{
    private long id;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private LocalDateTime date_register;
    private String type;
    private Set<Rol> roles;

}
