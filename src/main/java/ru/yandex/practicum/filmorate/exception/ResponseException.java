package ru.yandex.practicum.filmorate.exception;

public class ResponseException extends RuntimeException{

    public ResponseException(String message) {
        super(message);
    }
}
