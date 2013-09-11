package com.traffic.apn;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.log4j.PropertyConfigurator;

/**
 * Application Lifecycle Listener implementation class ApnListener
 * 
 */
@WebListener
public class ApnListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public ApnListener() {
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {
		
		String conf=ApnConfig.getInstance().getDefaultCommonConfigPath();
				
		PropertyConfigurator.configureAndWatch(conf+"/log4j.properties", 120);
		
		DBHelper.init(conf+"/apn.db",10);
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {

	}
	
	
}
