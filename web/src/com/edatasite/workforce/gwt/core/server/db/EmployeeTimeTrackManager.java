package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployeeTimeTrack;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;

public interface EmployeeTimeTrackManager extends Manager<EdsEmployeeTimeTrack> {

    EdsEmployeeTimeTrack getEmployeeTimeTrack(Integer employeeID, String status, DateNonConvertable date);
}
