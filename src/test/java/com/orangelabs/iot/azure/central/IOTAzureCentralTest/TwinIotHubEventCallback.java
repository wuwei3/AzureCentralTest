package com.orangelabs.iot.azure.central.IOTAzureCentralTest;

import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;

public class TwinIotHubEventCallback implements IotHubEventCallback{

	@Override
	public void execute(IotHubStatusCode responseStatus, Object callbackContext) {
		System.out.println("Property - Response from IoT Hub: " + responseStatus.name());
	}

}
