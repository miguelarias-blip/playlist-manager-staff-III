package com.playlist.manager.dto.request;

public record SongRequest(
        String titulo,
        String artista,
        String album,
        String anno,
        String genero
) {
    public SongRequest {
        titulo = titulo == null ? "" : titulo;
        artista = artista == null ? "" : artista;
        album = album == null ? "" : album;
        anno = anno == null ? "" : anno;
        genero = genero == null ? "" : genero;
    }
}
