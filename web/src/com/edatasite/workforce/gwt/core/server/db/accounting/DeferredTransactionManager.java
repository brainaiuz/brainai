package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsDeferredTransactionItem;
import com.edatasite.workforce.core.domain.enums.DeferredTransactionType;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Date;
import java.util.List;

/**
 * Created by Normurod Buriev.
 * Date: 6/17/2021 7:10 PM
 */
public interface DeferredTransactionManager extends Manager<EdsDeferredTransactionItem> {

    List<EdsDeferredTransactionItem> getItems(Date startDate, Date endDate);

    void deleteByTypeAndEntity(DeferredTransactionType type, Integer entityId);
}
