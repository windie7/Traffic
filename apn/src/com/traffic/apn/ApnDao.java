package com.traffic.apn;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
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

	public boolean createUser(UserBean user) {
		Connection conn = DBHelper.GetInstance();
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(conn,
					"insert into users(mobile,imei,createdate) values(?,?,?)",
					user.getMobile(), user.getImei(),user.getCreatedate());

		} catch (SQLException e) {
			log.error("create user errors", e);
			return false;
		} finally {
			DBHelper.ReleaseInstance(conn);
		}
		return true;
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

	public boolean saveLogin(LoginBean login) {
		Connection conn = DBHelper.GetInstance();
		QueryRunner qr = new QueryRunner();
		try {
			qr.update(
					conn,
					"insert into userlogin(userid,agent,logindate) values(?,?,?)",
					login.getUserid(), login.getAgent(), login.getLogindate());

		} catch (SQLException e) {
			log.error("save login record errors", e);
			return false;
		} finally {
			DBHelper.ReleaseInstance(conn);
		}
		return true;
	}
}
