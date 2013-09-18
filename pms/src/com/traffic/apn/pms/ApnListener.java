package com.traffic.apn.pms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd");

	private Timer timer;

	private String staticReposDir;

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
		staticReposDir = cpath + "/static";

		final String conf = ApnConfig.getInstance()
				.getDefaultCommonConfigPath();

		PropertyConfigurator.configureAndWatch(conf + "/log4j.properties", 120);

		// vel

		Velocity.init(conf + "/velocity.properties");
		/*
		 * Velocity.setApplicationAttribute(Velocity.FILE_RESOURCE_LOADER_PATH,
		 * cpath + "/WEB-INF/template/");
		 */

		DBHelper.init(Integer.valueOf(ApnConfig.getInstance().getProperties()
				.getProperty("apn.poolsize", "10")));

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 7);
		cal.set(Calendar.MINUTE, 30);
		cal.set(Calendar.SECOND, 0);
		Date first = cal.getTime();

		// crawler
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				PmsDao dao = new PmsDao();
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);

				Date start = cal.getTime();
				cal.add(Calendar.DAY_OF_MONTH, 1);
				Date end = cal.getTime();

				List<News> list = dao.getNewsByDate(start, end);
				if (list == null)
					return;
				poplateList(list);

				Map<NewsType, List<News>> map = new HashMap<NewsType, List<News>>();
				for (News news : list) {
					NewsType type = NewsType.valueOfId(news.getNewstype());
					if (map.get(type) == null) {
						map.put(type, new ArrayList<News>());
					}
					map.get(type).add(news);
				}

				for (NewsType t : map.keySet()) {
					poplateSubIndex(map.get(t), t);
				}

				poplateIndex(map);
			}

		}, first, 24 * 60 * 60 * 1000);

	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	private void poplateList(List<News> list) {
		for (News news : list) {
			try {
				VelocityContext context = new VelocityContext();
				context.put("news", news);
				Template template = Velocity
						.getTemplate("template/news/00001.vm");

				StringWriter sw = new StringWriter();
				template.merge(context, sw);
				sw.flush();

				String today = sdf.format(new Date());
				File dir = new File(staticReposDir + "/news/" + today);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				File f = new File(dir, news.getId() + ".html");
				if (!f.exists())
					f.createNewFile();

				FileWriter fos = new FileWriter(f);
				fos.write(sw.getBuffer().toString());
				fos.close();
			} catch (IOException e) {
				log.error("populate news error,id=" + news.getId() + ",title="
						+ news.getTitle(), e);
			}
		}
	}

	private void poplateSubIndex(List<News> list, NewsType type) {

		try {
			VelocityContext context = new VelocityContext();
			String today = sdf.format(new Date());
			context.put("list", list);
			context.put("dir", today);
			context.put("type", type);
			Template template = Velocity.getTemplate("template/news/"
					+ type.getStaticfile() + ".vm");

			StringWriter sw = new StringWriter();
			template.merge(context, sw);
			sw.flush();

			File dir = new File(staticReposDir + "/news/");
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File f = new File(dir, type.getStaticfile() + ".html");
			if (f.exists())
				f.delete();
			f.createNewFile();

			FileWriter fos = new FileWriter(f);
			fos.write(sw.getBuffer().toString());
			fos.close();
		} catch (IOException e) {
			log.error("populate news type error,type=" + type, e);
		}
	}

	private void poplateIndex(Map<NewsType, List<News>> map) {

		try {
			VelocityContext context = new VelocityContext();
			String today = sdf.format(new Date());
			context.put("map", map);
			context.put("dir", today);
			Template template = Velocity.getTemplate("template/news/index.vm");

			StringWriter sw = new StringWriter();
			template.merge(context, sw);
			sw.flush();

			File dir = new File(staticReposDir + "/news/");
			if (!dir.exists()) {
				dir.mkdirs();
			}

			File f = new File(dir, "index.html");
			if (f.exists())
				f.delete();
			f.createNewFile();

			FileWriter fos = new FileWriter(f);
			fos.write(sw.getBuffer().toString());
			fos.close();
		} catch (IOException e) {
			log.error("populate news index error", e);
		}
	}
}
