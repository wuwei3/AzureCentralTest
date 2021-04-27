package com.orangelabs.iot.azure.central.IOTAzureCentralTest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;

import com.github.lucadruda.iotc.device.ICentralStorage;
import com.github.lucadruda.iotc.device.models.Storage;
import com.microsoft.azure.sdk.iot.device.ClientOptions;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Pair;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.Property;
import com.microsoft.azure.sdk.iot.device.DeviceTwin.TwinPropertyCallBack;

public class TestPropertyCallback {
	
	static final String deviceId = "renxing2";
    static final String scopeId = "0ne00200B86";
    static final String deviceKey = "ytBZSkuCZZV0R8rbjawE7hZvhyBL9v9nT7D7jzsgo/Q=";
    
    static String conneString = "HostName=iotc-b8f1f26d-248f-40a7-a21e-522a880d3b38.azure-devices.net;DeviceId=renxing2;SharedAccessKey=ytBZSkuCZZV0R8rbjawE7hZvhyBL9v9nT7D7jzsgo/Q=";
    
    //device template->model->view entity
    static String modelID = "dtmi:olcsimulationcentral:testsen4471:firmwareVersion;1";
    
    static DeviceClient deviceClient;

    static class MemStorage implements ICentralStorage {

        @Override
        public void persist(Storage storage) {
            System.out.println("New credentials available:");
//            System.out.println(storage.getHubName());
//            System.out.println(storage.getDeviceId());
            System.out.println(storage.getConnectionString());
            return;
        }

        @Override
        public Storage retrieve() {
            return new Storage();
        }

    }

	public static void main(String[] args) throws Exception{
		System.out.println("Welcome to IoTCentral");
      
		initializeDeviceClient();
        
        deviceClient.startDeviceTwin(new TwinIotHubEventCallback(), null, new TargetTemperatureUpdateCallback(deviceClient), null);
        
        Map<Property, Pair<TwinPropertyCallBack, Object>> desiredPropertyUpdateCallback =
                Collections.singletonMap(
                        new Property("firmwareVersion", null),
                        new Pair<>(new TargetTemperatureUpdateCallback(deviceClient), null));
        deviceClient.subscribeToTwinDesiredProperties(desiredPropertyUpdateCallback);

	}
	
	private static void initializeDeviceClient() throws URISyntaxException, IOException {
        ClientOptions options = new ClientOptions();
        options.setModelId(modelID);
        deviceClient = new DeviceClient(conneString, IotHubClientProtocol.MQTT, options);

        deviceClient.registerConnectionStatusChangeCallback((status, statusChangeReason, throwable, callbackContext) -> {
        	System.out.println("Connection status change registered: status= " + status + " reason= " + statusChangeReason);

            if (throwable != null) {
            	System.out.println("The connection status change was caused by " + throwable.getMessage());
                throwable.printStackTrace();
            }
        }, deviceClient);

        deviceClient.open();
    }

}
