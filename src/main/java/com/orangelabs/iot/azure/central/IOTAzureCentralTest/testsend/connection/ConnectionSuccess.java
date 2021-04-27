package com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.connection;

import com.github.lucadruda.iotc.device.IoTCClient;

public class ConnectionSuccess {
	
	private IoTCClient client;
	
	private boolean isOpen;
	
	private Integer type;
	
	private boolean isSubscribeCommands;
	
	private boolean isSubscribeConfigUpdate;

	public IoTCClient getClient() {
		return client;
	}

	public void setClient(IoTCClient client) {
		this.client = client;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public boolean isSubscribeCommands() {
		return isSubscribeCommands;
	}

	public void setSubscribeCommands(boolean isSubscribeCommands) {
		this.isSubscribeCommands = isSubscribeCommands;
	}

	public boolean isSubscribeConfigUpdate() {
		return isSubscribeConfigUpdate;
	}

	public void setSubscribeConfigUpdate(boolean isSubscribeConfigUpdate) {
		this.isSubscribeConfigUpdate = isSubscribeConfigUpdate;
	}
}
