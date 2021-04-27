package com.orangelabs.iot.azure.central.IOTAzureCentralTest;

public enum StatusCode {
	
	COMPLETED (200),
    IN_PROGRESS (202),
    NOT_FOUND (404);

    private final int value;
    
    StatusCode(int value) {
        this.value = value;
    }
	public int getValue() {
		return value;
	}

}
