/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/17 8:33:26                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsMailList;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.MailListManager;
import com.edatasite.workforce.gwt.crm.client.rpc.MailListItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.01.2010
 * Time: 14:43:14
 * To change this template use File | Settings | File Templates.
 */
@Repository("mailListManager")
public class MailListManagerImpl extends BaseManager<EdsMailList> implements MailListManager {

    public MailListManagerImpl() {
        super(EdsMailList.class);
    }

    @Override
    public List<EdsMailList> getList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * from ").append(getCompanyId()).append(".maillist ml \n");
        sql.append("WHERE ml.deleted is not true AND ml.active is true \n");
        if (fp.getShowInListing() != null && !fp.getShowInListing() && getUser() != null) {
            sql.append("and ml.owner=").append(getUser().getObjectID()).append(" \n");
        }
        if (!StringUtils.isEmpty(fp.getSqlSearchKey())) {
            sql.append(" AND LOWER(ml.name) LIKE '").append(fp.getSqlSearchKey()).append("'");
        }
        return findNative(sql.toString(), EdsMailList.class);
    }

    @Override
    public List<Object[]> getListOfMailLists(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ml.id AS id, \n");
        sql.append("ml.name AS ").append(MailListItem.NAME).append(", \n");
        sql.append("(SELECT count(distinct cc.id) FROM ").append(getCompanyId()).append(".crmContact cc \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".leadMailList lml ON lml.entity_id=cc.id \n");
        sql.append("WHERE lml.maillistid=ml.id AND lml.deleted is not true AND cc.deleted is not true) AS ").append(MailListItem.MEMBERS_COUNT).append(", \n");
        sql.append("ml.active AS ").append(MailListItem.ACTIVE).append(", \n");
        sql.append("ml.creationtime AS ").append(MailListItem.CREATION_TIME).append(" \n");
        sql.append("FROM ").append(getCompanyId()).append(".maillist AS ml \n");
        sql.append("WHERE ml.deleted is not true ");
        if (fp.getShowInListing() != null && !fp.getShowInListing() && getUser() != null) {
            sql.append(" AND (ml.owner is null or ml.owner = " + getUser().getObjectID() + ")");
        }
        if (!StringUtils.isEmpty(fp.getSqlSearchKey())) {
            sql.append(" AND LOWER(ml.name) LIKE '" + fp.getSqlSearchKey().toLowerCase() + "' ");
        }
        sql.append(" ORDER BY ").append(StringUtils.isEmpty(fp.getSortField()) ? MailListItem.CREATION_TIME : fp.getSortField()).append(fp.isAscending() ? " DESC" : " ASC ");
        if (fp.getLimit() > 0) {
            sql.append(" LIMIT " + fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" OFFSET " + fp.getStart());
        }
        return slaveEntityManager.createNativeQuery(sql.toString()).getResultList();
//        return findNative(sql.toString());
    }

    @Override
    public Long getTotalCountOfMailLists(ListingFilterParameter fp) {
        /*StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(ml.objectID) from EdsMailList ml WHERE ml.deleted is not true ");
        if (fp.getShowInListing() != null && !fp.getShowInListing() && getUser() != null) {
            sql.append(" AND (ml.owner is null or ml.owner=:owner)");
        }
        if (!StringUtils.isEmpty(fp.getSqlSearchKey())) {
            sql.append(" AND LOWER(ml.name) LIKE :searchKey");
        }
        TypedQuery<Long> query = entityManager.createQuery(sql.toString(), Long.class);
        if (fp.getShowInListing() != null && !fp.getShowInListing() && getUser() != null) {
            query.setParameter("owner", getUser().getObjectID());
        }
        if (!StringUtils.isEmpty(fp.getSqlSearchKey())) {
            query.setParameter("searchKey", fp.getSqlSearchKey().toLowerCase());
        }
        return query.getSingleResult();
        */

        StringBuilder sql = new StringBuilder();
        sql.append("select count(ml.id) from ").append(getCompanyId()).append(".maillist ml");
        sql.append(" where ml.deleted is not true");
        if (fp.getShowInListing() != null && !fp.getShowInListing() && getUser() != null) {
            sql.append(" and (ml.owner is null or ml.owner = ").append(getUser().getObjectID()).append(")");
        }
        if (!StringUtils.isEmpty(fp.getSearchKey())) {
            sql.append(" and lower(ml.name) like '%").append(fp.getSearchKey()).append("%'");
        }

        BigInteger count = (BigInteger) findNativeSingle(sql.toString());
        return count != null ? count.longValue() : 0;
    }

    @Override
    public List<EdsMailList> getContactsEdsMailingLists(Integer contactID) {
        return slaveEntityManager.createQuery("select lml.mailList from EdsCrmEntityMailList lml where lml.deleted <> true " +
                "and lml.mailList.deleted <> true and lml.entity.objectID=:contactId", EdsMailList.class).setParameter("contactId", contactID).getResultList();
    }

    @Override
    public List<SelectItem> getContactsMailingLists(Integer contactID) {
        List<Object[]> mailList = slaveEntityManager.createQuery("select lml.mailList.objectID, lml.mailList.name from EdsCrmEntityMailList lml where lml.deleted <> true " +
                "and lml.mailList.deleted <> true and lml.entity.objectID=:contactId").setParameter("contactId", contactID).getResultList();

        return mailList.stream().map(m -> new SelectItem(((Integer) m[0]), (String) m[1])).collect(Collectors.toList());
    }

    @Override
    public Long getLeadCountByMessageID(Integer messageID) {
        return slaveEntityManager.createQuery("select count(distinct entity.objectID) from EdsCrmEntityMailList where deleted <> true and  mailList.objectID IN " +
                "(select mlm.mailList.objectID from EdsMailListMessage mlm where mlm.mailList.deleted=false and mlm.mailMessage.objectID=:messageId)", Long.class)
                .setParameter("messageId", messageID).getSingleResult();
    }
}
