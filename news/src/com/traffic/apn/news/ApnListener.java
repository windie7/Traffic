package com.traffic.apn.news;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Application Lifecycle Listener implementation class ApnListener
 * 
 */
@WebListener
public class ApnListener implements ServletContextListener {

	private static final Logger log = Logger.getLogger(ApnListener.class);

	@Resource(name = "java:jboss/datasources/ExampleDS")
	private DataSource ds;

	private Timer timer;

	/**
	 * Default constructor.
	 */
	public ApnListener() {
		timer = new Timer(true);
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {

		final String conf = ApnConfig.getInstance()
				.getDefaultCommonConfigPath();

		PropertyConfigurator.configureAndWatch(conf + "/log4j.properties", 120);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date first = cal.getTime();

		// crawler
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {

				Crawler crawler = new Crawler();
				RuleXmlParser ruleXmlParser = new RuleXmlParser();
				List<RuleObject> list = null;

				try {
					list = ruleXmlParser.parserXml(conf + "/news_rule.xml");
					for (int i = 0; i < list.size(); i++) {
						RuleObject ruleObject = list.get(i);
						List<News> news = crawler.crawler(ruleObject);
						save(news);
						// store news
					}
				} catch (Exception e) {
					log.error("Init crawler rule file error", e);
				}

			}

		}, first, 24 * 60 * 60 * 1000);

		// DBHelper.init(conf+"/apn.db",10);
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	private void save(List<News> news) {
		try {
			Connection conn = ds.getConnection();
			QueryRunner qr = new QueryRunner();
			Object params[][] = new Object[news.size()][];
			for (int i = 0; i < news.size(); i++) {
				params[i] = new Object[6];
				News n = news.get(i);
				params[i][0] = n.getLink();
				params[i][1] = n.getType();
				params[i][2] = n.getTitle();
				params[i][3] = n.getSource();
				params[i][4] = n.getSourceTime();
				params[i][5] = n.getContent();
			}
			qr.batch(
					conn,
					"insert into news(link,newstype,title,source,sourcetime,content,status) values(?,?,?,?,?,?,0)",
					params);
		} catch (SQLException e) {
			log.error("save news errors", e);
		}
	}

}
