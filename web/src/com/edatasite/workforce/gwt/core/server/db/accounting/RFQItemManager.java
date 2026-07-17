package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsRFQItem;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/10/12
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RFQItemManager extends Manager<EdsRFQItem> {
    void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID);
}
