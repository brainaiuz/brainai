package com.edatasite.workforce.gwt.core.server.db.impl.projectcost;

import com.edatasite.workforce.core.domain.projectcost.EdsCostPeriodDate;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.projectcost.CostPeriodDateManager;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Dilsh0d
 * Date: 27-May-2010
 * Time: 10:06:39
 */
@Repository("costPeriodDateManager")
public class CostPeriodDateManagerImpl extends BaseManager<EdsCostPeriodDate> implements CostPeriodDateManager {
    public CostPeriodDateManagerImpl() {
        super(EdsCostPeriodDate.class);
    }

    public List<EdsCostPeriodDate> getProjectCostPeriodList(Integer projectId, Integer taskId) {
        return find("select period from EdsCostPeriodDate period " +
                "where period.project.objectID=? and period.task.objectID=? ", projectId, taskId);
    }

    public boolean isEmpityPeriod(Integer projectId, Integer taskId, Date startDate, Date endDate) {
        Map<String, Object> map = new HashMap<>();
        map.put("projectID", projectId);
        map.put("taskID", taskId);
        map.put("startDATE", startDate);
        map.put("endDATE", endDate);
        List<EdsCostPeriodDate> periodList = findByNamedParams("select period from EdsCostPeriodDate period " +
                "where period.project.objectID=:projectID and period.task.objectID=:taskID " +
                "and ((period.startDate>=:startDATE and period.startDate<=:endDATE and period.endDate>:endDATE)  or  (period.startDate<:startDATE and period.endDate>=:startDATE and period.endDate<=:endDATE) or " +
                " (period.startDate<=:startDATE and period.endDate>=:startDATE and period.endDate<:endDATE) or (period.startDate>:startDATE and period.startDate<=:endDATE and period.endDate>=:endDATE) or " +
                " (period.startDate>:startDATE and period.endDate<:endDATE) or (period.startDate<:startDATE and period.endDate>:endDATE)) ", map);

        return periodList.size() == 0;
    }

    public Integer getProjectCostPeriodId(Integer projectId, Integer taskId, Date from, Date to) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return (Integer) findNativeSingle("select period.id from " + getCompanyId() + ".costperioddate period " +
                "where period.projectId=? and period.taskId=? and period.startDate='" + format.format(from) + " 00:00:00' and period.endDate='" + format.format(to) + " 23:59:59'", projectId, taskId);
    }
}
