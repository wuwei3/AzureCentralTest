package com.orangelabs.iot.azure.central.IOTAzureCentralTest;

import java.io.File;

import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.exception.BussinessException;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties.GetAccessPropertiesFactory;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties.GetAccessPropsActuator;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties.GetAccessPropsSensor;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.firmware.TestFirmwareUpdate;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.configupdate.TestConfigUpdate;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.connection.InitDeviceConnection;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.pushdata.TestPushData;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.PropertyUtil;

public class AzureCentralTest {

	public static void main(String[] args) throws Exception{
		printHead();

		if (args.length == 0) {
			throw new BussinessException(
					"Sorry, the parameters are invalid, you need to input parameters, the first is 'create'(unsupport) or 'delete'(unsupport) or 'test', the second is the 'xxx-xxx.properties' that stored in the zip package for  action, the third is the file which store deviceID and their keys.");
		}

		String a1 = args[0];
		System.out.println("The Action you want to do is '" + a1 + "'");

		if (!a1.contains("test")) {
			throw new BussinessException("The first parameter format is 'test'.");
		}

		String a2 = args[1];
		System.out.println("The config file is  " + a2);

		if (!a2.contains(".properties")) {
			throw new BussinessException(
					"The second parameter is the xxx-xxx.properties name in the zip package for action.");
		}

		GetAccessPropertiesFactory factory = checkParams(a2);

		if (a1.equals("create")) {
			//CreateDevices.getCreateDevices().createDevicesThread(factory);
		} else if (a1.equals("delete")) {
			//DeleteAllDevices.getDeleteAllDevicesInstance().deleteAllDevices(factory);
		} else if (a1.equals("test")) {
			InitDeviceConnection.initEveryDeviceConnection(factory);
			
			TestPushData.getInstance().executePushDataWithSpecifyTime(factory);
//			
			TestConfigUpdate.getInstance().executeConfigUpdateWithSpecifyTime(factory);
			
			if (a2.contains("sensor-config.properties")) {
			} else if (a2.contains("actuator-config.properties")) {
				//TestSendCommands.getInstance().executeSendCommandsWithSpecifyTime(factory);
			} else {
				throw new BussinessException(
						"Sorry, the config file name must be containing the 'sensor' or 'actuator' as prefix, it's better to keep the original file name while you getting them.");
			}
			TestFirmwareUpdate.getInstance().updateFirmwareSchedule(factory);
		} else {
			throw new BussinessException("invalid input command, cannot to do any action.");
		}

	}
	
