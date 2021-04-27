package com.orangelabs.iot.azure.central.IOTAzureCentralTest.firmware;

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

public class FirmwareUpdateThread implements Runnable {
	Logger log = Logger.getLogger(FirmwareUpdateThread.class);

	private GetAccessPropertiesFactory factory;

	private String deviceId;
	
    private AtomicInteger connectionErrorCount;
	
	private AtomicInteger firmwareErrorCount;
	
	private CountDownLatch latch;
	
	public FirmwareUpdateThread(GetAccessPropertiesFactory factory, String deviceId, AtomicInteger connectionErrorCount, AtomicInteger firmwareErrorCount, CountDownLatch latch) {
		this.factory = factory;
		this.deviceId = deviceId;
		this.connectionErrorCount = connectionErrorCount;
		this.firmwareErrorCount = firmwareErrorCount;
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
				cloudSendFirmwareUpdate(payLoad);
				Thread.sleep(3000);
			} else {
				log.error(deviceId + " cannot open the connection for this time on firmware update, we have to wait at next time!!!!!");
				connectionErrorCount.incrementAndGet();
			}
		} catch (Exception e) {
			log.error(deviceId + " firmware update failed, message is " + e.getMessage());
			firmwareErrorCount.incrementAndGet();
			//DeviceConnUtils.reBuildObject(client, deviceId, factory);
		}finally {
			latch.countDown();
			closeConnecton(client);
		}
	}
	
	private void closeConnecton(IoTCClient client) {
		Integer type = factory.getType();
		DeviceConnUtils.closeConnecton(client, deviceId, type, factory);
	}
	
	private IoTCClient getDeviceClient() {
		return factory.getDevicesConnectionMap().get(deviceId).getClient();
	}
	
	private void subscribeSendFirmware(IoTCClient client) throws Exception{
		try {
			DeviceConnUtils.subscribeForProperty(factory, client, deviceId);
		} catch (Exception e) {
			log.error(deviceId + " subscribe firmware  failed");
		}
	}
	
	private void cloudSendFirmwareUpdate(String payload) throws Exception {
        String url4 = "https://olcsimulationcentral.azureiotcentral.com/api/preview/devices/%s/components/%s/properties";
		
		String propertyComponentName = factory.getPropertyComponentName();
		
		String url = String.format(url4, deviceId, propertyComponentName);
		
		String token = factory.getCentralAPIToken();
		
		HttpRequest.doPatchForHttps(url, payload, null, token);
	}
	
	
	private boolean reOpenConnection() throws Exception{
		boolean reopen = true;

		ConnectionSuccess cs = factory.getDevicesConnectionMap().get(deviceId);
		IoTCClient client = cs.getClient();
		boolean openExistsInCS = cs.isOpen();
		if (!openExistsInCS) {
			subscribeSendFirmware(client);
			reopen = DeviceConnUtils.isOpen(client, deviceId, 4);
			if (reopen) {
				//if (cs.getType() == 2) {
					cs.setOpen(true);
					factory.getDevicesConnectionMap().put(deviceId, cs);
				//}
				//subscribeSendFirmware(client);
			} else {
				log.error(deviceId + " firware update reopen failed!!!!");
			}
		} else {
			subscribeSendFirmware(client);
		}
		return reopen;
	}
	
	private String getPayLoad() {
		JSONObject j = new JSONObject();

		int periodSimple = PropertiesParseUtil.getRandom(1, 10);
		log.info(deviceId + " send " + periodSimple + " to  firmwareUpdate");
		j.put("firmwareUpdate", String.valueOf(periodSimple));

		String payload = j.toJSONString();
		return payload;
	}
	

}
