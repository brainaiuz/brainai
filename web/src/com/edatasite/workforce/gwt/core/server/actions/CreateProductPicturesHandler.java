package com.edatasite.workforce.gwt.core.server.actions;

import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 18, 2010
 * Time: 2:03:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateProductPicturesHandler extends WfmCommandHandler {

    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;

    public void execute(Object command) throws Throwable {
        ProductDocumentCommand documentCommand = (ProductDocumentCommand) command;

        String[] values = wfmCommandServiceLocal.createProductPicturesHandler(documentCommand);
        setReturnValues(values[0]);
        setErrorString(values[1]);
    }
}
