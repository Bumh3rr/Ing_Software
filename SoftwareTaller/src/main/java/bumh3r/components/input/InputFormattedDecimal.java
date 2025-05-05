package bumh3r.components.input;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatFormattedTextField;
import java.text.DecimalFormat;
import javax.swing.JLabel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class InputFormattedDecimal extends FlatFormattedTextField {
    public static DecimalFormat decimalFormat = new DecimalFormat("#,###.00");

    public InputFormattedDecimal(double limit) {
        putClientProperty(FlatClientProperties.TEXT_FIELD_LEADING_COMPONENT, new JLabel("$") {
            @Override
            public void updateUI() {
                putClientProperty(FlatClientProperties.STYLE, ""
                        + "border:0,8,0,0;"
                        + "[light]foreground:lighten(@foreground,30%);"
                        + "[dark]foreground:darken(@foreground,30%);");
                super.updateUI();
            }
        });
        putClientProperty(FlatClientProperties.STYLE, ""
                + "iconTextGap:10;"
                + "showClearButton:true");
        putClientProperty("JTextField.clearCallback", (Runnable) () -> {
            // Custom logic when the clear button is pressed
            setText("");
            setValue(null);
        });

        setPlaceholderText("0.00");
        setFormatterFactory(new DefaultFormatterFactory(createNumberFormatter(limit)));
        addActionListener(e -> {
            String text = getText();
            if (text == null || text.isEmpty()) {
                setValue(null);
            }
        });
    }

    public NumberFormatter createNumberFormatter(double limit) {
        NumberFormatter decimalFormatter = new NumberFormatter(decimalFormat);
        decimalFormatter.setValueClass(Double.class);
        decimalFormatter.setMinimum(null);
        decimalFormatter.setMaximum(limit);
        decimalFormatter.setAllowsInvalid(false);
        return decimalFormatter;
    }

}
