package com.playlist.manager.controller;

import com.playlist.manager.dto.request.CreateListRequest;
import com.playlist.manager.dto.response.ListResponse;
import com.playlist.manager.service.ListService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/lists")
public class ListController {

    private final ListService service;

    public ListController(ListService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ListResponse> create(@Valid @RequestBody CreateListRequest request) {
        ListResponse created = service.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/lists/{listName}")
                .buildAndExpand(created.nombre())
                .encode()
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ListResponse>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{listName}")
    public ResponseEntity<ListResponse> getByName(@PathVariable String listName) {
        return ResponseEntity.ok(service.getByName(listName));
    }

    @DeleteMapping("/{listName}")
    public ResponseEntity<Void> delete(@PathVariable String listName) {
        service.delete(listName);
        return ResponseEntity.noContent().build();
    }
}
