package bumh3r.view.modal;

import bumh3r.fonts.FontPublicaSans;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.components.FlatTextPane;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import net.miginfocom.swing.MigLayout;
import raven.modal.component.Modal;
import raven.modal.component.ModalBorderAction;
import raven.modal.listener.ModalCallback;
import raven.modal.listener.ModalController;
import static bumh3r.archive.PathResources.Icon.modal;


public class ModalToas extends Modal implements ModalBorderAction {

    public static final String ID = ModalToas.class.getName();
    private JLabel logo;
    private JLabel title;
    private FlatTextPane message;
    private JButton btnClose;
    private JButton btnAccept;
    private JButton btnOther;

    private final ModalCallback callback;
    private final Type type;

    public ModalToas(Type type, String title, String message, ModalCallback callback) {
        this.callback = callback;
        this.type = type;
        initComponents();
        initLisners();
        initLogo(type);
        initTexts(title, message);
    }

    private void initTexts(String title, String message) {
        this.title.setText(title);
        this.message.setText(message);
    }

    private void initComponents() {
        btnClose = new JButton() {
            @Override
            public void updateUI() {
                putClientProperty(FlatClientProperties.STYLE, "" +
                        "arc:19;" +
                        "borderColor:#e0e0e0;" +
                        "borderWidth:1.8;" +
                        "background:null;");
                setIcon(new FlatSVGIcon("raven/modal/icon/close.svg", 0.4F).setColorFilter(new FlatSVGIcon.ColorFilter((x) -> Color.getColor("#2b2b2b"))));
                super.updateUI();
            }
        };

        btnOther = new JButton() {
            @Override
            public void updateUI() {
                setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.MEDIUM, 14f));
                putClientProperty(FlatClientProperties.STYLE, "" +
                        "arc:19;" +
                        "borderColor:#e0e0e0;" +
                        "borderWidth:1.8;" +
                        "background:null;");
                super.updateUI();
            }
        };

        btnAccept = new JButton("Aceptar") {
            @Override
            public void updateUI() {
                super.updateUI();
                setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.MEDIUM, 14f));
                putClientProperty(FlatClientProperties.STYLE, "" +
                        "borderWidth:1.8;" +
                        "innerFocusWidth:3;" +
                        "borderColor:saturate(" + type.getValue() + ",33%);" +
                        "focusedBorderColor:fade(" + type.getValue() + ",70%);" +
                        "focusColor:fade(" + type.getValue() + ",25%);" +
                        "background:" + type.getValue() + ";" +
                        "foreground:#FFF;");
            }
        };

        message = new FlatTextPane() {
            @Override
            public void updateUI() {
                setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.MEDIUM, 14f));
                putClientProperty(FlatClientProperties.STYLE, ""
                        + "background:null;"
                        + "[light]foreground:lighten(@foreground,30%);"
                        + "[dark]foreground:darken(@foreground,30%);");
                super.updateUI();
            }
        };
        message.setEditable(false);
        message.setBorder(BorderFactory.createEmptyBorder());

        // Centrar texto horizontalmente
        StyledDocument doc = message.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);

        title = new JLabel();
        title.setForeground(Color.getColor("#2b2b2b"));
        title.setFont(FontPublicaSans.getInstance().getFont(FontPublicaSans.FontType.BOLD, 24f));
        logo = new JLabel();

    }

    private void initLogo(Type type) {
        switch (type) {
            case SUCCESS -> changeLogo(modal + "ic_success.svg", Type.SUCCESS.getValue());
            case WARNING -> changeLogo(modal + "ic_warning.svg", Type.WARNING.getValue());
            case ERROR -> changeLogo(modal + "ic_error.svg", Type.ERROR.getValue());
            case INFO -> changeLogo(modal + "ic_info.svg", Type.INFO.getValue());
        }
    }

    private void changeLogo(String logoUrl, String color) {
        logo.putClientProperty(FlatClientProperties.STYLE, "" +
                "border:10,10,10,10,,,25;" +
                "background:fade(" + color + ",17%);");
        logo.setIcon(new FlatSVGIcon(logoUrl, 1.5F));
    }

    private void initLisners() {
        btnClose.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnClose.setIcon(new FlatSVGIcon("raven/modal/icon/close.svg", 0.4F).setColorFilter(new FlatSVGIcon.ColorFilter((x) -> UIManager.getColor("Component.accentColor"))));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btnClose.setIcon(new FlatSVGIcon("raven/modal/icon/close.svg", 0.4F).setColorFilter(new FlatSVGIcon.ColorFilter((x) -> Color.getColor("#2b2b2b"))));
            }
        });
        btnClose.addActionListener(e -> {
            ModalBorderAction.getModalBorderAction(btnClose).doAction(CLOSE_OPTION);

        });

        btnOther.addActionListener(e ->
                ModalBorderAction.getModalBorderAction(btnOther).doAction(OTHER));

        btnAccept.addActionListener(e ->
                ModalBorderAction.getModalBorderAction(btnAccept).doAction(ACCEPT_OPTION));
    }

    @Override
    public void installComponent() {
        setLayout(new MigLayout("wrap,fillx,hidemode 3,width 400:450:650,insets 10 10 25 10", "[fill,center]", "[]0[]"));
        add(btnClose, "grow 0,al trail");
        add(logo, "grow 0");
        add(title, "grow 0,gapy 5");
        add(message, "grow 0,w 370!,gapy 10");
        insertsButtons();
        updateUI();
        revalidate();
    }

    private void insertsButtons() {
        switch (type) {
            case SUCCESS, INFO -> {
                add(btnAccept, "grow,gapy 35,w 350!,h 35!");
                add(btnOther, "grow,gapy 15,w 350!,h 35!");
                btnOther.setText("Cerrar");
            }
            case WARNING -> {
                add(btnAccept, "grow,gapy 35,w 350!,h 35!");
                add(btnOther, "grow,gapy 15,w 350!,h 35!");
                btnOther.setText("Cancelar");
            }

            default -> add(btnAccept, "grow,gapy 35,w 350!,h 35!");
        }
    }

    private ModalController createController() {
        return new ModalController(this) {
            public void close() {
                ModalToas.this.getController().getModalContainer().closeModal();
            }
        };
    }

    @Override
    public void doAction(int action) {
        if (this.callback == null) {
            this.getController().getModalContainer().closeModal();
        } else {
            ModalController controller = this.createController();
            this.callback.action(controller, action);
            if (!controller.getConsume()) {
                this.getController().getModalContainer().closeModal();
            }
        }
    }

    public enum Type {
        SUCCESS("#38cb6e"),
        ERROR("#ed4949"),
        WARNING("#FAD738"),
        INFO("#2f6cff");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    public static int OTHER = 0;
    public static int ACCEPT_OPTION = 1;
    public static int CLOSE_OPTION = 2;
}
