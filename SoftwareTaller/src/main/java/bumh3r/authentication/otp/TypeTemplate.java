package bumh3r.authentication.otp;

import lombok.Getter;

public enum TypeTemplate {
    REGISTER_SUCCESS("Registro éxitoso","templates/register-success-template.html"),
    OTP("Validación OTP","templates/otp_email_template.html");

    @Getter
    private final String title;
    @Getter
    private final String path;

    TypeTemplate(String title,String path) {
        this.title = title;
        this.path = path;
    }
}
