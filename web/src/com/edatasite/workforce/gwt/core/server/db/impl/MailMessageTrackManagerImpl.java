package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsMailMessageTrack;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.MailMessageTrackManager;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.01.2010
 * Time: 15:04:09
 * To change this template use File | Settings | File Templates.
 */
@Repository("mailMessageTrackManager")
public class MailMessageTrackManagerImpl extends BaseManager<EdsMailMessageTrack> implements MailMessageTrackManager {

    public MailMessageTrackManagerImpl() {
        super(EdsMailMessageTrack.class);
    }

    @Override
    public List<EdsMailMessageTrack> getViewList(ListingFilterParameter fp) {
        boolean hasSearch = !StringUtils.isEmpty(fp.getSearchKey());
        boolean hasSort = !StringUtils.isEmpty(fp.getSortField());

        String sql = "select mt from EdsMailMessageTrack mt " +
                     "      where mt.message.objectID =:messageId ";
        if (hasSearch) {
            sql += " and lower(mt.entity.primaryEmail) like :searchKey or lower(mt.entity.firstName) like :searchKey or lower(mt.entity.lastName) like :searchKey ";
        }
        if (hasSort) {
            switch (fp.getSortField()) {
                case MailMessageItem.RECIPIENT -> sql += " order by mt.entity.primaryEmail ";
                case MailMessageItem.FIRSTNAME -> sql += " order by mt.entity.firstName ";
                case MailMessageItem.LASTNAME -> sql += " order by mt.entity.lastName ";
                case MailMessageItem.VIEW_COUNT -> sql += " order by mt.openedCount ";
                default -> sql += " order by mt.objectID ";
            }
            sql += fp.isAscending() ? "asc" : "desc";
        } else {
            sql += " order by mt.objectID desc ";
        }
        TypedQuery<EdsMailMessageTrack> query = slaveEntityManager.createQuery(sql, EdsMailMessageTrack.class)
                                                             .setParameter("messageId", fp.getObjectId())
                                                             .setMaxResults(fp.getLimit())
                                                             .setFirstResult(fp.getStart());
        if (hasSearch) {
            query = query.setParameter("searchKey", "%" + fp.getSearchKey().toLowerCase() + "%");
        }
        return query.getResultList();
    }

    @Override
    public Integer getViewListCount(ListingFilterParameter fp) {
        boolean hasSearch = !StringUtils.isEmpty(fp.getSearchKey());
        String sql = "select count(mt.objectID) from EdsMailMessageTrack mt " +
                     "      where mt.message.objectID =:messageId ";
        if (hasSearch) {
            sql += " and lower(mt.entity.primaryEmail) like :searchKey or lower(mt.entity.firstName) like :searchKey or lower(mt.entity.lastName) like :searchKey ";
        }
        TypedQuery<Long> query = slaveEntityManager.createQuery(sql, Long.class)
                                              .setParameter("messageId", fp.getObjectId());
        if (hasSearch) {
            query = query.setParameter("searchKey", "%" + fp.getSearchKey().toLowerCase() + "%");
        }
        List<Long> list = query.setMaxResults(1).getResultList();

        return list.isEmpty() ? 0 : list.get(0).intValue();
    }

    public List<EdsMailMessageTrack> getViewByMessageID(Integer messageID) {
        return (List<EdsMailMessageTrack>) find("select mt from EdsMailMessageTrack mt where mt.message.objectID=?", messageID);
    }

    public Long getViewCountByMessage(Integer messageID) {
        return (Long) findSingle("select count(DISTINCT mt.entity) from EdsMailMessageTrack mt where mt.message.objectID=?", messageID);
    }

    public EdsMailMessageTrack getByEntityAndMessage(Integer entityid, Integer messageID) {
        return (EdsMailMessageTrack) findSingle("from EdsMailMessageTrack mt where mt.entity.objectID=? and mt.message.objectID=?", entityid, messageID);
    }
}
