package com.knowroaming.store.Exceptions;

/**
 * Created by cerokuo on 04/04/2017.
 */
public class DataBaseConnectionException extends RuntimeException {

    public DataBaseConnectionException(String message)
    {
        super(message);
    }

    public DataBaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
