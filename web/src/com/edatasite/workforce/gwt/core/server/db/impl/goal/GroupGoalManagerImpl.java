package com.edatasite.workforce.gwt.core.server.db.impl.goal;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.goal.EdsGroupGoal;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.goal.GroupGoalManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupGoalITem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("groupGoalManager")
public class GroupGoalManagerImpl extends BaseManager<EdsGroupGoal> implements GroupGoalManager {

    public GroupGoalManagerImpl() {
        super(EdsGroupGoal.class);
    }

    @Override
    public List<EdsGroupGoal> getList(ListingFilterParameter fp) {
        StringBuilder query = createCaseQuery(fp);
        return findInterval(query.toString(), fp.getStart(), fp.getLimit());
    }

    private StringBuilder createCaseQuery(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        EdsUser user = getUser();
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        sql.append("select gg from EdsGroupGoal gg ");
        sql.append("left join gg.employee user ");
        sql.append("left join gg.currentApprover currap ");
        sql.append("left join currap.exactEmployee appr ");
        sql.append("left join gg.overallStatus st ");

        sql.append("where gg.deleted=false ");
        if (!ServerUtils.hasPermission(PermissionConstants.HRMS_GROUP_VIE_ALL_PERSONAL_GOALS, user)) {
            sql.append(" and ( gg.auditInfo.createdBy_id=").append(user.getObjectID());
            sql.append(" or appr.objectID =").append(user.getObjectID()).append(") ");
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(user.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" or lower(user.lastName) like '").append(fp.getSqlSearchKey()).append("') ");
        }

        sql.append(" order by ");

        if (fp.getSortField() == null) {
            sql.append("gg.id desc ");
        } else {
            switch (fp.getSortField()) {
                case GroupGoalITem.GROUP_GOAL_EMPLOYEE -> sql.append(" user.firstName,user.lastName ");
                case GroupGoalITem.GROUP_GOAL_APPROVER -> sql.append(" appr.firstName,appr.lastName ");
                case GroupGoalITem.GROUP_GOAL_STATUS -> sql.append(" st.name ");
            }
            sql.append((fp.isAscending() ? " asc " : " desc "));
        }
        return sql;
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter filterParametrs) {
        StringBuilder query = createCaseQuery(filterParametrs);
        return find(query.toString()).size();
    }
}
