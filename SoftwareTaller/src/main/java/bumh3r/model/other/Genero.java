package bumh3r.model.other;

public enum Genero {
    MASCULINO("Masculino"),
    FEMENINO("Femenino");
    private final String nombre;
    Genero(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
