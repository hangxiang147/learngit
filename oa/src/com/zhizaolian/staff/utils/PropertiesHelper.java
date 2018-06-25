package com.zhizaolian.staff.utils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesHelper {
	private final static String BASICURL = "/oa";
	private final static String SUFFIX = "properties";
	private static PropertiesHelper propertiesHelper;

	private PropertiesHelper() {
	}

	public static synchronized PropertiesHelper getInstance() {
		if (propertiesHelper == null) {
			propertiesHelper = new PropertiesHelper();
			return propertiesHelper;
		}
		return propertiesHelper;
	}

	private static Map<String, Properties> map = new HashMap<>();

	public Properties getProperties(String fileName) {
		try {
			Properties properties = map.get(fileName);
			if (properties == null) {
				try (InputStream is = getClass()
						.getResourceAsStream(BASICURL + File.separator + fileName + "." + SUFFIX)) {
					properties = new Properties();
					properties.load(is);
					map.put(fileName, properties);
					return properties;
				}
			}
		} catch (Exception e) {
			new RuntimeException(e);
		}
		return null;
	}

	public String getProperty(String fileName, String key) {
		try {
			Properties properties = getProperties(fileName);
			return properties.getProperty(key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
