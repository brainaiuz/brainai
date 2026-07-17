package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsDeferredTransactionItem;
import com.edatasite.workforce.core.domain.enums.DeferredTransactionType;
import com.edatasite.workforce.gwt.core.server.db.accounting.DeferredTransactionManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Normurod Buriev.
 * Date: 6/17/2021 7:11 PM
 */
@Repository("deferredTransactionManager")
public class DeferredTransactionManagerImpl extends BaseManager<EdsDeferredTransactionItem> implements DeferredTransactionManager {

    public DeferredTransactionManagerImpl() {
        super(EdsDeferredTransactionItem.class);
    }

    @Override
    public List<EdsDeferredTransactionItem> getItems(Date startDate, Date endDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String strStartDate = format.format(startDate);
        String strEndDate = format.format(endDate);

        StringBuilder sql = new StringBuilder();
        sql
                .append("SELECT dti.*, 0 as clazz_ FROM ").append(getCompanyId()).append(".deferred_transaction_item dti WHERE \n")
                .append("('").append(strStartDate).append("' between dti.fromDate AND dti.toDate) OR ('").append(strEndDate).append("' between dti.fromDate and dti.toDate) \n")
                .append("OR (dti.fromDate between '").append(strStartDate).append("' AND '").append(strEndDate).append("') OR (dti.toDate between '").append(strStartDate).append("' AND '").append(strEndDate).append("') ");
        return findNative(sql.toString(), EdsDeferredTransactionItem.class);
    }

    @Override
    public void deleteByTypeAndEntity(DeferredTransactionType type, Integer entityId) {
        update("DELETE FROM EdsDeferredTransactionItem ti WHERE ti.type = ? AND ti.entityId = ?", type, entityId);
    }
}
