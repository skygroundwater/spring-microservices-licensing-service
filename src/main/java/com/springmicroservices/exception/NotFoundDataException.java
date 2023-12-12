package com.springmicroservices.exception;

public class NotFoundDataException extends RuntimeException {

    public NotFoundDataException(String msg) {
        super(msg);
    }
}
