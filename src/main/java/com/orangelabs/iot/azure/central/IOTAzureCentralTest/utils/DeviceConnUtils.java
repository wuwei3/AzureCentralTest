package com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.github.lucadruda.iotc.device.IoTCClient;
import com.github.lucadruda.iotc.device.callbacks.CommandCallback;
import com.github.lucadruda.iotc.device.callbacks.PropertiesCallback;
import com.github.lucadruda.iotc.device.enums.IOTC_COMMAND_RESPONSE;
import com.github.lucadruda.iotc.device.enums.IOTC_CONNECT;
import com.github.lucadruda.iotc.device.enums.IOTC_EVENTS;
import com.github.lucadruda.iotc.device.enums.IOTC_LOGGING;
import com.github.lucadruda.iotc.device.exceptions.IoTCentralException;
import com.github.lucadruda.iotc.device.models.IoTCProperty;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties.GetAccessPropertiesFactory;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.firmware.FirmwareDownloadUtil;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.connection.ConnectionSuccess;

public class DeviceConnUtils {

	static Logger log = Logger.getLogger(DeviceConnUtils.class);

	public static boolean isOpen(IoTCClient client, String deviceId, int commandType) {
		boolean open = true;
		String typeOp = "";
		if (commandType == 1) {
			typeOp = "Push Data";
		} else if (commandType == 2) {
			typeOp = "Config Update";
		} else if (commandType == 3) {
			typeOp = "Send Commands";
		} else if (commandType == 4) {
			typeOp = "Firmware Update";
		}

		try {
			if (client != null) {
				client.Connect();
			} else {
				open = false;
			}
		} catch (IoTCentralException e) {
			if (e.getMessage().contains("Could not open the connection")) {
				log.error(deviceId + "  " + typeOp + " cannnot open connection! ");
			} else {
				log.error(deviceId + "  " + typeOp + " open connection get error, message " + e.getMessage());
			}

			if (commandType == 1) {
				AtomicInteger number = new AtomicInteger(0);
				while (true) {
					int count = number.incrementAndGet();
					try {
						if (count <= 3) {
							client.Connect();
							log.info(deviceId + " " + typeOp + " open connection retry success, retry number " + count);
						} else {
							log.error(deviceId + " " + typeOp
									+ " open connection retry has reached the 3, quit the loop, wait the next push data!!!");
							open = false;
						}
						break;
					} catch (Exception e1) {
						log.error(deviceId + " open connection action failed again, retry until it is success"
								+ ", message is " + e1.getMessage());
						continue;
					}
				}
			}
		}
		return open;
	}

	public static void closeConnecton(IoTCClient client, String deviceId, Integer type,
			GetAccessPropertiesFactory factory) {

		if (type == 1) {
			try {
				if (client != null) {
					if (client.IsConnected()) {
						client.Disconnect();
						ConnectionSuccess cs = factory.getDevicesConnectionMap().get(deviceId);
						cs.setOpen(false);
						factory.getDevicesConnectionMap().put(deviceId, cs);
					}
				} else {
					log.error("close action, DeviceClient is null!!!!");
				}
			} catch (Exception e) {
				log.error("closed connection failed for deviceId on push data " + deviceId + ", failed message is "
						+ e.getMessage());
			}

		}
	}
	
	
	public static void reBuildObject(IoTCClient client, String deviceId, GetAccessPropertiesFactory factory) {
		String filepath = factory.getStoreConnectionStringFile();
		String connstring = "";
		try {
			connstring = WriteConStrUtil.getProperty(filepath, deviceId);
		} catch (FileNotFoundException e2) {
			log.error("reBuildObject read the deviceId FILE, no connstring get!!");
		}
		
		if (connstring != null && !"".equals(connstring)) {
			try {
				
				String scopeId = factory.getScopeId();
				
				client = new IoTCClient(deviceId, scopeId, IOTC_CONNECT.DEVICE_KEY, connstring, new MemStorage());
		        client.SetLogging(IOTC_LOGGING.ALL);

				ConnectionSuccess cs = new ConnectionSuccess();
				cs.setClient(client);
				cs.setOpen(false);
				cs.setType(factory.getType());
				factory.getDevicesConnectionMap().put(deviceId, cs);
			} catch (IllegalArgumentException e2) {
			}
		}
	}

	public static void subscribeForCommand(GetAccessPropertiesFactory factory, IoTCClient client,
			String deviceId) throws Exception {
		
		if (client != null && client.IsConnected()) {
			CommandCallback onCommand = (command) -> {
	            log.info(String.format("Received command '%s' with value: %s", command.getName(),
	                    command.getRequestPayload().toString()) + " on device " + deviceId);
	            return command.reply(IOTC_COMMAND_RESPONSE.SUCCESS, deviceId + " Command executed");
	        };
	        
	        client.on(IOTC_EVENTS.Commands, onCommand);
		} else {
			log.info("client is null, cannot to subcribe for command");
		}
	}
	
	public static void subscribeForProperty(GetAccessPropertiesFactory factory, IoTCClient client,
			String deviceId) throws Exception {
		
		if (client != null) {
			PropertiesCallback onProps = (IoTCProperty property) -> {
				log.info(String.format("Received property '%s' with value: %s", property.getName(),
	                    property.getValue().toString()) + " on device " + deviceId);
				
				String propertyName = property.getName();
				if ("firmwareUpdate".equals(propertyName)) {
					CloudBlockBlob blob = FirmwareDownloadUtil.getCloudBlockBlob(factory);
					
					if (blob != null) {
						try {
							blob.downloadText();
							//log.info(deviceId + " Download complete on 1.5M content");
						} catch (Exception e) {
							e.printStackTrace();
							log.error(deviceId + " download firmware file failed, msg " + e.getMessage());
						} 
					} else {
						log.error(deviceId + " blob is null, cannot download!!!!!");
					}
					
					property.ack(deviceId + " firmware Property applied");
				} else if ("sendConfigToSensor".equals(propertyName)) {
					property.ack(deviceId + " config update applied");
				} else {
					property.ack(deviceId + " unknow property applied");
				}
	        };
	        
	        client.on(IOTC_EVENTS.Properties, onProps);
		} else {
			log.info("client is null, cannot to subcribe for property");
		}
	}

}
