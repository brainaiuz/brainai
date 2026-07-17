package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsShiftItem;
import com.edatasite.workforce.gwt.core.server.db.ShiftItemManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItems;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Repository("shiftItemManager")
public class ShiftItemManagerImpl extends BaseManager<EdsShiftItem> implements ShiftItemManager {
    public ShiftItemManagerImpl() {
        super(EdsShiftItem.class);
    }

    @Override
    public ArrayList<EdsShiftItem> getShiftItemsByShiftId(Integer shiftId) {
        return (ArrayList<EdsShiftItem>) findNative("select * from  " + getCompanyId() + ". shift_items where shift_id =  " + shiftId + " and  deleted is not true", EdsShiftItem.class);
    }

    @Override
    public ArrayList<Integer> getShiftItemsGroupId(Integer shiftId) {
        ArrayList<Integer> groupId = new ArrayList<>();
        List<Object[]> list = findNative("select distinct row,groupid  from  " + getCompanyId() + ". shift_items  where  deleted is not true and shift_id = " + shiftId + "  order by row,groupid ");
        for (Object[] objects : list) {
            groupId.add((Integer) objects[1]);
        }
        return groupId;
    }

    @Override
    public ArrayList<Integer> getTeamsIdByShift(Integer shiftId) {
        return (ArrayList<Integer>) findNative("select distinct shi.groupId from " + getCompanyId() + ".shift sh left join " + getCompanyId() + ".shift_items shi on sh.id = shi.shift_id where sh.deleted is not true and sh.id = " + shiftId);
    }


    @Override
    public LinkedHashMap<Integer, List<ShiftItems>> getShiftItemsByGroupId(Integer shiftId) {
        StringBuilder sql = new StringBuilder();
        sql
                .append("select shi.groupid, shi.id, shi.key, shs.id as sid, shs.short_name, shi.employee_assessment_id from ")
                .append(getCompanyId()).append(".shift_items shi")
                .append(" left join ").append(getCompanyId()).append(".shift_settings shs on (shi.shift_settings_id = shs.id)")
                .append(" left join ").append(getCompanyId()).append(".myuser my on (my.id = shi.groupid)")
                .append(" left join ").append(getCompanyId()).append(".employee emp on (my.id = emp.id)")
                .append(" left join ").append(getCompanyId()).append(".position pos on (pos.id = emp.positionid)")
                .append(" where shift_id = ").append(shiftId)
                .append(" and shi.deleted is not true")
                .append(" order by count(*) over (partition by pos.name), pos.name, my.firstname, my.lastname, my.middleName, row,  shi.groupid");

        List<Object[]> list = findNative(sql.toString());
        LinkedHashMap<Integer, List<ShiftItems>> itemsMap = new LinkedHashMap<>();
        if (!CollectionUtils.isEmpty(list)) {
            List<ShiftItems> items = new ArrayList<>();
            Integer itemId = null;
            for (Object[] objects : list) {
                if (itemId != null && !itemId.equals((Integer) objects[0])) {
                    items = new ArrayList<>();
                }
                itemId = (Integer) objects[0];
                ShiftItems shift = new ShiftItems();
                shift.setId((Integer) objects[1]);
                shift.setKey((String) objects[2]);
                shift.setTimeSlotId((Integer) objects[3]);
                shift.setTimeSlotShortName((String) objects[4]);
                shift.setAssessmentId((Integer) objects[5]);
                items.add(shift);
                itemsMap.put(itemId, items);
            }
        }
        return itemsMap;
    }

    @Override
    public void deleteShiftItem(EdsShiftItem shiftItem) {
        delete(shiftItem);
    }
}
