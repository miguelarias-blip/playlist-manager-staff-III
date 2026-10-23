package com.playlist.manager.exception;

public class InvalidListNameException extends RuntimeException {

    public InvalidListNameException(String listName) {
        super("El nombre de la lista no puede ser nulo, vacío o en blanco: " + listName);
    }
}
