package com.traffic.apn.news;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

/*
 * 按配置文件抓取新闻
 */
public class Crawler {

	private static final Logger log = Logger.getLogger(Crawler.class);

	public List<News> crawler(RuleObject ruleObject) {
		List<News> res = new ArrayList<News>();

		String newSource = ruleObject.getNewSource();
		String url = ruleObject.getUrl();
		String urlFilter = ruleObject.getUrlFilter();
		String newsType = ruleObject.getNewsType();
		if (newsType == null) {
			newsType = "0";
		}
		int newType=Integer.valueOf(ruleObject.getNewsType());

		RuleStrategy ruleStrategy = null;
		NewCollection newCollection = null;
		LinkParser linkParser = new LinkParser();

		Set<String> list = new HashSet<String>();
		if ("sina".equals(newSource)) {
			ruleStrategy = new SinaRule();
			newCollection = new SinaNewCollection();
		}

		if (ruleStrategy != null) {
			urlFilter = ruleStrategy.urlFilterStrategy(urlFilter);
			linkParser.setUrl(url);
			linkParser.setUrlFilter(urlFilter);
			list = linkParser.filterLinks(ruleObject.getSelector());
						
			int i=0;
			for (String s: list) {
				if (i >= ruleObject.getLimit())
					break;
				News news = newCollection.parser(s);
				if (news != null) {
					//news.setCollectDate(new Date());
					news.setType(newType);
					res.add(news);
				}
				i++;
			}
		} else {
			log.error("Load ruleStrategy failed!");
		}
		return res;
	}

	public static void main(String[] args) {
		Crawler crawler = new Crawler();
		RuleXmlParser ruleXmlParser = new RuleXmlParser();
		List<RuleObject> list = null;

		try {
			list = ruleXmlParser.parserXml("rule.xml");
			for (int i = 0; i < list.size(); i++) {
				RuleObject ruleObject = list.get(i);
				crawler.crawler(ruleObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}