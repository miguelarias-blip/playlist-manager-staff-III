import java.util.Objects;

public record Jugador(String nombre, Posicion posicion) {

    public Jugador {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("nombre no puede ser nulo o en blanco");
        }
        Objects.requireNonNull(posicion, "posicion no puede ser null");
    }
}
