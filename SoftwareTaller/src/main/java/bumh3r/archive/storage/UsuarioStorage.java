package bumh3r.archive.storage;

import bumh3r.archive.Archive;
import bumh3r.request.UsuarioLogin;
import java.io.*;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.Cleanup;

public class UsuarioStorage {

    private final String URL = Archive.URL_USUARIO;
    private File file;

    public UsuarioStorage() {
        file = new File(Archive.RUTA + URL);
    }

    public Optional<UsuarioLogin> readUser() {
        try {
            @Cleanup
            DataInputStream in = new DataInputStream(new FileInputStream(URL));
            if (in.available() > 0) {
                String username = in.readUTF();
                boolean remember = in.readBoolean();
                return Optional.of(new UsuarioLogin(username, null, remember));
            }
            return Optional.empty();
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    public void writeUser(UsuarioLogin usuario) throws IOException {
        @Cleanup
        DataOutputStream out = new DataOutputStream(new FileOutputStream(URL));
        out.writeUTF(usuario.username());
        out.writeBoolean(usuario.remember());
        out.flush();
    }

    public void delete() {
        Paths.get(Archive.URL_USUARIO).toFile().delete();
    }

}
