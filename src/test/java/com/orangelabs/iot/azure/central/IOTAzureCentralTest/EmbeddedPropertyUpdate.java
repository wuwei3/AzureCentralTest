package com.orangelabs.iot.azure.central.IOTAzureCentralTest;

public class EmbeddedPropertyUpdate {

    public Object value;
 
    public Integer ackCode;
    
    public Integer ackVersion;
    
    public String ackDescription;
    
    public EmbeddedPropertyUpdate() {
	}

	public EmbeddedPropertyUpdate(Object value, Integer ackCode, Integer ackVersion, String ackDescription) {
		super();
		this.value = value;
		this.ackCode = ackCode;
		this.ackVersion = ackVersion;
		this.ackDescription = ackDescription;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Integer getAckCode() {
		return ackCode;
	}

	public void setAckCode(Integer ackCode) {
		this.ackCode = ackCode;
	}

	public Integer getAckVersion() {
		return ackVersion;
	}

	public void setAckVersion(Integer ackVersion) {
		this.ackVersion = ackVersion;
	}

	public String getAckDescription() {
		return ackDescription;
	}

	public void setAckDescription(String ackDescription) {
		this.ackDescription = ackDescription;
	}
}
