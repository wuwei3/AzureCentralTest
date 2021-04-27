package com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class PropertiesParseUtil {

	public static List<Integer> getRandomNumberInDeviceCount(String deviceNumberCount,
			String sendCommandCountEveryDay) {
		System.out.println("generating the random device number.");

		// how many device created
		Integer devicecount = Integer.parseInt(deviceNumberCount);

		// how many commands send count
		Integer commandcount = Integer.parseInt(sendCommandCountEveryDay);

		List<Integer> deviceThingNumbers = new ArrayList<Integer>();

		do {
			Integer ran = getRandom(devicecount);
			if (!deviceThingNumbers.contains(ran)) {
				deviceThingNumbers.add(ran);
			}
		} while (deviceThingNumbers.size() != commandcount);

		System.out.println("generating the random device number finished.");
		return deviceThingNumbers;
	}

	public static Integer getRandom(int range) {
		Random random1 = new Random();
		return random1.nextInt(range) + 1;
	}
	
	public static Integer getRandom(int min, int max){
	    Random random = new Random();
	    int s = random.nextInt(max) % (max - min + 1) + min;
	    return s;

	}

	public static Date getExecuteTime(String time) {
		String[] times = time.split(":");

		String hh = times[0];
		String mm = times[1];
		String ss = times[2];

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hh.trim()));
		calendar.set(Calendar.MINUTE, Integer.parseInt(mm.trim()));
		calendar.set(Calendar.SECOND, Integer.parseInt(ss.trim()));

		Date executetime = calendar.getTime();

		return executetime;
	}

	public static String getDeviceConnection(String iothubname, String deviceId, String shareaccesskey) {

		String hostname = "HostName=" + iothubname + ".azure-devices.net;";
		String device = "DeviceId=" + deviceId + ";";
		String accesskey = "SharedAccessKey=" + shareaccesskey;

		String conn = hostname + device + accesskey;
		return conn;

	}

//	public static Map<String, String> getConnnectionMap(String iothuname, String deviceName, Integer deviceCount,
//			RegistryManager registryManager) {
//		Map<String, String> map = new HashMap<>();
//
//		Device device = null;
//		try {
//
//			for (int i = 0; i < deviceCount; i++) {
//				String deviceName2 = deviceName + (i + 1);
//
//				device = registryManager.getDevice(deviceName2);
//				String accesskey = device.getPrimaryKey();
//				String connection = PropertiesParseUtil.getDeviceConnection(iothuname, deviceName2, accesskey);
//
//				map.put(deviceName2, connection);
//			}
//		} catch (JsonSyntaxException e) {
//			System.out.println("JsonSyntaxException, message is " + e.getMessage());
//		} catch (IOException e) {
//			System.out.println("IOException, message is " + e.getMessage());
//		} catch (IotHubException e) {
//			System.out.println("IotHubException, message is " + e.getMessage());
//		}
//
//		return map;
//	}

	public static Map<String, String> readFile(String iothuname,String fileName) throws Exception {

		ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

		File filename = new File(fileName); // 要读取以上路径的input。txt文件
		InputStreamReader reader = new InputStreamReader(new FileInputStream(filename)); // 建立一个输入流对象reader
		BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
		String line = ""; // 每一行的内容
		int i = 1;
		while ((line = br.readLine()) != null) {
			String[] s = line.split("@");

			String deviceid = s[0];
			String pa = s[1];

			String connString = getDeviceConnection(iothuname, deviceid, pa);

			map.put(s[0], connString);
		}
		reader.close();
		br.close();

		System.out.println("final maop " + map.size());

		return map;
	}

}
