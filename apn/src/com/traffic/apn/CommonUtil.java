package com.traffic.apn;

import java.util.Random;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class CommonUtil {
	private static Logger log = Logger.getLogger(CommonUtil.class);
	private static Random r = new Random();

	private static Pattern mobile = Pattern.compile("^1\\d{10}$");

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
			String cmd[] = { "/home/root/openAccess", ip, mobile, "" };
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

	public static void main(String args[]) {
		System.out.println(isMobile(null));
		System.out.println(isMobile(""));
		System.out.println(isMobile("asdfaf"));
		System.out.println(isMobile("1231231231333"));
		System.out.println(isMobile("12345678909"));
	}
}
