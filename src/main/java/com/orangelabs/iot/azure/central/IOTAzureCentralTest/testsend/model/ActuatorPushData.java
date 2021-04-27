package com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.model;

public class ActuatorPushData extends PushData {

	private Integer electcomsuption;
	
	private String trunOff = "wait the commands";
	
	private String color = "no color";
	
	private Integer type = 2;
	
	public Integer getElectcomsuption() {
		return electcomsuption;
	}

	public void setElectcomsuption(Integer electcomsuption) {
		this.electcomsuption = electcomsuption;
	}

	public String getTrunOff() {
		return trunOff;
	}

	public void setTrunOff(String trunOff) {
		this.trunOff = trunOff;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
