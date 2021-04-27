package com.orangelabs.iot.azure.central.IOTAzureCentralTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.JSONObject;
import com.github.lucadruda.iotc.device.ICentralStorage;
import com.github.lucadruda.iotc.device.IoTCClient;
import com.github.lucadruda.iotc.device.callbacks.PropertiesCallback;
import com.github.lucadruda.iotc.device.enums.IOTC_CONNECT;
import com.github.lucadruda.iotc.device.enums.IOTC_EVENTS;
import com.github.lucadruda.iotc.device.enums.IOTC_LOGGING;
import com.github.lucadruda.iotc.device.exceptions.IoTCentralException;
import com.github.lucadruda.iotc.device.models.IoTCProperty;
import com.github.lucadruda.iotc.device.models.Storage;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.model.SensorPushData;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.DateUtil;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.PropertiesParseUtil;

public class TestSendData {
	
//	static final String deviceId = "OLCSensor2";
    static final String scopeId = "0ne00200B86";
//    static final String deviceKey = "07YajjL5MprNjk9KWTSFH+KN2uHzlpe9vPkxD/GRX+k=";
//    
//    //device template->model->view entity
//    static String modelID = "dtmi:olcsimulationcentral:testforsensor1bk;1";

    static class MemStorage implements ICentralStorage {

        @Override
        public void persist(Storage storage) {
            System.out.println("New credentials available:");
//            System.out.println(storage.getHubName());
//            System.out.println(storage.getDeviceId());
//            System.out.println(storage.getDeviceKey());
            return;
        }

        @Override
        public Storage retrieve() {
            return new Storage();
        }

    }

	public static void main(String[] args) {
//		System.out.println("Welcome to IoTCentral");
//        IoTCClient client = new IoTCClient(deviceId, scopeId, IOTC_CONNECT.DEVICE_KEY, deviceKey, new MemStorage());
//        client.SetLogging(IOTC_LOGGING.ALL);
//        
//        PropertiesCallback onProps = (IoTCProperty property) -> {
//            System.out.println(String.format("Received property '%s' with value: %s", property.getName(),
//                    property.getValue().toString()));
//            property.ack("Property applied");
//        };

//        CommandCallback onCommand = (command) -> {
//            System.out.println(String.format("Received command '%s' with value: %s", command.getName(),
//                    command.getRequestPayload().toString()));
//            return command.reply(IOTC_COMMAND_RESPONSE.SUCCESS, "Command executed");
//        };

//        client.on(IOTC_EVENTS.Properties, onProps);
//        //client.on(IOTC_EVENTS.Commands, onCommand);
//        
//        try {
//            client.Connect();
//            //client.SendProperty(j.toJSONString());
//            
//            while (true) {
//                System.out.println("Sending telemetry");
//                String payload = getPayLoad();
//                client.SendTelemetry(payload);
//                try {
//					Thread.sleep(300 * 1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//            }
//
//        } catch (IoTCentralException ex) {
//            System.out.println("Exception: " + ex.getMessage());
//        }
		
		connection();
	}
	
	private static void connection() {
		Map<String, String> map = getMap();
		
		for(Map.Entry<String, String> entry : map.entrySet()){
		    String deviceid = entry.getKey();
		    String key = entry.getValue();
		    
		    send(deviceid, key);
		}
	}
	
