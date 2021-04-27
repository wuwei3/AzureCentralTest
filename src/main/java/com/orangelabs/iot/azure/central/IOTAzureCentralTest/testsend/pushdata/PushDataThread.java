package com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.pushdata;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.github.lucadruda.iotc.device.IoTCClient;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties.GetAccessPropertiesFactory;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.connection.ConnectionSuccess;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.model.ActuatorPushData;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.model.SensorPushData;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.DateUtil;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.DeviceConnUtils;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.PropertiesParseUtil;

public class PushDataThread implements Runnable {

	Logger log = Logger.getLogger(PushDataThread.class);

	private GetAccessPropertiesFactory factory;

	private String deviceId;

	private Integer countTen;
	
	private AtomicInteger connectionErrorCount;
	
	private AtomicInteger pushDataErrorCount;

	private CountDownLatch latch;

	public PushDataThread(GetAccessPropertiesFactory factory, String deviceId, Integer countTen, AtomicInteger connectionErrorCount, AtomicInteger pushDataErrorCount, CountDownLatch latch) {
		this.factory = factory;
		this.deviceId = deviceId;
		this.countTen = countTen;
		this.connectionErrorCount = connectionErrorCount;
		this.pushDataErrorCount = pushDataErrorCount;
		this.latch = latch;
	}

	@Override
	public void run() {
		IoTCClient client = getDeviceClient();
		try {
			if (client == null) {
				log.error(deviceId +  " IoTCClient is null, cannot do anything!!!!");
			} else {
				String payload = getPayLoad();
				
				if (factory.getType() == 1) {
					boolean open = reOpenConnection();
					if (open) {
						sendMsg(client, payload);
						Thread.sleep(1000);
					} else {
						log.error(deviceId + " cannot open the connection for this time, we have to wait at next time!!!!!");
						connectionErrorCount.incrementAndGet();
					}
				} else {
					boolean open = reOpenConnectionForActuator();
					if (open) {
						sendMsg(client, payload);
						Thread.sleep(1000);
					} else {
						log.error(deviceId + " cannot open the connection for this time, we have to wait at next time!!!!!");
						connectionErrorCount.incrementAndGet();
					}
				}
			}
		} catch (Exception e) {
			log.error(deviceId + " unknow exception -> " + e.getMessage());
			//DeviceConnUtils.reBuildObject(client, deviceId, factory);
		} finally {
			latch.countDown();
			closeConnecton(client);
		}

	}

	public void closeConnecton(IoTCClient client) {
		Integer type = factory.getType();
		DeviceConnUtils.closeConnecton(client, deviceId, type, factory);
	}

	private void sendMsg(IoTCClient client, String payload) {
		try {
			client.SendTelemetry(payload);
			log.info(deviceId + " push success  ");
		} catch (Exception e) {
			log.error(deviceId + " push data get error, message " + e.getMessage());
			if (e.getMessage().contains("Cannot send event from an IoT Hub client that is closed")) {
				boolean reopen = DeviceConnUtils.isOpen(client, deviceId, 1);
				if (reopen) {
					ConnectionSuccess cs = factory.getDevicesConnectionMap().get(deviceId);
					cs.setOpen(true);
					factory.getDevicesConnectionMap().put(deviceId, cs);
				}
			}
			
			AtomicInteger number = new AtomicInteger(0);
			while (true) {
				int count = number.incrementAndGet();
				try {
					if (count <= 3) {
						client.SendTelemetry(payload);
						log.info(deviceId + " retry to resend success  " + " retry number " + count);
					} else {
						log.error(deviceId + " retry to resend has reached the 3, quit the loop, wait the next push data!!!");
						pushDataErrorCount.incrementAndGet();
					}
					break;
				} catch (Exception e1) {
					log.error(deviceId + " retry push data action failed again, retry until it is success" + ", message is "
							+ e1.getMessage());
					continue;
				}
			}
		}
	}

	private IoTCClient getDeviceClient() {
		return factory.getDevicesConnectionMap().get(deviceId).getClient();
	}

