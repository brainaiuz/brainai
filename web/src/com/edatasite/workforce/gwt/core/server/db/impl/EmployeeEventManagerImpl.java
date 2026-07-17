/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/19 3:56:10                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeEvent;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeEventManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Oct 20, 2009
 * Time: 4:41:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("employeeEventManager")
public class EmployeeEventManagerImpl extends BaseManager<EdsEmployeeEvent> implements EmployeeEventManager {

    @Autowired
    private ReferenceManager referenceManager;

    public EmployeeEventManagerImpl() {
        super(EdsEmployeeEvent.class);
    }

    /**
     * Will get list of WFT Calendar events from database in domain objects
     *
     *
     * @param employeeIDs
     * @param start
     * @param end
     * @param fromAgenda
     * @param hideCalls
     * @return
     */
    public List<EdsEmployeeEvent> getCalendarEvents(List<Integer> employeeIDs, Date start, Date end, boolean fromAgenda, Integer locationID, boolean hideCalls) {
        Map params = new HashMap();
        params.put("start", start);
        String ids = "";
        for (Integer id : employeeIDs) {
            if (!"".equals(ids)) {
                ids = ids + ",";
            }
            ids = ids + id;
        }
        params.put("employee", employeeIDs);
        if (locationID != null) {
            params.put("locationID", locationID);
        }

        if (!fromAgenda) {
            params.put("end", end);
            return findByNamedParams("select ee from EdsEmployeeEvent ee where ee.employee.objectID in (:employee) " +
                    (locationID != null ? " and ee.event.locationID = :locationID" : "") + " and (ee.event.startDate <= :end and ee.event.endDate >= :start) " +
                    " and (ee.event.deleted is null or ee.event.deleted<>true) and (ee.deleted is null or ee.deleted<>true) " +
                    (hideCalls ? " and ee.event.fromRecorder is false " : "") +
                    " order by ee.event.startDate asc, ee.event.subject asc", params);
        } else {
            return findLimited("select ee from EdsEmployeeEvent ee where ee.event.deleted<>true " +
                    " and ee.employee.objectID in (?) and (ee.event.endDate>=?) " +
                    " and (ee.event.deleted is null or ee.event.deleted<>true) and (ee.deleted is null or ee.deleted<>true) " +
                    (hideCalls ? " and ee.event.fromRecorder is false " : "") +
                    " order by ee.event.startDate ", 20, Integer.valueOf(ids), start);
        }
    }

    public List<EdsEmployeeEvent> getCalendarEvents(List<Integer> employeeIDs, Date start, Date end, boolean fromAgenda, Integer locationID, boolean hideCalls, boolean isCall) {
        Map params = new HashMap();
        params.put("start", start);
        String ids = "";
        for (Integer id : employeeIDs) {
            if (!"".equals(ids)) {
                ids = ids + ",";
            }
            ids = ids + id;
        }
        params.put("employee", employeeIDs);
        if (locationID != null) {
            params.put("locationID", locationID);
        }

        if (!fromAgenda) {
            params.put("end", end);
            return findByNamedParams("select ee from EdsEmployeeEvent ee where ee.employee.objectID in (:employee) " +
                    (locationID != null ? " and ee.event.locationID = :locationID" : "") + " and (ee.event.startDate <= :end and ee.event.endDate >= :start) " +
                    " and (ee.event.deleted is null or ee.event.deleted<>true) and (ee.deleted is null or ee.deleted<>true) " +
                    (hideCalls ? " and ee.event.fromRecorder is false " : "") +
                    (isCall ? " and ee.event.activityType = 2 " : " and ee.event.activityType != 2") +
                    " order by ee.event.startDate asc, ee.event.subject asc", params);
        } else {
            return findLimited("select ee from EdsEmployeeEvent ee where ee.event.deleted<>true " +
                    " and ee.employee.objectID in (?) and (ee.event.endDate>=?) " +
                    " and (ee.event.deleted is null or ee.event.deleted<>true) and (ee.deleted is null or ee.deleted<>true) " +
                    (hideCalls ? " and ee.event.fromRecorder is false " : "") +
                    (isCall ? " and ee.event.activityType = 2 " : " and ee.event.activityType != 2") +
                    " order by ee.event.startDate ", 20, Integer.valueOf(ids), start);
        }
    }

