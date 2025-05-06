package bumh3r.archive;

import javax.swing.ImageIcon;

public class PathResources {

    public static class Icon {
        public static final String dispositivos = "icon/svg/dispositivos/";
        public static final String drawer = "icon/svg/drawer/";
        public static final String home = "icon/svg/home/";
        public static final String marcas = "icon/svg/marcas/";
        public static final String modal = "icon/svg/modal/";
        public static final String vectores = "icon/svg/vectores/";
        public static final String payment = "icon/svg/payment/";
    }

    public static class Img {
        public static final String repair = "/img/repair/";
        public static final String categorydevice = "/img/categorydevice/";
        public static final String typeDevices = "/img/typedevices/";
        public static final String defaults = "/img/defaults/";
        public static final String defaultsFromUrlRead = "img/defaults/";
    }

    public static class Font {
        public static final String url = "font/";
    }

    public static class Json{
        public static final String url = "json/";
    }

    public static class Default {
        public static final String LOGO_DEFAULT_TALLER = "img-default-user.png";
        public static final String LOGO_DEFAULT_TECHNICIAN = "img_default-tecnico-2.png";
        public static final String LOGO_DEFAULT_NOTE = "img-default-note.png";
        public static final String LOGO_DEFAULT_SALE_NOTE = "img_default_sale_note.png";
        public static final ImageIcon LOGO_DEFAULT_NOTE_IMAGE = new ImageIcon(PathResources.class.getResource(Img.defaults + LOGO_DEFAULT_NOTE));
        public static final ImageIcon LOGO_DEFAULT__SALE_NOTE_IMAGE = new ImageIcon(PathResources.class.getResource(Img.defaults + LOGO_DEFAULT_SALE_NOTE));
        public static final ImageIcon LOGO_DEFAULT_TECHNICIAN_IMAGE = new ImageIcon(PathResources.class.getResource(Img.defaults + LOGO_DEFAULT_TECHNICIAN));
        public static final ImageIcon LOGO_DEFAULT_TALLER_IMAGE = new ImageIcon(PathResources.class.getResource(Img.defaults + LOGO_DEFAULT_TALLER));

        public static final String LOGO_DEFAULT_EMPLOYEE_MAM = "img_default-tecnico-2.png";
        public static final String LOGO_DEFAULT_EMPLOYEE_WOMAN = "img_default-employee-woman.png";
        public static final ImageIcon ICON_DEFAULT_EMPLOYEE_MAN_IMAGE = new ImageIcon(PathResources.class.getResource(Img.defaults + LOGO_DEFAULT_EMPLOYEE_MAM));
        public static final ImageIcon ICON_DEFAULT_EMPLOYEE_WOMAN_IMAGE = new ImageIcon(PathResources.class.getResource(Img.defaults + LOGO_DEFAULT_EMPLOYEE_WOMAN));
    }
}