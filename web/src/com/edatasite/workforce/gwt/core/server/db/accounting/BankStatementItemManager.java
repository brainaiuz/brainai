package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankStatement;
import com.edatasite.workforce.core.domain.accounting.EdsBankStatementItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: May 14, 2010
 * Time: 5:38:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BankStatementItemManager extends Manager<EdsBankStatementItem> {

    List<EdsBankStatementItem> getBankStatementItems(EdsBankStatement bankStatement);

    void deleteUploadFileStatementItems(Integer fileID);

    EdsBankStatementItem getBankStatementItem(Integer objectID,Integer bankStatementID);

    List<EdsBankStatementItem> getList(ListingFilterParameter fp);

    Integer getTotalCount(ListingFilterParameter fp);
}