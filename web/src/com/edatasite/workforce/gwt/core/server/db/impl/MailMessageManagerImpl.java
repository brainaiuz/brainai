package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsMailMessage;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.MailMessageManager;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.01.2010
 * Time: 14:43:14
 * To change this template use File | Settings | File Templates.
 */
@Repository("mailMessageManager")
public class MailMessageManagerImpl extends BaseManager<EdsMailMessage> implements MailMessageManager {

    public MailMessageManagerImpl() {
        super(EdsMailMessage.class);
    }

    public List<Object[]> getListForLead(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct m.id, m.subject, m.creationtime FROM" + getCompanyId() + ".mailmessage as m ");
        sql.append("inner join " + getCompanyId() + ".maillistmessage mlm on (mlm.mailmessageid=m.id) ");
        sql.append("inner join " + getCompanyId() + ".leadmaillist lml on (lml.maillistid=mlm.maillistid) ");
        sql.append(" where m.deleted <> true ");
        if (fp.getRelationID() != null) {
            sql.append(" and lml.entity_id = " + fp.getRelationID());
        }

        sql.append(" order by ");

        if (MailMessageItem.CREATED.equals(fp.getSortField())) {
            sql.append(" m.creationtime ");
        } else if (MailMessageItem.ID.equals(fp.getSortField())) {
            sql.append(" m.id ");
        } else if (MailMessageItem.SUBJECT.equals(fp.getSortField())) {
            sql.append(" m.subject ");
        } else {
            sql.append(" m.creationtime ");
        }

        if (fp.isAscending()) {
            sql.append(" asc ");
        } else {
            sql.append(" desc ");
        }

        if (fp.getLimit() != null && fp.getLimit() > 0) {
            sql.append(" limit ").append(fp.getLimit()).append(" offset ").append(fp.getStart());
        }

        return findNative(sql.toString());
    }

    public List<EdsMailMessage> getListOfMailMessages(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT m ");
        sql.append(getBaseMailMessageSql(fp));
        sql.append(" ORDER BY ");
        if (fp.getSortField() != null) {
            String code = fp.getSortField();
            if (MailMessageItem.SUBJECT.equals(code)) {
                sql.append(" m.subject ");
            } else if (MailMessageItem.FROM.equals(code)) {
                sql.append(" m.fromemail ");
            } else if (MailMessageItem.SCHEDULED.equals(code)) {
                sql.append(" m.scheduled ");
            } else if (MailMessageItem.CREATED.equals(code)) {
                sql.append(" m.creationTime ");
            } else if (MailMessageItem.UPDATED.equals(code)) {
                sql.append(" m.lastUpdateTime ");
            } else if (MailMessageItem.IS_SMS_MESSAGE.equals(code)) {
                sql.append(" m.isSmsMessage ");
            } else {
                sql.append(" m.lastUpdateTime ");
            }
            sql.append(fp.isAscending() ? " ASC " : " DESC ");
        } else {
            sql.append(" m.lastUpdateTime DESC ");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getCountOfMailMessages(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(*) ");
        sql.append(getBaseMailMessageSql(fp));
        return ((Long) findSingle(sql.toString())).intValue();
    }

    private String getBaseMailMessageSql(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append(" from EdsMailMessage m WHERE m.deleted is not true ");
        sql.append(" AND m.statusCode ").append(fp.isActive() ? " = " : " != ").append("'").append(MessageStatusEnum.SENT).append("' ");
        if (fp.getCampaignID() != null) {
            sql.append(" AND m.campaign.objectID=").append(fp.getCampaignID());
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" AND (LOWER(m.subject) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(m.fromemail) LIKE '").append(fp.getSqlSearchKey()).append("') ");
        }
        return sql.toString();
    }
}