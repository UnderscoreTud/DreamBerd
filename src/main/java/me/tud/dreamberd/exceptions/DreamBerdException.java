package me.tud.dreamberd.exceptions;

public class DreamBerdException extends RuntimeException {

    public DreamBerdException() {
    }

    public DreamBerdException(String message) {
        super(message);
    }

    public DreamBerdException(String message, Throwable cause) {
        super(message, cause);
    }

    public DreamBerdException(Throwable cause) {
        super(cause);
    }

    public DreamBerdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
