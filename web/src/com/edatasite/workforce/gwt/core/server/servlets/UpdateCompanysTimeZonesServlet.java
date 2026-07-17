package com.edatasite.workforce.gwt.core.server.servlets;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 08.11.13
 * Time: 16:29
 * To change this template use File | Settings | File Templates.
 */

public class UpdateCompanysTimeZonesServlet extends HttpServlet {

	private CommonServiceLocal commonService;

	@Override
	public void init() throws ServletException {
		commonService = (CommonServiceLocal) ApplicationContextProvider.applicationContext.getBean("commonService");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		commonService.updateCompanyTimezones();
	}
}
