package com.traffic.apn;

import java.util.Date;

public class UserBean {

	private int id;
	private String mobile;
	private String imei;
	private String code;
	private long codedate;
	private long createdate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public long getCodedate() {
		return codedate;
	}

	public void setCodedate(long codedate) {
		this.codedate = codedate;
	}

	public long getCreatedate() {
		return createdate;
	}

	public void setCreatedate(long createdate) {
		this.createdate = createdate;
	}


}
