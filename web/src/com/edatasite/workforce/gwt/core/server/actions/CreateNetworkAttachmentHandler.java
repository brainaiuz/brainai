package com.edatasite.workforce.gwt.core.server.actions;

import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: May 8, 2010
 * Time: 4:32:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateNetworkAttachmentHandler extends WfmCommandHandler {

    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;

    @Override
    public void execute(Object command) throws Throwable {
        CreateDocumentCommand documentCommand = (CreateDocumentCommand) command;

        String[] values = wfmCommandServiceLocal.createNetworkAttachmentHandler(documentCommand);
        setReturnValues(values[0]);
        setErrorString(values[1]);
    }
}
