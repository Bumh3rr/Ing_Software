package bumh3r.utils;

import bumh3r.notifications.Notify;
import java.util.function.Predicate;
import raven.modal.Toast;

public class CheckInput {

    /**
     * Si el input es vacío o no cumple con la expresión regular, muestra un mensaje de error y retorna true.<br/>
     * <b>Mensaje de error</b>: Es requerido el campo {fieldName} y {errorMessage}
     */
    public static boolean isInvalidInput(String input, Predicate<String> validator, String fieldName, String errorMessage) {
        if (input.isEmpty() || !validator.test(input)) {
            show( "Es requerido el campo " + fieldName + " y " + errorMessage);
            return true;
        }
        return false;
    }

    /**
     * Si el input es nulo o vacío, muestra un mensaje de error y retorna true.<br/>
    * <b>Mensaje de error</b>: Es requerido el campo {fieldName}
    * */
    public static boolean isNullInput(Object input, String fieldName) {
        if (input == null || input.toString().isEmpty()) {
            show( "Es requerido el campo " + fieldName);
            return true;
        }
        return false;
    }

    /**
     * Si el index es igual a 0, muestra un mensaje de error y retorna true.<br/>
     * <b>Mensaje de error</b>: Es requerido el campo {fieldName}
     */
    public static boolean isInvalidSelection(int selectedIndex, String fieldName) {
        if (selectedIndex == 0) {
            show( "Es requerido el campo " + fieldName);
            return true;
        }
        return false;
    }

    /**
     * Si el input es diferente de vació y si es diferente de que cumpla la expresión regular.<br/>
     * <b>Mensaje de error</b>: El campo {fieldName} debe ser válido
     */
    public static boolean isOptionalInvalidInput(String input, Predicate<String> validator, String fieldName) {
        if (!input.isBlank() && !validator.test(input.strip())) {
            show("El campo " + fieldName + " debe ser válido");
            return true;
        }
        return false;
    }

    /**
     * Si el input es vacío, muestra un mensaje de error y retorna true.<br/>
     * <b>Mensaje de error</b>: El campo {fieldName} es requerido
     */
    public static boolean isEmptyInput(String input, String fieldName) {
        if (input.isEmpty()) {
            show("El campo " + fieldName + " es requerido");
            return true;
        }
        return false;
    }

    private static void show(String fieldName) {
        Notify.getInstance().showToast(Toast.Type.WARNING, fieldName);
    }
}
