package com.edatasite.workforce.gwt.core.server.db.impl.goal;

import com.edatasite.workforce.core.domain.goal.EdsBusinessGoal;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.goal.BusinessGoalManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Sherali
 * Date: Oct 26, 2009
 * Time: 3:06:31 PM
 */
@Repository("businessGoalManager")
public class BusinessGoalManagerImpl extends BaseManager<EdsBusinessGoal> implements BusinessGoalManager {
    public BusinessGoalManagerImpl() {
        super(EdsBusinessGoal.class);
    }

    @Autowired
    private CompanyCustomFieldsManager companyCFSettingsManager;

    @Override
    public List<EdsBusinessGoal> list(ListingFilterParameter fp) {
        String companyID = getCompanyId();
        List<String> cfList = new ArrayList<>();
        boolean hasCustomFieldColumnName = false;
        if (fp.getEntityName() != null) {
            cfList = companyCFSettingsManager.getCompanyCustomFieldsColumnCodesList(fp.getEntityName());
            hasCustomFieldColumnName = fp.isCustomFieldsShown() && cfList != null && cfList.contains(fp.getSortField());
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT g.* ").append(hasCustomFieldColumnName ? (", gcf." + fp.getSortField() + " ") : "");
        sql.append(" FROM ").append(companyID).append(".businessgoal g \n");
        if (fp.isCustomFieldsShown()) {
            sql.append(" left outer join ").append(companyID).append(".goalcustomfields gcf on gcf.id = g.goalcustomfieldsid \n");
        }
        sql.append(" left join ").append(companyID).append(".reference ref on ref.id = g.status_id \n");
        sql.append("where g.deleted<>true \n");
        if (StringUtils.isNotBlank(fp.getSqlSearchKey())) {
            sql.append(" and (lower(g.title) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(g.description) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append("or lower(g.outcome) like '").append(fp.getSqlSearchKey()).append("' ");
            //searching by goal custom fields
            if (fp.isCustomFieldsShown() && cfList != null && cfList.size() > 0) {
                cfList.forEach(ccfS -> sql.append(" or lower(gcf.").append(ccfS).append(") like '").append(fp.getSqlSearchKey()).append("' "));
            }
            sql.append(")\n");
        }


        if (StringUtils.isNotBlank(fp.getSortField())) {
            sql.append("ORDER BY ");
            if (fp.getSortField().equals(GoalItem.COMPANY_GOAL_LIST_STATUS)) {
                sql.append("ref.name ");
            }
            if (fp.getSortField().equals(GoalItem.COMPANY_GOAL_LIST_OUTCOME)) {
                sql.append("g.outcome ");
            }
            if (fp.getSortField().equals(GoalItem.COMPANY_GOAL_LIST_FROM_DATE)) {
                sql.append("g.fromDate ");
            }
            if (fp.getSortField().equals(GoalItem.COMPANY_GOAL_LIST_TO_DATE)) {
                sql.append("g.toDate ");
            }
            if (fp.getSortField().equals(GoalItem.COMPANY_GOAL_LIST_DESCRIPTION)) {
                sql.append("g.description ");
            }
            if (fp.getSortField().equals(GoalItem.COMPANY_GOAL_LIST_TITLE)) {
                sql.append("g.title ");
            }

            if (cfList != null && cfList.contains(fp.getSortField())) {
                sql.append("gcf.").append(fp.getSortField()).append(" ");
            }

            if (!fp.isAscending()) {
                sql.append("DESC");
            }
        } else {
            sql.append("ORDER BY g.title asc");
        }
        return findNative(sql.toString(), EdsBusinessGoal.class);
    }

    @Override
    public void deleteCompanyGoal(Integer goalId) {
        update("update EdsBusinessGoal bg set bg.deleted = true where bg.objectID=? and bg.deleted <> true", goalId);
    }

    @Override
    public SelectItem[] getListAsSelectItems(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select g.id, g.title from ").append(getCompanyId()).append(".businessgoal g where (g.deleted is null or g.deleted is not true) ");
        if (StringUtils.isNotBlank(fp.getSqlSearchKey())) {
            sql.append(" and (");
            sql.append(" lower(g.title) like lower('").append(fp.getSqlSearchKey()).append("')");
            sql.append(" or lower(g.title) like lower('").append(fp.getSqlSearchKey()).append("%')");
            sql.append(" or lower(g.title) like lower('%").append(fp.getSqlSearchKey()).append("%')");
            sql.append(")");
        }
        if (fp.getLimit() > 0) {
            sql.append(" offset ").append(fp.getStart()).append(" limit ").append(fp.getLimit());
        }
        List<Object[]> objects = findNative(sql.toString());
        List<SelectItem> result = new ArrayList<>();
        if (objects != null && !objects.isEmpty()) {
            objects.forEach(object -> {
                SelectItem item = new SelectItem();
                item.setId((Integer) object[0]);
                item.setName((String) object[1]);
                result.add(item);
            });
        }
        return result.toArray(new SelectItem[0]);
    }
}
