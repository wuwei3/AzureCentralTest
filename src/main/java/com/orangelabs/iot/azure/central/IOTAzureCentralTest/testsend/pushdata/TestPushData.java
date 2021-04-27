package com.orangelabs.iot.azure.central.IOTAzureCentralTest.testsend.pushdata;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

import org.apache.log4j.Logger;

import com.orangelabs.iot.azure.central.IOTAzureCentralTest.core.properties.GetAccessPropertiesFactory;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.PropertiesParseUtil;

public class TestPushData {

	Logger log = Logger.getLogger(TestPushData.class);

	private static TestPushData pushdata = new TestPushData();

	private static LongAdder longAdder = new LongAdder();

	private static AtomicInteger sendCount = new AtomicInteger(1);

	private static AtomicInteger connectionErrorCount = new AtomicInteger(0);

	private static AtomicInteger pushDataErrorCount = new AtomicInteger(0);

	private TestPushData() {

	}

	public static TestPushData getInstance() {
		return pushdata;
	}

	public void executePushDataWithSpecifyTime(GetAccessPropertiesFactory factory) throws Exception {
		log.info("push data begin, need to wait the time arrives.");

		String time = factory.getPushDataExecuteTime();
		Date executetime = PropertiesParseUtil.getExecuteTime(time);

		if (factory.isMaster()) { // only the master need to listener
			//startRuleSubscription(factory);
			//startMysqlscription(factory);
		} else {
			log.info("it is not the master, no need to open republish listner");
		}

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				log.info("time is up, begin to push data");
				try {
					pushData(factory);
				} catch (Exception e) {
					log.info("push data for every min failed, message is " + e.getMessage());
					e.printStackTrace();
				}
			}
		}, executetime, 1000 * 432); // every 7.2 min push 

	}

//	public void startRuleSubscription(GetAccessPropertiesFactory factory) throws Exception {
//		log.info("start the rule trigger....");
//		String topic = factory.getRuleTriggerTopic();
//		String topicsub = factory.getRuleTriggerTopicSubscription();
//		String primaryString = factory.getServiceBusConnectionStringPrimaryKey();
//
//		String subname = topic + "/subscriptions/" + topicsub;
//		RuleTrigger.startRule(subname, primaryString, longAdder);
//	}
//
//	public void startMysqlscription(GetAccessPropertiesFactory factory) throws Exception {
//		log.info("start the mysql....");
//		String topic = factory.getMysqlTopic();
//		String topicsub = factory.getMysqlTopicSubscription();
//
//		String primaryString = factory.getServiceBusConnectionStringPrimaryKey();
//
//		String subname = topic + "/subscriptions/" + topicsub;
//		MysqlSubcribe.startMySqlSubscribe(subname, primaryString, factory);
//
//	}

	public void pushData(GetAccessPropertiesFactory factory) throws Exception {
		long begin = System.currentTimeMillis();
		int countTen = sendCount.getAndIncrement();
		long ruleCount = longAdder.longValue();
		if (countTen > 1) {
			log.info(" after push end ruleCount " + ruleCount);
		}

		int connectionerror = connectionErrorCount.get();
		int pushdataerror = pushDataErrorCount.get();
		
		Integer deviceNumberCount = Integer.valueOf(factory.getDeviceNumberCount());

		int counterror = countTen;
		if (connectionerror != 0) {
			BigDecimal rate = new BigDecimal(connectionerror).divide(new BigDecimal(deviceNumberCount)).setScale(4,
					BigDecimal.ROUND_HALF_UP);
			log.error((counterror - 1) + " fois  push data connection errror count is  " + connectionerror
					+ " the failed rate is " + (rate.floatValue() * 100) + "%");
			connectionErrorCount.set(0);
		}

		if (pushdataerror != 0) {
			BigDecimal rate = new BigDecimal(pushdataerror).divide(new BigDecimal(deviceNumberCount)).setScale(4,
					BigDecimal.ROUND_HALF_UP);
			log.error((counterror - 1) + " fois push data  errror count is  " + pushdataerror + " the failed rate is "
					+ (rate.floatValue() * 100) + "%");
			pushDataErrorCount.set(0);
		}
		
		//insert data last time storage in list
//		if (factory.isMaster()) {
//			Set<PushData> list = MysqlSubcribe.getList();
//			if (list != null && list.size() > 0) {
//				log.info("mysql insert, data size " + list.size());
//				new MySqlInsertThread(factory, list).start();
//				Thread.sleep(1000);
//				MysqlSubcribe.setEmpty();
//			} else {
//				log.info("mysql insert, no data this time!!!!!");
//			}
//		}

		sendDataLoop(factory, countTen);

		if (countTen == 200) {
			log.info("countTen reach the 200, means the day is over, reset to 1");
			sendCount.set(1);
		} else {
			log.info("countTen not reach the 200, still wait...");
		}

//		if (factory.isMaster()) {
//			if (countTen == 120) {
//				String date = DateUtil.getDate(DateUtil.datePattern, new Date());
//				String type = "";
//				if (factory.getType() == 1) {
//					type = "sensor";
//				} else {
//					type = "actuator";
//				}
//				new RedisThread(date, type, String.valueOf(ruleCount)).start();
//				longAdder.sumThenReset();
//			}
//		}

		long end = System.currentTimeMillis();
		log.info(" the " + (countTen) + "  fois has been send finished, cost time  " + ((end - begin) / 1000) + "s");

	}

	public void sendDataLoop(GetAccessPropertiesFactory factory, int countTen) throws Exception {
		String deviceName = factory.getDeviceName();

		String deviceNumberCount = factory.getDeviceNumberCount();

		Integer beginindex = factory.getBeginIndex();

		Integer endindex = factory.getEndIndex();

		Integer deviceCount = Integer.parseInt(deviceNumberCount);

		Integer threadpoolsize = factory.getPushThreadPool();
		int caculate = threadpoolsize;

		int begin = beginindex;
		int end = begin + (caculate - 1);

		int loopcount = deviceCount / caculate;

		do {
			long begintime = System.currentTimeMillis();

			log.info("push data begin " + begin + " end " + end);

			CountDownLatch latch = new CountDownLatch(caculate);
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(caculate);

			for (int i = begin; i <= end; i++) {
				String deviceId = deviceName + i;
				fixedThreadPool.execute(new PushDataThread(factory, deviceId, countTen, connectionErrorCount,
						pushDataErrorCount, latch));
			}
			
			try {
				latch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				fixedThreadPool.shutdown();
			}

//			fixedThreadPool.shutdown();
//			try {
//				fixedThreadPool.shutdown();
//				if (!fixedThreadPool.awaitTermination(40, TimeUnit.SECONDS)) {
//					log.error("push data -> device " + begin + " to  " + end + " cost more than 40s for connection and publish, need to shutdown the thread now!!!");
//					fixedThreadPool.shutdownNow();
//				}
//			} catch (Throwable e) {
//				fixedThreadPool.shutdownNow();
//				e.printStackTrace();
//			}

			Thread.sleep(1000);

			begin = begin + caculate;
			end = end + caculate;
			long endtime = System.currentTimeMillis();
			log.info("push data loopcount " + loopcount + " finished........., cost time "
					+ ((endtime - begintime) / 1000) + "s");
			loopcount--;
		} while (loopcount != 0);

		log.info(" push data  zhixing wanbi on " + countTen + " fois");
	}

}
