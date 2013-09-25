package com.traffic.apn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class CommonUtil {
	private static Logger log = Logger.getLogger(CommonUtil.class);
	private static Random r = new Random();

	private static Pattern mobile = Pattern.compile("^1\\d{10}$");

	private static Pattern macPattern = Pattern.compile("(\\w{2}:){5}\\w{2}");

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
					Matcher m = macPattern.matcher(s);
					if (m.find()) {
						mac = m.group();
					}
				}
			}

			log.info("get mac by ip=" + ip + ",mac=" + mac);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mac;
	}

	// mkdir /mnt/tmpfs
	// mount tmpfs /mnt/tmpfs -t tmpfs
	public static boolean isLogin(String mac) {
		File f = null;
		BufferedReader br = null;
		try {
			f = new File("/mnt/tmpfs/users");
			if (!f.exists()) {
				return false;
			}
			br = new BufferedReader(new InputStreamReader(
					new FileInputStream(f)));
			String s;
			while ((s = br.readLine()) != null) {
				if (s.indexOf(mac) > -1) {
					return true;
				}
			}

			return false;

		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean saveLogin(String mac, String ip, String mobile) {

		try {
			File f = null;
			BufferedWriter br = null;
			f = new File("/mnt/tmpfs/users");
			if (!f.exists()) {
				f.createNewFile();
			}

			br = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(f)));
			br.append(mac + "|" + ip + "|" + mobile + "|"
					+ System.currentTimeMillis());

			br.close();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String args[]) {

		System.out.println(isMobile(null));
		System.out.println(isMobile(""));
		System.out.println(isMobile("asdfaf"));
		System.out.println(isMobile("1231231231333"));
		System.out.println(isMobile("12345678909"));

		// ? (192.168.145.2) at 00:50:56:e1:1a:51 [ether] on eth0
		Matcher m = macPattern
				.matcher("? (192.168.145.2) at 00:50:56:e1:1a:51 [ether] on eth0");

		if (m.find()) {
			System.out.println(m.group());
		}

	}
}
