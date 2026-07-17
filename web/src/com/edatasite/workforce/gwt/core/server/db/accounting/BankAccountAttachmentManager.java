package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccountAttachment;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 12, 2010
 * Time: 7:38:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BankAccountAttachmentManager extends Manager<EdsBankAccountAttachment> {
    List<EdsBankAccountAttachment> getBankAccountAttachments(Integer bankAccountID, String bankAccAttchType);

    EdsUser getUser();
}