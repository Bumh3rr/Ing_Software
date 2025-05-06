package bumh3r.view.form;

import bumh3r.controller.Controller;
import bumh3r.dao.UsuarioDao;
import bumh3r.model.Usuario;
import java.util.function.BiConsumer;

public class ControlUsuarioViewController extends Controller {
    private final FormControlUsuario view;
    private final UsuarioDao usuarioDao;
    public ControlUsuarioViewController(FormControlUsuario view) {
        this.view = view;
        this.view.setEventFormInit(this::showUsuariosAll);
        this.view.setEventFormRefresh(this::showUsuariosAll);
        this.usuarioDao = getInstance(UsuarioDao.class);

    }

    private void showUsuariosAll() {
        view.eventAddUsuarioCard.accept(this.usuarioDao.getList());
    }

    public BiConsumer<Usuario, Runnable> eventChangePassword = (x,t)->{

    };

    public BiConsumer<Usuario, Runnable> eventDelete = (x,t)->{

    };
}
