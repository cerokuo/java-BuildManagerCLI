package com.knowroaming.model.Exceptions;

/**
 * Exception to inform that the data is not in the DDBB
 * Created by cerokuo on 04/04/2017.
 */
public class InformationNotFoundException extends RuntimeException {

    public InformationNotFoundException(String message)
    {
        super(message);
    }

}
