package com.orangelabs.iot.azure.central.IOTAzureCentralTest;

import java.io.IOException;
import java.util.Collections;

import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.TwinPropertyCallBack;

public class TargetTemperatureUpdateCallback implements TwinPropertyCallBack{
	
	private DeviceClient deviceClient;
	
	public TargetTemperatureUpdateCallback(DeviceClient deviceClient) {
		this.deviceClient = deviceClient;
	}

	@Override
	public void TwinPropertyCallBack(Property property, Object context) {
		 System.out.println("property key " + property.getKey());
		 System.out.println("property value " + property.getValue());
		 System.out.println("property version " + property.getVersion());
		 System.out.println("property last update version " + property.getLastUpdatedVersion());
		 
		 String componentName = (String) context;
		 
		 System.out.println("componentName " + componentName);
		 
		 EmbeddedPropertyUpdate pendingUpdate = new EmbeddedPropertyUpdate(property.getKey(), StatusCode.IN_PROGRESS.getValue(), property.getVersion(), "");
         Property reportedPropertyPending = new Property(property.getKey(), pendingUpdate);
         try {
             deviceClient.sendReportedProperties(Collections.singleton(reportedPropertyPending));
         } catch (IOException e) {
             throw new RuntimeException("IOException when sending reported property update: ", e);
         }
		 
         
         EmbeddedPropertyUpdate completedUpdate = new EmbeddedPropertyUpdate(property.getValue(), StatusCode.COMPLETED.getValue(), property.getVersion(), "Successfully updated target temperature");
         Property reportedPropertyCompleted = new Property(property.getKey(), completedUpdate);
         try {
			deviceClient.sendReportedProperties(Collections.singleton(reportedPropertyCompleted));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 
		
	}

}
