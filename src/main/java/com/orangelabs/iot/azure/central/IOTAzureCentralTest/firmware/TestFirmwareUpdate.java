package com.orangelabs.iot.azure.central.IOTAzureCentralTest.firmware;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.microsoft.azure.sdk.iot.device.DeviceTwin.DeviceMethod;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties.GetAccessPropertiesFactory;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.PropertiesParseUtil;

public class TestFirmwareUpdate {
	
	Logger log = Logger.getLogger(TestFirmwareUpdate.class);

	private static TestFirmwareUpdate firm = new TestFirmwareUpdate();
	
	private static AtomicInteger connectionErrorCount = new AtomicInteger(0);

	private static AtomicInteger firmwareErrorCount = new AtomicInteger(0);

	private TestFirmwareUpdate() {

	}

	public static TestFirmwareUpdate getInstance() {
		return firm;
	}
	
	public void updateFirmwareSchedule(GetAccessPropertiesFactory factory) {

		String firmwareUpdateDayOfEveryMonth = factory.getFirmwareUpdateDayOfEveryMonth();
		String firmwareUpdateExecuteTime = factory.getFirmwareUpdateExecuteTime();

		Date firmwareDate = PropertiesParseUtil.getExecuteTime(firmwareUpdateExecuteTime);

		log.info("###### Execute firmware update on the day " + firmwareUpdateDayOfEveryMonth + " of every month");

		Integer fd = Integer.parseInt(firmwareUpdateDayOfEveryMonth);

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				Calendar c = Calendar.getInstance();
				int day = c.get(Calendar.DAY_OF_MONTH);
				log.info("Question: Today is the day to execute firmware update?");
				if (day == fd) {
					log.info("Answer: Yes, C'est partir..........");
					try {
						firmwareUpdate(factory);
					} catch (Exception e) {
						log.error("firmware update get error " + e.getMessage());
					}
				} else {
					log.info("Answer: No, so still waiting...............");
				}
			}
		}, firmwareDate, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行

	}

	public void firmwareUpdate(GetAccessPropertiesFactory factory) throws Exception {
		
		long begin = System.currentTimeMillis();
		
		String httpsURL = FirmwareDownloadUtil.getHttpsUrl(factory);

		firmwareupdateLoop(factory, httpsURL);
		
		long end = System.currentTimeMillis();
		log.info("today's firmware update is finished, cost time  " + ((end - begin) / 1000) + "s");

	}
	
	public void firmwareupdateLoop(GetAccessPropertiesFactory factory, String httpsURL) throws Exception {
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

			log.info("firmware update begin " + begin + " end " + end); 

			CountDownLatch latch = new CountDownLatch(caculate);
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(caculate);

			for (int i = begin; i <= end; i++) {
				String deviceId = deviceName + i;
				fixedThreadPool.execute(new FirmwareUpdateThread(factory, deviceId, connectionErrorCount, firmwareErrorCount,latch));
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
			log.info("firmware update loopcount " + loopcount + " finished........., cost time " + ((endtime - begintime) / 1000) + "s");
			loopcount--;
			//Thread.sleep(1000 * 60 * 5);
		} while (loopcount != 0);

		log.info("firmware update quan bu zhixing wanbi ");
		
		int connectionerror = connectionErrorCount.get();
		int firmerror = firmwareErrorCount.get();

		
		if (connectionerror != 0) {
			BigDecimal rate = new BigDecimal(connectionerror).divide(new BigDecimal(deviceCount)).setScale(4,
					BigDecimal.ROUND_HALF_UP);
			log.error(" firmware update connection errror count is  " + connectionerror
					+ " the failed rate is " + (rate.floatValue() * 100) + "%");
			connectionErrorCount.set(0);
		}

		if (firmerror != 0) {
			BigDecimal rate = new BigDecimal(firmerror).divide(new BigDecimal(deviceCount)).setScale(4,
					BigDecimal.ROUND_HALF_UP);
			log.error(" fois firmware update errror count is  " + firmerror + " the failed rate is "
					+ (rate.floatValue() * 100) + "%");
			firmwareErrorCount.set(0);
		}
	}

}
