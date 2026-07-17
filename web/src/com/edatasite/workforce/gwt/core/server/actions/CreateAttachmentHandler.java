package com.edatasite.workforce.gwt.core.server.actions;

import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 01-Jun-2009
 * Time: 18:18:57
 * To change this template use File | Settings | File Templates.
 */
public class CreateAttachmentHandler extends WfmCommandHandler {

    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;

    private String[] result;

    public void execute(Object command) throws Throwable {
        CreateDocumentCommand documentCommand = (CreateDocumentCommand) command;

        String[] values = wfmCommandServiceLocal.createAttachmentHandler(documentCommand);
        setReturnValues(values[0]);
        setErrorString(values[1]);
        setResult(values);
    }

    public String[] getResult() {
        return result;
    }

    public void setResult(String[] result) {
        this.result = result;
    }
}