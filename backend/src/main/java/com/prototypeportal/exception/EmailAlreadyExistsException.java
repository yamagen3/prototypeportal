package com.prototypeportal.exception;

/**
 * メールアドレス重複例外
 */
public class EmailAlreadyExistsException extends BusinessException {

    public EmailAlreadyExistsException(String email) {
        super("Email already registered: " + email, "EMAIL_ALREADY_EXISTS");
    }
}
