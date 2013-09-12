package com.traffic.apn.news;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

public class ApnConfig {

	private static String defaultCommonConfigPath = null;

	private final static ApnConfig intance = new ApnConfig();

	private Properties properties = null;

	private ApnConfig() {
		init();
	}

	private void init() {
		properties = new Properties();
		try {
			properties.load(new FileInputStream(getDefaultCommonConfigPath()
					+ "/news.properties"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ApnConfig getInstance() {

		return intance;
	}

	/***
	 * 获取默认的配置包目录绝对路径
	 * 
	 * @return
	 */
	public synchronized String getDefaultCommonConfigPath() {
		if (defaultCommonConfigPath == null) {
			defaultCommonConfigPath = defaultTomcatCommonConfigPath();
		}
		return defaultCommonConfigPath;
	}

	private String defaultTomcatCommonConfigPath() {
		try {
			String cpath = ApnConfig.class.getResource("/").getPath();
			cpath = URLDecoder.decode(cpath, "UTF-8");
			final int posi = cpath.indexOf("deployments");
			if (posi > 0) {
				cpath = cpath.substring(0, posi);
				cpath = cpath + "common/news_config/";
			}
			return cpath;
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;

	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
