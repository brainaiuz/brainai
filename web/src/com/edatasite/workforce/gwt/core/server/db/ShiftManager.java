package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsShift;
import com.edatasite.workforce.core.domain.EdsShiftItem;
import com.edatasite.workforce.core.domain.EdsShiftSettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ShiftManager extends Manager<EdsShift> {

    List<EdsShift> getList(ListingFilterParameter fp, Integer userId, boolean isAdmin);

    List<EdsShift> getShiftsTypeTeamByEmployeeId(Integer empId);

    List<EdsShift> getShiftsTypeEmployeeByEmployeeId(Integer empId);

    Integer getTotalCount(ListingFilterParameter fp, Integer userid, boolean isAdmin);

    Integer getShiftLastIntNumber();

    boolean isShiftNumberExist(String numberString, Integer objectID);

    EdsShiftItem getEmployeeDuty(Date period, Integer employeeId, boolean checkForTimeslot);

    void updateEmployeeDuty(EdsShiftItem item, EdsShiftSettings timeslot);

    BigDecimal getShiftHours(Date period, Integer employeeId);

    List<Object[]> getCustomEmployeesShift(Integer shiftId);
}
