package com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.connection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.github.lucadruda.iotc.device.IoTCClient;
import com.github.lucadruda.iotc.device.enums.IOTC_CONNECT;
import com.github.lucadruda.iotc.device.enums.IOTC_LOGGING;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties.GetAccessPropertiesFactory;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.model.ActuatorPushData;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.model.PushData;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.model.SensorPushData;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.MemStorage;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.WriteConStrUtil;

public class ConnectionSetThread implements Runnable{
	
	Logger log = Logger.getLogger(ConnectionSetThread.class);
	
	private GetAccessPropertiesFactory factory;

	private String deviceId;

	private CountDownLatch latch;

	private ConcurrentHashMap<String, ConnectionSuccess> deviceConnectionMap;
	
	private ConcurrentHashMap<String, PushData> deviceObjectMap;

	public ConnectionSetThread(GetAccessPropertiesFactory factory, String deviceId, CountDownLatch latch,
			ConcurrentHashMap<String, ConnectionSuccess> deviceConnectionMap, ConcurrentHashMap<String, PushData> deviceObjectMap) {
		this.factory = factory;
		this.deviceId = deviceId;
		this.latch = latch;
		this.deviceConnectionMap = deviceConnectionMap;
		this.deviceObjectMap = deviceObjectMap;
	}

	@Override
	public void run() {
		initConnection();
		initPushDataObject();
		//log.info(" " + deviceId + " init connection and put device object are OK ");
	}
	
	
	public void initConnection() {
		try {
			connection();
		} catch (Exception e) {
			log.info(deviceId + "  init connection exception   " + e.getMessage());
			AtomicInteger number = new AtomicInteger(0);
			while (true) {
				log.info(deviceId + "  retry number " + number.incrementAndGet());
				try {
					connection();
					log.info(deviceId + " retry success!!!");
					break;
				} catch (Exception e1) {
					log.info(deviceId + "  init connection failed again, rerey until it is success" + "       " + e1.getMessage());
					continue;
				}
			}
		} finally {
			//registryManager.close();
			if (latch != null) {
				latch.countDown();
			}
		}
	}
	
	public void connection() throws Exception{
		String filepath = factory.getStoreConnectionStringFile();
		String connstring = WriteConStrUtil.getProperty(filepath, deviceId);
		
//		if (connstring == null || "".equals(connstring)) {
//			Device device = registryManager.getDevice(deviceId);
//			String accesskey = device.getPrimaryKey();
//			connstring = PropertiesParseUtil.getDeviceConnection(iothubname, deviceId, accesskey);
//			WriteConStrUtil.setProperty(filepath, deviceId, connstring);
//		} 
		
//		DeviceClient client = new DeviceClient(connstring, Constants.protocol);
//		client.setOperationTimeout(10 * 1000);
//		DeviceConnUtils.setRetryPolicy(client);
		boolean open = false;
		if (factory.getType() == 2) {
			//open = DeviceConnUtils.isOpen(client, deviceId);
		}
		
		String scopeId = factory.getScopeId();
		IoTCClient client = new IoTCClient(deviceId, scopeId, IOTC_CONNECT.DEVICE_KEY, connstring, new MemStorage());
        client.SetLogging(IOTC_LOGGING.ALL);
		
		ConnectionSuccess cs = new ConnectionSuccess();
		cs.setClient(client);
		cs.setOpen(open);
		cs.setType(factory.getType());
		
		//keep while only test congif fro sensor
		//DeviceConnUtils.subscribeForConfigUpdate(client, deviceId);
		
		if (open && factory.getType() == 2) {// actuator needs to subscribe first, sensor no need, because every time open-closed, need to subscribe again
			//DeviceConnUtils.subscribeForCloud2Device(factory, client, deviceId);
		}
		deviceConnectionMap.put(deviceId, cs);
	}
	
	
	public void initPushDataObject() {
		Integer type = factory.getType();
		if (type == 1) {
			SensorPushData data = new SensorPushData();
			data.setDeviceId(deviceId);
			
			deviceObjectMap.put(deviceId, data);
			
		} else {
			ActuatorPushData data = new ActuatorPushData();
			data.setDeviceId(deviceId);
			deviceObjectMap.put(deviceId, data);
		}
	}

}
