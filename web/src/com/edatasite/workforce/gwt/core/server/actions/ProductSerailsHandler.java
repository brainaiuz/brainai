package com.edatasite.workforce.gwt.core.server.actions;

import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Dilshod Madrahimov on 8/1/15 2:26 PM
 */
public class ProductSerailsHandler extends WfmCommandHandler {
    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;

    @Override
    public void execute(Object command) throws Throwable {
        WfmCommand documentCommand = (WfmCommand) command;
        String values[] = wfmCommandServiceLocal.uploadProductSerials(documentCommand);
        setReturnValues(values[0]);
        setErrorString(values[1]);
    }

}
