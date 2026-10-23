package com.playlist.manager.service.impl;

import com.playlist.manager.domain.entity.Playlist;
import com.playlist.manager.dto.request.CreateListRequest;
import com.playlist.manager.dto.response.ListResponse;
import com.playlist.manager.exception.DuplicateListException;
import com.playlist.manager.exception.InvalidListNameException;
import com.playlist.manager.exception.ListNotFoundException;
import com.playlist.manager.mapper.ListMapper;
import com.playlist.manager.repository.ListRepository;
import com.playlist.manager.service.ListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListServiceImpl implements ListService {

    private final ListRepository repository;
    private final ListMapper mapper;

    public ListServiceImpl(ListRepository repository, ListMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public ListResponse create(CreateListRequest request) {
        String nombre = request.nombre();
        if (!isValidName(nombre)) {
            throw new InvalidListNameException(nombre);
        }
        if (repository.existsByNombreIgnoreCase(nombre)) {
            throw new DuplicateListException(nombre);
        }
        Playlist saved = repository.save(mapper.toEntity(request));
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListResponse> getAll() {
        return repository.findAll().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ListResponse getByName(String listName) {
        Playlist playlist = repository.findByNombreIgnoreCase(listName)
                .orElseThrow(() -> new ListNotFoundException(listName));
        return mapper.toResponse(playlist);
    }

    @Override
    @Transactional
    public void delete(String listName) {
        Playlist playlist = repository.findByNombreIgnoreCase(listName)
                .orElseThrow(() -> new ListNotFoundException(listName));
        repository.delete(playlist);
    }

    private boolean isValidName(String nombre) {
        return nombre != null && !nombre.isBlank();
    }
}
