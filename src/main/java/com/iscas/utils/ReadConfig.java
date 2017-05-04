/**
 * 
 */
package com.iscas.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Summer on 2017/1/11.
 */
public class ReadConfig {

	public static String getConfigValue(ClassLoader cl, String key) {
		InputStream in = cl.getResourceAsStream("/param.properties");
		Properties prop = new Properties();
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String value = prop.getProperty(key, "none");
		return value;
	}
}
