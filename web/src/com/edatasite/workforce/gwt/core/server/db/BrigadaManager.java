package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBrigada;
import com.edatasite.workforce.core.domain.EdsBrigadaEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.List;

public interface BrigadaManager extends Manager<EdsBrigada> {

    List<EdsBrigada> getList(ListingFilterParameter filterParameter, Integer userId);

    List<EdsBrigada> getBrigadasForLookUp(Integer userId);

    void deleteBrigada(EdsBrigada project);

    List<EdsBrigadaEmployee> getEmployeesByBrigada(Integer projectId);

    List<Integer> getActiveTeamsId();

    Integer getBrigadaLastIntNumber();

    Integer getTotalCount(ListingFilterParameter fp, Integer totalCount);

    ArrayList<String> getSavedBrigadasForThisPeriod(ArrayList<Integer> ids, String period, Integer shiftId, Integer shiftType);

    ArrayList<String> getSavedOvertimeForThisPeriod(ArrayList<Integer> ids, String overtimeQuery, String period, Integer shiftId, Integer type);
}