	private static void send(String deviceid, String key) {
        IoTCClient client = new IoTCClient(deviceid, scopeId, IOTC_CONNECT.DEVICE_KEY, key, new MemStorage());
        client.SetLogging(IOTC_LOGGING.ALL);
        
        PropertiesCallback onProps = (IoTCProperty property) -> {
            System.out.println(String.format("%s Received property '%s' with value: %s", deviceid, property.getName(),
                    property.getValue().toString()));
            property.ack("Property applied");
        };

//        CommandCallback onCommand = (command) -> {
//            System.out.println(String.format("Received command '%s' with value: %s", command.getName(),
//                    command.getRequestPayload().toString()));
//            return command.reply(IOTC_COMMAND_RESPONSE.SUCCESS, "Command executed");
//        };

        client.on(IOTC_EVENTS.Properties, onProps);
        //client.on(IOTC_EVENTS.Commands, onCommand);
        
        try {
            client.Connect();
            //client.SendProperty(j.toJSONString());
            
            
            
            String payload = getPayLoad();
            //client.SendTelemetry(payload);
            
//            while (true) {
//                System.out.println("Sending telemetry");
//                String payload = getPayLoad();
//                client.SendTelemetry(payload);
//                try {
//					Thread.sleep(30 * 1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//            }
            
//                JSONObject j = new JSONObject();
//                j.put("sendConfigToSensor", "1");
//                client.SendProperty(j.toJSONString());
//                
//                try {
//					Thread.sleep(30 * 1000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//                
//                j = new JSONObject();
//                j.put("firmwareUpdate", "1");
//                client.SendProperty(j.toJSONString());

        } catch (IoTCentralException ex) {
            System.out.println("Exception: " + ex.getMessage());
        }
		
		
	}
	
	
	private static Map<String, String> getMap() {
		
		Map<String, String> m = new HashMap<>();
		
		m.put("OLCSensor2", "07YajjL5MprNjk9KWTSFH+KN2uHzlpe9vPkxD/GRX+k=");
		m.put("OLCSensor3", "SZOY4l4ShMa1U+5KRXXyG5c8Aafmot5Q1aghGo77nDE=");
		m.put("OLCSensor4", "XEmDovasLJKYyQCGDwGko2VA3pXEH0DqSy/HgzAahA4=");
		m.put("OLCSensor5", "2YOND8EJM5IdBX2jALCoHY7Q1M7vGsKA5vWPqgc+6XY=");
		m.put("OLCSensor6", "CGy0RzBxjZOAqK6+9zUBYPK45JnyQdcfDh6lib7kT9s=");
		m.put("OLCSensor7", "Y7pZBc4TDW+/dTurV/QqQUZRgjumCv/Bm/M90j8Ujeo=");
		m.put("OLCSensor8", "H5jeHqxU+qjwAlknUzZ+bIorNR3FNidJTNXYMQQspUY=");
		m.put("OLCSensor9", "q3dUAjvC+K8wUEglUEiYBphAm0tYuWD5ADd9tW8P4pE=");
		m.put("OLCSensor10", "Fg7rw1qFpfLUM537d5ngV3YVMoqn/09queOUcDKRO6Q=");
		m.put("OLCSensor11", "WFcaY59y+IqP87ajKFshNJwcODekfsTXXjxNyyQKsdY=");
		
		return m;
	}
	
	private static String getPayLoad() {

		JSONObject gson = new JSONObject();

		
			SensorPushData data = new SensorPushData();

			
			int temperature = PropertiesParseUtil.getRandom(100, 200);
			data.setTemperature(temperature);
			data.setCo2(String.valueOf(PropertiesParseUtil.getRandom(10, 25)));
			data.setHumidity(String.valueOf(PropertiesParseUtil.getRandom(10, 30)));
			data.setPollution(String.valueOf(PropertiesParseUtil.getRandom(15)));
			data.setId(UUID.randomUUID().toString());
			data.setInsertTime(DateUtil.getDate(DateUtil.dateTimePattern, new Date()));
			//data.setFiveKB(data.getFiveKB());

			 //payload = JSONObject.toJSON(data).toString();
			 gson.put("sensorProperty", data);
//			gson.put("id", UUID.randomUUID().toString());
//			gson.put("deviceId", "qiuqian8");
//			gson.put("insertTime", DateUtil.getDate(DateUtil.dateTimePattern, new Date()));
//			gson.put("temperature", temperature);
//			gson.put("humidity", String.valueOf(PropertiesParseUtil.getRandom(10, 30)));
//			gson.put("co2", String.valueOf(PropertiesParseUtil.getRandom(10, 25)));
//			gson.put("pollution", String.valueOf(PropertiesParseUtil.getRandom(15)));
			
			
		return gson.toJSONString();
	}
	
	private static String getPayLoad2() {

		JSONObject gson = new JSONObject();

		
			SensorPushData data = new SensorPushData();

			
			int temperature = PropertiesParseUtil.getRandom(100, 200);
			data.setTemperature(temperature);
			data.setCo2(String.valueOf(PropertiesParseUtil.getRandom(10, 25)));
			data.setHumidity(String.valueOf(PropertiesParseUtil.getRandom(10, 30)));
			data.setPollution(String.valueOf(PropertiesParseUtil.getRandom(15)));
			data.setId(UUID.randomUUID().toString());
			data.setInsertTime(DateUtil.getDate(DateUtil.dateTimePattern, new Date()));
			//data.setFiveKB(data.getFiveKB());

			// payload = JSONObject.toJSON(data).toString();
			gson.put("tempslong", 200);
		return gson.toJSONString();
	}

}
