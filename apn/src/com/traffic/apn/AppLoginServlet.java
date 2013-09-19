package com.traffic.apn;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/api/signin")
public class AppLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(AppLoginServlet.class);

	private ApnDao dao = new ApnDao();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AppLoginServlet() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	// 0:success 1:invalid parameter 2:register error 3:invalid app
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		BufferedReader sis;
		sis = request.getReader();
		char[] buf = new char[128];
		int len = 0;
		StringBuffer sb = new StringBuffer();
		while ((len = sis.read(buf)) != -1) {
			sb.append(buf, 0, len);
		}

		SignIn s = null;
		try {
			s = new ObjectMapper().readValue(sb.toString(), SignIn.class);
		} catch (Exception e) {

		}

		if (s == null) {
			sendRes(response, 1);
			return;
		}
		String mobile = s.getPn();
		String imei = s.getImsi();
		String sign = s.getSigned();
		if (!CommonUtil.isMobile(mobile) || CommonUtil.isEmpty(sign)
				|| CommonUtil.isEmpty(imei)) {
			sendRes(response, 1);
			return;
		}

		if (!checkSign(mobile, imei, sign)) {
			sendRes(response, 3);
			return;
		}

		UserBean user = dao.getUserByMobile(mobile);
		if (user == null) {
			user = new UserBean();
			user.setMobile(mobile);
			user.setImei(imei);
			user.setCreatedate(System.currentTimeMillis());
			int id = dao.createUser(user);
			if (id < 0) {
				sendRes(response, 2);
				return;
			}
			user.setId(id);
		}
		String ip = CommonUtil.getRequestIp(request);
		if (!CommonUtil.openAccess(ip, mobile)) {
			sendRes(response, 2);
			return;
		}
		dao.saveLogin(user.getId(), LoginBean.agent_app);
		sendRes(response, 0);
		return;

	}

	private void sendRes(HttpServletResponse response, int status)
			throws IOException {
		response.getOutputStream().write(
				("{\"status\":\"" + status + "\"}").getBytes());
	}

	private boolean checkSign(String mobile, String imei, String sign) {
		return true;
	}

}
