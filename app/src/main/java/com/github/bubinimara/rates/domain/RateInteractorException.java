package com.github.bubinimara.rates.domain;

/**
 * Created by davide.
 */
public class RateInteractorException extends Exception {
    public enum ErrorType {
        NETWORK, INVALID_USER_INPUT_VALUE
    }

    private final ErrorType error;

    public RateInteractorException(ErrorType error) {
        this.error = error;
    }

    public RateInteractorException(String message, ErrorType error) {
        super(message);
        this.error = error;
    }

    public RateInteractorException(ErrorType error, Throwable throwable) {
        super(throwable);
        this.error = error;
    }

    public ErrorType getError() {
        return error;
    }

}
