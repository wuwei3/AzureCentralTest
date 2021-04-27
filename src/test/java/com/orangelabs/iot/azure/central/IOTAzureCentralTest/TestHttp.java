package com.orangelabs.iot.azure.central.IOTAzureCentralTest;

import com.alibaba.fastjson.JSONObject;
import com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils.HttpRequest;

public class TestHttp {

	public static void main(String[] args) {
        String token = "SharedAccessSignature sr=110b00bb-4997-4ca5-b584-f527ff17839b&sig=PDieQsEMG9ey2Z1crfxeSgykEMnZBVsgAuWnyZFB%2FH8%3D&skn=token2&se=1643366855289";
		
//        String url = "https://olcsimulationcentral.azureiotcentral.com/api/preview/devices/send2/credentials";
//        
//        String result = HttpRequest.doGetForHttps(url, "", null, token);
//        System.out.println(result);
        
        String url2 = "https://olcsimulationcentral.azureiotcentral.com/api/preview/devices/send2/commands/sendConfigToSensor";
        String url3 = "https://olcsimulationcentral.azureiotcentral.com/api/preview/devices/%s/commands/%s";
        
        String url = String.format(url3, "send2", "sendConfigToSensor");
        //System.out.println(url);
        
        
//        JSONObject j = new JSONObject();
//        j.put("per", 32);
//        
//        JSONObject j2 = new JSONObject();
//        j2.put("request", j);
//        
//        String result2 = HttpRequest.doPostForHttps(url2, j2.toJSONString(), null, token);
//        System.out.println(result2);
        
        //String url4 = "https://olcsimulationcentral.azureiotcentral.com/api/preview/devices/OLCSensor1/properties";
        String url4 = "https://olcsimulationcentral.azureiotcentral.com/api/preview/devices/OLCSensor1/components/testforsensor318/properties";
        
        
        JSONObject j = new JSONObject();
        j.put("firmwareUpdate", "2");
        
        String result2 = HttpRequest.doPatchForHttps(url4, j.toJSONString(), null, token);
        System.out.println(result2);
        

	}

}
