package com.traffic.apn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Stack;

import org.apache.log4j.Logger;

public class DBHelper {

	private static final Logger log = Logger.getLogger(DBHelper.class);

	private static Stack<Connection> mInstancePool = new Stack<Connection>();
	private static Object mLockIntancePool = new Object();

	
	private static String dbFile="";
	
	private static int poolSize=10;
			
	public static void init(String dbFileName,int size) {
		dbFile=dbFileName;
		poolSize=size;
		try {
			Class.forName("org.sqlite.JDBC");
			for (int i = 0; i < poolSize; i++) {
				mInstancePool.push(newConn(dbFile));
			}
		} catch (ClassNotFoundException e) {

		}
	}

	public static Connection GetInstance() {
		Connection conn = null;
		synchronized (mLockIntancePool) {
			if (mInstancePool.size() != 0) {
				conn = mInstancePool.pop();
			} else {
				conn = newConn(dbFile);
			}
		}
		return conn;
	}

	public static void ReleaseInstance(Connection conn) {
		synchronized (mLockIntancePool) {
			mInstancePool.push(conn);
		}
	}

	private static Connection newConn(String dbFile) {
		try {
			return DriverManager.getConnection("jdbc:sqlite://"+dbFile);
		} catch (SQLException e) {
			log.error("create connection error", e);
			return null;
		}
	}
}
