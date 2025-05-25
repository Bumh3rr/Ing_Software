package bumh3r.view.modal;

import bumh3r.components.card.CardRepairInfo;
import bumh3r.components.card.ContainerCards;
import bumh3r.components.resposive.ResponsiveLayout;
import bumh3r.model.Reparacion;
import bumh3r.system.panel.Panel;
import java.awt.Dimension;
import java.util.List;
import java.util.function.BiConsumer;
import net.miginfocom.swing.MigLayout;

public class PanelModalInfoReparacion extends Panel {
    private ContainerCards<Reparacion> containerCards;

    public void installEventUpdateStatusCardRepair(BiConsumer<Reparacion, Runnable> event) {
        containerCards.installDependent1(event);
    }

    public void setValue(List<Reparacion> reparaciones) {
        containerCards.addItemsAll(reparaciones);
    }

    public PanelModalInfoReparacion() {
        initComponents();
        init();
    }

    private void initComponents() {
        containerCards = new ContainerCards<>(CardRepairInfo.class, new ResponsiveLayout(ResponsiveLayout.JustifyContent.CENTER, new Dimension(350, -1), 10, 10));
        containerCards.setLongitud(10000);
    }

    private void init() {
        setLayout(new MigLayout("wrap,fillx,insets 0,w 650:900,h 200:500:1000", "[fill]","[fill]"));
        add(containerCards, "grow,push");
    }
}
