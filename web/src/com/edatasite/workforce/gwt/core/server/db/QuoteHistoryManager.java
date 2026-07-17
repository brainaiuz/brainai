package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsQuoteHistory;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jul 24, 2009
 * Time: 1:17:52 AM
 * To change this template use File | Settings | File Templates.
 */
public interface QuoteHistoryManager extends Manager<EdsQuoteHistory> {
    List<EdsQuoteHistory> getQuoteRecords(Integer quoteId);
}
