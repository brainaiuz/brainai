package com.edatasite.workforce.gwt.backend.server.actions;

import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Ulugbek Normatov
 * Date: Sep 19, 2011
 * Time: 4:17:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateExcelReportTemplateHandler extends WfmCommandHandler {

    private String companyId;

    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;


    @Override
    public void execute(Object command) throws Throwable {
        WfmCommand documentCommand = (WfmCommand) command;
        String[] values = wfmCommandServiceLocal.createReportingExcelTemplateAttachmentHandler(documentCommand);
        setReturnValues(values[0]);
        setErrorString(values[1]);
        ServerSecurityContext.getInstance().setCompanyId(values[2]);
    }
}
