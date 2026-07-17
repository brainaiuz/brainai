package com.edatasite.workforce.gwt.profile.server.actions;

import com.edatasite.workforce.gwt.core.server.actions.CreateDocumentCommand;
import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

public class CreateLogoHandler extends WfmCommandHandler {

    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;

    public void execute(Object command) throws Throwable {
        CreateDocumentCommand documentCommand = (CreateDocumentCommand) command;

        String[] values = wfmCommandServiceLocal.createLogoHandler(documentCommand);
        setReturnValues(values[0]);
        setErrorString(values[1]);
    }
}
