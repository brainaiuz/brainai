package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsTrashBin;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.TrashBinManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/15/13
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("trashBinManager")
public class TrashBinManagerImpl extends BaseManager<EdsTrashBin> implements TrashBinManager {
    public TrashBinManagerImpl() {
        super(EdsTrashBin.class);
    }

    @Override
    public List<EdsTrashBin> getTrashBins(ListingFilterParameter filterParametrs) {
        return find("select tb from EdsTrashBin tb where tb.status = ? order by tb.objectID desc", Constants.TRASH_BIN_PENDING);
    }

    @Override
    public Integer getTrashBinsCount() {
        Long count = (Long) findSingle("select count(tb.objectID) from EdsTrashBin tb where tb.status = ?", Constants.TRASH_BIN_PENDING);
        return count != null ? count.intValue() : 0;
    }

    @Override
    public void saveTrashBin(Integer entityID, String entityType, String reference) {
        EdsTrashBin trashBin = new EdsTrashBin();
        trashBin.setEntityID(entityID);
        trashBin.setEntityType(entityType);
        trashBin.setReference(reference);
        create(trashBin);
    }
}
