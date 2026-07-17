package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.core.solr.component.EventSolrComponent;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.EventManager;
import com.edatasite.workforce.gwt.core.server.db.RelationManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.SolrTransactionManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.SolrEvent;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Jul 29, 2009
 * Time: 5:48:53 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("eventManager")
public class EventManagerImpl extends BaseManager<EdsEvent> implements EventManager {

    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private SolrTransactionManager solrTransactionManager;
    @Autowired
    private EventSolrComponent eventSolrComponent;
    @Autowired
    private RelationManager relationManager;

    public EventManagerImpl() {
        super(EdsEvent.class);
    }

    DateFormat format = new SimpleDateFormat("MMM d, yyyy");
    DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

    public List<EdsEvent> getList(ListingFilterParameter fp) {
        fp = fp == null ? new ListingFilterParameter() : fp;
        String companyId = getCompanyId();
        StringBuffer sql = null;
        Date selectedDate = null;
        String sDate = "";
        sql = new StringBuffer();
        sql.append("select distinct e.* from ").append(companyId).append(".event as e ");
        sql.append("left join ").append(companyId).append(".relation as r on ((r.fromType = '").append(RelationItem.TYPE_EVENT).append("' and r.fromid = e.id) or (r.toType = '").append(RelationItem.TYPE_EVENT).append("' and r.toid = e.id)) ");
        sql.append(" where ");
        if (fp != null) {
            if (fp.getEntityID() != null && !"".equals(fp.getEntityID()) && fp.getEntityID() > 0) {
                sql.append(" r.entityid = ").append(fp.getEntityID());
            } else {
                String relationType = null;
                Integer relationID = null;
                if (fp.getContactID() != null) {
                    relationID = fp.getContactID();
                    relationType = RelationItem.TYPE_CONTACT;
                }
                if (fp.getLeadID() != null) {
                    relationID = fp.getLeadID();
                    relationType = RelationItem.TYPE_LEAD;
                }
                if (fp.getAccountID() != null) {
                    relationID = fp.getAccountID();
                    relationType = RelationItem.TYPE_CRM_ACCOUNT;
                }
                if (fp.getOpportunityID() != null) {
                    relationID = fp.getOpportunityID();
                    relationType = RelationItem.TYPE_OPPORTUNITY;
                }
                if (fp.getCaseID() != null) {
                    relationID = fp.getCaseID();
                    relationType = RelationItem.TYPE_CASE;
                }
                if (relationID != null && relationType != null) {
                    sql.append(" ((r.totype = '").append(relationType).append("' and r.toid = ").append(relationID).append(") or (r.fromtype = '").append(relationType).append("' and r.fromid = ").append(relationID).append(")) ");
                }
            }
        }
        //search by date
        if (!"".equals(fp.getGroupByName()) && !"All".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 0) {
            if (!fp.getGroupByName().toLowerCase().contains("without")) {
                try {
                    selectedDate = format.parse(fp.getGroupByName());
                    sDate = format2.format(selectedDate);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                sql.append(" to_char(e.startDate, 'yyyy-mm-dd')= '").append(sDate).append("' and ");
            } else {
                sql.append(" e.startDate is null and ");
            }
        }

        if (!"".equals(fp.getGroupByName()) && !"All".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 4) {
            if (!fp.getGroupByName().toLowerCase().contains("without")) {
                try {
                    selectedDate = format.parse(fp.getGroupByName());
                    sDate = format2.format(selectedDate);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                sql.append(" to_char(e.endDate, 'yyyy-mm-dd')= '").append(sDate).append("' and ");
            } else {
                sql.append(" e.endDate is null and ");
            }
        }
        if (!sql.toString().trim().toLowerCase().endsWith("where")) {
            sql.append(" and ");
        }
        sql.append(ServerUtils.checkForDeleted("e.deleted"));
        if (fp != null && fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append(" and (");
            sql.append("(e.startDate>='").append(fp.getStartDate()).append("' and ").append("e.endDate<='").append(fp.getEndDate()).append("') ");
            sql.append("or (e.startDate>='").append(fp.getStartDate()).append("' and ").append("e.startDate<='").append(fp.getEndDate()).append("') ");
            sql.append("or (e.endDate>='").append(fp.getStartDate()).append("' and ").append("e.endDate<='").append(fp.getEndDate()).append("') ");
            if (fp.isLookUp()) {
                sql.append("or (e.startDate<='").append(fp.getStartDate()).append("' and ").append("e.endDate>='").append(fp.getEndDate()).append("') ");
            }
            sql.append(") ");
        }
        if (fp != null && fp.getSqlSearchKey() != null) {
            sql.append(" and lower(e.subject) like '").append(fp.getSqlSearchKey()).append("' ");
        }
        sql.append(" Order By e.creationTime ").append(fp.isAscending() ? "" : " desc ");
        if (fp.getLimit() != null && fp.getLimit() != 0) {
            sql.append(" offset ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        }
        return findNative(sql.toString(), EdsEvent.class);
    }

    @Override
    public Integer getListCount(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        String companyId = getCompanyId();
        StringBuffer sql = null;
        Date selectedDate = null;
        String sDate = "";
        sql = new StringBuffer();
        sql.append("select count(e.id) from ").append(companyId).append(".event as e ");
        sql.append("left join ").append(companyId).append(".relation as r on ((r.fromType = '").append(RelationItem.TYPE_EVENT).append("' and r.fromid = e.id) or (r.toType = '").append(RelationItem.TYPE_EVENT).append("' and r.toid = e.id)) ");
        sql.append(" where ");
        if (fp != null) {
            if (fp.getEntityID() != null && !"".equals(fp.getEntityID()) && fp.getEntityID() > 0) {
                sql.append(" r.entityid = ").append(fp.getEntityID());
            } else {
                String relationType = null;
                Integer relationID = null;
                if (fp.getContactID() != null) {
                    relationID = fp.getContactID();
                    relationType = RelationItem.TYPE_CONTACT;
                }
                if (fp.getLeadID() != null) {
                    relationID = fp.getLeadID();
                    relationType = RelationItem.TYPE_LEAD;
                }
                if (fp.getAccountID() != null) {
                    relationID = fp.getAccountID();
                    relationType = RelationItem.TYPE_CRM_ACCOUNT;
                }
                if (fp.getOpportunityID() != null) {
                    relationID = fp.getOpportunityID();
                    relationType = RelationItem.TYPE_OPPORTUNITY;
                }
                if (fp.getCaseID() != null) {
                    relationID = fp.getCaseID();
                    relationType = RelationItem.TYPE_CASE;
                }
                if (relationID != null && relationType != null) {
                    sql.append(" ((r.totype = '").append(relationType).append("' and r.toid = ").append(relationID).append(") or (r.fromtype = '").append(relationType).append("' and r.fromid = ").append(relationID).append(")) ");
                }
            }
        }
        //search by date
        if (!"".equals(fp.getGroupByName()) && !"All".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 0) {
            if (!fp.getGroupByName().toLowerCase().contains("without")) {
                try {
                    selectedDate = format.parse(fp.getGroupByName());
                    sDate = format2.format(selectedDate);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                sql.append(" to_char(e.startDate, 'yyyy-mm-dd')= '").append(sDate).append("' and ");
            } else {
                sql.append(" e.startDate is null and ");
            }
        }

        if (!"".equals(fp.getGroupByName()) && !"All".equals(fp.getGroupByName()) && fp.getGroupByName() != null && fp.getSearchType() == 4) {
            if (!fp.getGroupByName().toLowerCase().contains("without")) {
                try {
                    selectedDate = format.parse(fp.getGroupByName());
                    sDate = format2.format(selectedDate);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                sql.append(" to_char(e.endDate, 'yyyy-mm-dd')= '").append(sDate).append("' and ");
            } else {
                sql.append(" e.endDate is null and ");
            }
        }
        if (!sql.toString().trim().toLowerCase().endsWith("where")) {
            sql.append(" and ");
        }
        sql.append(ServerUtils.checkForDeleted("e.deleted"));
        if (fp != null && fp.getSqlSearchKey() != null) {
            sql.append(" and lower(e.subject) like '").append(fp.getSqlSearchKey()).append("' ");
        }
        BigInteger count = (BigInteger) findNativeSingle(sql.toString());
        if (count != null) {
            return count.intValue();
        }
        return 0;
    }

    public EdsEvent getFirstOrLastEventInRecurringSeries(Integer recurrenceID, boolean isFirst) {
        return (EdsEvent) findSingle("select event from EdsEvent event where " + ServerUtils.checkForDeleted("event.deleted") + " and event.recurrenceID = ? order by event.fireTime " + (isFirst ? "asc" : "desc"), recurrenceID);
    }

    public EdsEvent getEventInstance(Integer recurrenceID, Date fireTime) {
        return (EdsEvent) findSingle("select event from EdsEvent event where " + ServerUtils.checkForDeleted("event.deleted") + " and event.recurrenceID = ? and event.fireTime = ?", recurrenceID, fireTime);
    }

    public List<EdsEvent> getAllEventInstances(Integer recurrenceID) {
        return find("select event from EdsEvent event where " + ServerUtils.checkForDeleted("event.deleted") + " and event.recurrenceID = ?", recurrenceID);
    }

    public List<Integer> getAllEventInstancesIDs(Integer recurrenceID, Date afterFireTime) {
        return find("select event.objectID from EdsEvent event where " + ServerUtils.checkForDeleted("event.deleted") + (afterFireTime != null ? " and event.fireTime>='" + afterFireTime + "'" : "") + " and event.recurrenceID = ?", recurrenceID);
    }

    public List<EdsEvent> getAllEventInstancesAfter(Integer recurrenceID, Date afterFireTime) {
        return find("select event from EdsEvent event where " + ServerUtils.checkForDeleted("event.deleted") + " and event.recurrenceID = ? and event.fireTime>?  order by id asc", recurrenceID, afterFireTime);
    }

    @Override
    public List<Object[]> getEventsByDate(boolean isDueDate) {
        return findNative("select date_trunc('day', e." + (isDueDate ? "endDate" : "startDate") + ") as \"date\", count(e.id) from " + getCompanyId() + ".event e where e.deleted is not true and e." + (isDueDate ? "endDate" : "startDate") + " is not null group by \"date\" order by \"date\"");
    }

    @Override
    public BigInteger getEventsSize() {
        return (BigInteger) findNativeSingle("select count(id) from " + getCompanyId() + ".event where deleted is not true");
    }

    public void deleteEvents(Integer recurrenceID, Integer userID, Date afterFireTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("recurrenceID", recurrenceID);
        if (afterFireTime != null) {
            map.put("afterTime", afterFireTime);
        }
        StringBuffer sqlText = new StringBuffer();
        // delete all employee events
        sqlText.append("update EdsEmployeeEvent ee set ee.deleted = true where ee.event.objectID in (select event.objectID from EdsEvent event where event.recurrenceID=:recurrenceID");
        sqlText.append(afterFireTime != null ? " and event.fireTime>:afterTime)" : ")");
        updateByNamedParams(sqlText.toString(), map);
        // update all events in this series
        map.put("userID", userID);
        map.put("deletedDate", new Date());
        sqlText = new StringBuffer();
        sqlText.append("update EdsEvent event set event.recurrenceID = null, event.deleted=true, event.lastModifiedDate=:deletedDate, event.lastModifiedBy.objectID=:userID where event.recurrenceID=:recurrenceID");
        sqlText.append(afterFireTime != null ? " and event.fireTime>:afterTime" : "");
        updateByNamedParams(sqlText.toString(), map);
    }

    @Override
    public List<Integer> deleteEvents(List<Integer> objectIDs, EdsUser user) {
        if (user != null) {
            String subQuery = user.hasRoles(Constants.ADMIN) ? "" : " owner = " + user.getObjectID();
            updateNative("update " + getCompanyId() + ".event set deleted = true, workflowID = null where " + subQuery + (!"".equals(subQuery) ? " and " : "") + "id in (" + ServerUtils.getAsCommoDelimited(objectIDs, "0", ",") + ")");
            updateNative("update " + getCompanyId() + ".employeeevent set deleted = true where event_id in (select e.id from " + getCompanyId() + ".event e where " + subQuery + (!"".equals(subQuery) ? " and " : "") + "e.id in (" + ServerUtils.getAsCommoDelimited(objectIDs, "0", ",") + "))");
        }
        return (List<Integer>) findNative("select account.id from " + getCompanyId() + ".event account where deleted is true and id in (" + ServerUtils.getAsCommoDelimited(objectIDs, "0", ",") + ")");
    }

    @Override
    public List<Integer> getEventIDsBySolrIDs(List<Integer> objectIDs) {
        return findNative("select e.id from " + getCompanyId() + ".event e where e.deleted is not true");
    }

    @Override
    public List<EdsEvent> getWorkflowEvents(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        Integer workflowID = filterParameter.getWorkflowID();
        if (workflowID == null) {
            if (filterParameter.getRelationID() != null && filterParameter.getRelationType() != null && filterParameter.getRelationType().equals(RelationItem.TYPE_WORKFLOW)) {
                workflowID = filterParameter.getRelationID();
            }
        }
        sql.append("select distinct t.id, t.* , 0 as clazz_ from ").append(getCompanyId()).append(".event t").append(" where t.workflowID = " + workflowID);
        return findNative(sql.toString(), EdsEvent.class);
    }


    @Override
    public void deleteUserTemporaryKey(Integer userID, String temporaryKey) {
        String sql = "UPDATE "  + getCompanyId() + ".myUser SET temporaryKey = '" + temporaryKey + "' WHERE id = " + userID + " ;";
        updateNative(sql);
    }

    @Override
    public LinkedHashMap<Integer, ArrayList<Date>> getEventDates(Date startDate, boolean isPositive, ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct date(e.startDate) from ").append(getCompanyId()).append(".event e ");
        if (filterParameter.getRelationID() != null && StringUtils.isNotBlank(filterParameter.getRelationType())) {
            sql.append(" left join ").append(getCompanyId()).append(".relation rel on e.id = rel.fromid");
        }
        sql.append(" where ").append(ServerUtils.checkForDeleted("e.deleted"));

        if (filterParameter.getRelationID() != null && StringUtils.isNotBlank(filterParameter.getRelationType())) {
            sql.append(" and rel.toid = ").append(filterParameter.getRelationID());
            sql.append(" and rel.totype = '").append(filterParameter.getRelationType()).append("'");
        }
        if (!ServerUtils.hasPermission(PermissionConstants.CRM_ACTIVITIES_LOG_CALL_VIEW)) {
            sql.append(" and e.activityType = ").append(Appointment.EVENT);
        } else if (filterParameter.getEventType() != null) {
            sql.append(" and e.activityType = ").append(filterParameter.getEventType());
        }
        if (!ServerUtils.hasPermission(PermissionConstants.CRM_SEE_ALL_ACTIVITIES_LIST)) {
            sql.append(" and e.owner = ").append(getUser().getObjectID());
        }
        if (!getUser().hasEitherRoles(EdsRole.ADMIN)) {
            sql.append(" and e.fromRecorder = ").append(false);
        }
        if (filterParameter.getCreatedFrom() != null) {
            sql.append(" and e.createdFrom = ").append(filterParameter.getCreatedFrom());
        }
        if (isPositive) {
            sql.append(" and to_char(e.startDate,'yyyy-MM-dd') >= to_char(date('").append(startDate).append("'),'yyyy-MM-dd')");
        } else {
            sql.append(" and to_char(e.startDate,'yyyy-MM-dd') < to_char(date('").append(startDate).append("'),'yyyy-MM-dd')");
        }
        //Get total count
        Integer total = findNative(sql.toString()).size();

        sql.append(" order by date(e.startDate) ").append(isPositive ? "asc" : "desc");

        sql.append(" limit ").append(filterParameter.getLimit()).append(" offset ").append(filterParameter.getStart());
        //Get limited data
        ArrayList<Date> result = (ArrayList<Date>) findNative(sql.toString());

        LinkedHashMap resultMap = new LinkedHashMap<>();
        resultMap.put(total, result);

        return resultMap;
    }

    public EdsEvent getNextEventByRecurrence(EdsRecurrence recurrenceID, Integer objectId) {
        return (EdsEvent) findSingle("select distinct event from EdsEvent event where " + ServerUtils.checkForDeleted("event.deleted") + " and event.recurrenceID = ? and event.objectID > ? order by event.objectID asc", recurrenceID, objectId);
    }

    public void removeRecurrenceFromEvent(Integer recurrenceID, Integer companyID) {
        updateNative("update \"" + companyID + "\".event set recurrenceid = null where recurrenceid=" + recurrenceID);
    }

    //this function gets the latest event end date before the supplied currentEventFireTime

    public Date getRecurringEventFirstOrLastDate(Integer recurrenceID, Date currentEventFireTime, boolean isFirst) {
        return (Date) findSingle("select " + (isFirst ? "e.startDate" : "e.endDate") + " from EdsEvent e where  " + (isFirst ? "" : "e.fireTime < '" + currentEventFireTime + "' and") + "  e.deleted <> true and e.recurrenceID = " + recurrenceID + " order by e.fireTime " + (isFirst ? "asc" : "desc"));
    }

    public Long getAllEventInstancesSize(Integer recurrenceID) {
        return (Long) findSingle("select count(event) from EdsEvent event where " + ServerUtils.checkForDeleted("event.deleted") + " and event.recurrenceID = ?", recurrenceID);
    }

    public List<Integer> getCompanyDeletedEventListForSolr(SolrReindexRpc solrReindex) {
        return (List<Integer>) find("select e.objectID from EdsEvent e where e.deleted=true and e.lastUpdateTime>=?"
                + (solrReindex.getLastUpdateEndTime() != null ? " and e.lastUpdateTime<='" + solrReindex.getLastUpdateEndTime() + "'" : ""), solrReindex.getLastUpdateTime());
    }

    public List<EdsEvent> getCompanyEventListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder eventSqlQuery = new StringBuilder();
        eventSqlQuery.append(" SELECT e FROM EdsEvent e where e.deleted != true OR e.deleted IS NULL ");
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("updated", solrReindex.getLastUpdateTime());
            eventSqlQuery.append(" and e.lastUpdateTime >= :updated");
            if (solrReindex.getLastUpdateEndTime() != null) {
                eventSqlQuery.append(" and e.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        eventSqlQuery.append(" order by e.objectID asc ");
        return findIntervalByNamedParams(eventSqlQuery.toString(), start, limit, params);
    }

    @Override
    public List<Integer> getEventIdsByIDs(String ids) {
        return find("select e.objectID from EdsEvent e where e.objectID IN(" + ids + ")");
    }

    @Override
    public List<Integer> getEventIdsWithLimit(int startat, int limit) {
        return findLimited("select o.objectID from EdsEvent o where o.objectID > ? AND o.deleted != true order by o.objectID ASC", limit, startat);
    }

    @Override
    public EdsEvent getEventByID(Integer eventID) {
        return (EdsEvent) findSingle("select e from EdsEvent e where e.objectID=?", eventID);
    }

    @Override
    public List<EdsEvent> getEventsByIDs(String eventIDs) {
        return (List<EdsEvent>) find("select e from EdsEvent e where e.objectID in (" + eventIDs + ")");
    }

    public void create(EdsEvent event, boolean addToSolr) {
        create(event);
        addToSolr(event, addToSolr, true);
    }

    public void update(EdsEvent event, boolean addToSolr) {
        update(event);
        addToSolr(event, addToSolr, false);
    }

    @Override
    public void addToSolr(Integer... objectIDs) {
        if (objectIDs != null && objectIDs.length > 0) {
            List<EdsEvent> events = getEventsByIDs(ServerUtils.getAsCommoDelimited(Arrays.asList(objectIDs), "0", ","));
            if (events != null && !events.isEmpty()) {
                try {
                    eventSolrComponent.indexes(events);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void addToSolr(EdsEvent... events) {
        try {
            eventSolrComponent.indexes(Arrays.asList(events));
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public Map<Integer, Set<EdsUser>> getEventSharedUsers(ArrayList<Integer> eventIDs) {
        Map<Integer, Set<EdsUser>> map = new HashMap<>();
        if (eventIDs != null && eventIDs.size() > 0) {
            List<EdsEmployeeEvent> employeeEvents = find("select distinct ee from EdsEmployeeEvent ee where ee.deleted is not true and ee.employee.deleted is not true and  ee.event.objectID in (" + ServerUtils.getAsCommoDelimited(eventIDs, "0", ",") + ")");
            if (employeeEvents != null && employeeEvents.size() > 0) {
                for (EdsEmployeeEvent employeeEvent : employeeEvents) {
                    Integer eventID = employeeEvent.getEvent().getObjectID();
                    if (!map.containsKey(eventID)) {
                        map.put(eventID, new HashSet<>());
                    }
                    map.get(eventID).add(employeeEvent.getEmployee());
                }
            }
        }
        return map;
    }

    @Override
    public EdsEvent getEventByScheduledCourse(String subject, Date startDate, Date endDate) {
        return (EdsEvent) findSingle("SELECT e FROM EdsEvent e WHERE (e.deleted is false or e.deleted is null) and  lower(e.subject) like '" + subject.toLowerCase() + "%' AND e.startDate = ? AND e.endDate = ?", startDate, endDate);
    }

    private void addToSolr(EdsEvent event, boolean addToSolr, boolean isNew) {
        if (addToSolr) {
            addToSolr(event);
            if (isNew) {
                solrTransactionManager.registerEvent(SolrEvent.EVENT_ADD, event, companyManager.get(SecurityContext.getCompanyID()));
            }
        }
    }

    @Override
    public void create(EdsEvent obj) {
        obj.setCreationTime(new Date());
        super.create(obj);
    }

    @Override
    public void update(EdsEvent obj) {
        obj.setLastModifiedDate(new Date());
        super.update(obj);
    }

    @Override
    public EdsEvent getByAsteriskid(String asteriskid) {
        return (EdsEvent) findSingle("select distinct event from EdsEvent event where " + ServerUtils.checkForDeleted("event.deleted") + " and event.asteriskid = ? order by event.objectID asc", asteriskid);
    }
}
