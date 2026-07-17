package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsTrashBin;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/15/13
 * Time: 5:58 PM
 * To change this template use File | Settings | File Templates.
 */
public interface TrashBinManager extends Manager<EdsTrashBin> {
    List<EdsTrashBin> getTrashBins(ListingFilterParameter filterParametrs);

    Integer getTrashBinsCount();

    void saveTrashBin(Integer entityID, String entityType, String reference);
}