    @Override
    public List<Integer> getCalendarEventIDs(List<Integer> employeeIDs, Date start, Date end, boolean fromAgenda, Integer locationID, boolean hideCalls, boolean isCall) {
        Map params = new HashMap();
        params.put("start", start);
        String ids = "";
        for (Integer id : employeeIDs) {
            if (!"".equals(ids)) {
                ids = ids + ",";
            }
            ids = ids + id;
        }
        params.put("employee", employeeIDs);
        if (locationID != null) {
            params.put("locationID", locationID);
        }

        if (!fromAgenda) {
            params.put("end", end);
            return findByNamedParams("select ee.event.objectID from EdsEmployeeEvent ee where ee.employee.objectID in (:employee) " +
                    (locationID != null ? " and ee.event.locationID = :locationID" : "") + " and (ee.event.startDate <= :end and ee.event.endDate >= :start) " +
                    " and (ee.event.deleted is null or ee.event.deleted<>true) and (ee.deleted is null or ee.deleted<>true) " +
                    (hideCalls ? " and ee.event.fromRecorder is false " : "") +
                    (isCall ? " and ee.event.activityType = 2 " : " and ee.event.activityType != 2") +
                    " order by ee.event.startDate asc, ee.event.subject asc", params);
        } else {
            return findLimited("select ee.event.objectID from EdsEmployeeEvent ee where ee.event.deleted<>true " +
                    " and ee.employee.objectID in (?) and (ee.event.endDate>=?) " +
                    " and (ee.event.deleted is null or ee.event.deleted<>true) and (ee.deleted is null or ee.deleted<>true) " +
                    (hideCalls ? " and ee.event.fromRecorder is false " : "") +
                    (isCall ? " and ee.event.activityType = 2 " : " and ee.event.activityType != 2") +
                    " order by ee.event.startDate ", 20, Integer.valueOf(ids), start);
        }
    }

    public EdsEmployeeEvent getEmployeeEvent(EdsUser employee, EdsEvent event) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(getCompanyId()).append(".employeeevent ev ");
        sql.append(" left join ").append(getCompanyId()).append(".event e on ev.event_id = e.id");
        sql.append(" where (ev.deleted=false OR ev.deleted is null) AND ev.employee_id = ").append(employee.getObjectID()).append(" and e.id =").append(event.getObjectID());

