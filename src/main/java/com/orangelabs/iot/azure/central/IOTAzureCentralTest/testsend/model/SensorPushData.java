package com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.model;

public class SensorPushData extends PushData {

	private Integer temperature;
	
	private String humidity;
	
	private String co2;
	
	private String pollution;
	
	public Integer getTemperature() {
		return temperature;
	}
	public void setTemperature(Integer temperature) {
		this.temperature = temperature;
	}
	public String getHumidity() {
		return humidity;
	}
	public void setHumidity(String humidity) {
		this.humidity = humidity;
	}
	public String getCo2() {
		return co2;
	}
	public void setCo2(String co2) {
		this.co2 = co2;
	}
	public String getPollution() {
		return pollution;
	}
	public void setPollution(String pollution) {
		this.pollution = pollution;
	}
}
