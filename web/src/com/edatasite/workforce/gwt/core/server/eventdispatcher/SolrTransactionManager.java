package com.edatasite.workforce.gwt.core.server.eventdispatcher;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.SolrEvent;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;

/**
 * User: Abdulaziz
 * Date: Aug 13, 2010
 * Time: 3:02:27 PM
 */
public interface SolrTransactionManager {
    <E extends EdsObject> SolrEvent registerEvent(WfmType<E> eventType, E entity, EdsCompany company);
    void rollbackTransaction();

    void clearList();
}
