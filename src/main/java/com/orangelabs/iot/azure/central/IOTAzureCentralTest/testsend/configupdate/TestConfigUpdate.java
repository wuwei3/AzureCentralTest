package com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.configupdate;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties.GetAccessPropertiesFactory;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.PropertiesParseUtil;


public class TestConfigUpdate {
	
	Logger log = Logger.getLogger(TestConfigUpdate.class);

	public static TestConfigUpdate configupdate = new TestConfigUpdate();
	
	private static AtomicInteger connectionErrorCount = new AtomicInteger(0);

	private static AtomicInteger configErrorCount = new AtomicInteger(0);

	private TestConfigUpdate() {

	}

	public static TestConfigUpdate getInstance() {
		return configupdate;
	}

	public void executeConfigUpdateWithSpecifyTime(GetAccessPropertiesFactory factory) throws Exception {
		log.info("config update is waiting the time arrives.");

		String time = factory.getConfigUpdateExecuteTime();
		Date executetime = PropertiesParseUtil.getExecuteTime(time);

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				log.info("time is up, begin to config update");
				try {
					testConfigUpdate(factory);
				} catch (Exception e) {
					log.info("config update for everyday failed, message is " + e.getMessage());
					e.printStackTrace();
				}
			}
		}, executetime, 1000 * 60 * 60 * 24);//

	}

	public void testConfigUpdate(GetAccessPropertiesFactory factory) throws Exception {
		
		String deviceNumberCount = factory.getDeviceNumberCount();
		Integer deviceCount = Integer.parseInt(deviceNumberCount);
		
		int connectionerror = connectionErrorCount.get();
		int configerror = configErrorCount.get();

		if (connectionerror != 0) {
			BigDecimal rate = new BigDecimal(connectionerror).divide(new BigDecimal(deviceCount)).setScale(4,
					BigDecimal.ROUND_HALF_UP);
			log.error(" last fois  config update connection errror count is  " + connectionerror
					+ " the failed rate is " + (rate.floatValue() * 100) + "%");
			connectionErrorCount.set(0);
		}

		if (configerror != 0) {
			BigDecimal rate = new BigDecimal(configerror).divide(new BigDecimal(deviceCount)).setScale(4,
					BigDecimal.ROUND_HALF_UP);
			log.error("last fois config update errror count is  " + configerror + " the failed rate is "
					+ (rate.floatValue() * 100) + "%");
			configErrorCount.set(0);
		}
		
		long begin = System.currentTimeMillis();

		configupdateLoop(factory);

		long end = System.currentTimeMillis();
		log.info("today's config update is finished, cost time  " + ((end - begin) / 1000) + "s");
	}

	public void configupdateLoop(GetAccessPropertiesFactory factory) throws Exception {
		String deviceName = factory.getDeviceName();

		String deviceNumberCount = factory.getDeviceNumberCount();

		Integer beginindex = factory.getBeginIndex();

		Integer endindex = factory.getEndIndex();

		Integer threadpoolsize = factory.getPushThreadPool();

		Integer deviceCount = Integer.parseInt(deviceNumberCount);
		int caculate = threadpoolsize;
		//int caculate = 4;

		int begin = beginindex;
		int end = begin + (caculate - 1);

		int loopcount = deviceCount / caculate;

		do {
			long begintime = System.currentTimeMillis();

			log.info("config update begin " + begin + " end " + end); 

			CountDownLatch latch = new CountDownLatch(caculate);
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(caculate);

			for (int i = begin; i <= end; i++) {
				String deviceId = deviceName + i;
				fixedThreadPool.execute(new ConfigUpdateThread(factory, deviceId, connectionErrorCount, configErrorCount, latch));
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
			
			long endtime = System.currentTimeMillis();
			log.info("confgi update loopcount " + loopcount + " finished........., cost time " + ((endtime - begintime) / 1000) + "s");
			loopcount--;
			//Thread.sleep(1000 * 60 * 5);
		} while (loopcount != 0);

		log.info("config update quan bu zhixing wanbi ");
	}

}
