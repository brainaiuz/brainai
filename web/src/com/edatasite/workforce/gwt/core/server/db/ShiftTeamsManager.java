package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsShiftTeams;

import java.util.ArrayList;
import java.util.List;

public interface ShiftTeamsManager extends Manager<EdsShiftTeams> {
    ArrayList<EdsShiftTeams> getShiftTeamsByShiftId(Integer id);

    ArrayList<EdsShiftTeams> getShiftTeamsByShiftAndGroupId(Integer shiftId, Integer groupId);

    ArrayList<Integer> getEmployeeIdsByShiftAndGroupId(Integer shiftId, Integer groupId);

    List<Integer> getTeamsIdByShift(Integer id);

    void deleteShiftTeam(EdsShiftTeams edsShiftTeams);
}
