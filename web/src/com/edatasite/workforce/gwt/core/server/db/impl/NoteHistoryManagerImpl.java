package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.NoteHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 17:23:37
 */
@Repository("noteHistoryManager")
public class NoteHistoryManagerImpl extends BaseManager<EdsNoteHistory> implements NoteHistoryManager {
    public NoteHistoryManagerImpl() {
        super(EdsNoteHistory.class);
    }

    DateFormat format = new SimpleDateFormat("MMM d, yyyy");
    DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private CrmAccountManager crmAccountManager;

    /**
     * @param relationType
     * @param ids
     * @return
     */
    public Map<Integer, String> getLastNotesAsMap(Integer relationType, List<Integer> ids) {
        Map<Integer, String> notes = new HashMap<>();
        List<Object[]> noteList = findNative("select note.related_id, note.comment from " + getCompanyId() + ".note note, (select cth.related_id ,max(cth.id) as id from " + getCompanyId() + ".note cth\n" +
                "where cth.related_to = " + relationType + " and cth.related_id in (" + ServerUtils.getAsCommoDelimited(ids, "0") + ")" +
                "group by cth.related_id) as tempTable where note.id = tempTable.id;");

        for (Object[] note : noteList) {
            if (note != null && note.length > 1 && note[0] != null && note[1] != null) {
                notes.put((Integer) note[0], (String) note[1]);
            }
        }
        return notes;
    }

    @Override
    public void updateNotesWithAccountID(Integer objectID, List<Integer> otherAccountIDs) {
        update("update EdsNoteHistory set relatedId = " + objectID + " where relatedTo = '" + EdsNoteHistory.CRM_ACCOUNT + "' and relatedId in(" + ServerUtils.getAsCommoDelimited(otherAccountIDs, "0", ",") + ")");
    }

    @Override
    public void updateNotesWithContactID(Integer contactID, List<Integer> otherContactIDs) {
        update("UPDATE EdsNoteHistory set relatedId = " + contactID + " WHERE relatedTo = '" + EdsNoteHistory.CRM_CONTACT + "' AND relatedId IN(" + ServerUtils.getAsCommoDelimited(otherContactIDs, "0", ",") + ")");
    }

    @Override
    public List<EdsNoteHistory> getNoteList(ListingFilterParameter fp) {
        if (fp.getRelationType() == null && fp.getRelationID() == null && fp.getSearchKey() != null && fp.getSearchKey() != "") {
            return (List<EdsNoteHistory>) findNative(getNoteQuery(fp).toString(), EdsNoteHistory.class);
        }
        return findInterval(getNoteQuery(fp, true).toString(), fp.getStart(), fp.getLimit());
    }

    public Integer getListCount(ListingFilterParameter fp) {
        if (fp.getRelationType() == null && fp.getRelationID() == null && fp.getSearchKey() != null && fp.getSearchKey() != "") {
            List<EdsNoteHistory> result = findNative(getNoteQuery(fp).toString(), EdsNoteHistory.class);
            return result != null ? result.size() : 0;
        }
        return ((Long) findSingle(getNoteQuery(fp, false).toString())).intValue();
    }