        return (EdsEmployeeEvent) findNativeSingle(sql.toString(), EdsEmployeeEvent.class);
    }

    public List<Integer> getEventRelatedEmployees(Integer eventID) {
        return find("select ee.employee.objectID from EdsEmployeeEvent ee where ee.event.objectID = ? and " +
                ServerUtils.checkForDeleted("ee.event.deleted") +
                " and " + ServerUtils.checkForDeleted("ee.deleted") +
                " and (ee.shared = true or ee.edit = true)", eventID);
    }

    @Override
    public String getEventEmployees(Integer eventID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT array_to_string(array_agg(my.firstname||' '||my.lastname),',') FROM ").append(getCompanyId()).append(".employeeEvent ee ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".myUser my ON my.id = ee.employee_id ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".event e ON e.id = ee.event_id ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("my.deleted")).append(" AND e.id = ").append(eventID);
        return (String) findNativeSingle(sql.toString());
    }

    @Override
    public String getEventRelatedEmployeesEmails(Integer eventID) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT array_to_string(array_agg(my.email),',') FROM ").append(getCompanyId()).append(".employeeEvent ee ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".myUser my ON my.id = ee.employee_id ");
        sql.append("LEFT JOIN ").append(getCompanyId()).append(".event e ON e.id = ee.event_id ");
        sql.append("WHERE ").append(ServerUtils.checkForDeleted("my.deleted")).append(" AND e.id = ").append(eventID).append(" AND ee.employee_id <> e.owner");
        return (String) findNativeSingle(sql.toString());
    }

    /**
     * There is no need to get the list of conflicted events.
     * Because anyway we only check the existing of such events.
     *
     * @param eventID
     * @param employee
     * @param start
     * @param end
     * @return
     */
    public EdsEmployeeEvent hasConflictedEvents(Integer eventID, EdsEmployee employee, Date start, Date end) {
        Map params = new HashMap();
        params.put("eventID", eventID);
        params.put("employee", employee);
        params.put("start", start);
        params.put("end", end);

        StringBuilder sql = new StringBuilder();
        sql.append("from EdsEmployeeEvent ee ");
        sql.append("where ee.employee = :employee ");
        sql.append("and ee.event.objectID <> :eventID ");
        sql.append("and (ee.shared = true or ee.edit = true) ");
        sql.append("and ee.deleted <> true ");
        sql.append("and ee.event.deleted <> true ");
        sql.append("and ((ee.event.startDate <= :start and ee.event.endDate > :start) ");
        sql.append("or (ee.event.startDate < :end and ee.event.endDate >= :end))");

        return (EdsEmployeeEvent) findSingleByNamedParams(sql.toString(), params);
    }

    public List<EdsEmployeeEvent> getEmployeeAllEvents(EdsEmployee employee) {
        return getEmployeeAllEvents(employee, false);
    }

    public List<EdsEmployeeEvent> getEmployeeAllEvents(EdsEmployee employee, Boolean withRecurrence) {
        return find("select ev from EdsEmployeeEvent ev where ev.event.deleted = false and ev.deleted = false and ev.event.owner = ?" +
                (withRecurrence ? " and ev.event.recurrenceID is not null" : ""), employee);
    }

    public void removeGoogleIDFromEmployeeEvents(EdsEmployee employee) {
        updateNative("update " + getCompanyId() + ".employeeevent set googleId = null where event_id in (select id from " + getCompanyId() + ".event e where e.deleted = false and e.owner = " + employee.getObjectID() + ") and deleted = false");
    }

    public List<EdsUser> getEventAttendees(EdsEvent event) {
        return find("select ee.employee from EdsEmployeeEvent ee where ee.event = ? and (ee.deleted <> true or ee.deleted is null) and ee.shared = true and (ee.edit is false or ee.edit is null)", event);
    }

    public EdsEmployeeEvent getEmployeeEventByEvent(EdsEvent event) {
        String companyId = getCompanyId();
        List<EdsEmployeeEvent> list = (List<EdsEmployeeEvent>) findNative("select distinct ee.id, ee.*, 0 as clazz_ from " + companyId + ".employeeevent ee where " + ServerUtils.checkForDeleted("ee.deleted") + " and ee.event_id = " + event.getObjectID() + " and ee.edit = true order by ee.id desc", EdsEmployeeEvent.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<Integer> getUnavailableEmployeeIDs(EdsCompany company, Date startDate, Date endDate) {
        Map<String, Object> paramMap = new HashMap<>();

        StringBuffer sql = null;
        sql = new StringBuffer();
        EdsReference reference = referenceManager.findReference(EdsSickRequest._SICK_STATUS, EdsSickRequest.APPROVED);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        paramMap.put("statusID", reference.getObjectID());

        return findByNamedParams("select ee.employee.objectID from EdsEmployeeEvent ee where " +
                ServerUtils.checkForDeleted("ee.deleted") + " and " + ServerUtils.checkForDeleted("ee.event.deleted") + " and (ee.event.startDate <= :endDate or ee.event.endDate >= :startDate) " +
                "and ee.employee.objectID not in (select req.employee.objectID from EdsSickRequest req where " +
                "(req.startDate <= :endDate or ee.event.endDate >= :startDate) and req.overallStatus.objectID=:statusID and req.employee.objectID = ee.employee.objectID) " +
                "and (ee.event.deleted = false)", paramMap);
    }

    public List<EdsUser> getEventSharedEmployees(Integer eventID) {
        return find("select ee.employee from EdsEmployeeEvent ee where " + ServerUtils.checkForDeleted("ee.event.deleted") + " and " + ServerUtils.checkForDeleted("ee.employee.deleted") + " and " + ServerUtils.checkForDeleted("ee.deleted") + " and ee.event.objectID = ? " +
                "and (ee.shared is true or ee.edit is true)", eventID);
    }

    public String getEmployeeAllEventsCount(EdsEmployee employee) {
        return findSingle("select count(ev.objectID) from EdsEmployeeEvent ev where " +
                ServerUtils.checkForDeleted("ev.event.deleted") + " and " + ServerUtils.checkForDeleted("ev.deleted") +
                " and ev.employee = ? and (ev.shared = true or ev.edit = true)", employee).toString();
    }

    public void deleteEmployeeEvents(EdsEvent event) {
        update("update EdsEmployeeEvent ee set ee.deleted = true where ee.event = ?", event);
    }

    public List<EdsEmployeeEvent> getEmployeeEvents(Integer eventID) {
        return findNative("select ee.* from" + getCompanyId() + ".employeeevent ee left join " + getCompanyId() + ".event e on ee.event_id=e.id" +
                " where (e.deleted<> true or e.deleted is null) and (ee.deleted <> true or ee.deleted is null) " +
                " and ee.permission is not null and e.id = " + eventID +
                " order by e.startDate asc, e.subject asc", EdsEmployeeEvent.class);
    }

    public void setEmployeeEventsModifiedDate(EdsEvent event, Date lastModifiedDate) {
        update("update EdsEmployeeEvent ee set ee.lastModifiedDate = ? where ee.event = ?", lastModifiedDate, event);
    }

    @Override
    public void removeOfficeIDFromEmployeeEvents(EdsEmployee employee) {
        updateNative("update " + getCompanyId() + ".employeeevent set officeid = null where event_id in (select id from " + getCompanyId() + ".event e where e.deleted = false and e.owner = " + employee.getObjectID() + ") and deleted = false");
    }

    @Override
    public EdsEmployeeEvent getByOfficeID(String id) {
        List<EdsEmployeeEvent> list = (List<EdsEmployeeEvent>) find("select emploeeevent from EdsEmployeeEvent emploeeevent where emploeeevent.officeID = ? order by emploeeevent.lastModifiedDate desc", id);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void removeOfficeIDFromEvents(EdsEmployee employee) {
        updateNative("update " + getCompanyId() + ".event set officeid = null where deleted = false and owner = " + employee.getObjectID());
    }


    public void create(List<EdsEmployeeEvent> employeeEvents) {
        EntityManager em = jpaTemplate.getHibernateEntityManager();
        try (Session session = em.unwrap(Session.class)) {
            final Transaction tx = session.getTransaction();
            tx.begin();
            for (EdsEmployeeEvent employeeEvent : employeeEvents) {
                create(employeeEvent);
            }
            tx.commit();
        }
        em.close();
    }
}