	private static GetAccessPropertiesFactory checkParams(String fileName) throws Exception {
		File f = new File(fileName);
		GetAccessPropertiesFactory factory = null;
		if (!f.exists()) {
			throw new BussinessException("File " + fileName + " not exists!!!!");
		} else {
			System.out.println("file absolutely path " + f.getAbsolutePath());
			
			if (fileName.contains("sensor-config.properties")) {
				factory = GetAccessPropsSensor.getSensorInstance();
				factory.setType(1);
			} else if (fileName.contains("actuator-config.properties")) {
				factory = GetAccessPropsActuator.getActuatorInstance();
				factory.setType(2);
			}
			
			String IOTCentralName = PropertyUtil.getProperty(f.getAbsolutePath(), "IOTCentralName");
			if (IOTCentralName == null || "".equals(IOTCentralName)) {
				throw new BussinessException("You must specify the 'IOTCentralName' property in properties file.");
			} else {
				factory.setIOTCentralName(IOTCentralName);
			}

			String deviceName = PropertyUtil.getProperty(f.getAbsolutePath(), "deviceName");
			if (deviceName == null || "".equals(deviceName)) {
				throw new BussinessException("You must specify the 'thingName' property in properties file.");
			} else {
				factory.setDeviceName(deviceName);
			}

			String deviceNumberCount = PropertyUtil.getProperty(f.getAbsolutePath(), "deviceNumberCount");
			if (deviceNumberCount == null || "".equals(deviceNumberCount)) {
				throw new BussinessException("You must specify the 'deviceNumberCount' property in properties file.");
			} else {
				factory.setDeviceNumberCount(deviceNumberCount);
			}
			
			String scopeId = PropertyUtil.getProperty(f.getAbsolutePath(),
					"scopeId");
			if (scopeId == null || "".equals(scopeId)) {
				throw new BussinessException(
						"You must specify the 'scopeId' property in properties file.");
			} else {
				factory.setScopeId(scopeId);
			}


			String centralAPIToken = PropertyUtil.getProperty(f.getAbsolutePath(), "centralAPIToken");
			if (centralAPIToken == null || "".equals(centralAPIToken)) {
				throw new BussinessException(
						"You must specify the 'centralAPIToken' property in properties file.");
			} else {
				factory.setCentralAPIToken(centralAPIToken);
			}
			
			String propertyComponentName = PropertyUtil.getProperty(f.getAbsolutePath(), "propertyComponentName");
			if (propertyComponentName == null || "".equals(propertyComponentName)) {
				throw new BussinessException(
						"You must specify the 'propertyComponentName' property in properties file.");
			} else {
				factory.setPropertyComponentName(propertyComponentName);
			}


			String pushDataProperties = PropertyUtil.getProperty(f.getAbsolutePath(), "pushDataProperties");
			if (pushDataProperties == null || "".equals(pushDataProperties)) {
				throw new BussinessException("You must specify the 'pushDataProperties' property in properties file.");
			} else {
				factory.setPushDataProperties(pushDataProperties);
			}

			String pushDataExecuteTime = PropertyUtil.getProperty(f.getAbsolutePath(), "pushDataExecuteTime");
			if (pushDataExecuteTime == null || "".equals(pushDataExecuteTime)) {
				throw new BussinessException("You must specify the 'pushDataExecuteTime' property in properties file.");
			} else {
				if (!pushDataExecuteTime.contains(":")) {
					throw new BussinessException("the 'pushDataExecuteTime' content  is like 'HH:MM:SS'.");
				}
				factory.setPushDataExecuteTime(pushDataExecuteTime);
			}

			if (fileName.contains("actuator-config.properties")) {
				String sendCommandProperties = PropertyUtil.getProperty(f.getAbsolutePath(), "sendCommandProperties");
				if (sendCommandProperties == null || "".equals(sendCommandProperties)) {
					throw new BussinessException(
							"You must specify the 'sendCommandProperties' property in properties file.");
				} else {
					factory.setSendCommandProperties(sendCommandProperties);
				}

				String sendCommandExecuteTime = PropertyUtil.getProperty(f.getAbsolutePath(), "sendCommandExecuteTime");
				if (sendCommandExecuteTime == null || "".equals(sendCommandExecuteTime)) {
					throw new BussinessException(
							"You must specify the 'sendCommandExecuteTime' property in properties file.");
				} else {
					factory.setSendCommandExecuteTime(sendCommandExecuteTime);
				}

				String sendCommandCountEveryDay = PropertyUtil.getProperty(f.getAbsolutePath(),
						"sendCommandCountEveryDay");
				if (sendCommandCountEveryDay == null || "".equals(sendCommandCountEveryDay)) {
					throw new BussinessException(
							"You must specify the 'sendCommandCountEveryDay' property in properties file.");
				} else {
					factory.setSendCommandCountEveryDay(sendCommandCountEveryDay);
				}
			}
			
			String configUpdateProperties = PropertyUtil.getProperty(f.getAbsolutePath(), "configUpdateProperties");
			if (configUpdateProperties == null || "".equals(configUpdateProperties)) {
				throw new BussinessException(
						"You must specify the 'configUpdateProperties' property in properties file.");
			} else {
				factory.setConfigUpdateProperties(configUpdateProperties);
			}

			String configUpdateExecuteTime = PropertyUtil.getProperty(f.getAbsolutePath(), "configUpdateExecuteTime");
			if (configUpdateExecuteTime == null || "".equals(configUpdateExecuteTime)) {
				throw new BussinessException(
						"You must specify the 'configUpdateExecuteTime' property in properties file.");
			} else {
				if (!configUpdateExecuteTime.contains(":")) {
					throw new BussinessException("the 'configUpdateExecuteTime' content  is like 'HH:MM:SS'.");
				}
				factory.setConfigUpdateExecuteTime(configUpdateExecuteTime);
			}
			
			String firmwareUpdateDayOfEveryMonth = PropertyUtil.getProperty(f.getAbsolutePath(), "firmwareUpdateDayOfEveryMonth");
			if (firmwareUpdateDayOfEveryMonth == null || "".equals(firmwareUpdateDayOfEveryMonth)) {
				throw new BussinessException("You must specify the 'firmwareUpdateDayOfEveryMonth' property in properties file.");
			} else {
				factory.setFirmwareUpdateDayOfEveryMonth(firmwareUpdateDayOfEveryMonth);
			}
			
			
			String firmwareUpdateExecuteTime = PropertyUtil.getProperty(f.getAbsolutePath(), "firmwareUpdateExecuteTime");
			if (firmwareUpdateExecuteTime == null || "".equals(firmwareUpdateExecuteTime)) {
				throw new BussinessException("You must specify the 'firmwareUpdateExecuteTime' property in properties file.");
			} else {
				if (!firmwareUpdateExecuteTime.contains(":")) {
					throw new BussinessException("the 'firmwareUpdateExecuteTime' content  is like 'HH:MM:SS'.");
				}
				factory.setFirmwareUpdateExecuteTime(firmwareUpdateExecuteTime);
			}
			
			String storageAccountName = PropertyUtil.getProperty(f.getAbsolutePath(), "storageAccountName");
			if (storageAccountName == null || "".equals(storageAccountName)) {
				throw new BussinessException("You must specify the 'storageAccountName' property in properties file.");
			} else {
				factory.setStorageAccountName(storageAccountName);
			}
			
			String storageAccountKey = PropertyUtil.getProperty(f.getAbsolutePath(), "storageAccountKey");
			if (storageAccountKey == null || "".equals(storageAccountKey)) {
				throw new BussinessException("You must specify the 'storageAccountKey' property in properties file.");
			} else {
				factory.setStorageAccountKey(storageAccountKey);
			}
			
			String storageContainerName = PropertyUtil.getProperty(f.getAbsolutePath(), "storageContainerName");
			if (storageContainerName == null || "".equals(storageContainerName)) {
				throw new BussinessException("You must specify the 'storageContainerName' property in properties file.");
			} else {
				factory.setStorageContainerName(storageContainerName);
			}
			
			String firmwareFileName = PropertyUtil.getProperty(f.getAbsolutePath(), "firmwareFileName");
			if (firmwareFileName == null || "".equals(firmwareFileName)) {
				throw new BussinessException("You must specify the 'firmwareFileName' property in properties file.");
			} else {
				factory.setFirmwareFileName(firmwareFileName);
			}
			
			String storeConnectionStringFile = PropertyUtil.getProperty(f.getAbsolutePath(), "storeConnectionStringFile");
			if (storeConnectionStringFile == null || "".equals(storeConnectionStringFile)) {
				throw new BussinessException("You must specify the 'storeConnectionStringFile' property in properties file.");
			} else {
				factory.setStoreConnectionStringFile(storeConnectionStringFile);
			}

		}
		
		return factory;
	}
	
