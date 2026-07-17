package com.edatasite.workforce.gwt.accounting.server.app;

import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 18.04.2009
 * Time: 13:12:36
 * To change this template use File | Settings | File Templates.
 */
public class ChartOfAccountsBackendUploadHandler extends WfmCommandHandler {

    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;

    public void execute(Object command) throws Throwable {
        WfmCommand document = (WfmCommand) command;
        String[] values = wfmCommandServiceLocal.createChartOfAccountsBackendHandler(document);
        setErrorString(values[1]);
    }
}