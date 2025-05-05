package bumh3r.utils;

public class CheckInputs {
    public static boolean isOnlyLetters(String input) {
        return input.matches("^[a-zA-Z]+$");
    }

    public static boolean isNameValid(String input) {
        return input.matches("^[a-zA-Z\\s]+$");
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
}