	private static void printHead() {
		StringBuffer sb = new StringBuffer();
		System.out.println("                   _ooOoo_");
		System.out.println("                  o8888888o");
		System.out.println("                  88\" . \"88");
		System.out.println("                  (| -_- |)");
		System.out.println("                  O\\  =  /O");
		System.out.println("               ____/`---'\\____");
		System.out.println("             .'  \\\\|     |//  `.");
		System.out.println("            /  \\\\|||  :  |||//  \\");
		System.out.println("           /  _||||| -:- |||||-  \\");
		System.out.println("           |   | \\\\\\  -  /// |   |");
		System.out.println("           | \\_|  ''\\---/''  |   |");
		System.out.println("           \\  .-\\__  `-`  ___/-. /");
		System.out.println("         ___`. .'  /--.--\\  `. . __");
		System.out.println("      .\"\" '<  `.___\\_<|>_/___.'  >'\"\".");
		System.out.println("     | | :  `- \\`.;`\\ _ /`;.`/ - ` : | |");
		System.out.println("     \\  \\ `-.   \\_ __\\ /__ _/   .-` /  /");
		System.out.println("======`-.____`-.___\\_____/___.-`____.-'======");
		System.out.println("                   `=---='                    ");
		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
		System.out.println(sb.toString());
	}

}
