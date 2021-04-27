package com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties;

import java.util.concurrent.ConcurrentHashMap;

import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.connection.ConnectionSuccess;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.model.PushData;

public class GetAccessPropertiesFactory {

	private String IOTCentralName;

	private String deviceName;

	private String deviceNumberCount;
	
    private String pushDataProperties;
	
	private String pushDataExecuteTime;
	
	private String configUpdateProperties;
	
	private String configUpdateExecuteTime;
	
	//actuator send commands
	private String sendCommandProperties;
			
	private String sendCommandExecuteTime;
		
	private String sendCommandCountEveryDay;

	private String firmwareUpdateDayOfEveryMonth;

	private String firmwareUpdateExecuteTime;
	
	private String storageAccountName;
	
	private String storageAccountKey;
	
	private String storageContainerName;
	
	private String firmwareFileName;

	private ConcurrentHashMap<String, ConnectionSuccess> devicesConnectionMap = new ConcurrentHashMap<>();
	
	// init push data json obejct for begin and end
	private ConcurrentHashMap<String, PushData> deviceObjectMap = new ConcurrentHashMap<>();
		
	// init push data json obejct for all devices of firmware update
	private ConcurrentHashMap<String, PushData> firmwareMap = new ConcurrentHashMap<>();

	// for build on 5, 1-1000, 1001-2000, 2001-3000,3001-4000, 4001-5000
	private Integer beginIndex = 2;

	private Integer endIndex = 11;

	// 1-1000 is master
	private boolean master = true;

	// 1001-3000 3001-5000 is 40, 1-1000 is 20 for actuator, because it only support 100/sec
	private Integer pushThreadPool = 10;

	// 1 sensor; 2 actuaotr
	private Integer type;
	
	private String storeConnectionStringFile;
	
	private String scopeId;
	
	private String centralAPIToken;
	
	private String propertyComponentName;

	public String getIOTCentralName() {
		return IOTCentralName;
	}

	public void setIOTCentralName(String iOTCentralName) {
		IOTCentralName = iOTCentralName;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceNumberCount() {
		return deviceNumberCount;
	}

	public void setDeviceNumberCount(String deviceNumberCount) {
		this.deviceNumberCount = deviceNumberCount;
	}

	public String getSendCommandCountEveryDay() {
		return sendCommandCountEveryDay;
	}

	public void setSendCommandCountEveryDay(String sendCommandCountEveryDay) {
		this.sendCommandCountEveryDay = sendCommandCountEveryDay;
	}
	
	public String getPushDataProperties() {
		return pushDataProperties;
	}

	public void setPushDataProperties(String pushDataProperties) {
		this.pushDataProperties = pushDataProperties;
	}

	public String getPushDataExecuteTime() {
		return pushDataExecuteTime;
	}

	public void setPushDataExecuteTime(String pushDataExecuteTime) {
		this.pushDataExecuteTime = pushDataExecuteTime;
	}


	public String getConfigUpdateProperties() {
		return configUpdateProperties;
	}

	public void setConfigUpdateProperties(String configUpdateProperties) {
		this.configUpdateProperties = configUpdateProperties;
	}

	public String getSendCommandProperties() {
		return sendCommandProperties;
	}

	public void setSendCommandProperties(String sendCommandProperties) {
		this.sendCommandProperties = sendCommandProperties;
	}

	public String getSendCommandExecuteTime() {
		return sendCommandExecuteTime;
	}

	public void setSendCommandExecuteTime(String sendCommandExecuteTime) {
		this.sendCommandExecuteTime = sendCommandExecuteTime;
	}

	public String getConfigUpdateExecuteTime() {
		return configUpdateExecuteTime;
	}

	public void setConfigUpdateExecuteTime(String configUpdateExecuteTime) {
		this.configUpdateExecuteTime = configUpdateExecuteTime;
	}

	public String getFirmwareUpdateDayOfEveryMonth() {
		return firmwareUpdateDayOfEveryMonth;
	}

	public void setFirmwareUpdateDayOfEveryMonth(String firmwareUpdateDayOfEveryMonth) {
		this.firmwareUpdateDayOfEveryMonth = firmwareUpdateDayOfEveryMonth;
	}

	public String getFirmwareUpdateExecuteTime() {
		return firmwareUpdateExecuteTime;
	}

	public void setFirmwareUpdateExecuteTime(String firmwareUpdateExecuteTime) {
		this.firmwareUpdateExecuteTime = firmwareUpdateExecuteTime;
	}

	public String getStorageAccountName() {
		return storageAccountName;
	}

	public void setStorageAccountName(String storageAccountName) {
		this.storageAccountName = storageAccountName;
	}

	public String getStorageAccountKey() {
		return storageAccountKey;
	}

	public void setStorageAccountKey(String storageAccountKey) {
		this.storageAccountKey = storageAccountKey;
	}

	public String getStorageContainerName() {
		return storageContainerName;
	}

	public void setStorageContainerName(String storageContainerName) {
		this.storageContainerName = storageContainerName;
	}

	public String getFirmwareFileName() {
		return firmwareFileName;
	}

	public void setFirmwareFileName(String firmwareFileName) {
		this.firmwareFileName = firmwareFileName;
	}

	public ConcurrentHashMap<String, ConnectionSuccess> getDevicesConnectionMap() {
		return devicesConnectionMap;
	}

	public void setDevicesConnectionMap(ConcurrentHashMap<String, ConnectionSuccess> devicesConnectionMap) {
		this.devicesConnectionMap = devicesConnectionMap;
	}

	public ConcurrentHashMap<String, PushData> getDeviceObjectMap() {
		return deviceObjectMap;
	}

	public void setDeviceObjectMap(ConcurrentHashMap<String, PushData> deviceObjectMap) {
		this.deviceObjectMap = deviceObjectMap;
	}

	public ConcurrentHashMap<String, PushData> getFirmwareMap() {
		return firmwareMap;
	}

	public void setFirmwareMap(ConcurrentHashMap<String, PushData> firmwareMap) {
		this.firmwareMap = firmwareMap;
	}

	public Integer getBeginIndex() {
		return beginIndex;
	}

	public void setBeginIndex(Integer beginIndex) {
		this.beginIndex = beginIndex;
	}

	public Integer getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(Integer endIndex) {
		this.endIndex = endIndex;
	}

	public boolean isMaster() {
		return master;
	}

	public void setMaster(boolean master) {
		this.master = master;
	}

	public Integer getPushThreadPool() {
		return pushThreadPool;
	}

	public void setPushThreadPool(Integer pushThreadPool) {
		this.pushThreadPool = pushThreadPool;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getStoreConnectionStringFile() {
		return storeConnectionStringFile;
	}

	public void setStoreConnectionStringFile(String storeConnectionStringFile) {
		this.storeConnectionStringFile = storeConnectionStringFile;
	}

	public String getScopeId() {
		return scopeId;
	}

	public void setScopeId(String scopeId) {
		this.scopeId = scopeId;
	}

	public String getCentralAPIToken() {
		return centralAPIToken;
	}

	public void setCentralAPIToken(String centralAPIToken) {
		this.centralAPIToken = centralAPIToken;
	}

	public String getPropertyComponentName() {
		return propertyComponentName;
	}

	public void setPropertyComponentName(String propertyComponentName) {
		this.propertyComponentName = propertyComponentName;
	}
}