    private StringBuilder getNoteQuery(ListingFilterParameter fp) {
        EdsUser user = referenceManager.getUser();
        String key = fp.getSearchKey().toLowerCase();
        StringBuilder sql = new StringBuilder();
        sql.append("select n.*, h.* from (");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".project p on n.related_id=p.id where n.related_to=" + EdsNoteHistory.PROJECT);
        sql.append(" and (lower(p.number) like '%" + key + "%' or lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(p.name) like '%").append(key).append("%') ");

        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".task t on n.related_id=t.id where n.related_to=" + EdsNoteHistory.TASK);
        sql.append(" and (lower(t.number) like '%" + key + "%' or lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(t.name) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".task t on n.related_id=t.id where n.related_to=" + EdsNoteHistory.PM_CONTRACT);
        sql.append(" and (lower(t.number) like '%" + key + "%' or lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(t.name) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".crmAccount ca on n.related_id=ca.id where n.related_to=" + EdsNoteHistory.CLIENT);
        sql.append(" and (lower(ca.number) like '%" + key + "%' or lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(ca.name) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".employee e on n.related_id=e.id left join " + getCompanyId() + ".myuser mu on e.id=mu.id where n.related_to=" + EdsNoteHistory.EMPLOYEE);
        sql.append(" and (lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(mu.firstname) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".team t on n.related_id=t.id where n.related_to=" + EdsNoteHistory.DEPARTMENT);
        sql.append(" and (lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(t.name) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".crmAccount sup on n.related_id=sup.id where n.related_to=" + EdsNoteHistory.SUPPLIER);
        sql.append(" and (lower(sup.number) like '%" + key + "%' or lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(sup.name) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".issue iss on n.related_id=iss.id left join " + getCompanyId() + ".task t on iss.id=t.id where n.related_to=" + EdsNoteHistory.PM_ISSUE);
        sql.append(" and (lower(iss.number) like '%" + key + "%' or lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(t.name) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".meetingminutes mm on n.related_id=mm.id where n.related_to=" + EdsNoteHistory.MEETING_MINUTES);
        sql.append(" and (lower(mm.meetingnumber) like '%" + key + "%' or lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(mm.title) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".goal pg on n.related_id=pg.id where n.related_to=" + EdsNoteHistory.PERSONAL_GOAL);
        sql.append(" and (lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(pg.title) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".goal dg on n.related_id=dg.id where n.related_to=" + EdsNoteHistory.DEPARTMENT_GOAL);
        sql.append(" and (lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(dg.title) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".goal pjg on n.related_id=pjg.id where n.related_to=" + EdsNoteHistory.PROJECT_GOAL);
        sql.append(" and (lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(pjg.title) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".goal bus on n.related_id=bus.id where n.related_to=" + EdsNoteHistory.BUSINESS_GOAL);
        sql.append(" and (lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(bus.title) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".vacancy vac on n.related_id=vac.id where n.related_to=" + EdsNoteHistory.VACANCY);
        sql.append(" and (lower(vac.vacancynumber) like '%" + key + "%' or lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%' or lower(vac.jobtitle) like '%").append(key).append("%') ");
        sql.append(" union all ");
        sql.append("select n.* from " + getCompanyId() + ".note n left join " + getCompanyId() + ".placement plc on n.related_id=plc.id where n.related_to=" + EdsNoteHistory.PLACEMENT);
        sql.append(" and (lower(n.comment) like '%").append(key).append("%' or lower(n.subject) like '%").append(key).append("%') ");
        sql.append(") as n  left join " + getCompanyId() + ".myuser u on n.commentatorId = u.id ");
        sql.append(" left join " + getCompanyId() + ".employee e on n.commentatorId = e.id ");
        sql.append(" left join " + getCompanyId() + ".history h on n.id = h.id WHERE ");
        sql.append(" e.id = ").append(user.getObjectID());
        sql.append(" and (h.visibility = false or h.visibility is null) ");
        sql.append(" order by ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField()) && !HistoryListItem.visibilit.equals(fp.getSortField())) {
            if (HistoryListItem.SUBJECT.equals(fp.getSortField())) {
                sql.append("n.subject ");
            } else if (HistoryListItem.NOTE.equals(fp.getSortField())) {
                sql.append("n.comment ");
            } else if (HistoryListItem.relatedTo.equals(fp.getSortField())) {
                sql.append("n.related_to ");
            } else if (HistoryListItem.owner.equals(fp.getSortField())) {
                sql.append("u.firstName ");
            } else {
                sql.append("n.lastUpdated ");
            }
            if (!fp.isAscending()) {
                if (!sql.toString().contains(sql)) {
                    sql.append(sql).append("desc ");
                }
            }
        } else {
            sql.append(" h.date desc ");
        }
        return sql;
    }

    private StringBuilder getNoteQuery(ListingFilterParameter fp, boolean isList) {
        EdsUser user = referenceManager.getUser();

        StringBuilder sql = new StringBuilder();
        if (isList) {
            sql.append("SELECT n FROM EdsNoteHistory n left join n.employee e WHERE 1=1 ");
        } else {
            sql.append("SELECT count(n.objectID) FROM EdsNoteHistory n left join n.employee e WHERE 1=1 ");
        }
        int relatedTo = 0;
        int additionalRelation = 0;
        if (fp != null) {
            if (fp.getRelationType() != null && fp.getRelationID() != null) {
                if (fp.isFromMobile()) {
                    if (RelationItem.TYPE_CONTACT.equals(fp.getRelationType()))
                        relatedTo = EdsNoteHistory.CRM_CONTACT;
                    else
                        relatedTo = Integer.valueOf(fp.getRelationType());
                } else {
                    if (RelationItem.TYPE_CONTACT.equals(fp.getRelationType()) || RelationItem.TYPE_LEAD.equals(fp.getRelationType()) || RelationItem.TYPE_CANDIDATE.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.CRM_CONTACT;
                    } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.CRM_ACCOUNT;
                        if (fp != null && fp.getRelationID() != null) {
                            EdsCrmAccount edsCrmAccount = crmAccountManager.get(fp.getRelationID());
                            if (edsCrmAccount != null && !edsCrmAccount.isDeleted()) {
                                if (edsCrmAccount.isClient()) {
                                    additionalRelation = EdsNoteHistory.CLIENT;
                                } else if (edsCrmAccount.isSupplier()) {
                                    additionalRelation = EdsNoteHistory.SUPPLIER;
                                }
                            }
                        }
                    } else if (RelationItem.TYPE_CASE.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.CRM_CASE;
                    } else if (RelationItem.TYPE_OPPORTUNITY.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.CRM_OPPORTUNITY;
                    } else if (CrmConstants.CAMPAIGN.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.CRM_CAMPAIGN;
                    } else if (CrmConstants.PROJECT.equals(fp.getRelationType()) || RelationItem.TYPE_PROJECT.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.PROJECT;
                    } else if (CrmConstants.TASK.equals(fp.getRelationType()) || RelationItem.TYPE_TASK.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.TASK;
                    } else if (Constants.PM_ISSUE.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.PM_ISSUE;
                    } else if (RelationItem.TYPE_EMPLOYEE.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.EMPLOYEE;
                    } else if (RelationItem.TYPE_DEPARTMENT.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.DEPARTMENT;
                    } else if (RelationItem.TYPE_CLIENT.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.CLIENT;
                    } else if (RelationItem.TYPE_SUPPLIER.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.SUPPLIER;
                    } else if (Constants.VACANCY.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.VACANCY;
                    } else if (Constants.PLACEMENT.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.PLACEMENT;
                    } else if (Constants.MEETING_MINUTES.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.MEETING_MINUTES;
                    } else if (Constants.PERSONAL_GOAL.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.PERSONAL_GOAL;
                    } else if (Constants.DEPARTMENT_GOAL.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.DEPARTMENT_GOAL;
                    } else if (Constants.PROJECT_GOAL.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.PROJECT_GOAL;
                    } else if (Constants.BUSINESS_GOAL.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.BUSINESS_GOAL;
                    } else if (Constants.COMPANY_GOAL.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.COMPANY_GOAL;
                    } else if (RelationItem.TYPE_CONTRACT.equals(fp.getRelationType())) {
                        relatedTo = EdsNoteHistory.PM_CONTRACT;
                    }
                }
            }
            if (fp.getProjectId() != null) {
                relatedTo = EdsNoteHistory.PROJECT;
                fp.setRelationID(fp.getProjectId());
            }
            if (fp.getTaskPriorityId() != null) {
                relatedTo = EdsNoteHistory.TASK;
                fp.setRelationID(fp.getTaskPriorityId());
            }
        }
        //for project customization -> with project tasks notes
        if (fp != null && fp.isWithAllTaskNotes()) {
            String relatedToIds = EdsNoteHistory.PROJECT + "," + EdsNoteHistory.TASK;
            String projectTasksIds = fp.getTaskIds();
            sql.append(" and ((n.relatedId in ").append(projectTasksIds).append(" or n.relatedId=").append(fp.getProjectId()).append(") ");
            sql.append(" and n.relatedTo in (").append(relatedToIds).append(")) ");
        } else {
            if (fp != null && fp.getRelationID() != null && relatedTo > 0) {
                if (additionalRelation > 0) {
                    sql.append(" and ((n.relatedId = ").append(fp.getRelationID()).append(" and (n.relatedTo = ").append(relatedTo).append(" or n.relatedTo = ").append(additionalRelation).append(")");
                } else {
                    sql.append(" and ((n.relatedId = ").append(fp.getRelationID()).append(" and n.relatedTo = ").append(relatedTo);
                }
                if (fp.getEntityID() != null) {
                    sql.append(" ) or n.entityID = ").append(fp.getEntityID()).append(")");
                } else {
                    sql.append("))");
                }
            }
        }
        if (user != null && !user.isClientContact()) {
            if (fp.getParams() != null && !fp.getParams().isEmpty()) {  //this is for the stacky note
                sql.append(" AND e.objectID = '").append(user.getObjectID()).append("' ");
            } else {
                sql.append(" AND (e.objectID = '").append(user.getObjectID()).append("' OR n.visibility = false OR n.visibility is null) ");
            }
        }

        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            sql.append(" AND e.objectID = '").append(getUser().getObjectID()).append("' ");
        }

        if (user != null && user.isClientContact()) {
            sql.append(" AND (e.objectID in (SELECT cc.objectID FROM EdsClientContact cc) or n.visibility is false or n.visibility is null").append(")");
        }

        if (fp.getGroupById() != null && fp.getGroupById() != 0) {
            if (fp.getRelationID() != null && !fp.getRelationID().equals(0)) {
                sql.append(" AND n.relatedId=").append(fp.getRelationID());
            }
            sql.append(" AND n.relatedTo=").append(fp.getGroupById());
        }
        if (fp.getStartDate() != null && fp.getEndDate() != null && fp.getEndDate().compareTo(fp.getStartDate()) >= 0) {
            sql.append(" AND to_date(to_char(n.eventDate,'yyyy-mm-dd'), 'yyyy-mm-dd') between to_date('").append(format2.format(fp.getStartDate())).append("', 'yyyy-mm-dd') AND to_date('").append(format2.format(fp.getEndDate())).append("', 'yyyy-mm-dd') ");
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" and (lower(n.comment) like '%").append(fp.getSearchKey().toLowerCase()).append("%' or lower(n.subject) like '%").append(fp.getSearchKey().toLowerCase()).append("%') ");
        }
        if (!isList) {
            return sql;
        }

        sql.append("ORDER BY ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField()) && !HistoryListItem.visibilit.equals(fp.getSortField())) {
            if (HistoryListItem.id.equals(fp.getSortField())) {
                sql.append("n.id ");
            } else if (HistoryListItem.SUBJECT.equals(fp.getSortField())) {
                sql.append("n.subject ");
            } else if (HistoryListItem.NOTE.equals(fp.getSortField())) {
                sql.append("n.comment ");
            } else if (HistoryListItem.relatedTo.equals(fp.getSortField())) {
                sql.append("n.relatedTo ");
            } else if (HistoryListItem.owner.equals(fp.getSortField())) {
                sql.append("n.employee.firstName ");
            } else {
                sql.append("n.eventDate ");
            }
            if (!fp.isAscending()) {
                sql.append("DESC ");
            } else {
                sql.append("ASC ");
            }
        } else {
            sql.append(" n.eventDate DESC ");
        }
        return sql;
    }
}