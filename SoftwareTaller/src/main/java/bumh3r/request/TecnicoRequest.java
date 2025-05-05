package bumh3r.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TecnicoRequest(
        String firstname,
        String lastname,
        String rfc,
        String sex,
        String email,
        String phone,
        String state,
        String municipality,
        String colony,
        String street,
        String zip
) {

}