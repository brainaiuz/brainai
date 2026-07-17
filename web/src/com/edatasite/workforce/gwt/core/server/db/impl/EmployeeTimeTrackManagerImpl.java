package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployeeTimeTrack;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.EmployeeTimeTrackManager;
import org.springframework.stereotype.Repository;

@Repository("employeeTimeTrackManager")
public class EmployeeTimeTrackManagerImpl extends BaseManager<EdsEmployeeTimeTrack> implements EmployeeTimeTrackManager {


    public EmployeeTimeTrackManagerImpl() {
        super(EdsEmployeeTimeTrack.class);
    }

    @Override
    public EdsEmployeeTimeTrack getEmployeeTimeTrack(Integer employeeID, String status, DateNonConvertable date) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(getCompanyId()).append(".employeetimetrack ett");
        sql.append(" where ett.employeeId = ").append(employeeID);
        sql.append(" and date(ett.date)= date('").append(date.getNonConvertedDate()).append("')");

        switch (status) {
            case Constants.AVAILABLE -> sql.append(" and ett.checkIn is null");
            case Constants.BREAK -> sql.append(" and ett.onBreak is null");
            case Constants.ON_DUTY -> sql.append(" and ett.onDuty is null");
            case Constants.NOT_AVAILABLE -> sql.append(" and ett.checkOut is null");
        }
        sql.append(" order by ett.id");
        return (EdsEmployeeTimeTrack) findNativeSingle(sql.toString(), EdsEmployeeTimeTrack.class);
    }
}
