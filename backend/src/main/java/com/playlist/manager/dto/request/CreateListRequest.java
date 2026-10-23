package com.playlist.manager.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateListRequest(
        @NotBlank(message = "El nombre de la lista no puede ser nulo, vacío o en blanco")
        String nombre,
        String descripcion,
        @Valid List<SongRequest> canciones
) {
    public CreateListRequest {
        nombre = nombre == null ? null : nombre.trim();
        if (canciones == null) {
            canciones = List.of();
        }
    }
}
