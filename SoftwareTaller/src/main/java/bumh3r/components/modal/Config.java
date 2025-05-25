package bumh3r.components.modal;

import java.awt.Color;
import raven.modal.ModalDialog;
import raven.modal.option.BorderOption;
import raven.modal.option.Option;

public class Config {

    public static Option getModelShowModalFromNote() {
        Option option = ModalDialog.createOption()
                .setCloseOnPressedEscape(true)
                .setBackgroundClickType(Option.BackgroundClickType.BLOCK)
                .setAnimationEnabled(true)
                .setOpacity(0.25f);
        option.getBorderOption().setShadow(BorderOption.Shadow.DOUBLE_EXTRA_LARGE).setRound(50).setBorderWidth(0.1f).setBorderColor(Color.decode("#F5F5F5"));;
        return option;
    }
}
