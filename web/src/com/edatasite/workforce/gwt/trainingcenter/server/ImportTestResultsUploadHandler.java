package com.edatasite.workforce.gwt.trainingcenter.server;

import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created with IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 17.09.12
 * Time: 21:23
 * To change this template use File | Settings | File Templates.
 */

public class ImportTestResultsUploadHandler extends WfmCommandHandler {

	@Autowired
	private WfmCommandServiceLocal wfmCommandServiceLocal;

	@Override
	public void execute(Object command) throws Throwable {
		WfmCommand documentCommand = (WfmCommand) command;
		String[] result =  wfmCommandServiceLocal.importTestResultsUploadHandler(documentCommand);
		setReturnValues(result[0]);
		setErrorString(result[1]);
	}
}
