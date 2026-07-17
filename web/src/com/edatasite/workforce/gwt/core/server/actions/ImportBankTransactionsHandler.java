package com.edatasite.workforce.gwt.core.server.actions;

import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 12, 2010
 * Time: 7:10:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImportBankTransactionsHandler extends WfmCommandHandler {

    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;

    public void execute(Object command) throws Throwable {
        BankAccountDocumentCommand documentCommand = (BankAccountDocumentCommand) command;

        String[] values = wfmCommandServiceLocal.importBankTransactionsHandler(documentCommand);
        setReturnValues(values[0]);
        setErrorString(values[1]);
    }
}
