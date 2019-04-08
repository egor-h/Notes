package com.example.Notes.exception;

public class NoteDoesntExistException extends NoteException {
    public NoteDoesntExistException() {
    }

    public NoteDoesntExistException(String message) {
        super(message);
    }

    public NoteDoesntExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoteDoesntExistException(Throwable cause) {
        super(cause);
    }
}
