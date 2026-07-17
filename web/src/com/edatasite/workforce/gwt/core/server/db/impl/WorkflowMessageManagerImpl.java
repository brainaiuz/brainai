package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSuperMessage;
import com.edatasite.workforce.core.domain.EdsWorkflowMessage;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.WorkflowMessageManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.profile.client.rpc.MessageItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Azazello on 7/11/2017.
 */
@Transactional
@Repository("workflowMessageManager")
public class WorkflowMessageManagerImpl extends BaseManager<EdsWorkflowMessage> implements WorkflowMessageManager {
    public WorkflowMessageManagerImpl() {
        super(EdsWorkflowMessage.class);
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter filterParameter, boolean isWorkflowMessages) {
        return ((Long) findSingle("select count(m) from " + (isWorkflowMessages ? "EdsWorkflowMessage" : "EdsMessage") + " m where m.companyID=?", SecurityContext.getCompanyID())).intValue();
    }

    @Override
    public List<EdsSuperMessage> getList(ListingFilterParameter filterParameter, boolean isWorkflowMessages) {
        StringBuilder sql = new StringBuilder();
        sql.append("select m from ").append(isWorkflowMessages ? "EdsWorkflowMessage" : "EdsMessage").append(" as m ");
        sql.append("where m.companyID=? ");
        if (filterParameter != null) {
            String searchKey = filterParameter.getSearchKey();
            if (!StringUtils.isEmpty(searchKey)) {
                searchKey = searchKey.toLowerCase();
                sql.append("and (lower(m.fromEmail) like '%").append(searchKey).append("%' ");
                sql.append("or lower(m.fromName) like '%").append(searchKey).append("%' ");
                sql.append("or lower(m.to) like '%").append(searchKey).append("%' ");
                sql.append("or lower(m.subject) like '%").append(searchKey).append("%') ");
            }
            if (!StringUtils.isEmpty(filterParameter.getSortField())) {
                String code = filterParameter.getSortField();
                if (MessageItem.FROM.equals(code)) {
                    sql.append(" ORDER BY m.fromEmail ");
                } else if (MessageItem.TO.equals(code)) {
                    sql.append(" ORDER BY m.to ");
                } else if (MessageItem.SUBJECT.equals(code)) {
                    sql.append(" ORDER BY m.subject ");
                } else if (MessageItem.STATUS.equals(code)) {
                    sql.append(" ORDER BY m.status ");
                } else if (MessageItem.ATTEMPTS.equals(code)) {
                    sql.append(" ORDER BY m.attempts ");
                } else if (MessageItem.CREATION_DATE.equals(code)) {
                    sql.append(" ORDER BY m.creationDate ");
                } else if (MessageItem.SENT_DATE.equals(code)) {
                    sql.append(" ORDER BY m.sentDate ");
                } else {
                    sql.append(" ORDER BY m.objectID ");
                }
                sql.append(!filterParameter.isAscending() ? " desc " : " ");
            } else {
                sql.append(" ORDER BY m.objectID desc ");
            }
        }
        return findInterval(sql.toString(), filterParameter.getStart(), filterParameter.getLimit(), SecurityContext.getCompanyID());
    }
}
