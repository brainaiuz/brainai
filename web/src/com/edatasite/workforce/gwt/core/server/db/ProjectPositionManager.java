package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsProjectEmployee;
import com.edatasite.workforce.core.domain.EdsProjectPosition;

import java.util.Date;
import java.util.List;

/**
 * Created by Normurod on 8/5/15.
 */
public interface ProjectPositionManager extends Manager<EdsProjectPosition> {

    List<EdsProjectPosition> getProjectPositions(Integer projectID);

    List<EdsProjectEmployee> getProjectPositionEmployees(Integer projectID, Integer positionID);

    EdsProjectPosition getProjectPosition(Integer projectID, Integer positionID);

    Date getProjectPositionLastDate(Integer contractID);
}
