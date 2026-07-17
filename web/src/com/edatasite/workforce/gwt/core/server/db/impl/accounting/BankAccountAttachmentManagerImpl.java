package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankAccountAttachment;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankAccountAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.impl.UploadManagerImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 12, 2010
 * Time: 7:40:58 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("bankAccountAttachmentManager")
public class BankAccountAttachmentManagerImpl extends UploadManagerImpl<EdsBankAccountAttachment>
        implements BankAccountAttachmentManager {

    public List<EdsBankAccountAttachment> getBankAccountAttachments(Integer bankAccountID, String bankAccAttchType) {
        return find("SELECT ba FROM EdsBankAccountAttachment ba WHERE "+ ServerUtils.checkForDeleted("ba.deleted")+" and ba.bankAccount.objectID=? and ba.bankAccAttchType.parent.code=? and ba.bankAccAttchType.code=? ORDER BY ba.objectID desc", bankAccountID, CommandConstants._BANK_ACCOUNT_TYPE, bankAccAttchType);
    }

    public BankAccountAttachmentManagerImpl() {
        super(EdsBankAccountAttachment.class);
    }

}
