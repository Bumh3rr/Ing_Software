package bumh3r.model.other;

public class Verify {

    public static <T> boolean isNotNull(T objeto) {
        return objeto != null;
    }

    public static <T> boolean isNotEmpty(String objeto) {
        return !objeto.isEmpty();
    }

}
