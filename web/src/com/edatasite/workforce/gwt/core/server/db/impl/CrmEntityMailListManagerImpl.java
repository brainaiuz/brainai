package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsCrmEntityMailList;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.core.client.enums.MessageStatusEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmEntityMailListManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25.01.2010
 * Time: 15:04:09
 * To change this template use File | Settings | File Templates.
 */
@Repository("crmEntityMailListManager")
public class CrmEntityMailListManagerImpl extends BaseManager<EdsCrmEntityMailList> implements CrmEntityMailListManager, Constants {

    public CrmEntityMailListManagerImpl() {
        super(EdsCrmEntityMailList.class);
    }

    @Override
    public List<EdsCrmEntityMailList> getList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct lml.* from ").append(getCompanyId()).append(".leadmaillist as lml ");
        sql.append(" where lml.entity_id = ").append(fp.getContactID()).append(" AND (lml.deleted <> true OR lml.deleted is null) ");
        return findNative(sql.toString(), EdsCrmEntityMailList.class);
    }

    @Override
    public Long getCrmEntityCount(ArrayList<Integer> mailListIDs) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(lml.id) FROM ").append(getCompanyId()).append(".leadMailList lml ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".mailList ml ON ml.id = lml.maillistid ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmContact c ON c.id = lml.entity_id ");
        sql.append("WHERE lml.deleted is not true ");
        sql.append("AND ml.deleted is not true ");
        sql.append("AND c.deleted is not true ");
        sql.append("AND ml.id in (").append(ServerUtils.getAsCommoDelimited(mailListIDs, "0", ",")).append(")");
        return ((BigInteger) findNativeSingle(sql.toString())).longValue();
    }

    @Override
    public List<EdsCrmContact> getMailListMembers(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT lml.entity FROM EdsCrmEntityMailList lml WHERE lml.entity.deleted is not true \n");
        sql.append("AND lml.mailList.objectID=:mailListID \n");
        if (fp.isShowUnsbcribeds()) {
            sql.append(" AND lml.deleted is true \n");
        } else {
            sql.append(" AND (lml.deleted is not true OR lml.deleted is null) \n");
        }
        if (!StringUtils.isEmpty(fp.getSearchKey())) {
            String searchKey = fp.getSearchKey().toLowerCase();
            sql.append(" AND (lower(lml.entity.firstName) LIKE '%" + searchKey + "%' OR lower(lml.entity.lastName) LIKE '%" + searchKey + "%' OR lower(lml.entity.primaryEmail) LIKE '%" + searchKey + "%') \n");
        }
        if (!StringUtils.isEmpty(fp.getSortField())) {
            sql.append(" ORDER BY ").append(fp.getSortField().equals("email") ? "lml.entity.primaryEmail" : " lml.entity.firstName ").append(fp.isAscending() ? " ASC " : " DESC ");
        }
        return slaveEntityManager.createQuery(sql.toString(), EdsCrmContact.class).setParameter("mailListID", fp.getMailListID()).setFirstResult(fp.getStart()).setMaxResults(fp.getLimit()).getResultList();
    }

    @Override
    public Long getMailListMembersCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(DISTINCT lml.objectID) FROM EdsCrmEntityMailList lml WHERE lml.entity.deleted is not true \n");
        sql.append("AND lml.mailList.objectID=:mailListID \n");
        if (fp.isShowUnsbcribeds()) {
            sql.append(" AND lml.deleted is true \n");
        } else {
            sql.append(" AND (lml.deleted is not true OR lml.deleted is null) \n");
        }
        if (!StringUtils.isEmpty(fp.getSearchKey())) {
            String searchKey = fp.getSearchKey().toLowerCase();
            sql.append(" AND (lower(lml.entity.firstName) LIKE '%" + searchKey + "%' OR lower(lml.entity.lastName) LIKE '%" + searchKey + "%' OR lower(lml.entity.primaryEmail) LIKE '%" + searchKey + "%') \n");
        }
        return slaveEntityManager.createQuery(sql.toString(), Long.class).setParameter("mailListID", fp.getMailListID()).getSingleResult();
    }

    @Override
    public void subscribeOrUnsubscribeUsers(Integer mailListID, ArrayList<Integer> entityIDs, boolean unsubscribe) {
        masterEntityManager.createQuery("UPDATE EdsCrmEntityMailList set deleted=:unsubscribe where mailList.objectID=:mailListID and entity.objectID in (:entityIDs)")
                .setParameter("unsubscribe", unsubscribe).setParameter("mailListID", mailListID).setParameter("entityIDs", entityIDs).executeUpdate();
    }

    @Override
    public List<Integer> getMailListEntityIDs(Integer mailListID, List<Integer> subscriberID) {
        return find("SELECT DISTINCT item.entity.objectID FROM EdsCrmEntityMailList item WHERE item.mailList.objectID = " + mailListID + " and item.entity.objectID in (" + ServerUtils.getAsCommoDelimited(subscriberID, "0") + ")");
    }

    @Override
    public List<EdsCrmEntityMailList> getSubscribedListsByCrmEntityId(Integer crmEntityID) {
        /*return (List<EdsCrmEntityMailList>) find("SELECT lml FROM EdsCrmEntityMailList lml WHERE  " +
                "(lml.deleted <> TRUE OR lml.deleted IS null) " +
                "AND lml.entity.objectID = ? ", crmEntityID);*/
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct lml.* from ").append(getCompanyId()).append(".leadmaillist as lml");
        sql.append(" left join ").append(getCompanyId()).append(".maillist ml on lml.maillistid = ml.id");
        sql.append(" where lml.entity_id = ").append(crmEntityID);
        sql.append(" and (lml.deleted is null or lml.deleted is not true)");
        sql.append(" and (ml.deleted is null or ml.deleted is not true)");
        return findNative(sql.toString(), EdsCrmEntityMailList.class);
    }

    @Override
    public Long getCountByDay(Calendar date) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(lml.id) ");
        sql.append(" FROM " + getCompanyId() + ".mailmessage as m ");
        sql.append(" LEFT JOIN " + getCompanyId() + ".maillistmessage mlm ON m.id = mlm.mailmessageid");
        sql.append(" LEFT JOIN " + getCompanyId() + ".maillist ml ON mlm.maillistid = ml.id");
        sql.append(" LEFT JOIN " + getCompanyId() + ".leadmaillist lml ON lml.maillistid = ml.id");
        sql.append(" LEFT JOIN " + getCompanyId() + ".crmContact c ON lml.entity_id = c.id");
        sql.append(" WHERE ").append(ServerUtils.checkForDeleted("m.deleted"));
        sql.append(" AND ").append(ServerUtils.checkForDeleted("lml.deleted"));
        sql.append(" AND ").append(ServerUtils.checkForDeleted("c.deleted"));
        sql.append(" AND m.statusCode in ('").append(MessageStatusEnum.SENT).append("','").append(MessageStatusEnum.IN_PROGRESS).append("','").append(MessageStatusEnum.PENDING).append("')");
        sql.append(" AND (extract(year from m.scheduled) = ").append(date.get(Calendar.YEAR)).append(" and extract(month from m.scheduled) = ").append(date.get(Calendar.MONTH) + 1).append(" and extract(day from m.scheduled) = ").append(date.get(Calendar.DAY_OF_MONTH)).append(")");
        return ((BigInteger) findNativeSingle(sql.toString())).longValue();
    }

    @Override
    public EdsCrmEntityMailList getMailListDeletedEntity(Integer mailListID, Integer entityID) {
        return (EdsCrmEntityMailList) findSingle("SELECT item FROM EdsCrmEntityMailList item WHERE item.mailList.objectID = ? and item.entity.objectID = ? AND item.deleted is true ", mailListID, entityID);
    }

    @Override
    public Map<Integer, List<Integer>> getByCrmEntityIDs(ArrayList<Integer> crmEntityIDs) {
        Map<Integer, List<Integer>> map = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct lml.* from ").append(getCompanyId()).append(".leadmaillist as lml ");
        sql.append(" where lml.entity_id in ( ").append(ServerUtils.getAsCommoDelimited(crmEntityIDs, "0", ",")).append(")");
        sql.append("  AND (lml.deleted is not true OR lml.deleted is null) ");
        List<EdsCrmEntityMailList> mailLists = findNative(sql.toString(), EdsCrmEntityMailList.class);
        if (mailLists != null && mailLists.size() > 0) {
            for (EdsCrmEntityMailList mailList : mailLists) {
                if (mailList.getEntity() != null) {
                    map.computeIfAbsent(mailList.getEntity().getObjectID(), k -> new ArrayList<>());
                    if (mailList.getMailList() != null
                            && mailList.getEntity() != null
                            && map.get(mailList.getEntity().getObjectID()) != null
                            && !map.get(mailList.getEntity().getObjectID()).contains(mailList.getMailList().getObjectID())) {
                        map.get(mailList.getEntity().getObjectID()).add(mailList.getMailList().getObjectID());
                    }
                }
            }
        }
        return map;
    }

    @Override
    public List<Integer> getCrmEntitiesSubscribedLists(Integer entityID) {
        return (List<Integer>) find("select lml.mailList.objectID from EdsCrmEntityMailList lml where  (lml.deleted <> true OR lml.deleted is null) AND lml.entity.objectID = ? ", entityID);
    }

    @Override
    public List<Integer> getCrmEntitiesUnsubscribedLists(Integer crmEntityID) {
        return (List<Integer>) find("select lml.mailList.objectID from EdsCrmEntityMailList lml where lml.deleted is true AND lml.entity.objectID = ? ", crmEntityID);
    }
}
