package com.mindshift.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
