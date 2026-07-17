package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsRFQItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.RFQItemManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/10/12
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("rfqItemManager")
public class RFQItemManagerImpl extends BaseManager<EdsRFQItem> implements RFQItemManager {
    public RFQItemManagerImpl() {
        super(EdsRFQItem.class);
    }

    @Override
    public void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID) {
        updateNative("UPDATE " + getCompanyId() + ".rfqitem SET supplierid = " + newAccountID + " WHERE supplierid in (" + ServerUtils.getAsCommoDelimited(oldAccountIDs, "0", ",") + ")");
    }
}
