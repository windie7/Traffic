package com.traffic.apn;

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
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(value = { "/login", "/api/signin" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(LoginServlet.class);
	private static final long codeExpire = 600 * 1000;

	private ApnDao dao = new ApnDao();

	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 20,
			300, TimeUnit.SECONDS, new LinkedBlockingQueue());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
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
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String op = request.getParameter("op");
		if (StringUtil.isEmpty(op)) {
			this.doApi(request, response, op);
		} else if (op.equalsIgnoreCase("code")) {
			this.doCode(request, response, op);
		} else if (op.equalsIgnoreCase("login")) {
			this.doLogin(request, response, op);
		}
		return;
	}

	// 0:success 1:invalid mobile 2:already sent 3:send sms fail
	private void doCode(HttpServletRequest request,
			HttpServletResponse response, String op) throws ServletException,
			IOException {
		String ret = "0";
		final String mobile = request.getParameter("mobile");
		if (StringUtil.isMobile(mobile)) {
			UserBean user = dao.getUserByMobile(mobile);
			if (user == null) {
				user = new UserBean();
				user.setMobile(mobile);
				user.setImei("");
				user.setCreatedate(System.currentTimeMillis());
				dao.createUser(user);
			}

			if (user.getCode() == null
					|| ((System.currentTimeMillis() - user.getCodedate()) >= this.codeExpire)) {

				final String code = StringUtil.genCode();
				dao.saveCode(MD5Util.md5Hex(code), mobile);

				executor.execute(new Runnable() {
					@Override
					public void run() {
						// send messages
						log.info("Sent message to mobile=" + mobile + ", code="
								+ code);
						try {

							String cmd[] = { "/root/sms/sendsms",
									"/dev/ttyACM0", mobile,
									" 您好，欢迎使用客运大巴无线接入，你的登陆验证码为" + code + "。" };

							/*
							 * String cmd[] = { "/root/sms/sendsms",
							 * "/dev/ttyACM0", mobile, code };
							 */

							Process p = Runtime.getRuntime().exec(cmd);
							int ret = p.waitFor();
							log.info("sendsms ret=" + ret);
						} catch (Exception e) {
							log.error("Send SMS error", e);
						}
					}
				});

			} else {
				ret = "2";
			}

		} else {
			ret = "1";
		}

		ServletOutputStream sos = response.getOutputStream();
		sos.print(ret);
		sos.flush();
		sos.close();

		return;
	}

	// 0:success 1:invalid mobile 2:invalid code 3:code expires
	private void doLogin(HttpServletRequest request,
			HttpServletResponse response, String op) throws ServletException,
			IOException {

		String mobile = request.getParameter("mobile");

		String msg = "";
		if (!StringUtil.isMobile(mobile))
			msg = "手机号无效";
		else {
			UserBean user = dao.getUserByMobile(mobile);
			if (user == null)
				msg = "该手机用户不存在";
			else {
				String code = request.getParameter("code");
				if (StringUtil.isEmpty(code)
						|| StringUtil.isEmpty(user.getCode())
						|| !MD5Util.md5Hex(code).equalsIgnoreCase(
								user.getCode())) {
					msg = "验证码错误";
				} else if ((System.currentTimeMillis() - user.getCodedate()) > this.codeExpire) {
					msg = "验证码超时";
				} else {
					msg = "登录成功";

					dao.saveLogin(user.getId(), LoginBean.agent_page);
					// call pass apn
					// redirect?
					// response.sendRedirect("http://www.baidu.com");
				}
			}
		}

		request.setAttribute("msg", msg);
		request.getRequestDispatcher("/index.jsp").forward(request, response);

		return;
	}

	// 0:success 1:invalid parameter 2:register error 3:invalid app
	private void doApi(HttpServletRequest request,
			HttpServletResponse response, String op) throws ServletException,
			IOException {

		SignIn s = new ObjectMapper().readValue(request.getInputStream(),
				SignIn.class);
		int status = 0;
		String mobile = s.getPn();
		String imei = s.getImsi();
		String sign = s.getSigned();
		if (!StringUtil.isMobile(mobile) || StringUtil.isEmpty(sign)
				|| StringUtil.isEmpty(imei)) {
			status = 1;
			new ObjectMapper().writeValue(response.getOutputStream(), status);
			return;
		}

		if (!checkSign(mobile, imei, sign)) {
			status = 3;
			new ObjectMapper().writeValue(response.getOutputStream(), status);
			return;
		}

		UserBean user = dao.getUserByMobile(mobile);
		if (user == null) {
			user = new UserBean();
			user.setMobile(mobile);
			user.setImei("");
			user.setCreatedate(System.currentTimeMillis());
			int id = dao.createUser(user);
			if (id < 0) {
				status = 2;
				new ObjectMapper().writeValue(response.getOutputStream(),
						status);
			}
			user.setId(id);
		}
		dao.saveLogin(user.getId(), LoginBean.agent_app);
		status = 0;
		new ObjectMapper().writeValue(response.getOutputStream(), status);
		return;
	}

	private boolean checkSign(String mobile, String imei, String sign) {
		return true;
	}
}
