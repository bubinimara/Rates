package com.github.bubinimara.rates.app.model;

/**
 * Created by davide.
 */
public class ErrorModel {

    public enum Error {
        NETWORK,
        INVALID_USER_INPUT_VALUE
    }

    private Error error;
    private boolean isHandled;

    public ErrorModel(Error error) {
        this.error = error;
        this.isHandled = false;
    }

    public Error getError() {
        return error;
    }

    public boolean isHandled() {
        return isHandled;
    }

    public void markAsHandled() {
        isHandled = true;
    }

}
