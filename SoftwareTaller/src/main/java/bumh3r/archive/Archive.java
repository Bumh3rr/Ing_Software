package bumh3r.archive;

import java.io.File;

public class Archive {

    public static final String RUTA = "Archivos";
    public static final String URL_TOKEN = RUTA +"/token.txt";
    public static final String URL_USUARIO = RUTA +"/user.txt";

    public static boolean buildRute() {
        File fileUser = new File(RUTA);
        if (!fileUser.exists()) {
            return fileUser.mkdirs();
        }
        return true;
    }

    public static String getRuta(long id) {
        File fileUser = new File(RUTA + "/" + id);
        if (!fileUser.exists()) {
            if (!fileUser.mkdirs()) {
                return "";
            }
        }
        return fileUser.getAbsolutePath();
    }



}
