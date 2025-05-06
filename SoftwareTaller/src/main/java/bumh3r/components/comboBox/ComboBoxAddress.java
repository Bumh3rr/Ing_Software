package bumh3r.components.comboBox;

import bumh3r.model.other.EstadosMx;
import com.formdev.flatlaf.extras.components.FlatComboBox;
import javax.swing.DefaultComboBoxModel;
import lombok.Getter;

@Getter
public class ComboBoxAddress {
    private FlatComboBox<String> states;
    private FlatComboBox<String> municipality;

    public ComboBoxAddress() {
        municipality = new FlatComboBox<>();
        states = new FlatComboBox<>();
        states.setModel(new DefaultComboBoxModel<>(new String[]{"Seleccione el Estado"}));
        EstadosMx.getInstance().addItemsStates(states);
        initListeners();
    }

    private void initListeners() {
        states.addActionListener(x -> {
            if (states.getSelectedIndex() != 0) {
                municipality.removeAllItems();
                EstadosMx.getInstance().addItemsMunicipality(states.getSelectedItem().toString(), this.municipality);
            } else {
                municipality.removeAllItems();
            }
        });
    }
}