	private boolean reOpenConnection(){
		boolean reopen = true;

		ConnectionSuccess cs = factory.getDevicesConnectionMap().get(deviceId);
		boolean openExistsInCS = cs.isOpen();
		if (!openExistsInCS) {
			IoTCClient client = cs.getClient();
			reopen = DeviceConnUtils.isOpen(client, deviceId, 1);
			if (reopen) {
				//if (cs.getType() == 2) {
					cs.setOpen(true);
					if (cs.getType() == 2) {
						//sendCommandsSubscribe(client);
					}
					//subscribeSendFirmware(client);
					//configUpdateSubscribe(client);
					factory.getDevicesConnectionMap().put(deviceId, cs);
				//}
			} else {
				log.info(deviceId + " push data reopen failed!!!!");
			}
		}
		return reopen;
	}
	
	private boolean reOpenConnectionForActuator(){
		boolean reopen = true;
		//if (countTen == 1) { // only open for the first time
			ConnectionSuccess cs = factory.getDevicesConnectionMap().get(deviceId);
			boolean openExistsInCS = cs.isOpen();
			if (!openExistsInCS) {
				IoTCClient client = cs.getClient();
				reopen = DeviceConnUtils.isOpen(client, deviceId, 1);
				if (reopen) {
					//if (cs.getType() == 2) {
						cs.setOpen(true);
						if (cs.getType() == 2) {
							//sendCommandsSubscribe(client);
						}
						//subscribeSendFirmware(client);
						//configUpdateSubscribe(client);
						factory.getDevicesConnectionMap().put(deviceId, cs);
					//}
				} else {
					log.info(deviceId + " push data reopen failed!!!!");
				}
			}
		//}
		return reopen;
	}
	
	
	private void subscribeSendFirmware(IoTCClient client){
		try {
			DeviceConnUtils.subscribeForProperty(factory, client, deviceId);
		} catch (Exception e) {
			log.error(deviceId + " subscribe firmware while push data failed");
		}
	}
	
	private void sendCommandsSubscribe(IoTCClient client) {
		try {
			DeviceConnUtils.subscribeForCommand(factory, client, deviceId);
		} catch (Exception e) {
			log.error(deviceId + " subscribe command while push data failed");
		}
	}
	
	private void configUpdateSubscribe(IoTCClient client) {
		try {
			DeviceConnUtils.subscribeForProperty(factory, client, deviceId);
		} catch (Exception e) {
			log.error(deviceId + " subscribe config update while push data failed");
		}
	}

	private String getPayLoad() {

		String payload = "";

		Integer type = factory.getType();

		if (type == 1) {
			SensorPushData data = (SensorPushData) factory.getDeviceObjectMap().get(deviceId);

			int temperature = 0;
			if (countTen <= 10) {
				temperature = PropertiesParseUtil.getRandom(100, 200);
			} else {
				temperature = PropertiesParseUtil.getRandom(40);
			}

			data.setTemperature(temperature);
			data.setCo2(String.valueOf(PropertiesParseUtil.getRandom(10, 25)));
			data.setHumidity(String.valueOf(PropertiesParseUtil.getRandom(10, 30)));
			data.setPollution(String.valueOf(PropertiesParseUtil.getRandom(15)));
			data.setId(UUID.randomUUID().toString());
			data.setInsertTime(DateUtil.getDate(DateUtil.dateTimePattern, new Date()));
			data.setDeviceId(deviceId);

			JSONObject gson = new JSONObject();
			gson.put("sensorProperty", data);
			
			payload = gson.toJSONString();

		} else {
			ActuatorPushData data = (ActuatorPushData) factory.getDeviceObjectMap().get(deviceId);
			int electconsumption = 0;
			if (countTen <= 10) {
				electconsumption = PropertiesParseUtil.getRandom(100, 200);
			} else {
				electconsumption = PropertiesParseUtil.getRandom(100);
			}

			data.setElectcomsuption(electconsumption);
			data.setId(UUID.randomUUID().toString());
			data.setInsertTime(DateUtil.getDate(DateUtil.dateTimePattern, new Date()));
			//data.setFiveKB(data.getFiveKB());

			JSONObject gson = new JSONObject();
			gson.put("actuatorProperty", data);
			
			payload = gson.toJSONString();
		}
		return payload;
	}

}
