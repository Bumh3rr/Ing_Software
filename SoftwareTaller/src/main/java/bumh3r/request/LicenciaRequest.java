package bumh3r.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LicenciaRequest(
        String key_licence
) {
}
