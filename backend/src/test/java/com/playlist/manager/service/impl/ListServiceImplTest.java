package com.playlist.manager.service.impl;

import com.playlist.manager.domain.entity.Playlist;
import com.playlist.manager.domain.entity.Song;
import com.playlist.manager.dto.request.CreateListRequest;
import com.playlist.manager.dto.request.SongRequest;
import com.playlist.manager.dto.response.ListResponse;
import com.playlist.manager.exception.DuplicateListException;
import com.playlist.manager.exception.InvalidListNameException;
import com.playlist.manager.exception.ListNotFoundException;
import com.playlist.manager.mapper.ListMapper;
import com.playlist.manager.repository.ListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListServiceImplTest {

    @Mock
    private ListRepository repository;

    private ListMapper mapper;
    private ListServiceImpl service;

    @BeforeEach
    void setUp() {
        mapper = new ListMapper();
        service = new ListServiceImpl(repository, mapper);
    }

    @Test
    void create_shouldPersistAndReturnListResponse() {
        CreateListRequest request = new CreateListRequest(
                "Lista 1",
                "Lista de canciones de Spotify",
                List.of(new SongRequest("Song A", "Artista A", "Album A", "2024", "Pop"))
        );

        when(repository.existsByNombreIgnoreCase("Lista 1")).thenReturn(false);
        when(repository.save(any(Playlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ListResponse response = service.create(request);

        assertEquals("Lista 1", response.nombre());
        assertEquals("Lista de canciones de Spotify", response.descripcion());
        assertEquals(1, response.canciones().size());
        assertEquals("Song A", response.canciones().get(0).titulo());

        verify(repository).existsByNombreIgnoreCase("Lista 1");
        verify(repository).save(any(Playlist.class));
    }

    @Test
    void create_shouldThrowDuplicateWhenNameAlreadyExists() {
        CreateListRequest request = new CreateListRequest("Lista 1", null, null);

        when(repository.existsByNombreIgnoreCase("Lista 1")).thenReturn(true);

        assertThrows(DuplicateListException.class, () -> service.create(request));

        verify(repository, never()).save(any(Playlist.class));
    }

    @Test
    void getByName_shouldReturnListResponse() {
        Playlist playlist = new Playlist("Rock", "Clásicos del rock");
        playlist.addSong(new Song("Bohemian Rhapsody", "Queen", "A Night at the Opera", "1975", "Rock"));

        when(repository.findByNombreIgnoreCase("Rock")).thenReturn(Optional.of(playlist));

        ListResponse response = service.getByName("Rock");

        assertEquals("Rock", response.nombre());
        assertEquals(1, response.canciones().size());
        assertEquals("Queen", response.canciones().get(0).artista());
    }

    @Test
    void getByName_shouldThrowNotFoundWhenMissing() {
        when(repository.findByNombreIgnoreCase("Inexistente")).thenReturn(Optional.empty());

        assertThrows(ListNotFoundException.class, () -> service.getByName("Inexistente"));
    }

    @Test
    void create_shouldThrowInvalidListNameWhenBlank() {
        CreateListRequest request = new CreateListRequest("   ", null, null);

        assertThrows(InvalidListNameException.class, () -> service.create(request));

        verify(repository, never()).save(any(Playlist.class));
    }

    @Test
    void create_shouldThrowInvalidListNameWhenNull() {
        CreateListRequest request = new CreateListRequest(null, null, null);

        assertThrows(InvalidListNameException.class, () -> service.create(request));

        verify(repository, never()).save(any(Playlist.class));
    }

    @Test
    void create_shouldHandleNullSongsListGracefully() {
        CreateListRequest request = new CreateListRequest("Lista Vacía", "Sin canciones", null);

        when(repository.existsByNombreIgnoreCase("Lista Vacía")).thenReturn(false);
        when(repository.save(any(Playlist.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ListResponse response = service.create(request);

        assertEquals("Lista Vacía", response.nombre());
        assertEquals(0, response.canciones().size());

        verify(repository).save(any(Playlist.class));
    }

    @Test
    void getAll_shouldReturnAllLists() {
        Playlist rock = new Playlist("Rock", "Clásicos");
        rock.addSong(new Song("Song A", "Artista A", "Album A", "2024", "Rock"));
        Playlist pop = new Playlist("Pop", "Éxitos");

        when(repository.findAll()).thenReturn(List.of(rock, pop));

        List<ListResponse> result = service.getAll();

        assertEquals(2, result.size());
        assertEquals("Rock", result.get(0).nombre());
        assertEquals("Pop", result.get(1).nombre());
    }

    @Test
    void delete_shouldRemoveExistingList() {
        Playlist playlist = new Playlist("Rock", "Clásicos");

        when(repository.findByNombreIgnoreCase("Rock")).thenReturn(Optional.of(playlist));

        service.delete("Rock");

        verify(repository).delete(playlist);
    }

    @Test
    void delete_shouldThrowNotFoundWhenMissing() {
        when(repository.findByNombreIgnoreCase("Inexistente")).thenReturn(Optional.empty());

        assertThrows(ListNotFoundException.class, () -> service.delete("Inexistente"));

        verify(repository, never()).delete(any(Playlist.class));
    }
}
