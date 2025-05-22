package bumh3r.drawer;

import bumh3r.archive.PathResources;
import bumh3r.system.form.AllFormsMain;
import bumh3r.system.form.Form;
import bumh3r.system.form.FormsManager;
import bumh3r.view.form.*;
import com.formdev.flatlaf.FlatClientProperties;
import java.util.Arrays;
import javax.swing.JComponent;
import raven.modal.drawer.DrawerPanel;
import raven.modal.drawer.data.Item;
import raven.modal.drawer.data.MenuItem;
import raven.modal.drawer.menu.MenuOption;
import raven.modal.drawer.menu.MenuStyle;
import raven.modal.drawer.renderer.DrawerStraightDotLineStyle;
import raven.modal.drawer.simple.SimpleDrawerBuilder;
import raven.modal.drawer.simple.footer.LightDarkButtonFooter;
import raven.modal.drawer.simple.footer.SimpleFooterData;
import raven.modal.drawer.simple.header.SimpleHeaderData;
import raven.modal.option.Option;

public class MyDrawerTallerBuilder extends SimpleDrawerBuilder {

    private final int SHADOW_SIZE = 12;

    public MyDrawerTallerBuilder() {
        super(createSimpleMenuOption());
        LightDarkButtonFooter lightDarkButtonFooter = (LightDarkButtonFooter) footer;
        lightDarkButtonFooter.setVisible(false);
    }

    @Override
    public SimpleHeaderData getSimpleHeaderData() {
        return new SimpleHeaderData()
                .setIcon(null)
                .setTitle("Taller de Celulares")
                .setDescription(null);
    }
    @Override
    public SimpleFooterData getSimpleFooterData() {
        return new SimpleFooterData()
                .setTitle("")
                .setDescription("");
    }

    public static MenuOption createSimpleMenuOption() {
        MenuOption simpleMenuOption = new MenuOption();
        MenuItem items[] = new MenuItem[]{
                new Item.Label("PRINCIPAL"),
                new Item("Inicio", "dashboard.svg", FormInicio.class),
                new Item("Notas", "forms.svg", FormNotes.class),
                new Item("Inventario", "ic_inventario.svg")
                        .subMenu("Productos", FormInventario.class)
                        .subMenu("Pedidos", FormPedidos.class)
                        .subMenu("Proveedor", FormProveedor.class),
                new Item("Ventas", "ic_sale.svg")
                        .subMenu("Registrar Venta", FormRegistroVenta.class)
                        .subMenu("Historial Ventas", FormHistorialVentas.class),
                new Item("Clientes", "ic_customer.svg", FormCliente.class),
                new Item("Empleados", "employee.svg", FormEmployee.class),
                new Item("Control Usuarios", "ic_users.svg", FormControlUsuario.class),
                new Item("Cerrar Sesión", "logout.svg")

        };
        simpleMenuOption.setMenuStyle(new MenuStyle() {

            @Override
            public void styleMenu(JComponent component) {
                component.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
            }
        });

        simpleMenuOption.getMenuStyle().setDrawerLineStyleRenderer(new DrawerStraightDotLineStyle());
        simpleMenuOption.setMenuItemAutoSelectionMode(MenuOption.MenuItemAutoSelectionMode.SELECT_SUB_MENU_LEVEL);
        simpleMenuOption.addMenuEvent((action,index)->{
            System.out.println("Drawer menu selected " + Arrays.toString(index));
            Class<?> itemClass = action.getItem().getItemClass();
            if (itemClass == null || !Form.class.isAssignableFrom(itemClass)) {
                action.consume();
                return;
            }
            Class<? extends Form> formClass = (Class<? extends Form>) itemClass;
            FormsManager.showFormMain(AllFormsMain.getForm(formClass));
        });


        simpleMenuOption.setMenus(items)
                .setBaseIconPath(PathResources.Icon.drawer)
                .setIconScale(0.45f);

        return simpleMenuOption;
    }

    @Override
    public int getDrawerWidth() {
        return 220 + SHADOW_SIZE;
    }

    @Override
    public int getDrawerCompactWidth() {
        return 80 + SHADOW_SIZE;
    }

    @Override
    public int getOpenDrawerAt() {
        return 1000;
    }

    @Override
    public Option getOption() {
        Option option = super.getOption();
        option.setOpacity(0.3f);
        option.getBorderOption()
                .setShadowSize(SHADOW_SIZE);
        return option;
    }

    @Override
    public boolean openDrawerAtScale() {
        return false;
    }

    @Override
    public void build(DrawerPanel drawerPanel) {
        drawerPanel.putClientProperty(FlatClientProperties.STYLE, getDrawerBackgroundStyle());
    }

    private static String getDrawerBackgroundStyle() {
        return ""
                + "border:8,0,0,0;"
                + "[light]background:tint($Panel.background,100%);"
                + "[dark]background:tint($Panel.background,5%);";
    }
}
