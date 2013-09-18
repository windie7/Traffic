package com.traffic.apn;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

public class ApnDao {

	private final static Logger log = Logger.getLogger(ApnDao.class);

	public UserBean getUserByMobile(String mobile) {
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
	}

	public int createUser(UserBean user) {
		int id = -1;
		Connection conn = DBHelper.GetInstance();
		QueryRunner qr = new QueryRunner();
		try {
			conn.setAutoCommit(false);
			qr.update(conn,
					"insert into users(mobile,imei,createdate) values(?,?,?)",
					user.getMobile(), user.getImei(), user.getCreatedate());

			id = ((Long) qr.query(conn, "SELECT LAST_INSERT_ID()",
					new ScalarHandler(1))).intValue();

			conn.commit();
		} catch (SQLException e) {
			log.error("create user errors", e);
			return id;
		} finally {
			DBHelper.ReleaseInstance(conn);
		}
		return id;
	}

	public boolean saveCode(String code, String mobile) {
		Connection conn = DBHelper.GetInstance();
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(conn,
					"update users set code=?,codedate=? where mobile =?", code,
					System.currentTimeMillis(), mobile);

		} catch (SQLException e) {
			log.error("save code errors", e);
			return false;
		} finally {
			DBHelper.ReleaseInstance(conn);
		}
		return true;
	}

	public boolean saveLogin(int userid, int agent) {
		Connection conn = DBHelper.GetInstance();
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(
					conn,
					"insert into userlogin(userid,agent,logindate) values(?,?,?)",
					userid, agent, System.currentTimeMillis());

		} catch (SQLException e) {
			log.error("save login record errors", e);
			return false;
		} finally {
			DBHelper.ReleaseInstance(conn);
		}
		return true;
	}
}
