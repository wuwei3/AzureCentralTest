package com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties;

public class GetAccessPropsSensor extends GetAccessPropertiesFactory{
	
	private static GetAccessPropsSensor sensor = new GetAccessPropsSensor();
	
	private GetAccessPropsSensor() {
		
	}
	
	public static GetAccessPropsSensor getSensorInstance() {
		return sensor;
	}

}
