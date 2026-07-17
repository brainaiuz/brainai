package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsQuoteHistory;
import com.edatasite.workforce.gwt.core.server.db.QuoteHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jul 24, 2009
 * Time: 1:19:11 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("quoteHistoryManager")
public class QuoteHistoryManagerImpl extends BaseManager<EdsQuoteHistory> implements QuoteHistoryManager {
    public QuoteHistoryManagerImpl() {
        super(EdsQuoteHistory.class);
    }

    public List<EdsQuoteHistory> getQuoteRecords(Integer quoteId) {
        return find("select qh from EdsQuoteHistory qh where qh.quote.objectID=? order by qh.eventDate", quoteId);
    }
}
