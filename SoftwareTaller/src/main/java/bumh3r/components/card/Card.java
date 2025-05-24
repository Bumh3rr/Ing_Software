package bumh3r.components.card;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javax.swing.JPanel;

public class Card<T> extends JPanel {

    public Card(T object, BiConsumer<T, Runnable> event1,BiConsumer<T, Runnable> event2) {}
    public Card(T object, BiConsumer<T, Runnable> event1) {}

}
