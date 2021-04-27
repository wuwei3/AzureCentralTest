package com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.configupdate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.github.lucadruda.iotc.device.IoTCClient;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties.GetAccessPropertiesFactory;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.connection.ConnectionSuccess;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.DeviceConnUtils;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.HttpRequest;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.PropertiesParseUtil;


public class ConfigUpdateThread implements Runnable {

	Logger log = Logger.getLogger(ConfigUpdateThread.class);

	private GetAccessPropertiesFactory factory;

	private String deviceId;
	
    private AtomicInteger connectionErrorCount;
	
	private AtomicInteger configErrorCount;

	private CountDownLatch latch;

	public ConfigUpdateThread(GetAccessPropertiesFactory factory, String deviceId, AtomicInteger connectionErrorCount, AtomicInteger configErrorCount, CountDownLatch latch) {
		this.factory = factory;
		this.deviceId = deviceId;
		this.connectionErrorCount = connectionErrorCount;
		this.configErrorCount = configErrorCount;
		this.latch = latch;
	}

	@Override
	public void run() {
		IoTCClient client = getDeviceClient();
		try {
			boolean open = reOpenConnection();
			if (open) {
				Thread.sleep(5000);
				String payLoad = getPayLoad();
				cloudSendConfigUpdate(payLoad);
			} else {
				log.error(deviceId + " cannot open the connection for this time on config update, we have to wait at next time!!!!!");
				connectionErrorCount.incrementAndGet();
			}
		} catch (Exception e) {
			log.error("failed for deviceId on config update " + deviceId + " , failed message is " + e.getMessage());
			configErrorCount.incrementAndGet();
			DeviceConnUtils.reBuildObject(client, deviceId, factory);
		} finally {
			latch.countDown();
			//DeviceConnUtils.closeForce(client, deviceId, factory);
			closeConnecton(client);
		}
	}

	private void configUpdateSubscribe(IoTCClient client) throws Exception {
		try {
			DeviceConnUtils.subscribeForProperty(factory, client, deviceId);
		} catch (Exception e) {
			log.error(deviceId + " subscribe config update failed");
		}
	}
	
	private void cloudSendConfigUpdate(String payload) throws Exception {
		String url4 = "https://olcsimulationcentral.azureiotcentral.com/api/preview/devices/%s/components/%s/properties";
		
		String propertyComponentName = factory.getPropertyComponentName();
		
		String url = String.format(url4, deviceId, propertyComponentName);
		
		String token = factory.getCentralAPIToken();
		
		HttpRequest.doPatchForHttps(url, payload, null, token);
		
	}

	private void closeConnecton(IoTCClient client) {
		Integer type = factory.getType();
		DeviceConnUtils.closeConnecton(client, deviceId, type, factory);
	}

	private IoTCClient getDeviceClient() {
		return factory.getDevicesConnectionMap().get(deviceId).getClient();
	}

	private boolean reOpenConnection() throws Exception{
		boolean reopen = true;

		ConnectionSuccess cs = factory.getDevicesConnectionMap().get(deviceId);
		IoTCClient client = cs.getClient();
		boolean openExistsInCS = cs.isOpen();
		if (!openExistsInCS) {
			configUpdateSubscribe(client);
			reopen = DeviceConnUtils.isOpen(client, deviceId,2);
			if (reopen) {
				//if (cs.getType() == 2) {
					cs.setOpen(true);
					factory.getDevicesConnectionMap().put(deviceId, cs);
				//}
				//configUpdateSubscribe(client);
			} else {
				log.error(deviceId + " config update reopen failed!!!!");
			}
		} else {
			configUpdateSubscribe(client);
		}
		return reopen;
	}

	private String getPayLoad() {
		JSONObject j = new JSONObject();

		int periodSimple = PropertiesParseUtil.getRandom(1, 100);
		
		log.info(deviceId + " send " + periodSimple + " to  sendConfigToSensor");
		j.put("sendConfigToSensor", String.valueOf(periodSimple));
		

		String payload = j.toJSONString();
		return payload;
	}

}
