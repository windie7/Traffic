package com.traffic.apn.pms;

import java.util.Date;

public class News {

	public static final int type_slotline = 1;
	public static final int type_china = 2;
	public static final int type_world = 3;
	public static final int type_society = 4;
	public static final int type_ent = 5;

	private String link;
	private int type;

	private String title; // 新闻标题
	private String source; // 新闻来源
	private Date sourceTime; // 新闻来源时间
	private String Content; // 新闻内容

	private Date collectDate;

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	

	public Date getSourceTime() {
		return sourceTime;
	}

	public void setSourceTime(Date sourceTime) {
		this.sourceTime = sourceTime;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public Date getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(Date collectDate) {
		this.collectDate = collectDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
