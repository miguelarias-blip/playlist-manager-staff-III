package com.playlist.manager.exception;

public class ListNotFoundException extends RuntimeException {

    public ListNotFoundException(String listName) {
        super("Lista de reproducción no encontrada: " + listName);
    }
}
