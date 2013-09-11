package com.traffic.apn;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MD5Util {

	private MD5Util() {
	}

	/** Returns a MessageDigest for the given <code>algorithm</code>.
	 * 
	 * @param algorithm
	 *            The MessageDigest algorithm name.
	 * @return An MD5 digest instance.
	 * @throws RuntimeException
	 *             when a {@link java.security.NoSuchAlgorithmException} is
	 *             caught */

	static MessageDigest getDigest() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (final NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	/** Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
	 * 
	 * @param data
	 *            Data to digest
	 * @return MD5 digest */
	public static byte[] md5(final byte[] data) {
		return getDigest().digest(data);
	}

	/** Calculates the MD5 digest and returns the value as a 16 element <code>byte[]</code>.
	 * 
	 * @param data
	 *            Data to digest
	 * @return MD5 digest */
	public static byte[] md5(final String data) {
		try {
			return md5(data.getBytes("UTF-8"));
		} catch (final UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/** Calculates the MD5 digest and returns the value as a 32 character hex
	 * string.
	 * 
	 * @param data
	 *            Data to digest
	 * @return MD5 digest as a hex string */
	public static String md5Hex(final byte[] data) {
		return HexUtil.toHexString(md5(data));
	}

	public static String hmacMd5Hex(final byte[] data, final byte[] key) {
		try {
			final Mac mac = Mac.getInstance("HmacMD5");
			mac.init(new SecretKeySpec(key, "HmacMD5"));
			return HexUtil.toHexString(mac.doFinal(data));
		} catch (final Exception e) {
		}
		return null;
	}

	public static String hmacMd5Hex(final String data, final String key) {

		try {
			return hmacMd5Hex(data.getBytes("utf-8"), key.getBytes("utf-8"));
		} catch (final UnsupportedEncodingException e) {
		}
		return null;
	}

	/** Calculates the MD5 digest and returns the value as a 32 character hex
	 * string.
	 * 
	 * @param data
	 *            Data to digest
	 * @return MD5 digest as a hex string */
	public static String md5Hex(final String data) {
		return HexUtil.toHexString(md5(data));
	}

	public static byte[] digest(final String username, final String passwd) {
		try {
			final MessageDigest md5 = getDigest();
			md5.update(username.getBytes("UTF-8"));
			md5.update(passwd.getBytes("UTF-8"));
			return md5.digest();
		} catch (final UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(final String[] args) {
		System.out.println(MD5Util.md5Hex("123456"));
	}
}
