package com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.model;

public class ConfigUpdate {
	
	private Long periodSimple = 0l;
	
	private String updateVersion = "no version";

	public Long getPeriodSimple() {
		return periodSimple;
	}

	public void setPeriodSimple(Long periodSimple) {
		this.periodSimple = periodSimple;
	}

	public String getUpdateVersion() {
		return updateVersion;
	}

	public void setUpdateVersion(String updateVersion) {
		this.updateVersion = updateVersion;
	}
}
