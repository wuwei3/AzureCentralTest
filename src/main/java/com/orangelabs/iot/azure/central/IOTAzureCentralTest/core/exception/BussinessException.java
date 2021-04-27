package com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.exception;

public class BussinessException extends RuntimeException{
	
	private static final long serialVersionUID = 477399363086602984L;

    public BussinessException()
    {
        super("System exception");
    }

    public BussinessException(String message)
    {
        super(message);
    }

    public BussinessException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
