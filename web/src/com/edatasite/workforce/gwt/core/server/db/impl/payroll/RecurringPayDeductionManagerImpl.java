package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsRecurringPayDeduction;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.RecurringPayDeductionManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("recurringPayDeductionManager")
public class RecurringPayDeductionManagerImpl extends BaseManager<EdsRecurringPayDeduction> implements RecurringPayDeductionManager {

    public RecurringPayDeductionManagerImpl() {
        super(EdsRecurringPayDeduction.class);
    }

    @Override
    public List<EdsRecurringPayDeduction> getAllItems(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select rpd.* from ").append(getCompanyId()).append(".recurringPayDeduction rpd ");
        sql.append("inner join ").append(getCompanyId()).append(".myUser u on u.id = rpd.employee_id ");
        sql.append("where (rpd.deleted is null OR rpd.deleted <> true) ");
        if (fp.isValidSearchKey()) {
            sql.append("and  (lower(u.userName) like lower('" + fp.getSqlSearchKey() + "') ");
            sql.append("or lower(u.lastname ||u.firstname) like  lower('" + fp.getSqlSearchKey() + "')) ");

        }
        sql.append(" order by rpd.id desc ");
        sql.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());

        return findNative(sql.toString(), EdsRecurringPayDeduction.class);
    }

    @Override
    public Integer getTotalCount() {
        Long total = (Long) findSingle("select count(*) from EdsRecurringPayDeduction where (deleted is null OR deleted <> true)");
        return total.intValue();
    }
}
