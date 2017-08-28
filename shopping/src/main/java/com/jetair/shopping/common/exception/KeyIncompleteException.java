package com.jetair.shopping.common.exception;

public class KeyIncompleteException extends  BaseException {

    public KeyIncompleteException() {
        super();
    }

    public KeyIncompleteException(String code, String message) {
        super(code, message);
    }

    public KeyIncompleteException(Throwable throwable, String code, String message) {
        super(throwable, code, message);
    }
}
