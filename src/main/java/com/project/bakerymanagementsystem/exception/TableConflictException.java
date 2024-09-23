package com.project.bakerymanagementsystem.exception;

public class TableConflictException extends ConflictException{
    public TableConflictException(String message) {
        super(message);
    }
}
