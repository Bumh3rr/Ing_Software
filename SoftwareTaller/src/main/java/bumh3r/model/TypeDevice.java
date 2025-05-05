package bumh3r.model;

public class TypeDevice {
    private static TypeDevice instance;
    private Type[] listMarcasPhone;

    public static TypeDevice getInstance() {
        if (instance == null) {
            instance = new TypeDevice();
        }
        return instance;
    }

    public TypeDevice() {
        listMarcasPhone = Type.values();
    }

    public Type[] getListTypeDevice() {
        return listMarcasPhone;
    }

    public enum Type {
        TELEFONO("Teléfono Móvil", "img_deviceAndroid2.png"),
        TABLET("Tablet", "img_deviceTablet.png"),
        LAPTOP("Laptop & Computadora", "img_deviceComputer.png"),
        CONSOLA("Consola", "img_deviceConsola.png"),
        TELEVISION("Televisión", "img_deviceTelevision.png"),
        CAMARA("Cámara", "img_deviceCamara.png"),
        OTHER("Otro", "img_deviceOther.png");

        private final String name;
        private final String urlIcon;

        Type(String name, String urlIcon) {
            this.name = name;
            this.urlIcon = urlIcon;
        }

        public String getName() {
            return name;
        }

        public String getUrlIcon() {
            return urlIcon;
        }

        @Override
        public String toString() {
            return name;
        }

        public static Type fromNombre(String nombre) {
            for (Type device : Type.values()) {
                if (device.getName().equalsIgnoreCase(nombre)) {
                    return device;
                }
            }
            throw new IllegalArgumentException("Tipo de dispositivo no encontrado: " + nombre);
        }
    }
}
