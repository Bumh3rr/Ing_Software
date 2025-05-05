package bumh3r.notifications;

import java.awt.Component;
import java.awt.Container;
import java.awt.LayoutManager;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import raven.modal.Toast;
import raven.modal.layout.FrameModalLayout;
import raven.modal.layout.FrameToastLayout;
import raven.modal.option.Location;
import raven.modal.toast.ToastContainerLayer;
import raven.modal.toast.ToastPanel;
import raven.modal.toast.ToastPromise;
import raven.modal.toast.option.ToastLocation;
import raven.modal.toast.option.ToastOption;
import raven.modal.toast.option.ToastStyle;

public class Notify {
    private static Container contexto = null;
    private static Notify instance = null;

    private final Integer LAYER = JLayeredPane.POPUP_LAYER;
    private Map<RootPaneContainer, ToastContainerLayer> map;
    private Map<Toast.Type, ToastPanel.ThemesData> themesDataMap;
    private ToastOption defaultOption;
    private boolean reverseOrder;

    public static Notify getInstance() {
        if (instance == null) {
            instance = new Notify();
        }
        return instance;
    }

    public Notify() {
        map = new HashMap<>();
        initThemesData();
        defaultOption = ToastOption.getDefault();
        defaultOption.getLayoutOption()
                .setLocation(Location.CENTER, Location.TOP)
                .setAnimateDistance(0, -1f);
    }

    private void initThemesData() {
        themesDataMap = new HashMap<>();
        themesDataMap.put(Toast.Type.SUCCESS, new ToastPanel.ThemesData("raven/modal/icon/toast_success.svg", new String[]{"#1EA97C", "#1EA97C"}));
        themesDataMap.put(Toast.Type.INFO, new ToastPanel.ThemesData("raven/modal/icon/toast_info.svg", new String[]{"#3B82F6", "#3B82F6"}));
        themesDataMap.put(Toast.Type.WARNING, new ToastPanel.ThemesData("raven/modal/icon/toast_warning.svg", new String[]{"#CC8925", "#CC8925"}));
        themesDataMap.put(Toast.Type.ERROR, new ToastPanel.ThemesData("raven/modal/icon/toast_error.svg", new String[]{"#FF5757", "#FF5757"}));
        themesDataMap.put(Toast.Type.DEFAULT, new ToastPanel.ThemesData(null, new String[]{"#64748b", "#64748b"}));
        themesDataMap.put(null, new ToastPanel.ThemesData(null, new String[]{"#64748b", "#64748b"}));
    }

    public void showToast(Toast.Type type, String text) {
        Toast.show(contexto, type, text, configDefault());
    }
    public void showToast(ToastOption position,Toast.Type type, String text) {
        Toast.show(contexto, type, text, position);
    }

    public void showToast(Toast.Type type, JPanel x, String text) {
        Toast.show(x, type, text, configDefault());
    }

    public static ToastOption configDefault() {
        ToastOption option = Toast.createOption();
        option.setAnimationEnabled(true)
                .setPauseDelayOnHover(true)
                .setAutoClose(true)
                .setCloseOnClick(true)
                .setLayoutOption(ToastLocation.from(Location.TRAILING, Location.TOP).getLayout())
                .getStyle().setBackgroundType(ToastStyle.BackgroundType.DEFAULT)
                .setBorderType(ToastStyle.BorderType.LEADING_LINE)
                .setShowLabel(true)
                .setIconSeparateLine(true)
                .setShowCloseButton(true)
                .setPromiseLabel("");
        return option;
    }
    public static ToastOption optionBottom() {
        ToastOption option = Toast.createOption();
        option.setAnimationEnabled(true)
                .setPauseDelayOnHover(true)
                .setAutoClose(true)
                .setCloseOnClick(false)
                .setLayoutOption(ToastLocation.from(Location.TRAILING, Location.BOTTOM).getLayout())
                .getStyle().setBackgroundType(ToastStyle.BackgroundType.GRADIENT)
                .setBorderType(ToastStyle.BorderType.LEADING_LINE)
                .setShowLabel(false)
                .setIconSeparateLine(true)
                .setShowCloseButton(false)
                .setPromiseLabel("");
        return option;
    }
    public ToastOption getSelectedOptionTop() {
        ToastOption option = Toast.createOption();
        option.setAnimationEnabled(true)
                .setPauseDelayOnHover(true)
                .setAutoClose(true)
                .setCloseOnClick(true)
                .setLayoutOption(ToastLocation.from(Location.CENTER, Location.TOP).getLayout())
                .getStyle().setBackgroundType(ToastStyle.BackgroundType.DEFAULT)
                .setBorderType(ToastStyle.BorderType.LEADING_LINE)
                .setShowLabel(true)
                .setIconSeparateLine(true)
                .setShowCloseButton(true)
                .setPromiseLabel("");
        return option;
    }

    public static void showPromise(Component owner, String message, ToastOption option, ToastPromise promise) {
        show(owner, null, message, option, promise);
    }

