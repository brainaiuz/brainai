package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsContract;
import com.edatasite.workforce.core.domain.EdsProjectPosition;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.Date;
import java.util.List;

/**
 * User:
 * Date: 07.01.2008
 * Time: 15:25:20
 */
public interface ContractManager extends Manager<EdsContract> {

    void deleteContract(EdsContract project);

    List<Object[]> getList(ListingFilterParameter fp);

    List<EdsCrmAccount> getClientContract();

    Integer listCount(ListingFilterParameter fp);

    List<EdsProjectPosition> getContractPositions(Integer contractID);

    String getTotaLChargeFormula(String priceTypeString);

    String getTotaLCharge(String totalcharceString);

    EdsContract getContractByProjectId(Integer objectID);

    @Deprecated
    Date getContractMaxEndDate(Integer contractId);

    SelectItem[] getAsSelectItem(ListingFilterParameter fp);

    List<EdsCase> getRelatedCases(Integer contractId);
}