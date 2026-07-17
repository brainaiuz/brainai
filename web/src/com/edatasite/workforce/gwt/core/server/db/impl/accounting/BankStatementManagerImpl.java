package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccountAttachment;
import com.edatasite.workforce.core.domain.accounting.EdsBankStatement;
import com.edatasite.workforce.core.domain.accounting.EdsBankStatementItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankStatementManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 14, 2010
 * Time: 3:49:20 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("bankStatementManager")
public class BankStatementManagerImpl extends BaseManager<EdsBankStatement> implements BankStatementManager {

    public BankStatementManagerImpl() {
        super(EdsBankStatement.class);
    }

    public List<EdsBankStatement> getBankStatements(EdsBankAccountAttachment bankAccountAttachment) {
        return find("SELECT bs FROM EdsBankStatement bs WHERE bs.bankAccountAttachment=? ORDER BY bs.objectID desc", bankAccountAttachment);
    }

    public List<EdsBankStatement> getBankStatements(EdsBankAccount bankAccount) {
        return find("SELECT bs FROM EdsBankStatement bs WHERE bs.bankAccount=? and "+ ServerUtils.checkForDeleted("bs.uploadedFileDeleted")+"  ORDER BY bs.objectID desc", bankAccount);
    }

    public void deleteBankStatementAndItems(Integer bankStatementID) {
        update("delete from EdsBankStatementItem where bankStatement.objectID = ?", bankStatementID);
        update("delete from EdsBankStatement where objectID = ?", bankStatementID);
    }

    public List<EdsBankStatementItem> getBankAccountStatementItems(Date from, Date to, Integer bankAccountID) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String companyId = getCompanyId();
        return findNative("select distinct bsi.*, 0 as clazz_ " +
                "from " + companyId + ".bankStatementItem bsi " +
                "inner join " + companyId + ".bankStatement bs on bsi.bankstatementid=bs.id " +
                "inner join " + companyId + ".bankAccount ba on bs.bankaccountid=ba.id " +
                "left join " + companyId + ".bankaccountattachement baa on baa.bankaccountid=ba.id " +
                "where ba.id=" + bankAccountID + " and bs.uploadedFileDeleted is not true and (baa.isdeleted is null or baa.isdeleted=false) and (baa.isimported is null or baa.isimported=true) " +
                "and bsi.transactionDate between '" + format.format(from) + "' and '" + format.format(to) + "' and (bsi.reconciled is null or bsi.reconciled=false) and bsi.uploadedfiledeleted <> true order by bsi.transactionDate", EdsBankStatementItem.class);
    }

    @Override
    public void deleteUploadFileStatement(Integer fileID) {
        updateNative("update " + getCompanyId() + ".bankStatement set uploadedFileDeleted=true where bankaccountattachementid=" + fileID);
    }
}