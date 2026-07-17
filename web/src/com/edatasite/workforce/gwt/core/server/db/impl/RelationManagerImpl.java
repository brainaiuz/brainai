package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsRelation;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.crm.EdsCase;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.solr.component.*;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 8/12/11
 * Time: 5:33 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("relationManager")
public class RelationManagerImpl extends BaseManager<EdsRelation> implements RelationManager {
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private TaskManager taskManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private IssueManager issueManager;
    @Autowired
    private CaseManager caseManager;
    @Autowired
    private OpportunityManager opportunityManager;
    @Autowired
    private CaseSolrComponent caseSolrComponent;
    @Autowired
    private TaskSolrComponent taskSolrComponent;
    @Autowired
    private ProjectSolrComponent projectSolrComponent;
    @Autowired
    private EventSolrComponent eventSolrComponent;
    @Autowired
    private OpportunitySolrComponent opportunitySolrComponent;
    @Autowired
    private RelationManager relationManager;

    @Override
    public void updateSolr(HashMap<String, ArrayList<Integer>> types) throws SolrServerException, IOException, InterruptedException {
        if (types != null && !types.isEmpty()) {
            for (Map.Entry<String, ArrayList<Integer>> entry : types.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    String type = entry.getKey();
                    for (Integer entityID : entry.getValue()) {
                        if (RelationItem.TYPE_PROJECT.equals(type)) {
                            EdsProject entity = projectManager.get(entityID);
                            projectSolrComponent.index(entity);
                        }
                        if (RelationItem.TYPE_TASK.equals(type)) {
                            EdsTask entity = taskManager.get(entityID);
                            taskSolrComponent.index(entity);
                        }
                        if (RelationItem.TYPE_EVENT.equals(type)) {
                            EdsEvent entity = eventManager.get(entityID);
                            eventSolrComponent.index(entity);
                        }
                        if (RelationItem.TYPE_CONTACT.equals(type) || RelationItem.TYPE_LEAD.equals(type) || RelationItem.TYPE_CANDIDATE.equals(type)) {
                            //hali relationIDlar solrga, listingga kiritilmagan.
                        }
                        if (RelationItem.TYPE_CRM_ACCOUNT.equals(type) || RelationItem.TYPE_CLIENT.equals(type) || RelationItem.TYPE_SUPPLIER.equals(type)) {
                            //hali relationIDlar solrga, listingga kiritilmagan.
                        }
                        if (RelationItem.TYPE_OPPORTUNITY.equals(type)) {
                            EdsOpportunity entity = opportunityManager.get(entityID);
                            opportunitySolrComponent.index(entity);
                        }
                        if (RelationItem.TYPE_CASE.equals(type)) {
                            EdsCase entity = caseManager.get(entityID);
                            caseSolrComponent.index(entity);
                        }
                        if (RelationItem.TYPE_SALEQUOTE.equals(type)) {
                            //hali relationIDlar solrga, listingga kiritilmagan.
                        }
                        if (RelationItem.TYPE_PRODUCT.equals(type)) {
                            //hali relationIDlar solrga, listingga kiritilmagan.
                        }
                    }
                }
            }
        }
    }

    @Override
    public void changeTypesByType(String fromType, ArrayList<Integer> typeIDs, String toType) {
        updateNative("update " + getCompanyId() + ".relation set totype = '" + toType + "' where totype = '" + fromType + "' and toid in (" + ServerUtils.getAsCommoDelimited(typeIDs, "0", ",") + ")");
        updateNative("update " + getCompanyId() + ".relation set fromtype = '" + toType + "' where fromtype = '" + fromType + "' and fromid in (" + ServerUtils.getAsCommoDelimited(typeIDs, "0", ",") + ")");
    }

    public RelationManagerImpl() {
        super(EdsRelation.class);
    }

    @Override
    public List<EdsRelation> getAllRelations(String type, Integer id) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("id", id);

        StringBuilder query = new StringBuilder();
        query.append("select relation from EdsRelation relation where relation.toID is not null and relation.fromID is not null and ((relation.toType = :type ")
                .append("and relation.toID = :id) or (relation.fromType = :type and relation.fromID = :id ))");

        return (List<EdsRelation>) findByNamedParams(query.toString(), params);
    }

    @Override
    public LinkedHashMap<Integer, List<EdsRelation>> getAllRelationsMapByObjectId(String type, List<Integer> ids) {
        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("ids", ids);

        String query;
        query = "select relation from EdsRelation relation where relation.toID is not null and relation.toType = :type  and relation.toID  in ( :ids ) ";
        List<EdsRelation> toReletations = (List<EdsRelation>) findByNamedParams(query, params);
        query = "select relation from EdsRelation relation where relation.fromID is not null and relation.fromType = :type and relation.fromID in ( :ids ) ";
        List<EdsRelation> fromReletations = (List<EdsRelation>) findByNamedParams(query, params);
        LinkedHashMap<Integer, List<EdsRelation>> relationMap = new LinkedHashMap<>();
        putTheMap(toReletations, relationMap, true);
        putTheMap(fromReletations, relationMap, false);
        return relationMap;
    }

    private void putTheMap(List<EdsRelation> relations, LinkedHashMap<Integer, List<EdsRelation>> relationMap, boolean getfromTo) {
        for (EdsRelation item : relations) {
            Integer entityId = getfromTo ? item.getToID() : item.getFromID();
            List<EdsRelation> releations = relationMap.get(entityId);
            if (releations == null) {
                releations = new ArrayList<>();
                releations.add(item);
                relationMap.put(entityId, releations);
                continue;
            }
            releations.add(item);
        }
    }

    @Override
    public List<EdsRelation> getAllRelations(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select relation.* from ").append(getCompanyId()).append(".relation relation");
        sql.append(" where ((relation.totype ='").append(filterParameter.getRelationType()).append("' and relation.toid = ").append(filterParameter.getRelationID()).append(")");
        sql.append(" or (relation.fromType ='").append(filterParameter.getRelationType()).append("' and relation.fromid = ").append(filterParameter.getRelationID()).append("))");
        if (filterParameter.isFromMobile()) {//if request from email, ignore email tracker type. Because there is no this type in mobile app
            sql.append(" and relation.fromType!='").append(RelationItem.TYPE_EMAIL_TRACKER).append("'");
        }
        sql.append(" order by ");
        if ("date".equalsIgnoreCase(filterParameter.getSortField())) {
            sql.append(" relation.createddate ");
        } else if ("name".equalsIgnoreCase(filterParameter.getSortField())) {
            sql.append(" relation.toName ");
        } else {
            sql.append(" relation.toId ");
        }

        sql.append(filterParameter.isAscending() ? " asc " : "desc");

        if (filterParameter.getLimit() > 0) {
            sql.append(" limit ").append(filterParameter.getLimit()).append(" offset ").append(filterParameter.getStart());
        }
        return findNative(sql.toString(), EdsRelation.class);

    }

    @Override
    public List<EdsRelation> getAllRelations(String type, List<Integer> ids) {
        return getAllRelations(type, ServerUtils.getAsCommoDelimited(ids, "0", ","));
    }

    @Override
    public List<EdsRelation> getAllFromRelations(String type, List<Integer> ids) {
        return find("select relation from EdsRelation relation where relation.fromType = '" + type + "' and relation.fromID in (" + ServerUtils.getAsCommoDelimited(ids, "0", ",") + ")");
    }

    @Override
    public List<EdsRelation> getAllToRelations(String type, List<Integer> ids) {
        return find("select relation from EdsRelation relation where relation.toType = '" + type + "' and relation.toID in (" + ServerUtils.getAsCommoDelimited(ids, "0", ",") + ")");
    }

    private List<EdsRelation> getAllRelations(String type, String ids) {
        return find("select relation from EdsRelation relation where (relation.toType = '" + type + "' and relation.toID in (" + ids + ")) or (relation.fromType = '" + type + "' and relation.fromID in (" + ids + "))");
    }

    @Override
    public void deleteAllRelations(String type, Integer id) {
        updateNative("delete from " + getCompanyId() + ".relation where (totype = '" + type + "' and toid= " + id + ") or (fromtype = '" + type + "' and fromid = " + id + ")");
    }

    @Override
    public void deleteWorkflowRelatedRelations(Integer objectID, String type) {
        updateNative("delete from " + getCompanyId() + ".relation where (totype = '" + type + "' and toid= " + objectID + " and fromtype = '" + RelationItem.TYPE_WORKFLOW + "') or (fromtype = '" + type + "' and fromid = " + objectID + " and totype = '" + RelationItem.TYPE_WORKFLOW + "')");
    }

    @Override
    public List<Integer> getRelationIDsByType(Integer id, Integer entityID, String type, String relationType) {
        List<Integer> ids = find("select relation.toID from EdsRelation relation where fromType ='" + type + "' and fromID = " + id + " and toType  ='" + relationType + "'");
        ids.addAll(find("select relation.fromID from EdsRelation relation where toType ='" + type + "' and toID = " + id + " and fromType  ='" + relationType + "'"));
        if (entityID != null) {
            ids.addAll(find("select relation.fromID from EdsRelation relation where (toType ='" + relationType + "' or fromType ='" + relationType + "') and relation.entityID = " + entityID));
        }
        return ids;
    }

    @Override
    public List<Integer> getRelationIDsByType(Integer id, String type, String relationType) {
        return find("select relation.fromID from EdsRelation relation where toType ='" + type + "' and toID = " + id + " and fromType  ='" + relationType + "'");
    }

    @Override
    public List<Integer> getRelationIDsByTypeAndIds(Integer id, String type, String relationType) {
        List<Integer> ids = find("select relation.fromID from EdsRelation relation where toType ='" + type + "' and toID = " + id + " and fromType  ='" + relationType + "'");
        ids.addAll(find("select relation.toID from EdsRelation relation where fromType ='" + type + "' and fromID = " + id + " and toType  ='" + relationType + "'"));
        return ids;
    }

    @Override
    public List<Object[]> getRelationsByIdAndType(Integer id, String totype, String relationType) {

        StringBuilder sql = new StringBuilder("SELECT t.id, t.name, t.duedate, rt.fromtype \n");
        sql.append("FROM ").append(getCompanyId()).append(".relation rt INNER JOIN \n");
        sql.append(getCompanyId()).append(".task t ON rt.totype=? AND rt.fromtype=? AND rt.fromid=t.id INNER JOIN\n");
        sql.append(getCompanyId()).append(".reference r ON t.statusid=r.id \n");
        sql.append("WHERE rt.toid=? AND r.code IN ('").append(EdsTask.IN_PROGRESS).append("', '")
                .append(EdsTask.NOT_STARTED).append("', '")
                .append(EdsTask.WAITING_FOR_SOMEONE_ELSE).append("') \nORDER BY t.duedate");

        return findNative(sql.toString(), totype, relationType, id);
//        return find("select relation.fromID from EdsRelation relation where toType ='" + type + "' and toID = " + id + " and fromType  ='" + relationType + "'");
    }

    @Override
    public List<EdsRelation> getByRelationItem(RelationItem relationItem, boolean exactRelation) {
        StringBuilder sql = new StringBuilder();
        sql.append("select r from EdsRelation r ");
        if (relationItem != null) {
            if (relationItem.getFromType() != null && relationItem.getFromID() != null && relationItem.getToID() != null && relationItem.getToType() != null) {
                sql.append(" where ");
                sql.append(exactRelation ? "" : "(").append(" fromtype ='").append(relationItem.getFromType()).append("' and fromid = ").append(relationItem.getFromID()).
                        append(" and totype = '").append(relationItem.getToType()).append("' and toid = ").append(relationItem.getToID()).append(exactRelation ? "" : ")");
                if (exactRelation) {
                    sql.append(" or ");
                    sql.append(exactRelation ? "" : "(").append(" totype ='").append(relationItem.getFromType()).append("' and toid = ").append(relationItem.getFromID()).
                            append(" and fromtype = '").append(relationItem.getToType()).append("' and fromid = ").append(relationItem.getToID()).append(exactRelation ? "" : ")");
                }
            }
        }
        return find(sql.toString());
    }

    @Override
    public void delete(Integer objectID) {
        if (objectID == null) {
            return;
        }
        updateNative("delete from " + getCompanyId() + ".relation where id = " + objectID);
    }

    public void updateName(String type, Integer id, String name) {
        update("update EdsRelation rel set fromName = ? where fromType = ? and fromID = ?", name, type, id);
        update("update EdsRelation rel set toName = ? where toType = ? and toID = ?", name, type, id);
    }

    @Override
    public void mergeCrmAccountRelations(Integer objectID, String name, List<Integer> otherAccountIDs) {
        update("update EdsRelation set fromname = ?, fromID = " + objectID + " where fromType = '" + RelationItem.TYPE_CRM_ACCOUNT + "' and fromID in (" + ServerUtils.getAsCommoDelimited(otherAccountIDs, "0") + ")", name);
        update("update EdsRelation set toname = ?, toID = " + objectID + " where toType = '" + RelationItem.TYPE_CRM_ACCOUNT + "' and toID in (" + ServerUtils.getAsCommoDelimited(otherAccountIDs, "0") + ")", name);
    }

    @Override
    public void mergeRelationByType(String relationType, Integer objectID, String name, List<Integer> otherAccountIDs) {
        update("update EdsRelation set toname = ?, toID = " + objectID + " where fromType = '" + relationType + "' and toID in (" + ServerUtils.getAsCommoDelimited(otherAccountIDs, "0") + ")", name);
    }

    @Override
    public void create(EdsRelation relation) {
        relation.setCreatedDate(new Date());
        relation.setLastModifiedDate(new Date());
        if (relation.getEntityID() == null) {
            relation.setEntityID(getEntity(relation));
        }
        super.create(relation);
    }

    private Integer getEntity(EdsRelation relation) {
        String tableName = null;
        Integer objectID = null;
        if (RelationItem.TYPE_CONTACT.equals(relation.getFromType()) || RelationItem.TYPE_LEAD.equals(relation.getFromType()) || RelationItem.TYPE_CONTACT.equals(relation.getToType()) || RelationItem.TYPE_LEAD.equals(relation.getToType())) {
            tableName = "EdsCrmContact";
            if (RelationItem.TYPE_CONTACT.equals(relation.getFromType()) || RelationItem.TYPE_LEAD.equals(relation.getFromType())) {
                objectID = relation.getFromID();
            } else {
                objectID = relation.getToID();
            }
        } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(relation.getFromType()) || RelationItem.TYPE_CRM_ACCOUNT.equals(relation.getToType())) {
            tableName = "EdsCrmAccount";
            if (RelationItem.TYPE_CRM_ACCOUNT.equals(relation.getFromType())) {
                objectID = relation.getFromID();
            } else {
                objectID = relation.getToID();
            }
        }
        return tableName != null && objectID != null ? getEntity(tableName, objectID) : null;
    }

    private Integer getEntity(String table, Integer objectID) {
        return (Integer) findSingle(" select table.entityID from " + table + " table where table.objectID = " + objectID);
    }

    @Override
    public Map<Integer, String> getAllRelationsForTimesheet(String type, String ids) {
        Map<Integer, String> map = new HashMap<>();
        List<Integer> idList = ServerUtils.getStringAsList(ids, ",");
        for (EdsRelation relation : getAllRelations(type, ids)) {
            if (EdsRelation.TYPE_TASK.equals(relation.getFromType()) && EdsRelation.TYPE_TASK.equals(relation.getToType())) {
                Integer idTo = relation.getIDByType(type, false);
                if (idList.contains(idTo)) {
                    getRelation(type, map, relation, idTo, true);
                }
                Integer idFrom = relation.getIDByType(type, true);
                if (idList.contains(idFrom)) {
                    getRelation(type, map, relation, idFrom, false);
                }
            } else {
                Integer id = relation.getIDByType(type, false);
                getRelation(type, map, relation, id, true);
            }
        }
        return map;
    }

    private void getRelation(String type, Map<Integer, String> map, EdsRelation relation, Integer id, boolean viceVersa) {
        if (id != null) {
            if (map.containsKey(id)) {
                String value = map.get(id);
                value = value + ", " + relation.getNameByType(type, viceVersa);
                map.put(relation.getIDByType(type, !viceVersa), value);
            } else {
                map.put(relation.getIDByType(type, !viceVersa), relation.getNameByType(type, viceVersa));
            }
        }
    }

    @Override
    public void update(EdsRelation relation) {
        relation.setLastModifiedDate(new Date());
        super.update(relation);
    }

    @Override
    public List<EdsRelation> getRelationsByRelationTypeToID(String toType, Integer toId) {
        return find("select relation from EdsRelation relation where relation.toType = ? and relation.toID = ?", toType, toId);
    }

    @Override
    public List<EdsRelation> getRelationsByRelationFromTypeToID(String fromType, Integer toId) {
        return find("select relation from EdsRelation relation where relation.fromType = ? and relation.toID = ?", fromType, toId);
    }

    @Override
    public List<EdsRelation> getRelationsByRelationFromTypeFromID(String fromType, Integer fromId) {
        return find("select relation from EdsRelation relation where relation.fromType = ? and relation.fromID = ?", fromType, fromId);
    }

    public List<Object[]> getCurrentInterviews() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT e.id, r.toname, e.startDate, e.all_day, array_to_string(array_agg(mu.firstname || ' ' || mu.lastname), ',') as sharedEmployees \n");
        sql.append("FROM ").append(getCompanyId()).append(".relation r \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".event e ON (e.id = r.fromID) \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".employeeevent ee ON (ee.event_id = e.id) \n");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".myuser mu ON (mu.id = ee.employee_id) \n");

        sql.append("WHERE r.toType = 'candidate' and r.fromType = 'event' \n");
        sql.append("AND e.activityType = " + Appointment.INTERVIEW + " \n");
        sql.append("AND (ee.shared is true OR ee.edit is true) \n");
        sql.append("AND e.endDate >= now() \n");
        sql.append("AND (e.deleted is not true) \n");
        sql.append("AND (ee.deleted is not true) \n");
        sql.append("AND (mu.deleted is not true) \n");
        sql.append("GROUP BY e.id, r.toname, e.startDate, e.all_day \n");
        sql.append("ORDER BY e.startDate DESC LIMIT 20");

        return (List<Object[]>) findNative(sql.toString());
    }

    @Override
    public void mergeCrmContactRelations(Integer objectID, String name, List<Integer> otherContactIDs) {
        update("update EdsRelation set fromname = ?, fromID = " + objectID + " where fromType = '" + RelationItem.TYPE_CONTACT +
                "' and fromID in (" + ServerUtils.getAsCommoDelimited(otherContactIDs, "0") + ")", name);
        update("update EdsRelation set toname = ?, toID = " + objectID + " where toType = '" + RelationItem.TYPE_CONTACT + "' " +
                " and fromtype is not '" + RelationItem.TYPE_SALEINVOICE + "' " +
                " and fromtype is not '" + RelationItem.TYPE_SALEQUOTE + "' and toID in (" + ServerUtils.getAsCommoDelimited(otherContactIDs, "0") + ")", name);
    }

    @Override
    public void mergeCrmContactInvoices(Integer otherContactObjectID, String fromType, Integer newContactID) {
        update("update EdsRelation set toid = " + newContactID + " where fromType = '" + fromType +
                "' and totype = '" + RelationItem.TYPE_CONTACT + "' and toid = " + otherContactObjectID);
    }

    @Override
    public List<Integer> getCustomFormForCurrentUser(Integer currentUserId, String formId) {
        return findNative("select toid from " + getCompanyId() + ".relation where totype = '" + formId + "' and fromtype = '" + RelationItem.TYPE_EMPLOYEE + "' and fromid = " + currentUserId
                + " union all  select fromid from " + getCompanyId() + ".relation where fromtype = '" + formId + "' and totype = '" + RelationItem.TYPE_EMPLOYEE + "' and toid = " + currentUserId);
    }
}
