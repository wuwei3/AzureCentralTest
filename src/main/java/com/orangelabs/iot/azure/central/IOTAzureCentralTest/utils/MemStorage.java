package com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils;

import com.github.lucadruda.iotc.device.ICentralStorage;
import com.github.lucadruda.iotc.device.models.Storage;

public class MemStorage implements ICentralStorage{

	@Override
	public void persist(Storage storage) {
	}

	@Override
	public Storage retrieve() {
		return new Storage();
	}

}
