package com.epam.exception;


public class EntityDoesNotExistException extends RuntimeException {
    public EntityDoesNotExistException(Object id, String identifier,  String entity) {
        super(format(id, identifier, entity));
    }

    public static String format(Object id, String identifier, String entity) {
        return String.format("%s with %s %s does not exist", entity, identifier, id);
    }

}
