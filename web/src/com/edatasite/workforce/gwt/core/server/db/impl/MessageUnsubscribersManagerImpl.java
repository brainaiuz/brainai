package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsMessageUnsubscribers;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.MessageUnsubscribersManager;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Azazello on 7/10/2017.
 */
@Repository("messageUnsubscribersManager")
public class MessageUnsubscribersManagerImpl extends BaseManager<EdsMessageUnsubscribers> implements MessageUnsubscribersManager {
    public MessageUnsubscribersManagerImpl() {
        super(EdsMessageUnsubscribers.class);
    }

    @Override
    public EdsMessageUnsubscribers getByMsgAndEntity(Integer msgID, Integer entityID) {
        return (EdsMessageUnsubscribers) findSingle("SELECT mu from EdsMessageUnsubscribers mu WHERE mu.entity.objectID=? AND mu.mailmessage.objectID=?", entityID, msgID);
    }

    @Override
    public void insertUnsubscriber(Integer subscriberID, Integer msgID, Integer mailListID) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(getCompanyId()).append(".messageUnsubscribers (entity_id,messageid,maillist_id) VALUES ");
        sql.append("(").append(subscriberID).append(",").append(msgID).append(",").append(mailListID).append(")");
        updateNative(sql.toString());
    }

    @Override
    public Long getEntitiesCountByMessageID(ListingFilterParameter fp) {
        boolean hasSearch = !StringUtils.isEmpty(fp.getSearchKey());
        String sql = "select count(distinct mt.entity.objectID) from EdsMessageUnsubscribers mt where mt.mailmessage.objectID =:messageId ";
        if (hasSearch) {
            sql += " and lower(mt.entity.primaryEmail) like :searchKey or lower(mt.entity.firstName) like :searchKey or lower(mt.entity.lastName) like :searchKey ";
        }
        TypedQuery<Long> query = slaveEntityManager.createQuery(sql, Long.class)
                .setMaxResults(1)
                .setParameter("messageId", fp.getObjectId());
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
    public List<EdsCrmContact> getEntitiesByMessageID(ListingFilterParameter fp) {
        boolean hasSearch = !StringUtils.isEmpty(fp.getSearchKey());
        boolean hasSort = !StringUtils.isEmpty(fp.getSortField());

        String sql = "select mt.entity from EdsMessageUnsubscribers mt where mt.mailmessage.objectID =:messageId ";
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
                .setMaxResults(fp.getLimit())
                .setFirstResult(fp.getStart());
        if (hasSearch) {
            query = query.setParameter("searchKey", "%" + fp.getSearchKey().toLowerCase() + "%");
        }
        return query.getResultList();
    }

    @Override
    public Long getUnsubscribersCountByMessageID(Integer msgID) {
        return (Long) findSingle("select count(distinct mu.entity.objectID) from EdsMessageUnsubscribers mu where mu.mailmessage.objectID=?", msgID);
    }
}
