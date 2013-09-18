package com.traffic.apn.pms;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

public class PmsDao {

	private final static Logger log = Logger.getLogger(PmsDao.class);

	public List<News> getNewsByDate(Date start, Date end) {
		Connection conn = DBHelper.GetInstance();
		QueryRunner qr = new QueryRunner();
		List<News> list = null;
		try {
			
			list = qr.query(conn,
					"select * from news where sourcetime>=? and sourcetime<?",
					new BeanListHandler(News.class),
					new java.sql.Timestamp(start.getTime()),
					new java.sql.Timestamp(end.getTime()));

		} catch (SQLException e) {
			log.error("load news errors", e);
		} finally {
			DBHelper.ReleaseInstance(conn);
		}
		return list;
	}

}
