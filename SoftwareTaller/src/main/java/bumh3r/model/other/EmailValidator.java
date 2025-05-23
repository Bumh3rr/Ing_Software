package bumh3r.model.other;

import bumh3r.notifications.Notify;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import raven.modal.Toast;

public class EmailValidator {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"
            + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        Matcher matcher = PATTERN.matcher(email);
        boolean matches = matcher.matches();
        if (!matches) {
            Notify.getInstance().showToast(Toast.Type.WARNING, "Correo invalido!");
        }
        return matches;
    }
}
