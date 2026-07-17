package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsSharedNumber;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by Sherzod on 7/14/2015.
 */
public interface SharedNumberManager extends Manager<EdsSharedNumber> {
    boolean isNumberExists(String number, Integer objectID, String code, String type);

    Integer getLastIntNumber(String code, String type);

    Integer saveNumberData(String number, Integer intNumber, String code, Integer entityID, String entityCode);

    EdsSharedNumber getByEntityID(Integer entityID, String entityCode);

    BankTransferNumberData generateNewNumber(BankTransferNumberData numberData);
}
