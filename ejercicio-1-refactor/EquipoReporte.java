public final class EquipoReporte {

    public String generar(Equipo equipo) {
        StringBuilder sb = new StringBuilder();

        sb.append(equipo.getNombre())
                .append(" - Titulos: ").append(equipo.getTitulos())
                .append(" - Total jugadores: ").append(equipo.totalJugadores())
                .append(System.lineSeparator());

        for (Jugador jugador : equipo.getTitulares()) {
            sb.append("Titular: ")
                    .append(jugador.nombre())
                    .append(" (").append(jugador.posicion().getEtiqueta()).append(")")
                    .append(System.lineSeparator());
        }

        for (Jugador jugador : equipo.getSuplentes()) {
            sb.append("Suplente: ")
                    .append(jugador.nombre())
                    .append(" (").append(jugador.posicion().getEtiqueta()).append(")")
                    .append(System.lineSeparator());
        }

        return sb.toString();
    }
}
