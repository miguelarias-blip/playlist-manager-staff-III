package com.playlist.manager.dto.response;

import java.util.List;

public record ListResponse(
        String nombre,
        String descripcion,
        List<SongResponse> canciones
) {
}
