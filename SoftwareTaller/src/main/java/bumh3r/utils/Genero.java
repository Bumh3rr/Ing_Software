package bumh3r.utils;

import javax.swing.DefaultComboBoxModel;

public class Genero {
    public static String[] generos = {"Masculino", "Femenino"};

    public static String getGenero(int index) {
        return generos[index];
    }

    public static DefaultComboBoxModel getDefaultModelComBox() {
        return new DefaultComboBoxModel<>(generos);
    }
}
