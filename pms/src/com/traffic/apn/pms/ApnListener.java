package com.traffic.apn.pms;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

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

		String cpath = arg0.getServletContext().getRealPath("/");

		final String conf = ApnConfig.getInstance()
				.getDefaultCommonConfigPath();

		PropertyConfigurator.configureAndWatch(conf + "/log4j.properties", 120);

		// vel

		Velocity.init(conf + "/velocity.properties");
		/*Velocity.setApplicationAttribute(Velocity.FILE_RESOURCE_LOADER_PATH,
				cpath + "/WEB-INF/template/");*/

		DBHelper.init(Integer.valueOf(ApnConfig.getInstance().getProperties()
				.getProperty("apn.poolsize", "10")));

		VelocityContext context = new VelocityContext();

		context.put("people", "黎明");

		Template template = Velocity.getTemplate("template/news/test.vm");

		StringWriter sw = new StringWriter();

		template.merge(context, sw);

		sw.flush();

		System.out.println(sw.toString());

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 10);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date first = cal.getTime();

		// crawler
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
			}

		}, first, 24 * 60 * 60 * 1000);

	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {

	}
}
