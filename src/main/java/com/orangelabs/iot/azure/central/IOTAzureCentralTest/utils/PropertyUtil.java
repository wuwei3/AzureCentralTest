package com.orangelabs.iot.azure.central.IOTAzureCentralTest.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
	
	private static Properties props;
	  
    synchronized static private void loadProps(String path) throws FileNotFoundException{  
        props = new Properties();
        
        InputStream in = null;
        try {
        	in = new BufferedInputStream(new FileInputStream(  
                    new File(path)));  
  
        	props.load(in);  
        } catch (FileNotFoundException e) {  
            throw new FileNotFoundException("not found the file " + path);
        } catch (IOException e) {  
            e.printStackTrace(); 
        } finally {  
            try {  
                if(null != in) {  
                    in.close();  
                }  
            } catch (IOException e) {
            	
            }  
        } 
    }  
  
    public static String getProperty(String path, String key) throws FileNotFoundException{  
        if(null == props) {  
            loadProps(path);  
        }  
        return props.getProperty(key);  
    }
    
    public static void setProperty(String file, String key, String value) {
    	try {
			//if(null == props) {  
				InputStream in = new PropertyUtil().getClass().getResourceAsStream(file);
			    loadProps(file);  
			//}
			
			FileOutputStream oFile = new FileOutputStream(file, true);//true表示追加打开
			props.setProperty(key, value);
			props.store(oFile, null);
			oFile.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
  
}
