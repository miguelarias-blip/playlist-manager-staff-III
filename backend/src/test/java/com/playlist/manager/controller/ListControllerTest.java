package com.playlist.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playlist.manager.dto.request.CreateListRequest;
import com.playlist.manager.dto.request.SongRequest;
import com.playlist.manager.dto.response.ListResponse;
import com.playlist.manager.dto.response.SongResponse;
import com.playlist.manager.exception.DuplicateListException;
import com.playlist.manager.exception.ListNotFoundException;
import com.playlist.manager.service.ListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ListController.class)
@AutoConfigureMockMvc(addFilters = false)
class ListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ListService listService;

    @Test
    void create_shouldReturn201WithEncodedLocationHeader() throws Exception {
        CreateListRequest request = new CreateListRequest(
                "Lista 1",
                "Lista de canciones de Spotify",
                List.of(new SongRequest("Song A", "Artista A", "Album A", "2024", "Pop"))
        );
        ListResponse response = new ListResponse(
                "Lista 1",
                "Lista de canciones de Spotify",
                List.of(new SongResponse("Song A", "Artista A", "Album A", "2024", "Pop"))
        );

        when(listService.create(any(CreateListRequest.class))).thenReturn(response);

        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString("/lists/Lista%201")))
                .andExpect(jsonPath("$.nombre").value("Lista 1"))
                .andExpect(jsonPath("$.canciones[0].titulo").value("Song A"));
    }

    @Test
    void create_shouldReturn400WhenNombreIsBlank() throws Exception {
        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"   \",\"descripcion\":\"x\",\"canciones\":[]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn400WhenNombreIsMissing() throws Exception {
        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descripcion\":\"x\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByName_shouldReturn200WhenExists() throws Exception {
        ListResponse response = new ListResponse(
                "Rock",
                "Clásicos",
                List.of(new SongResponse("Bohemian Rhapsody", "Queen", "A Night at the Opera", "1975", "Rock"))
        );

        when(listService.getByName("Rock")).thenReturn(response);

        mockMvc.perform(get("/lists/Rock"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Rock"))
                .andExpect(jsonPath("$.canciones[0].artista").value("Queen"));
    }

    @Test
    void getByName_shouldReturn404WhenMissing() throws Exception {
        when(listService.getByName("Inexistente")).thenThrow(new ListNotFoundException("Inexistente"));

        mockMvc.perform(get("/lists/Inexistente"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByName_shouldDecodeSpecialCharactersInPath() throws Exception {
        ListResponse response = new ListResponse("Lista Con Espacios", "desc", List.of());

        when(listService.getByName("Lista Con Espacios")).thenReturn(response);

        mockMvc.perform(get("/lists/{name}", "Lista Con Espacios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Lista Con Espacios"));
    }

    @Test
    void getAll_shouldReturn200WithLists() throws Exception {
        when(listService.getAll()).thenReturn(List.of(
                new ListResponse("Rock", "Clásicos", List.of()),
                new ListResponse("Pop", "Éxitos", List.of())
        ));

        mockMvc.perform(get("/lists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Rock"))
                .andExpect(jsonPath("$[1].nombre").value("Pop"));
    }

    @Test
    void create_shouldReturn400WhenDuplicateName() throws Exception {
        when(listService.create(any(CreateListRequest.class)))
                .thenThrow(new DuplicateListException("Lista 1"));

        mockMvc.perform(post("/lists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nombre\":\"Lista 1\",\"descripcion\":\"x\",\"canciones\":[]}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Duplicate list"));
    }

    @Test
    void delete_shouldReturn204WhenExists() throws Exception {
        mockMvc.perform(delete("/lists/Rock"))
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_shouldReturn404WhenMissing() throws Exception {
        doThrow(new ListNotFoundException("Inexistente")).when(listService).delete("Inexistente");

        mockMvc.perform(delete("/lists/Inexistente"))
                .andExpect(status().isNotFound());
    }
}
