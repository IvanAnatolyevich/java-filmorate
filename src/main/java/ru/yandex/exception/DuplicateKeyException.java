package ru.yandex.exception;

public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(final String message) {
        super(message);
    }
}