    private static void show(Component owner, Toast.Type type, Object object, ToastOption option, ToastPromise promise) {
        ToastPanel.ThemesData themesData = getInstance().themesDataMap.get(type);
        RootPaneContainer rootPaneContainer = getRootPaneContainer(owner);
        ToastContainerLayer toastContainerLayer = getInstance().getToastContainerLayered(rootPaneContainer);
        String message = object instanceof String ? object.toString() : null;
        ToastPanel toastPanel = new ToastPanel(toastContainerLayer, new ToastPanel.ToastData(type, option, themesData, message));
        if (object instanceof Component) {
            toastContainerLayer.add(toastPanel.createToastCustom((Component) object), 0);
        } else if (promise != null) {
            toastContainerLayer.add(toastPanel.createToastPromise(promise), 0);
        } else {
            toastContainerLayer.add(toastPanel.createToast(), 0);
        }
        if (rootPaneContainer.getRootPane().getComponentOrientation().isLeftToRight() != toastPanel.getComponentOrientation().isLeftToRight()) {
            toastPanel.applyComponentOrientation(rootPaneContainer.getRootPane().getComponentOrientation());
        }
        toastContainerLayer.setVisible(true);
        toastPanel.start();
        toastContainerLayer.revalidate();
        toastContainerLayer.addToastPanel(toastPanel);
    }

    public static void install(Container container) {
        contexto = container;
    }

    public void setContainer(Container container) {
        contexto = container;
    }

    public static void closeAll() {
        Toast.closeAll();
    }

    protected static RootPaneContainer getRootPaneContainer(Component component) {
        if (component == null) {
            throw new IllegalArgumentException("parent component must not null");
        }
        if (component instanceof JFrame || component instanceof JDialog || component instanceof JInternalFrame) {
            return (RootPaneContainer) component;
        }
        return getRootPaneContainer(component.getParent());
    }

    private ToastContainerLayer getToastContainerLayered(RootPaneContainer rootPaneContainer) {
        ToastContainerLayer toastContainerLayer;
        if (map.containsKey(rootPaneContainer)) {
            toastContainerLayer = map.get(rootPaneContainer);
        } else {
            // get layeredPane from window
            JLayeredPane windowLayeredPane = rootPaneContainer.getLayeredPane();

            // create new toast container layered
            toastContainerLayer = createToastContainerLayered(rootPaneContainer);

            // add toast container layered to window layeredPane
            windowLayeredPane.add(toastContainerLayer, LAYER);

            // check layout right to left
            if (rootPaneContainer.getRootPane().getComponentOrientation().isLeftToRight() != toastContainerLayer.getComponentOrientation().isLeftToRight()) {
                toastContainerLayer.applyComponentOrientation(rootPaneContainer.getRootPane().getComponentOrientation());
            }

            // set custom layout to window layeredPane
            LayoutManager layout = windowLayeredPane.getLayout();
            FrameToastLayout frameToastLayout = new FrameToastLayout(toastContainerLayer);
            if (layout != null && layout instanceof FrameModalLayout) {
                ((FrameModalLayout) layout).setOtherComponent(toastContainerLayer, frameToastLayout);
            } else {
                // install toast layout
                windowLayeredPane.setLayout(frameToastLayout);
            }

            // store toast container layered to map
            map.put(rootPaneContainer, toastContainerLayer);

            // install property to remove the root pane map after windows removed
            toastContainerLayer.setPropertyData(installProperty(rootPaneContainer.getRootPane()));
        }
        return toastContainerLayer;
    }

    private Object installProperty(JRootPane rootPane) {
        PropertyChangeListener propertyChangeListener = e -> {
            if (e.getNewValue() == null && e.getOldValue() instanceof RootPaneContainer) {
                uninstall((RootPaneContainer) e.getOldValue());
            }
        };
        rootPane.addPropertyChangeListener("ancestor", propertyChangeListener);
        return propertyChangeListener;
    }

    private void uninstall(RootPaneContainer rootPaneContainer) {
        if (map.containsKey(rootPaneContainer)) {
            ToastContainerLayer toastContainerLayer = map.get(rootPaneContainer);
            JLayeredPane windowLayeredPane = rootPaneContainer.getLayeredPane();
            rootPaneContainer.getRootPane().removePropertyChangeListener("ancestor", (PropertyChangeListener) toastContainerLayer.getPropertyData());

            // uninstall layout
            LayoutManager oldLayout = windowLayeredPane.getLayout();
            if (oldLayout != null) {
                if (oldLayout instanceof FrameToastLayout) {
                    windowLayeredPane.setLayout(null);
                } else if (oldLayout instanceof FrameModalLayout) {
                    ((FrameModalLayout) oldLayout).setOtherComponent(null, null);
                }
            }

            // remove
            map.remove(rootPaneContainer);
            windowLayeredPane.remove(toastContainerLayer);
            toastContainerLayer.remove();
        }
    }

    private ToastContainerLayer createToastContainerLayered(RootPaneContainer rootPaneContainer) {
        ToastContainerLayer layeredPane = new ToastContainerLayer();
        layeredPane.setVisible(false);
        return layeredPane;
    }

}

