package com.traffic.apn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class CommonUtil {
	private static Logger log = Logger.getLogger(CommonUtil.class);
	private static Random r = new Random();

	private static Pattern mobile = Pattern.compile("^1\\d{10}$");

	private static Pattern macPattern = Pattern.compile("..:..:..:..:..:..");

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static boolean isMobile(String s) {
		return !isEmpty(s) && mobile.matcher(s).matches();
	}

	public static String genCode() {
		return String.valueOf(r.nextInt(900000) + 100000);
	}

	public static String getRequestIp(HttpServletRequest request) {
		String ip;
		if (request.getHeader("x-forwarded-for") == null) {
			ip = request.getRemoteAddr();
		} else {
			ip = request.getHeader("x-forwarded-for");
		}
		return ip;
	}

	public static boolean openAccess(String ip, String mobile) {
		// call pass apn
		try {
			String cmd[] = {
					ApnConfig.getInstance().getProperties()
							.getProperty("net.exe"), ip, mobile, "" };
			Process p = Runtime.getRuntime().exec(cmd);
			int ret = p.waitFor();
			log.info("open access ret=" + ret + ",mobile=" + mobile + ",ip="
					+ ip);
			return ret == 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// return true;
		}
	}

	public static String getMac(String ip) {
		String mac = null;
		try {
			String cmd[] = {
					ApnConfig.getInstance().getProperties()
							.getProperty("arp.exe"), "-a", ip };
			Process p = Runtime.getRuntime().exec(cmd);
			int ret = p.waitFor();
			if (ret == 0) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				String s = br.readLine();
				if (!isEmpty(s)) {
					mac = macPattern.matcher(s).group(0);
				}
			}

			log.info("get mac by ip=" + ip + ",mac=" + mac);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mac;
	}

	public static void main(String args[]) {
		System.out.println(isMobile(null));
		System.out.println(isMobile(""));
		System.out.println(isMobile("asdfaf"));
		System.out.println(isMobile("1231231231333"));
		System.out.println(isMobile("12345678909"));

		System.out.println(macPattern.matcher(
				"? (192.168.145.2) at 00:50:56:e1:1a:51 [ether] on eth0")
				.group(0));

	}
}
