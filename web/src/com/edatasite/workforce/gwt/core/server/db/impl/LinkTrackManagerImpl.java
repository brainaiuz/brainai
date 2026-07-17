package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsLinkTrack;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.LinkTrackManager;
import com.edatasite.workforce.gwt.crm.client.rpc.MailMessageItem;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 10.06.2013
 * Time: 14:43:14
 */
@Repository
public class LinkTrackManagerImpl extends BaseManager<EdsLinkTrack> implements LinkTrackManager {

    public LinkTrackManagerImpl() {
        super(EdsLinkTrack.class);
    }

    @Override
    public EdsLinkTrack getByEntityAndMessageID(Integer messageID, Integer entityID, Integer linkID) {
        return (EdsLinkTrack) findSingle("select et from EdsLinkTrack et where et.messageID = " + messageID + " and entity = " + entityID + (linkID != null ? " and linkID = " + linkID : ""));
    }

    @Override
    public Long getClickCountByMessageID(Integer messageID) {
        return (Long) findSingle("select count(DISTINCT mt.entity) from EdsLinkTrack mt where mt.messageID=?", messageID);
    }

    @Override
    public List<Object[]> getClickedEntitiesList(ListingFilterParameter fp) {
        return findNative(getQuery(fp, false));
    }

    @Override
    public Integer getClickedEntitiesCount(ListingFilterParameter fp) {
        return ((BigInteger) findNativeSingle(getQuery(fp, true))).intValue();
    }

    public String getQuery(ListingFilterParameter fp, boolean count) {
        StringBuilder sql = new StringBuilder();
        if (count) {
            sql.append("SELECT count(lt.id) \n");
        } else {
            sql.append("SELECT lt.entity, cc.primaryEmail, cc.firstName, cc.lastName, cc.contactType, cc.crmAccount, l.original_link, lt.openedCount \n");
        }
        sql.append("FROM ").append(getCompanyId()).append(".linkTrack lt \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".crmContact cc ON cc.id = lt.entity \n");
        sql.append("INNER JOIN ").append(getCompanyId()).append(".link l ON lt.linkID = l.id \n");
        sql.append("WHERE lt.messageID = ").append(fp.getObjectId()).append(" \n");
        if (!StringUtils.isEmpty(fp.getSearchKey())) {
            sql.append(" and lower(cc.primaryEmail) like '%").append(fp.getSearchKey()).append("%' or ")
                    .append(" lower(cc.firstName) like '%").append(fp.getSearchKey()).append("%' or ")
                    .append(" lower(cc.lastName) like '%").append(fp.getSearchKey()).append("%' or ")
                    .append(" lower(l.original_link) like '%").append(fp.getSearchKey()).append("%' ");
        }
        if (!count) {
            sql.append(" order by ");
            if (!StringUtils.isEmpty(fp.getSortField())) {
                switch (fp.getSortField()) {
                    case MailMessageItem.RECIPIENT -> sql.append(" cc.primaryEmail ");
                    case MailMessageItem.FIRSTNAME -> sql.append(" cc.firstName ");
                    case MailMessageItem.LASTNAME -> sql.append(" cc.lastName ");
                    case MailMessageItem.LINK -> sql.append(" l.original_link ");
                    case MailMessageItem.CLICK_COUNT -> sql.append(" lt.openedCount ");
                    default -> sql.append(" lt.id ");
                }
            } else {
                sql.append(" lt.id ");
            }
            sql.append(fp.isAscending() ? "asc" : "desc");
        }
        return sql.toString();
    }
}
