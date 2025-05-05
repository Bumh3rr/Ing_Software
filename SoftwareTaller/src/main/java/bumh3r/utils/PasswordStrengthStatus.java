package bumh3r.utils;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.miginfocom.swing.MigLayout;

public class PasswordStrengthStatus extends JPanel {

    private JPasswordField passwordField;
    private DocumentListener documentListener;
    private JLabel label;
    private int type;
    private Status status = Status.NONE;

    public PasswordStrengthStatus() {
        init();
    }

    private void init() {
        putClientProperty(FlatClientProperties.STYLE, ""
                + "background:null");
        setLayout(new MigLayout("fill,insets 0", "3[100,fill,grow0][]", "[fill,grow 0]"));
        label = new JLabel("none");
        label.setVisible(false);
        add(new LabelStatus());
        add(label);
    }

    private Color getStrengthColor(int type) {
        if (type == 1) {
            return Color.decode("#FF4D4D");
        } else if (type == 2) {
            return Color.decode("#FFB04D");
        } else {
            return Color.decode("#58C359");
        }
    }

    public Status getStatus() {
        return status;
    }

    private void checkPassword(String password) {
        this.type = password.isEmpty() ? 0 : MethodUtil.checkPasswordStrength(password);
        if (type == 0) {
            status = Status.NONE;
            label.setText("none");
            label.setVisible(false);
        } else {
            label.setVisible(true);
            if (type == 1) {
                status = Status.DEBIL;
                label.setText("Demasiado débil");
            } else if (type == 2) {
                status = Status.MEDIO;
                label.setText("Medio");
            } else {
                status = Status.FUERTE;
                label.setText("Fuerte");
            }
            label.setForeground(getStrengthColor(type));
        }
        repaint();
    }

    public void initPasswordField(JPasswordField txt, Boolean borderColor) {
        if (documentListener == null) {
            documentListener = new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    checkPassword(String.valueOf(txt.getPassword()));
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    checkPassword(String.valueOf(txt.getPassword()));
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    checkPassword(String.valueOf(txt.getPassword()));
                }
            };
        }
        if (passwordField != null) {
            passwordField.getDocument().removeDocumentListener(documentListener);
        }
        txt.getDocument().addDocumentListener(documentListener);
        passwordField = txt;

        if (borderColor) {
            txt.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                    //...
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    //...
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    switch (status) {
                        case DEBIL ->
                            setBorderColorField(txt,
                                    "#FF3353", 2, true);

                        case MEDIO ->
                            setBorderColorField(txt,
                                    "#FCE13F", 2, true);

                        case FUERTE ->
                            setBorderColorField(txt,
                                    "#57FF33", 2, true);

                        default ->
                            setBorderColorDefaultField(txt);
                    }
                }
            });
        }
    }
    
    

    public static void setBorderColorField(JComponent field, String color, int borderWidth, Boolean showRevealButton) {
        field.putClientProperty(FlatClientProperties.STYLE, ""
                + "focusedBorderColor:fade(" + color + ",70%);"
                + "focusColor:fade(" + color + ",25%);"
                + "borderColor:fade(" + color + ",70%);"
                + "borderWidth:" + borderWidth + ";"
                + "showRevealButton:" + showRevealButton + ";"
                + "iconTextGap:10");
    }
    public static void setBorderColorDefaultField(JComponent field) {
        field.putClientProperty(FlatClientProperties.STYLE, ""
                + "showRevealButton:true;"
                + "iconTextGap:10");
    }

    public void resetPasswordField() {
        passwordField.setText("");
        setBorderColorDefaultField(passwordField);
    }

    private class LabelStatus extends JLabel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();
            int size = (int) (height * 0.3f);
            Graphics2D g2 = (Graphics2D) g.create();
            FlatUIUtils.setRenderingHints(g2);
            int gap = UIScale.scale(5);
            int w = (width - gap * 2) / 3;
            int y = (height - size) / 2;
            Color disableColor = Color.decode(FlatLaf.isLafDark() ? "#404040" : "#CECECE");
            if (type >= 1) {
                g2.setColor(getStrengthColor(1));
            } else {
                g2.setColor(disableColor);
            }
            FlatUIUtils.paintComponentBackground(g2, 0, y, w, size, 0, 999);
            if (type >= 2) {
                g2.setColor(getStrengthColor(2));
            } else {
                g2.setColor(disableColor);
            }
            FlatUIUtils.paintComponentBackground(g2, w + gap, y, w, size, 0, 999);
            if (type >= 3) {
                g2.setColor(getStrengthColor(3));
            } else {
                g2.setColor(disableColor);
            }
            FlatUIUtils.paintComponentBackground(g2, (w + gap) * 2, y, w, size, 0, 999);
            g2.dispose();
        }
    }

    public static enum Status {
        NONE, DEBIL, MEDIO, FUERTE
    }
}
