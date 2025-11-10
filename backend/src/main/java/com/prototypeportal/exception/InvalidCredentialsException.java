package com.prototypeportal.exception;

/**
 * 認証情報無効例外
 */
public class InvalidCredentialsException extends BusinessException {

    public InvalidCredentialsException() {
        super("Invalid email or password", "INVALID_CREDENTIALS");
    }

    public InvalidCredentialsException(String message) {
        super(message, "INVALID_CREDENTIALS");
    }
}
