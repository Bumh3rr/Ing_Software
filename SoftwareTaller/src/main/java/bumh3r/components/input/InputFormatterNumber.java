package bumh3r.components.input;

import com.formdev.flatlaf.extras.components.FlatFormattedTextField;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

public class InputFormatterNumber extends FlatFormattedTextField {

    public InputFormatterNumber(int limit) {
        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        integerFormat.setGroupingUsed(false);
        NumberFormatter formatter = new NumberFormatter(integerFormat);
        formatter.setValueClass(Integer.class);
        formatter.setAllowsInvalid(false);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        setFormatterFactory(new DefaultFormatterFactory(formatter));
        setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        setValue(null);
        putClientProperty("JTextField.clearCallback", (Runnable) () -> {
            setText("");
            setValue(null);
        });
    }

}
