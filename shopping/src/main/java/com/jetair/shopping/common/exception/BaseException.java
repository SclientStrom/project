package com.jetair.shopping.common.exception;

public class BaseException extends Exception{
    private String code;
    private String message;

    public BaseException() {
        super();
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(Throwable throwable, String code, String message) {
        super(throwable);
        this.code = code;
        this.message = message;
    }
}
