package bumh3r.components.card;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.JPanel;

public class Card<T> extends JPanel {

    public Card(T object, BiConsumer<T, Consumer<Void>> event1,BiConsumer<T, Consumer<Void>> event2) {}
    public Card(T object, BiConsumer<T, Consumer<Void>> event1) {}

}
