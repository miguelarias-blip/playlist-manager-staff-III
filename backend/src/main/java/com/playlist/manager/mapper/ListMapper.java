package com.playlist.manager.mapper;

import com.playlist.manager.domain.entity.Playlist;
import com.playlist.manager.domain.entity.Song;
import com.playlist.manager.dto.request.CreateListRequest;
import com.playlist.manager.dto.request.SongRequest;
import com.playlist.manager.dto.response.ListResponse;
import com.playlist.manager.dto.response.SongResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ListMapper {

    public Playlist toEntity(CreateListRequest request) {
        Playlist playlist = new Playlist(request.nombre(), request.descripcion());
        if (request.canciones() != null) {
            for (SongRequest songRequest : request.canciones()) {
                playlist.addSong(toSongEntity(songRequest));
            }
        }
        return playlist;
    }

    public ListResponse toResponse(Playlist playlist) {
        List<SongResponse> canciones = playlist.getCanciones().stream()
                .map(this::toSongResponse)
                .toList();
        return new ListResponse(playlist.getNombre(), playlist.getDescripcion(), canciones);
    }

    private Song toSongEntity(SongRequest request) {
        return new Song(
                request.titulo(),
                request.artista(),
                request.album(),
                request.anno(),
                request.genero()
        );
    }

    private SongResponse toSongResponse(Song song) {
        return new SongResponse(
                song.getTitulo(),
                song.getArtista(),
                song.getAlbum(),
                song.getAnno(),
                song.getGenero()
        );
    }
}
