package com.traffic.apn.news;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.HtmlPage;

/**
 * 　*　 搜集新闻链接地址。将符合正则表达式的URL添加到URL数组中。 　
 */
public class LinkParser {
	private String url = "";
	private String urlFilter = "http://mil.news.sina.com.cn/2011-07-21/[\\d]+.html";

	private static final Logger log = Logger.getLogger(LinkParser.class);

	// 通过正则表达式过滤一个网页上的所有链接
	public Set<String> filterLinks(String selector) {
		Set<String> links = new HashSet<String>();
		try {
			Parser parser = new Parser(url);
			parser.setEncoding("gb2312");

			HtmlPage page = new HtmlPage(parser);

			// HtmlPage extends visitor,Apply the given visitor to the current
			// page.
			parser.visitAllNodesWith(page);
			// 所有的节点
			NodeList nodelist = page.getBody();

			if (StringUtils.isNotEmpty(selector)) {
				CssSelectorNodeFilter csf = new CssSelectorNodeFilter(selector);
				nodelist = nodelist.extractAllNodesThatMatch(csf, true);
			}

			// 建立一个节点filter用于过滤节点
			NodeFilter filter = new TagNameFilter("a");
			// 得到所有过滤后，想要的节点
			nodelist = nodelist.extractAllNodesThatMatch(filter, true);
			for (int i = 0; i < nodelist.size(); i++) {
				LinkTag tag = (LinkTag) nodelist.elementAt(i);
				// 链接地址
				String link = tag.getAttribute("href");
				if (link != null && link.length() > 0) {
					String str = url + link.substring(1);
					if (accept(link, urlFilter)) {
						links.add(link);
					}
					if (accept(str, urlFilter)) {
						links.add(str);
					}
				}

			}
		} catch (ParserException e) {
			log.error("Filter all link error,url=" + url, e);
		}
		return links;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setUrlFilter(String urlFilter) {
		this.urlFilter = urlFilter;
	}

	private boolean accept(String url, String filterRule) {
		if (url.matches(filterRule)) {
			return true;
		} else {
			return false;
		}
	}

	// 测试主页新闻，可以得到主页上所有符合要求的网页地址
	public static void main(String[] args) {
		List<String> links = new ArrayList<String>();
		// String url = "http://news.sohu.com/";
		// String urlFilter = "http://news.sohu.com/20110616/n[\\d]+.shtml";
		// String url = "http://mil.news.sina.com.cn/";
		// String urlFilter =
		// "http://mil.news.sina.com.cn/2011-07-21/[\\d]+.html";
		String url = "http://sports.sina.com.cn/";
		String urlFilter = "http://sports.sina.com.cn/[a-z]/2011-07-26/[\\d]+.shtml";
		// String url = "http://ent.sina.com.cn/";
		// String urlFilter =
		// "http://ent.sina.com.cn/[a-z]/m/2011-07-26/[\\d]+.shtml";
		// String url = "http://tech.sina.com.cn/";
		// String urlFilter =
		// "http://tech.sina.com.cn/[a-z]{1,2}/2011-07-14/[\\d]+.shtml";
		// String url = "http://sports.163.com/";
		// String urlFilter =
		// "http://sports.163.com/11/0726/[\\d]+/[\\w]+.html";
		// String url = "http://mili.cn.yahoo.com/";
		// String urlFilter =
		// "http://mili.cn.yahoo.com/ypen/20110714/[\\d]+.html";
		LinkParser parser = new LinkParser();
		parser.setUrl(url);
		parser.setUrlFilter(urlFilter);

		for (Iterator it = links.iterator(); it.hasNext();) {
			System.out.println(it.next().toString());
		}
		System.out.println(links.size());
		System.out.println(url);
		System.out.println(urlFilter);
	}
}
