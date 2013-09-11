package com.traffic.apn;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class ApnConfig {

	private static String defaultCommonConfigPath = null;

	private final static ApnConfig intance = new ApnConfig();

	

	private ApnConfig() {
		
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
			final int posi = cpath.indexOf("webapps");
			if (posi > 0) {
				cpath = cpath.substring(0, posi);
				cpath = cpath + "common/apn_config/";
			}
			return cpath;
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
