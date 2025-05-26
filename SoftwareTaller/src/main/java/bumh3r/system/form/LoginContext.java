package bumh3r.system.form;

import bumh3r.model.Usuario;
import lombok.Getter;

public class LoginContext {
    private static LoginContext instance;
    @Getter
    private Usuario usuario;

    public static LoginContext getInstance() {
        if (instance == null) {
            instance = new LoginContext();
        }
        return instance;
    }

    public void login(Usuario usuario) {
        this.usuario = usuario;
    }

    public void logout() {
        this.usuario = null;
    }


}
