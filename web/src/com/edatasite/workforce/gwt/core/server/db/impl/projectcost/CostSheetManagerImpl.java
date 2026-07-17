package com.edatasite.workforce.gwt.core.server.db.impl.projectcost;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.projectcost.EdsCostSheet;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.projectcost.CostSheetManager;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostAllDataItem;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 28.04.2010
 * Time: 19:07:21
 * To change this template use File | Settings | File Templates.
 */
@Repository("costSheetManager")
public class CostSheetManagerImpl extends BaseManager<EdsCostSheet> implements CostSheetManager {

    public CostSheetManagerImpl() {
        super(EdsCostSheet.class);
    }

    public List<Object[]> getProjectStandartCostItems(ProjectCostAllDataItem costItemData, Integer costTypeId) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        if (costItemData.isEstemitedCost()) {
            sql.append("select projectCostItem.id,SUM(costSheet.plannedUnit) as plannedHours,MAX(costSheet.plannedUnit) as daily,MAX(costSheet.plannedRate) as rate,SUM(costSheet.plannedCost) as plannedCost \n");
        } else {
            sql.append("select projectCostItem.id,SUM(costSheet.actualUnit) as actualHours,MAX(costSheet.actualUnit) as daily,MAX(costSheet.actualRate) as rate,SUM(costSheet.actualCost) as actualCost\n");
        }
        sql.append("from " + getCompanyId() + ".costSheet \n");
        sql.append("    inner join " + getCompanyId() + ".projectCostItem on projectCostItem.id=costSheet.projectCostItemId \n");
        sql.append("    left join " + getCompanyId() + ".project on project.id=projectCostItem.projectid \n");
        sql.append("    left join " + getCompanyId() + ".task on task.id=projectCostItem.taskid \n");
        sql.append("    left join " + getCompanyId() + ".reference resourceType on resourceType.id=projectCostItem.resourceTypeId \n");
        sql.append("    left join " + getCompanyId() + ".reference costType on costType.id=projectCostItem.costTypeId \n");
        sql.append("where projectCostItem.startdate='" + format.format(costItemData.getFrom()) + " 00:00:00' and  projectCostItem.enddate='" + format.format(costItemData.getTo()) + " 23:59:59' \n");
        sql.append("and project.id=:projectID and task.id=:taskID and resourceType.id=:resourceTypeID and  costType.id=:costTypeID \n");
        sql.append("group by projectCostItem.id \n");
        sql.append("order by projectCostItem.id \n");

        Map<String, Object> map = new HashMap<>();
        map.put("projectID", costItemData.getProjectId());
        map.put("taskID", costItemData.getTaskId());
        map.put("resourceTypeID", costItemData.getResourceTypeId());
        map.put("costTypeID", costTypeId);

        return findNativeByNamedParams(sql.toString(), map);
    }

    public List<Object[]> getProjectMarkupCostItems(ProjectCostAllDataItem costItemData, Integer costTypeId) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        StringBuilder sql = new StringBuilder();
        if (costItemData.isEstemitedCost()) {
            sql.append("select projectCostItem.id,SUM(costSheet.plannedPercentCompleted) as amount,MAX(costSheet.plannedPercentCharge) as percent,SUM(costSheet.plannedCost) as plannedCost \n");
        } else {
            sql.append("select projectCostItem.id,SUM(costSheet.actualPercentCompleted) as amount,MAX(costSheet.actualPercentCharge) as percent,SUM(costSheet.actualCost) as actualCost \n");
        }
        sql.append("from " + getCompanyId() + ".costSheet \n");
        sql.append("    inner join " + getCompanyId() + ".projectCostItem on projectCostItem.id=costSheet.projectCostItemId \n");
        sql.append("    left join " + getCompanyId() + ".project on project.id=projectCostItem.projectid \n");
        sql.append("    left join " + getCompanyId() + ".task on task.id=projectCostItem.taskid \n");
        sql.append("    left join " + getCompanyId() + ".reference resourceType on resourceType.id=projectCostItem.resourceTypeId \n");
        sql.append("    left join " + getCompanyId() + ".reference costType on costType.id=projectCostItem.costTypeId \n");
        sql.append("where projectCostItem.startdate='" + format.format(costItemData.getFrom()) + " 00:00:00' and  projectCostItem.enddate='" + format.format(costItemData.getTo()) + " 23:59:59' \n");
        sql.append(" and project.id=:projectID and task.id=:taskID and resourceType.id=:resourceTypeID and  costType.id=:costTypeID \n");
        sql.append("group by projectCostItem.id \n");
        sql.append("order by projectCostItem.id \n");

        Map<String, Object> map = new HashMap<>();
        map.put("projectID", costItemData.getProjectId());
        map.put("taskID", costItemData.getTaskId());
        map.put("resourceTypeID", costItemData.getResourceTypeId());
        map.put("costTypeID", costTypeId);

        return findNativeByNamedParams(sql.toString(), map);
    }

    public List<EdsCostSheet> getCostSheetList(Integer projectCostItemId, Date from, Date to) {
        return find("select cs from EdsCostSheet cs where  cs.projectCostItem.objectID=? and cs.date between ? and  ? " +
                "order by cs.date asc ", projectCostItemId, from, to);
    }

    public List<EdsCostSheet> getProjectActualCostItems(ProjectCostAllDataItem costAllDataItem, EdsReference costType) {
        Calendar fromTime = Calendar.getInstance();
        fromTime.setTime(costAllDataItem.getFrom());
        fromTime.set(Calendar.AM_PM, 0);
        fromTime.set(Calendar.HOUR_OF_DAY, 0);
        fromTime.set(Calendar.MINUTE, 0);
        fromTime.set(Calendar.SECOND, 0);
        fromTime.set(Calendar.MILLISECOND, 0);
        return find("select cs from EdsCostSheet cs " +
                " left join cs.projectCostItem costItem " +
                " where cs.date=? and costItem.project.objectID=? and costItem.task.objectID=? " +
                " and costItem.resourceType.objectID=?  and costItem.costType=?",
                fromTime.getTime(), costAllDataItem.getProjectId(), costAllDataItem.getTaskId(), costAllDataItem.getResourceTypeId(), costType);

    }
}