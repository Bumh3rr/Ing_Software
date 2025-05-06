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
    private static Notify instance;
    private Container contexto;

    public static Notify getInstance() {
        if (instance == null) {
            instance = new Notify();
        }
        return instance;
    }

    public static void install(JFrame contexto) {
        getInstance().contexto = contexto;
    }

    public static void closeAll() {
        Toast.closeAll();
    }

    public void showToast(Toast.Type type, String text) {
        Toast.show(contexto, type, text, configDefault());
    }

    public void showToast(ToastOption position, Toast.Type type, String text) {
        Toast.show(contexto, type, text, position);
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

    public static void showPromise(String message, ToastOption option, ToastPromise promise) {
        Toast.showPromise(getInstance().contexto, message, option, promise);
    }

    public static void showPromise(String message, ToastPromise promise) {
        Toast.showPromise(getInstance().contexto, message, configDefault(), promise);
    }
}

