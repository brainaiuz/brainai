package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsComissionAllocateItem;
import com.edatasite.workforce.gwt.core.server.db.accounting.ComissionAllocateManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12/22/12
 * Time: 8:08 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("comissionAllocateManager")
public class ComissionAllocateManagerImpl extends BaseManager<EdsComissionAllocateItem> implements ComissionAllocateManager {

    public ComissionAllocateManagerImpl() {
        super(EdsComissionAllocateItem.class);
    }

    @Override
    public void deleteAllocateItemsByQuote(Integer quoteId) {
        update("DELETE FROM EdsComissionAllocateItem ai WHERE ai.quote.objectID = ?", quoteId);
    }
}
