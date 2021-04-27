package com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.connection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties.GetAccessPropertiesFactory;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.model.PushData;

public class InitDeviceConnection {
	
	static Logger log = Logger.getLogger(InitDeviceConnection.class);
	
	public static void initEveryDeviceConnection(GetAccessPropertiesFactory factory) throws Exception{
		long beginTime = System.currentTimeMillis();
		
		String deviceName = factory.getDeviceName();
		String deviceNumberCount = factory.getDeviceNumberCount();
		Integer deviceCount = Integer.parseInt(deviceNumberCount);
		Integer beginindex = factory.getBeginIndex();
		Integer threadpoolsize = factory.getPushThreadPool();

		int caculate = threadpoolsize;
//		if (factory.getType() == 1) { // sensor no need to open at begin
//			caculate = threadpoolsize;
//		} else {
//			caculate = 10; // start actuator one by one, not at the same time
//		}
		
		//int caculate = threadpoolsize;
		int begin = beginindex;
		int end = begin + (caculate - 1);
		
		ConcurrentHashMap<String, ConnectionSuccess> pushDataConnectionMap = factory.getDevicesConnectionMap();
		
		ConcurrentHashMap<String, PushData> deviceObjectMap = factory.getDeviceObjectMap();
		
		int loopcount = deviceCount / caculate;
		
		do {
			log.info("connection init begin " + begin + "   end " + end);

			long begintime2 = System.currentTimeMillis();
			CountDownLatch latch = new CountDownLatch(caculate);
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(caculate);

			for (int i = begin; i <= end; i++) {
				String deviceId = deviceName + i;
				fixedThreadPool.execute(new ConnectionSetThread(factory, deviceId, latch, pushDataConnectionMap, deviceObjectMap));
			}

			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				fixedThreadPool.shutdown();
			}

			begin = begin + caculate;
			end = end + caculate;
			long endtime2 = System.currentTimeMillis();
			log.info("connection loopcount " + loopcount + " finished........., cost time " + ((endtime2 - begintime2) / 1000) + "s");
			loopcount--;
//			if (factory.getType() == 1) {
//				Thread.sleep(2000);
//			} else {
//				log.info("sleep 1 min for actuator");
//				Thread.sleep(1000 * 60 * 1);
//			}
//			Thread.sleep(2000);
		} while (loopcount != 0);
		
		long endTime = System.currentTimeMillis();
		log.info(" connection set thread  has been finished, cost time  " + ((endTime - beginTime) / 1000) + "s");
		log.info(" push data map for connection  " + factory.getDevicesConnectionMap().size());
		log.info(" push data map for obeject  " + factory.getDeviceObjectMap().size());
		
		
	}
}
