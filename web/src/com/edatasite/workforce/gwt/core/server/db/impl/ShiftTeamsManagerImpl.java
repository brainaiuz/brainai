package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsShiftTeams;
import com.edatasite.workforce.gwt.core.server.db.ShiftTeamsManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("shiftTeamsManager")
public class ShiftTeamsManagerImpl extends BaseManager<EdsShiftTeams> implements ShiftTeamsManager {

    public ShiftTeamsManagerImpl() {
        super(EdsShiftTeams.class);
    }

    @Override
    public ArrayList<EdsShiftTeams> getShiftTeamsByShiftId(Integer id) {
        return (ArrayList<EdsShiftTeams>) findNative("select * from  " + getCompanyId() + ". shift_teams_data where shift_id =  " + id, EdsShiftTeams.class);

    }

    @Override
    public ArrayList<EdsShiftTeams> getShiftTeamsByShiftAndGroupId(Integer shiftId, Integer groupId) {
        return (ArrayList<EdsShiftTeams>) findNative("select * from  " + getCompanyId() + ". shift_teams_data where shift_id =  " + shiftId + (groupId != null ? " and teamid =  " + groupId : ""), EdsShiftTeams.class);

    }

    @Override
    public ArrayList<Integer> getEmployeeIdsByShiftAndGroupId(Integer shiftId, Integer groupId) {
        return (ArrayList<Integer>) findNative("select empid from  " + getCompanyId() + ". shift_teams_data where shift_id =  " + shiftId + (groupId != null ? " and teamid =  " + groupId : ""));

    }

    @Override
    public List<Integer> getTeamsIdByShift(Integer id) {
        return (List<Integer>) findNative("select distinct teamid from  " + getCompanyId() + " .shift_teams_data  where shift_id =  " + id);
    }

    @Override
    public void deleteShiftTeam(EdsShiftTeams edsShiftTeams) {
        delete(edsShiftTeams);
    }
}
