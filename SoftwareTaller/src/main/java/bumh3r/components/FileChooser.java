package bumh3r.components;

import bumh3r.system.form.FormsManager;
import jnafilechooser.api.JnaFileChooser;

public class FileChooser extends JnaFileChooser {

    public FileChooser() {
        setTitle("Seleccionar Imagen");
        setOpenButtonText("Seleccionar");
        addFilter("Image", "png", "jpg");
    }

    public boolean showOpenDialog() {
        return showOpenDialog(FormsManager.getFrame());
    }
}
