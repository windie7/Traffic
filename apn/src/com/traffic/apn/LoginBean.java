package com.traffic.apn;

import java.util.Date;

public class LoginBean {

	public static final int agent_page = 0;
	public static final int agent_app = 1;

	private int id;
	private int userid;
	private int agent;
	private long logindate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getAgent() {
		return agent;
	}

	public void setAgent(int agent) {
		this.agent = agent;
	}

	public long getLogindate() {
		return logindate;
	}

	public void setLogindate(long logindate) {
		this.logindate = logindate;
	}

}
