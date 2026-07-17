package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsComissionAllocateItem;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12/22/12
 * Time: 8:05 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ComissionAllocateManager extends Manager<EdsComissionAllocateItem> {

    void deleteAllocateItemsByQuote(Integer quoteId);
}
