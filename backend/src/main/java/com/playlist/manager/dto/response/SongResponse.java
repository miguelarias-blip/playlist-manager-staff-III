package com.playlist.manager.dto.response;

public record SongResponse(
        String titulo,
        String artista,
        String album,
        String anno,
        String genero
) {
}
