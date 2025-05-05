package bumh3r.model;

public class Marcas {
    private static Marcas instance;
    private TypePhone[] listMarcasPhone;

    public static Marcas getInstance() {
        if (instance == null) {
            instance = new Marcas();
        }
        return instance;
    }

    public Marcas() {
        listMarcasPhone = TypePhone.values();
    }

    public TypePhone[] getListMarcasPhone() {
        return listMarcasPhone;
    }



    public enum TypePhone {
        APPLE("Apple", "ic_apple.svg"),
        OPPO("Oppo", "ic_oppo.svg"),
        SAMSUNG("Samsung", "ic_samsung.svg"),
        HUAWEI("Huawei", "ic_huawei.svg"),
        MOTOROLA("Motorola", "ic_moto.svg"),
        ALCATEL("Alcatel", "ic_alcatel.svg"),
        LG("LG", "ic_lg.svg"),
        SONY("Sony", "ic_sony.svg"),
        NOKIA("Nokia", "ic_nokia.svg"),
        ZTE("ZTE", "ic_zte.svg"),
        XIAOMI("Xiaomi", "ic_xiaomi.svg"),
        REDMI("Redmi", "ic_xiaomi.svg"),
        REALME("Realme", "ic_realme.svg"),
        HISENSE("Hisense", "ic_hisense.svg"),
        GOOGLE("Google", "ic_google.svg"),
        ONE_PLUS("One Plus", "ic_oneplus.svg"),
        HONOR("Honor", "ic_honor.svg"),
        VIVO("Vivo", "ic_vivo.svg"),
        TECNO_MOBILE("TecnoMobile", "ic_tecno.svg"),
        BLU("Blu", "ic_other.svg"),
        OTRO("Otro", "ic_other.svg");

        private final String value;
        private final String urlIcon;

        TypePhone(String value, String urlIcon) {
            this.value = value;
            this.urlIcon = urlIcon;
        }

        public String getValue() {
            return value;
        }

        public String getUrlIcon() {
            return urlIcon;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public static TypePhone fromNombre(String nombre) {
            for (TypePhone device : TypePhone.values()) {
                if (device.getValue().equalsIgnoreCase(nombre)) {
                    return device;
                }
            }
            throw new IllegalArgumentException("Marca no encontrada: " + nombre);
        }

    }
}
