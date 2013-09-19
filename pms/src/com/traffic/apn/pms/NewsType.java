package com.traffic.apn.pms;

public enum NewsType {

	none(0, "", ""), slot(1, "头条", "headline"), china(2, "国内", "china"), world(
			3, "国际", "world"), society(4, "社会", "society"), en(5, "娱乐", "ent");

	public static final int type_slotline = 1;
	public static final int type_china = 2;
	public static final int type_world = 3;
	public static final int type_society = 4;
	public static final int type_ent = 5;

	private int id;
	private String name;
	private String staticfile;

	private NewsType(int id, String name, String staticfile) {
		this.id = id;
		this.name = name;
		this.staticfile = staticfile;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStaticfile() {
		return staticfile;
	}

	public void setStaticfile(String staticfile) {
		this.staticfile = staticfile;
	}

	public static NewsType valueOfId(int id) {
		return NewsType.values()[id];
	}
}
