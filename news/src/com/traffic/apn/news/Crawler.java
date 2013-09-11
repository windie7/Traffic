package com.traffic.apn.news;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
			newsType = "";
		}

		RuleStrategy ruleStrategy = null;
		NewCollection newCollection = null;
		LinkParser linkParser = new LinkParser();

		List<String> list = new ArrayList<String>();
		if ("sina".equals(newSource)) {
			ruleStrategy = new SinaRule();
			newCollection = new SinaNewCollection();
		}

		if (ruleStrategy != null) {
			urlFilter = ruleStrategy.urlFilterStrategy(urlFilter);
			linkParser.setUrl(url);
			linkParser.setUrlFilter(urlFilter);
			list = linkParser.filterLinks(ruleObject.getSelector());
			for (int i = 0; i < list.size(); i++) {
				if (i >= ruleObject.getLimit())
					break;
				News news = newCollection.parser((String) list.get(i));
				if (news != null) {
					news.setCollectDate(new Date());
					news.setType(Integer.valueOf(ruleObject.getNewsType()));

					res.add(news);
				}

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