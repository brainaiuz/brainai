package com.edatasite.workforce.gwt.core.server.actions;

import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 7:14:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateProductCategoryPicturesHandler extends WfmCommandHandler {

    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;

    public void execute(Object command) throws Throwable {
        ProductCategoryDocumentCommand documentCommand = (ProductCategoryDocumentCommand) command;

        String[] values = wfmCommandServiceLocal.createProductCategoryPicturesHandler(documentCommand);
        setReturnValues(values[0]);
        setErrorString(values[1]);
    }
}