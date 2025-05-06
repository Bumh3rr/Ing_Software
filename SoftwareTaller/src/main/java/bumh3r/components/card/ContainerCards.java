package bumh3r.components.card;

import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.system.panel.Panel;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import javax.swing.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;

@Slf4j
public class ContainerCards<T> extends JScrollPane {
    private final Panel panelCards;
    @Getter
    private final JPanel panelPaginacion;
    private final ButtonControl buttonAnterior;
    private final ButtonControl buttonSiguiente;
    private final JLabel labelPagInfo;

    private final List<T> list = new ArrayList<>();
    private final Class<? extends Card> cardClass;
    private BiConsumer<T, Runnable> event1;
    private BiConsumer<T, Runnable> event2;

    private int longitud = 4;
    private int pagActual = 0;
    private int pagTotal;


    public ContainerCards(Class<? extends Card> cardClass, ResponsiveLayout responsiveLayout) {
        this.cardClass = cardClass;

        // Configuración del panel de tarjetas
        panelCards = new Panel();
        panelCards.setLayout(responsiveLayout);
        panelCards.putClientProperty(FlatClientProperties.STYLE, ""
                + "[light]background:@background;"
                + "[dark]background:#171717;"
                + "border:10,10,10,10;");
        super.setViewportView(panelCards);
        setBorder(BorderFactory.createEmptyBorder());
        configureScrollBars();

        // Configuración de la paginación
        buttonAnterior = new ButtonControl("< Anterior");
        buttonSiguiente = new ButtonControl("Siguiente >");
        labelPagInfo = new JLabel("Página 1 de 1");

        buttonAnterior.addActionListener(e -> paginaAnterior());
        buttonSiguiente.addActionListener(e -> paginaSiguiente());

        panelPaginacion = new JPanel(new MigLayout("insets 2"));
        panelPaginacion.putClientProperty(FlatClientProperties.STYLE, "background:null;");
        panelPaginacion.add(buttonAnterior);
        panelPaginacion.add(labelPagInfo, "gapx 5 5");
        panelPaginacion.add(buttonSiguiente);
    }

    private void configureScrollBars() {
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:0,0,0,0;");
        getHorizontalScrollBar().setUnitIncrement(10);
        getVerticalScrollBar().setUnitIncrement(10);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setColorScroll("@background");
    }

    public void setColorScroll(String color) {
        getVerticalScrollBar().putClientProperty(FlatClientProperties.STYLE, ""
                + "thumbInsets:0,0,0,0;"
                + "thumb:lighten($ScrollBar.thumb,10%);"
                + "pressedThumbColor:$ScrollBar.thumb;"
                + "width:7;"
                + "track:" + color + ";"
        );
    }

    public void cleanCards() {
        SwingUtilities.invokeLater(() -> {
            list.clear();
            panelCards.removeAll();
            pagActual = 0;
            pagTotal = 0;
            labelPagInfo.setText("Página 1 de 1");
            buttonAnterior.setEnabled(false);
            buttonSiguiente.setEnabled(false);
            panelCards.revalidate();
            panelCards.repaint();
        });
    }

    public void installDependent1(BiConsumer<T, Runnable> event) {
        this.event1 = event;
    }

    public void installDependent2(BiConsumer<T, Runnable> event) {
        this.event2 = event;
    }

    public void addItemsAll(List<T> items) {
        SwingUtilities.invokeLater(() -> {
            list.clear();
            list.addAll(items);
            calcularTotalPaginas();
            mostrarPagina(0);
        });
    }

    public void addItemOne(T item) {
        SwingUtilities.invokeLater(() -> {
            list.add(item);
            calcularTotalPaginas();
            if (longitud > panelCards.getComponents().length) {
                createdCard(item);
            }
        });
    }

    public List<T> getListItems() {
        return new ArrayList<>(list);
    }

    public Component[] getComponents() {
        return panelCards.getComponents();
    }

    public void updateUICards() {
        panelCards.panelCheckUI();
        panelCards.revalidate();
        panelCards.repaint();
    }

    private void calcularTotalPaginas() {
        pagTotal = (int) Math.ceil((double) list.size() / longitud);
        actualizarControlesPaginacion();
    }

    private void actualizarControlesPaginacion() {
        labelPagInfo.setText("Página " + (pagActual + 1) + " de " + pagTotal);
        buttonAnterior.setEnabled(pagActual > 0);
        buttonSiguiente.setEnabled(pagActual < pagTotal - 1);
    }

    private void mostrarPagina(int numeroPagina) {
        SwingUtilities.invokeLater(() -> {
            panelCards.removeAll();
            int inicio = numeroPagina * longitud;
            int fin = Math.min(inicio + longitud, list.size());
            for (int i = inicio; i < fin; i++) {
                T item = list.get(i);
                createdCard(item);
            }
            pagActual = numeroPagina;
            actualizarControlesPaginacion();
            panelCards.revalidate();
            panelCards.repaint();
        });
    }

    private void createdCard(T item) {
        try {
            Card card;
            if (event2!= null)
              card = cardClass.getDeclaredConstructor(item.getClass(), BiConsumer.class, BiConsumer.class).newInstance(item, event1, event2);
            else
                card = cardClass.getDeclaredConstructor(item.getClass(), BiConsumer.class).newInstance(item, event1);
            panelCards.add(card);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la tarjeta", e);
        }
    }

    private void paginaAnterior() {
        if (pagActual > 0) {
            mostrarPagina(pagActual - 1);
        }
    }

    private void paginaSiguiente() {
        if (pagActual < pagTotal - 1) {
            mostrarPagina(pagActual + 1);
        }
    }

    public class ButtonControl extends JButton {
        public ButtonControl(String text) {
            super.setText(text);
            putClientProperty(FlatClientProperties.STYLE, ""
                    + "arc:16;"
                    + "borderWidth:0;"
                    + "focusWidth:0;"
                    + "innerFocusWidth:0;"
                    + "margin:5,13,5,13;"
                    + "background:null");
        }
    }
}