public enum Posicion {
    DELANTERO("Delantero"),
    PORTERO("Portero"),
    DEFENSA("Defensa"),
    MEDIOCAMPO("Mediocampo");

    private final String etiqueta;

    Posicion(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
