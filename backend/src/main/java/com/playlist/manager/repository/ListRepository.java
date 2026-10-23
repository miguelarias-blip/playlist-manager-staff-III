package com.playlist.manager.repository;

import com.playlist.manager.domain.entity.Playlist;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListRepository extends JpaRepository<Playlist, Long> {

    @EntityGraph(attributePaths = "canciones")
    @Override
    List<Playlist> findAll();

    @EntityGraph(attributePaths = "canciones")
    Optional<Playlist> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);
}
