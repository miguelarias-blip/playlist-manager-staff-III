package com.playlist.manager.service;

import com.playlist.manager.dto.request.CreateListRequest;
import com.playlist.manager.dto.response.ListResponse;

import java.util.List;

public interface ListService {

    ListResponse create(CreateListRequest request);

    List<ListResponse> getAll();

    ListResponse getByName(String listName);

    void delete(String listName);
}
