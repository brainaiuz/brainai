package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsRFPItem;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFPItemManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/8/13
 * Time: 2:50 PM
 */
@Repository("rfpItemManager")
public class RFPItemManagerImpl extends BaseManager<EdsRFPItem> implements RFPItemManager {
    public RFPItemManagerImpl() {
        super(EdsRFPItem.class);
    }

    @Override
    public List<EdsRFPItem> getRFPItemByRFPID(Integer objectID) {
        return find("select a from EdsRFPItem a where a.rfp.objectID = ?", objectID);
    }

    @Override
    public List<EdsRFPItem> getRFPItemsByRFPIDs(String rfpIDs) {
        return find("select a from EdsRFPItem a where a.rfp.objectID in (" + rfpIDs + ") and a.selected = true");
    }
}
