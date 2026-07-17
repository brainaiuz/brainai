package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccountAttachment;
import com.edatasite.workforce.core.domain.accounting.EdsBankStatement;
import com.edatasite.workforce.core.domain.accounting.EdsBankStatementItem;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 14, 2010
 * Time: 3:34:37 AM
 * To change this template use File | Settings | File Templates.
 */
public interface BankStatementManager extends Manager<EdsBankStatement> {

    List<EdsBankStatement> getBankStatements(EdsBankAccountAttachment bankAccountAttachment);

    List<EdsBankStatement> getBankStatements(EdsBankAccount bankAccount);

    void deleteBankStatementAndItems(Integer bankStatementID);

    List<EdsBankStatementItem> getBankAccountStatementItems(Date from, Date to, Integer bankAccountID);

    void deleteUploadFileStatement(Integer fileID);
}
