package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsRFP;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/8/13
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RFPManager extends Manager<EdsRFP> {

    List<EdsRFP> getEdsRFPList(ListingFilterParameter fp, boolean isTotalQuery);

    void deleteRFPItems(Integer rfpID);

    Map<Integer, BigDecimal> getRequestedRFPItems(List<Integer> ids);

    Map<Integer, BigDecimal> getRemainingQtys(List<Integer> ids);

    boolean isRFPNumberExist(String numberString, Integer objectID);

    Integer getRfpLastIntNumber();
}
