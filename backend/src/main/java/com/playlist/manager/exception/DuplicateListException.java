package com.playlist.manager.exception;

public class DuplicateListException extends RuntimeException {

    public DuplicateListException(String listName) {
        super("Ya existe una lista de reproducción con el nombre: " + listName);
    }
}
