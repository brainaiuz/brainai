package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsOvertimeObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.OvertimeManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("overtimeManager")
public class OvertimeManagerImpl extends BaseManager<EdsOvertimeObject> implements OvertimeManager {

    public OvertimeManagerImpl() {
        super(EdsOvertimeObject.class);
    }

    @Override
    public List<EdsOvertimeObject> getAllItems(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select cf from EdsOvertimeObject cf where (cf.deleted is null OR cf.deleted <> true) order by cf.objectID desc ");
        return (List<EdsOvertimeObject>) findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getTotalUndeletedItemCount() {
        Long total = (Long) findSingle("select count(*) from EdsOvertimeObject where (deleted is null OR deleted <> true)");
        return total.intValue();
    }

    @Override
    public Integer getOvertimeLastIntNumber() {
        return (Integer) findSingle("select ovr.intNumber from EdsOvertimeObject ovr where (ovr.deleted = false or ovr.deleted is null) and ovr.intNumber is not null order by ovr.intNumber desc");

    }
}
