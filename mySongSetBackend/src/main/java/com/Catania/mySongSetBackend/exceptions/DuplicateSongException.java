package com.Catania.mySongSetBackend.exceptions;

public class DuplicateSongException extends RuntimeException {
    public DuplicateSongException(String message) {
        super(message);
    }
}