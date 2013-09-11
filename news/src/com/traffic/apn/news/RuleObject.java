package com.traffic.apn.news;

/*
 * 规则的简单数据对象
 */
public class RuleObject {
	private String newSource = ""; // 新闻来源
	private String url = ""; // 链接地址，新闻链接从此页面中解析获取
	private String urlFilter = ""; // 过滤新闻链接的正则表达式
	private String newsType = ""; // 新闻分类
	private int limit;
	private String selector;

	public String getNewSource() {
		return newSource;
	}

	public void setNewSource(String newSource) {
		this.newSource = newSource;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlFilter() {
		return urlFilter;
	}

	public void setUrlFilter(String urlFilter) {
		this.urlFilter = urlFilter;
	}

	public String getNewsType() {
		return newsType;
	}

	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSelector() {
		return selector;
	}

	public void setSelector(String selector) {
		this.selector = selector;
	}
}