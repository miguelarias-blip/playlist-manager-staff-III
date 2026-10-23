import java.util.List;
import java.util.Objects;

public final class Equipo {

    private final String nombre;
    private final String estadio;
    private final int titulos;
    private final List<Jugador> titulares;
    private final List<Jugador> suplentes;

    public Equipo(String nombre,
                  String estadio,
                  int titulos,
                  List<Jugador> titulares,
                  List<Jugador> suplentes) {
        this.nombre = validarTextoNoBlanco(nombre, "nombre");
        this.estadio = validarTextoNoBlanco(estadio, "estadio");
        if (titulos < 0) {
            throw new IllegalArgumentException("titulos no puede ser negativo: " + titulos);
        }
        this.titulos = titulos;
        this.titulares = List.copyOf(Objects.requireNonNull(titulares, "titulares no puede ser null"));
        this.suplentes = List.copyOf(Objects.requireNonNull(suplentes, "suplentes no puede ser null"));
    }

    public String getNombre() {
        return nombre;
    }

    public String getEstadio() {
        return estadio;
    }

    public int getTitulos() {
        return titulos;
    }

    public List<Jugador> getTitulares() {
        return titulares;
    }

    public List<Jugador> getSuplentes() {
        return suplentes;
    }

    public int totalJugadores() {
        return titulares.size() + suplentes.size();
    }

    private static String validarTextoNoBlanco(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException(campo + " no puede ser nulo o en blanco");
        }
        return valor;
    }

    public static void main(String[] args) {
        Equipo equipo = new Equipo(
                "Equipo A",
                "Estadio Central",
                5,
                List.of(
                        new Jugador("Juan", Posicion.DELANTERO),
                        new Jugador("Pedro", Posicion.PORTERO),
                        new Jugador("Luis", Posicion.DEFENSA)
                ),
                List.of(
                        new Jugador("Mario", Posicion.MEDIOCAMPO),
                        new Jugador("Ana", Posicion.DELANTERO)
                )
        );

        System.out.print(new EquipoReporte().generar(equipo));
    }
}
