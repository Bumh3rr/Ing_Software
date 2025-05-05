package bumh3r.components;

import bumh3r.components.label.LabelPublicaSans;
import bumh3r.fonts.FontPublicaSans;
import bumh3r.model.Dispositivo;
import bumh3r.model.Nota;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatPopupMenu;
import java.awt.Color;
import java.awt.Cursor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import static bumh3r.archive.PathResources.Icon.home;

public class Button_Overflow_Menu extends JToggleButton {
    private JLabel icon;
    private JPopupMenu popup;
    private Items items;
    private HashMap<Nota, LinkedList<Dispositivo>> map;

    public Button_Overflow_Menu(HashMap<Nota, LinkedList<Dispositivo>> map) {
        this.map = map;
        init();
    }

    private void init() {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        addActionListener(e -> showPopup());

        setLayout(new MigLayout("fill,insets 0", "[center]", "[center]"));
        putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:20;" +
                "margin:2,4,2,4;" +
                "borderWidth:0;" +
                "innerFocusWidth:0;" +
                "focusedBorderColor:null;" +
                "focusColor:null;" +
                "selectedBackground:darken(@background,5%);" +
                "[light]background:darken($Panel.background,1%);" +
                "[dark]background:lighten($Panel.background,1%);"
        );

        icon = new JLabel(new FlatSVGIcon(home + "ic_overflowMenu.svg"));

        add(icon, "grow 0");
        updateUI();
        revalidate();
    }

    private void showPopup() {
        if (popup == null) {
            popup = new FlatPopupMenu();

            popup.addPopupMenuListener(new PopupMenuListener() {
                @Override
                public void popupMenuWillBecomeVisible(PopupMenuEvent e) {

                }

                @Override
                public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {

                }

                @Override
                public void popupMenuCanceled(PopupMenuEvent e) {
                    closePop();
                }
            });

            items = new Items();
            popup.putClientProperty(FlatClientProperties.STYLE, "" +
                    "[light]background:#f9f7f6;" +
                    "[dark]background:lighten(@background,2%);"
            );
            popup.setBorder(BorderFactory.createEmptyBorder());
            popup.add(items);
        }
        if (this.isSelected()) {
            popup.show(this, (-186 + this.getWidth()), this.getHeight());
        }
    }

    private void closePop() {
        if (popup != null) {
            popup.setVisible(false);
            setSelected(false);
        }
    }


    private class Items extends JPanel {
        public Items() {
            init();
        }

        private void init() {
            setLayout(new MigLayout("wrap,fillx,insets 2 4,width 180", "[fill]", "[]0[]"));
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "arc:99;" +
                    "background:null;");

//            add(new ButtonMenu(ButtonMenu.Type.UPDATE, null, (e) -> {
//                closePop();
//            }), "gapy 1 1");
            add(new ButtonMenu(ButtonMenu.Type.STATUS, null, null), "gapy 1 1");
//            add(new ButtonMenu(ButtonMenu.Type.DELETE, null, null), "gapy 1 1");

            updateUI();
            revalidate();
        }
    }


    private class ButtonMenu extends JButton {
        private Type type;
        private Consumer<Object[]> c;
        private Object[] objects;

        public ButtonMenu(Type type, Object[] objects, Consumer<Object[]> c) {
            this.objects = objects;
            this.c = c;
            this.type = type;
            setLayout(new MigLayout("height 20,insets 3 3", "[]12[]"));
            putClientProperty(FlatClientProperties.STYLE, "" +
                    "arc:22;" +
                    "borderWidth:0;" +
                    "innerFocusWidth:0;" +
                    "focusedBorderColor:null;" +
                    "focusColor:null;" +
                    "[light]hoverBackground:darken(@background,3%);" +
                    "[dark]hoverBackground:lighten(@background,1%);" +
                    "pressedBackground:lighten(@background,2%);" +
                    "background:lighten(@background,4%);"
            );
            addActionListener(e -> this.c.accept(objects));

            add(createIcon());
            add(new LabelPublicaSans(type.getText()).style(
                    "[light]foreground:" + type.getColor() + ";" +
                            "[dark]foreground:#FFF;" +
                            "").type(FontPublicaSans.FontType.BOLD).size(12.5f).emptyBorder());
            updateUI();
            revalidate();
        }

        private JComponent createIcon() {
            if (type == Type.DELETE) {
                return new JLabel(new FlatSVGIcon(type.getUrl(), 0.8f).setColorFilter(new FlatSVGIcon.ColorFilter((x) -> Color.decode(type.getColor()))));
            } else {
                return new JLabel(new FlatSVGIcon(type.getUrl(), 0.9f));
            }
        }

        private enum Type {
            UPDATE("Actualizar Cliente", home + "ic_update.svg", "#0d0a08"),
            STATUS("Editar Status", home + "ic_status.svg", "#0d0a08"),
            DELETE("Eliminar", home + "ic_update.svg", "#dc2828");

            @Getter
            private String url;
            @Getter
            private String text;
            @Getter
            private String color;

            Type(String text, String url, String color) {
                this.color = color;
                this.text = text;
                this.url = url;
            }

        }

    }

}
