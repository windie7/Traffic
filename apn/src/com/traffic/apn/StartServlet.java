package com.traffic.apn;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.PropertyConfigurator;

/**
 * Servlet implementation class StartServlet
 */
@WebServlet(urlPatterns = { "/StartServlet" }, initParams = { @WebInitParam(name = "load-on-startup", value = "1") })
public class StartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void init() throws ServletException {/*
		super.init();
		String conf = ApnConfig.getInstance().getDefaultCommonConfigPath();
		PropertyConfigurator.configureAndWatch(conf + "/log4j.properties", 120);
		DBHelper.init(conf + "/apn.db", 3);
	*/}

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public StartServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
