package com.traffic.apn.news;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/*
 * 解析规则xml类
 */
public class RuleXmlParser {

	public List<RuleObject> parserXml(String fileName) throws DocumentException, FileNotFoundException {
		List<RuleObject> list = new ArrayList<RuleObject>();
		// SAXReadr对象
		SAXReader saxr = new SAXReader();

		// 使用getResourceAsStream返回的是InputStream流对象
		// 使用getResoruce返回是Java.net.url对象
		/*Document document = saxr.read(RuleXmlParser.class.getClassLoader()
				.getResourceAsStream(fileName));*/
		
		Document document = saxr.read(new FileInputStream(fileName));
		
		// 获得文档的根元素
		Element rootElement = document.getRootElement();
		// 遍历一级节点
		for (Iterator i = rootElement.elementIterator(); i.hasNext();) {
			RuleObject ruleObject = new RuleObject();
			Element tag = (Element) i.next();
			ruleObject.setNewSource(tag.attributeValue("source"));
			ruleObject.setUrl(tag.element("url").getText());
			ruleObject.setUrlFilter(tag.element("urlFilter").getText());
			ruleObject.setNewsType(tag.element("newsType").getText());
			ruleObject
					.setLimit(Integer.valueOf(tag.element("limit").getText()));
			ruleObject.setSelector(tag.element("selector").getText());

			list.add(ruleObject);
		}
		return list;
	}

	public static void main(String[] args) {
		RuleXmlParser ruleXmlParser = new RuleXmlParser();
		try {
			List<RuleObject> list = ruleXmlParser.parserXml("rule.xml");
			System.out.println(list.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}