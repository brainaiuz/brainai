package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsConsignment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Normurod on 6/15/15.
 */
public interface ConsignmentManager extends Manager<EdsConsignment> {

    List<EdsConsignment> list(ListingFilterParameter filterParameter);

    Integer listCount(ListingFilterParameter filterParametrs);

    Integer getLastInNumber();

    EdsConsignment getConsignmentBySubsidiaryUniqNum(String subsidiaryUniqNum);

    void deleteConsignmentItems(Integer objectID);

    boolean isConsignmentNumberExists(String number, Integer objectID);

    BigDecimal getConsignmentQty(Integer clientID, Integer productID, Integer consignmentID);

    BigDecimal getConsignmentQtyToSell(Integer toCompanyID, Integer productID, Integer invoiceID);

    BigDecimal getConsignmentQtyToPurchase(Integer toCompanyID, Integer productID, Integer invoiceID);

    BigDecimal getSoldQty(Integer clientID, Integer productID);

    BigDecimal getSoldQty(List<Integer> clientIds, Integer productID);

    List<Integer> getClientListByProduct(Integer productID);
}
