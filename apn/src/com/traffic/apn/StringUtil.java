package com.traffic.apn;

import java.util.Random;
import java.util.regex.Pattern;

public class StringUtil {
	
	private static Random r=new Random();

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

	public static void main(String args[]) {
		System.out.println(isMobile(null));
		System.out.println(isMobile(""));
		System.out.println(isMobile("asdfaf"));
		System.out.println(isMobile("1231231231333"));
		System.out.println(isMobile("12345678909"));
	}
}
