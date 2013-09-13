package com.traffic.apn.news;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import oracle.jdbc.OraclePreparedStatement;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Application Lifecycle Listener implementation class ApnListener
 * 
 */
@WebListener
public class ApnListener implements ServletContextListener {

	private static final Logger log = Logger.getLogger(ApnListener.class);

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
		Connection conn = null;
		OraclePreparedStatement ops = null;
		try {
			conn = DriverManager.getConnection(
					ApnConfig.getInstance().getProperties()
							.getProperty("apn.url"),
					ApnConfig.getInstance().getProperties()
							.getProperty("apn.user"),
					ApnConfig.getInstance().getProperties()
							.getProperty("apn.passpord"));
			ops = (OraclePreparedStatement) conn
					.prepareStatement("insert into news(id,link,newstype,title,source,sourcetime,content,status) values(news_seq.nextval,?,?,?,?,?,?,0)");

			for (News n : news) {
				try {
					ops.setString(1, n.getLink());
					ops.setInt(2, n.getType());
					ops.setString(3, n.getTitle());
					ops.setString(4, n.getSource());
					ops.setTimestamp(5, new java.sql.Timestamp(n
							.getSourceTime().getTime()));
					ops.setStringForClob(6, n.getContent());
					ops.executeUpdate();
				} catch (SQLException e) {
					
				}
			}

		} catch (SQLException e) {
			log.error("save news errors", e);
		} finally {

			try {
				if (ops != null)
					ops.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {

			}
		}
	}
}
