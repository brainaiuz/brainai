/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/17 8:36:7                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsCrmEntitySendMessageStatus;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.CrmEntitySendMessageStatusManager;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 30-Jan-2010
 * Time: 18:55:20
 * To change this template use File | Settings | File Templates.
 */
@Repository("crmEntitySendMessageStatusManager")
public class CrmEntitySendMessageStatusManagerImpl extends BaseManager<EdsCrmEntitySendMessageStatus> implements CrmEntitySendMessageStatusManager {
    public CrmEntitySendMessageStatusManagerImpl() {
        super(EdsCrmEntitySendMessageStatus.class);
    }

    @Override
    public List<EdsCrmContact> getBouncedEntitiesList(ListingFilterParameter fp) {
        boolean hasSearch = !StringUtils.isEmpty(fp.getSearchKey());
        boolean hasSort = !StringUtils.isEmpty(fp.getSortField());

        String sql = "select mt.entity from EdsCrmEntitySendMessageStatus mt where mt.mailmessage.objectID =:messageId and mt.status = :status ";
        if (hasSearch) {
            sql += " and lower(mt.entity.primaryEmail) like :searchKey or lower(mt.entity.firstName) like :searchKey or lower(mt.entity.lastName) like :searchKey ";
        }
        if (hasSort) {
            switch (fp.getSortField()) {
                case MailMessageItem.RECIPIENT -> sql += " order by mt.entity.primaryEmail ";
                case MailMessageItem.FIRSTNAME -> sql += " order by mt.entity.firstName ";
                case MailMessageItem.LASTNAME -> sql += " order by mt.entity.lastName ";
                default -> sql += " order by mt.objectID ";
            }
            sql += fp.isAscending() ? "asc" : "desc";
        } else {
            sql += " order by mt.objectID desc ";
        }
        TypedQuery<EdsCrmContact> query = slaveEntityManager.createQuery(sql, EdsCrmContact.class)
                .setParameter("messageId", fp.getObjectId())
                .setParameter("status", MessageStatusEnum.BOUNCED)
                .setMaxResults(fp.getLimit())
                .setFirstResult(fp.getStart());
        if (hasSearch) {
            query = query.setParameter("searchKey", "%" + fp.getSearchKey().toLowerCase() + "%");
        }
        return query.getResultList();
    }

    @Override
    public Long getBouncedEntitiesCount(ListingFilterParameter fp) {
        boolean hasSearch = !StringUtils.isEmpty(fp.getSearchKey());
        String sql = "select count(distinct mt.entity.objectID) from EdsCrmEntitySendMessageStatus mt " +
                "      where mt.mailmessage.objectID =:messageId and mt.status = :status ";
        if (hasSearch) {
            sql += " and lower(mt.entity.primaryEmail) like :searchKey or lower(mt.entity.firstName) like :searchKey or lower(mt.entity.lastName) like :searchKey ";
        }
        TypedQuery<Long> query = slaveEntityManager.createQuery(sql, Long.class)
                .setParameter("messageId", fp.getObjectId())
                .setParameter("status", MessageStatusEnum.BOUNCED)
                .setMaxResults(1);
        if (hasSearch) {
            query.setParameter("searchKey", "%" + fp.getSearchKey().toLowerCase() + "%");
        }
        try {
            return query.getSingleResult();
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            return 0L;
        }
    }

    @Override
    public EdsCrmEntitySendMessageStatus getEntity(Integer messageID, Integer entityID) {
        return (EdsCrmEntitySendMessageStatus) findSingle("SELECT ms FROM EdsCrmEntitySendMessageStatus ms WHERE ms.mailmessage.objectID=? AND ms.entity.objectID=?", messageID, entityID);
    }

    @Override
    public Long getStatusCountByMessageID(Integer messageID, MessageStatusEnum status) {
        if (status != null) {
            return (Long) findSingle("SELECT count(t) from EdsCrmEntitySendMessageStatus t WHERE t.mailmessage.objectID=? AND t.status=?", messageID, status);
        } else {
            return (Long) findSingle("SELECT count(t) from EdsCrmEntitySendMessageStatus t WHERE t.mailmessage.objectID=?", messageID);
        }
    }
}
