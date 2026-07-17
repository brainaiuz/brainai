package com.edatasite.workforce.gwt.project.server.app;

import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 25.02.2010
 * Time: 15:23:37
 * To change this template use File | Settings | File Templates.
 */

public class MSProjectFileUploadHandler extends WfmCommandHandler {

    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;

    public void execute(Object command) throws Throwable {
        WfmCommand documentCommand = (WfmCommand) command;
        wfmCommandServiceLocal.mSProjectFileUploadHandler(documentCommand);
    }
}
