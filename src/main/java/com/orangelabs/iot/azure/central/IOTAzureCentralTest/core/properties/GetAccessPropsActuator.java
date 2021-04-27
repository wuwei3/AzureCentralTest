package com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties;

public class GetAccessPropsActuator extends GetAccessPropertiesFactory {
	private static GetAccessPropsActuator actuator = new GetAccessPropsActuator();

	private GetAccessPropsActuator() {
		
	}

	public static GetAccessPropsActuator getActuatorInstance() {
		return actuator;
	}

}
