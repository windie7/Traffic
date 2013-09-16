package com.traffic.apn.pms;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.log4j.Logger;

public class PmsDao {

	private final static Logger log = Logger.getLogger(PmsDao.class);
/*
	public News getUserByMobile(String mobile) {
		Connection conn = DBHelper.GetInstance();
		QueryRunner qr = new QueryRunner();
		UserBean person = null;
		try {
			person = (UserBean) qr.query(conn,
					"select * from users where mobile=?", new BeanHandler(
							UserBean.class), mobile);

		} catch (SQLException e) {
			log.error("load user errors", e);
		} finally {
			DBHelper.ReleaseInstance(conn);
		}
		return person;
	}*/

}
