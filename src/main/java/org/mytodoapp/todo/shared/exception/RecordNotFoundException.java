package org.mytodoapp.todo.shared.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException (String entityName, Object id) {
        super(entityName + " with id " + id + " not found.");
    }

}
