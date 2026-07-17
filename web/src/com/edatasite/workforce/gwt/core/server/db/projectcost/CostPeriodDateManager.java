package com.edatasite.workforce.gwt.core.server.db.projectcost;

import com.edatasite.workforce.core.domain.projectcost.EdsCostPeriodDate;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Date;
import java.util.List;

/**
 * User: Dilsh0d
 * Date: 27-May-2010
 * Time: 10:04:40
 */
public interface CostPeriodDateManager extends Manager<EdsCostPeriodDate> {
    List<EdsCostPeriodDate> getProjectCostPeriodList(Integer projectId, Integer taskId);

    boolean isEmpityPeriod(Integer projectId, Integer taskId, Date startDate, Date endDate);

    Integer getProjectCostPeriodId(Integer projectId, Integer taskId, Date from, Date to);
}
