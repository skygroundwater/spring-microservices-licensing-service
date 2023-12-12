package com.springmicroservices.exception;

public class NotValidDtoException extends RuntimeException {

    public NotValidDtoException(String msg) {
        super(msg);
    }
}