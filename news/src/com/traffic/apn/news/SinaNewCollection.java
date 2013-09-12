package com.traffic.apn.news;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NotFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

/*
 * 对新浪网的一条新闻进行数据采集
 */
public class SinaNewCollection implements NewCollection {
	private Parser parser = null; // 用于分析网页的分析器。
	private ParserUtil parserUtil = new ParserUtil();

	private static final Logger log = Logger.getLogger(SinaNewCollection.class);

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-mm-dd hh:mm");

	/**
	 * 对新闻URL进行解析并采集数据
	 * 
	 * @param url
	 *            新闻连接。
	 */
	public News parser(String url) {
		String title = ""; // 新闻标题
		String source = ""; // 新闻来源
		String sourceTime = ""; // 新闻来源时间
		String content = ""; // 新闻内容

		News news = new News();
		news.setLink(url);
		try {
			parser = new Parser(url);
			parser.setEncoding("GB2312");
			// 标题
			NodeFilter titleFilter = new TagNameFilter("h1");
			NodeList titleNodeList = (NodeList) parser.parse(titleFilter);
			title = parserUtil.getNodeListText(titleNodeList);
			parser.reset(); // 每次获取都必须reset，不然后面获取不到数据
			news.setTitle(title);
			// 来源
			NodeFilter sourceFilter = new AndFilter(new TagNameFilter("span"),
					new HasAttributeFilter("id", "media_name"));
			NodeList sourceNodeList = (NodeList) parser.parse(sourceFilter);
			source = parserUtil.getNodeListText(sourceNodeList);
			parser.reset();
			news.setSource(source);
			// 来源时间
			NodeFilter sourceTimeFilter = new AndFilter(new TagNameFilter(
					"span"), new HasAttributeFilter("id", "pub_date"));
			NodeList sourceTimeNodeList = (NodeList) parser
					.parse(sourceTimeFilter);
			String str = parserUtil.getNodeListText(sourceTimeNodeList);
			sourceTime = str.replace("年", "-").replace("月", "-")
					.replace("日", " ").replace("&nbsp;", "");
			parser.reset();
			news.setSourceTime(sdf.parse(sourceTime));

			// 正文
			NodeFilter ContentTimeFilter = new AndFilter(new TagNameFilter(
					"div"), new HasAttributeFilter("id", "artibody"));
			NodeList ContentTimeNodeList = (NodeList) parser
					.parse(ContentTimeFilter);
			NodeList childList = ContentTimeNodeList.elementAt(0).getChildren();
			childList.keepAllNodesThatMatch(new NotFilter(new TagNameFilter(
					"div"))); // 去掉非正文部分
			// childList.keepAllNodesThatMatch(new RegexFilter("　　相关专题"));

			content = parserUtil.getNodeListHTML(ContentTimeNodeList);
			content = ParserUtil.getPlainText(content);
			news.setContent(content);
			parser.reset();

		} catch (Exception e) {
			log.error("parsing news error, link=" + url, e);
			return null;
		}
		return news;
	}

	public static void main(String[] args) {
		SinaNewCollection newCollection = new SinaNewCollection();
		newCollection
				.parser("http://tech.sina.com.cn/d/2011-07-28/09485850149.shtml");
	}
}