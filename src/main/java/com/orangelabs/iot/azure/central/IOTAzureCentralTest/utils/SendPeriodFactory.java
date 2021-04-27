package com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils;

public class SendPeriodFactory {
	
	private static SendPeriodFactory factory = new SendPeriodFactory();
	
	private String period;
	
	private SendPeriodFactory() {
		
	}
	
	public static SendPeriodFactory getInstance() {
		return factory;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

}
