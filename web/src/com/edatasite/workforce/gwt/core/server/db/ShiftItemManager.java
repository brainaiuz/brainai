package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsShiftItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItems;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public interface ShiftItemManager extends Manager<EdsShiftItem> {

    ArrayList<EdsShiftItem> getShiftItemsByShiftId(Integer shiftId);

    ArrayList<Integer> getShiftItemsGroupId(Integer shiftId);

    ArrayList<Integer> getTeamsIdByShift(Integer shiftId);

    LinkedHashMap<Integer, List<ShiftItems>> getShiftItemsByGroupId(Integer shiftId);

    void deleteShiftItem(EdsShiftItem shiftItem);
}
