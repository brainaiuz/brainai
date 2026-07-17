package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsQuoteItem;
import com.edatasite.workforce.gwt.core.server.db.QuoteItemManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Atabaev on 10/10/2018.
 */

@Repository
public class QuoteItemManagerImpl extends BaseManager<EdsQuoteItem> implements QuoteItemManager {
    public QuoteItemManagerImpl() {
        super(EdsQuoteItem.class);
    }
}
