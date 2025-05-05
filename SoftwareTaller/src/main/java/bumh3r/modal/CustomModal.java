package bumh3r.modal;

import bumh3r.components.MyScrollPane;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import java.awt.Component;
import java.awt.Graphics;
import java.util.function.Consumer;
import javax.swing.*;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.miginfocom.swing.MigLayout;
import raven.modal.ModalDialog;
import raven.modal.Toast;
import raven.modal.component.Modal;

@Slf4j
@Builder
@RequiredArgsConstructor
public class CustomModal extends Modal {

    private final Component component;
    private final String title;
    private final String icon;
    private final String ID;
    private final Consumer consumer;
    private boolean buttonClose;

    private static JLabel titleLabel;
    private static  JLabel iconLabel;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        log.info("titleLabel getWidth: " + titleLabel.getWidth());
        log.info("titleLabel getHeight: " + titleLabel.getHeight());

        log.info("iconLabel getWidth: " + iconLabel.getWidth());
        log.info("iconLabel getHeight: " + iconLabel.getHeight());
    }


    public CustomModal(Component component, String title, String icon, String ID, Consumer<Boolean> consumer, boolean buttonClose) {
        this.component = component;
        this.title = title;
        this.icon = icon;
        this.ID = ID;
        this.consumer = consumer;
        this.buttonClose = buttonClose;
    }

    @Override
    public void installComponent() {
        setLayout(new MigLayout("wrap,fillx,insets 5 10 2 10","[fill]"));
        add(createHeader());
        add(new MyScrollPane(component));
    }

    protected Component createHeader() {
        JPanel panel = new JPanel(new MigLayout("fillx,insets 5 20 3 20,gapx 10"));
        panel.putClientProperty(FlatClientProperties.STYLE, "" +
                "background:null;");
        if (consumer != null) {
            panel.add(createBackButton());
        }
        if (icon != null){
            panel.add(createIcon());
        }
        panel.add(createTitle(),"push");
        if (buttonClose) {
            panel.add(createdButtonClose());
        }
        return panel;
    }

    protected Component createTitle() {
        titleLabel = new JLabel(title);
        titleLabel.putClientProperty(FlatClientProperties.STYLE, "" +
                "font:bold +5;");
        return titleLabel;
    }

    protected JComponent createBackButton() {
        JButton buttonClose = new JButton(new FlatSVGIcon("raven/modal/icon/back.svg", 0.4F));
        buttonClose.setFocusable(false);
        buttonClose.addActionListener((e) -> CallBack());
        buttonClose.putClientProperty("FlatLaf.style", "arc:999;margin:5,5,5,5;borderWidth:0;focusWidth:0;innerFocusWidth:0;background:null;");
        return buttonClose;
    }

    protected JComponent createdButtonClose() {
        JButton buttonClose = new JButton(new FlatSVGIcon("raven/modal/icon/close.svg", 0.4f));
        buttonClose.setFocusable(false);
        buttonClose.addActionListener(e -> {
            ModalDialog.closeModal(ID);
        });
        buttonClose.putClientProperty(FlatClientProperties.STYLE, "" +
                "arc:999;" +
                "margin:5,5,5,5;" +
                "borderWidth:0;" +
                "focusWidth:0;" +
                "innerFocusWidth:0;" +
                "background:null;");
        return buttonClose;
    }

    private void CallBack() {
        Toast.closeAll();
        consumer.accept(true);
    }

    protected JLabel createIcon() {
        FlatSVGIcon svgIcon = new FlatSVGIcon(icon, 0.4f).setColorFilter(new FlatSVGIcon.ColorFilter(color -> UIManager.getColor("Component.accentColor")));
        iconLabel = new JLabel(svgIcon);
        iconLabel.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:10,10,10,10,fade($Component.accentColor,50%),,999;" +
                "background:fade($Component.accentColor,10%);");
        return iconLabel;
    }
}
