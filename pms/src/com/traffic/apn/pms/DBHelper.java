package com.traffic.apn.pms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

import org.apache.log4j.Logger;

public class DBHelper {

	private static final Logger log = Logger.getLogger(DBHelper.class);

	private static Stack<Connection> mInstancePool = new Stack<Connection>();
	private static Object mLockIntancePool = new Object();

	private static int poolSize = 10;

	public static void init(int size) {

		poolSize = size;

		for (int i = 0; i < poolSize; i++) {
			mInstancePool.push(newConn());
		}

	}

	public static Connection GetInstance() {
		Connection conn = null;
		synchronized (mLockIntancePool) {
			if (mInstancePool.size() != 0) {
				conn = mInstancePool.pop();
			} else {
				conn = newConn();
			}
		}
		return conn;
	}

	public static void ReleaseInstance(Connection conn) {
		synchronized (mLockIntancePool) {
			mInstancePool.push(conn);
		}
	}

	private static Connection newConn() {
		try {
			return DriverManager.getConnection(
					ApnConfig.getInstance().getProperties()
							.getProperty("apn.url"),
					ApnConfig.getInstance().getProperties()
							.getProperty("apn.user"),
					ApnConfig.getInstance().getProperties()
							.getProperty("apn.passpord"));
		} catch (SQLException e) {
			log.error("create connection error", e);
			return null;
		}
	}
}
