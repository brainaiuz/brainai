package com.edatasite.workforce.gwt.core.server.db.projectcost;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.projectcost.EdsCostSheet;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectCostAllDataItem;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 28.04.2010
 * Time: 19:04:41
 * To change this template use File | Settings | File Templates.
 */
public interface CostSheetManager extends Manager<EdsCostSheet> {

    List<Object[]> getProjectStandartCostItems(ProjectCostAllDataItem costItemData, Integer costTypeId);

    List<Object[]> getProjectMarkupCostItems(ProjectCostAllDataItem costItemData, Integer costTypeId);

    List<EdsCostSheet> getCostSheetList(Integer projectCostItemId, Date from, Date to);

    List<EdsCostSheet> getProjectActualCostItems(ProjectCostAllDataItem costAllDataItem, EdsReference costType);
}
