package com.epam.exception;


public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(Long id, String identifier, String entity) {
        super(format(id, identifier, entity));
    }

    private static String format(Long id, String identifier, String entity) {
        return String.format("%s with %s %s already exists", entity, identifier, id);
    }
}
