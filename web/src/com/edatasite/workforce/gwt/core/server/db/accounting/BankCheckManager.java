package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankCheck;
import com.edatasite.workforce.core.domain.accounting.EdsBankCheckItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/15/12
 * Time: 5:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BankCheckManager extends Manager<EdsBankCheck> {
    List<EdsBankCheck> getBankCheckList(ListingFilterParameter filterParametrs);

    Integer getBankCheckLastIntNumber();

    List<EdsBankCheckItem> getBankCheckItemsBySupplier(Integer supplierID, Date date);

    EdsBankCheckItem getBankCheckItem(Integer bankCheckItemID);

    void deleteBankCheckItems(Integer bankCheckID);

    boolean isCheckEditable(Integer bankCheckID);

    List<EdsBankCheck> getBankCheksByIds(String Ids);

    EdsBankCheck getBankCheckByCode(String code);

    List<String> isUsedAsPayment(Integer bankCheckId);

    List<EdsBankCheck> getPostDatedPreCheckList(Date date);

    void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID);

    int getBankCheckListCount(ListingFilterParameter filterParametrs);
}
