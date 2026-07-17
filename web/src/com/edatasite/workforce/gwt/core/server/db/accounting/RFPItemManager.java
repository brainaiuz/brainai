package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsRFPItem;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/8/13
 * Time: 2:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RFPItemManager extends Manager<EdsRFPItem>{
    List<EdsRFPItem> getRFPItemByRFPID(Integer objectID);

    List<EdsRFPItem> getRFPItemsByRFPIDs(String rfpIDs);
}
