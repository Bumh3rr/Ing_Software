package bumh3r.components.input;

import javax.swing.JFormattedTextField;

public class InputFormatterNumber extends JFormattedTextField {

    public InputFormatterNumber() {
        setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
        setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,###.00"))));
    }

}
