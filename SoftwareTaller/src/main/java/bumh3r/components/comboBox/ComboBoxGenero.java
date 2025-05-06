package bumh3r.components.comboBox;

import bumh3r.model.other.Genero;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import javax.swing.DefaultComboBoxModel;

public class ComboBoxGenero extends FlatComboBox {

    public ComboBoxGenero() {
        setModel(new DefaultComboBoxModel(Genero.values()));
        setSelectedIndex(0);
        setFocusable(false);
        setEditable(false);
    }

}
