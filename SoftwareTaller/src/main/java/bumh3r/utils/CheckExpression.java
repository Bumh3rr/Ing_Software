package bumh3r.utils;

public class CheckExpression {

    public static boolean isNameValid(String input) {
        return input.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    public static boolean isValidAddress(String address) {
        return address.matches("^[a-zA-Z0-9\\s,\\-\\.\\#]+$");
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    public static boolean isValidRFC(String rfc) {
        return rfc.matches("^[A-ZÑ&]{3,4}\\d{6}[A-Z0-9]{2,3}$");
    }

    public static boolean isValidRFCTaller(String rfc) {
        return rfc.matches("^[A-ZÑ&]{3}[0-9]{6}[A-Z0-9]{3}$");
    }
    public static boolean isValidUsername(String rfc) {
        return rfc.matches("^[a-zA-ZñÑ0-9._-]{3,20}$");
    }
}