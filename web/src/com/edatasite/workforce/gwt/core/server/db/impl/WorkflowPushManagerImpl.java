package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.workflow.EdsWorkflowPush;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.WorkflowPushManager;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowPush;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Azazello on 10/15/15.
 */
@Repository("workflowPushManager")
public class WorkflowPushManagerImpl extends BaseManager<EdsWorkflowPush> implements WorkflowPushManager {
    public WorkflowPushManagerImpl() {
        super(EdsWorkflowPush.class);
    }

    @Override
    public void deletePushs(ArrayList<Integer> ids) {
        updateNative("UPDATE " + getCompanyId() + ".workflowPush set deleted = true WHERE id in (" + ServerUtils.getAsCommoDelimited(ids, "0", ",") + ")");
    }

    @Override
    public List<EdsWorkflowPush> list(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT wp from EdsWorkflowPush wp ");
        sql.append("WHERE wp.deleted is not true ");
        if(fp.getWorkflowID() != null){
            sql.append("AND wp.workflow.objectID = " + fp.getWorkflowID() + " ");
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append("AND (lower(wp.subject) like '").append(fp.getSqlSearchKey()).append("'");
        }
        sql.append(" ORDER BY ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (WorkflowPush.SUBJECT.equals(fp.getSortField())) {
                sql.append("wp.subject");
            } else if (WorkflowPush.RECIPIENT.equals(fp.getSortField())) {
                sql.append("wp.recipient");
            } else {
                sql.append("wp.subject");
            }
            sql.append(!fp.isAscending() ? " DESC " : " ");
        } else {
            sql.append(" wp.subject DESC");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());

    }

    @Override
    public Integer getTotalCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(wp.objectID) from EdsWorkflowPush wp ");
        sql.append("WHERE wp.deleted is not true ");
        if(fp.getWorkflowID() != null){
            sql.append("AND wp.workflow.objectID = " + fp.getWorkflowID() + " ");
        }
        return ((Long) findSingle(sql.toString())).intValue();
    }
}
