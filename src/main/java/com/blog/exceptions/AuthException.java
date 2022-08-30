package com.blog.exceptions;

public class AuthException extends  RuntimeException {

    public AuthException(String message) {
        super(message);
    }

    public AuthException() {
        super();
    }

}
