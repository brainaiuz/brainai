package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBugReport;
import com.edatasite.workforce.gwt.backend.client.rpc.BugsPerEmployeesListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.BugReportManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Dec 11, 2008
 * Time: 1:35:01 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("bugReportManager")
public class BugReportManagerImpl extends BaseManager<EdsBugReport> implements BugReportManager {
    public BugReportManagerImpl() {
        super(EdsBugReport.class);
    }

    public List<EdsBugReport> getBugLists(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct br.* from " + getPublic() + ".bugreport br where br.parent_id is null ");
        if (fp != null) {
            if (fp.getSqlSearchKey() != null) {
                sql.append(" and ( ");
                sql.append(" lower(br.description) like '");
                sql.append(fp.getSqlSearchKey());
                sql.append("' ");
                sql.append("or lower(br.assignName) like '");
                sql.append(fp.getSqlSearchKey());
                sql.append("' ");
                sql.append("or lower(br.creatorName) like '");
                sql.append(fp.getSqlSearchKey());
                sql.append("' ");
                sql.append("or lower(br.companyName) like '");
                sql.append(fp.getSqlSearchKey());
                sql.append("' ");
                sql.append(") ");
            } else {
                if (fp.getStatusValues() != null && !"".equals(fp.getStatusValues())) {
                    sql.append(" and br.status ='");
                    sql.append(fp.getStatusValues());
                    sql.append("'");
                }
                if (fp.getBugAssigneeId() != null && fp.getBugAssigneeId() != 0 && fp.getBugAssigneeId() != -2) {
                    sql.append(" and br.assign_id =");
                    sql.append(fp.getBugAssigneeId());
                }
                if ((fp.getBugAssigneeId() != null && fp.getBugAssigneeId() != 0) && fp.getBugAssigneeId() == -2) {
                    sql.append(" and br.assign_id is null");
                }
            }
        }
        if (fp != null && fp.getSortField() != null) {
            sql.append(" order by br." + fp.getSortField() + (fp.isAscending() ? " ASC" : " DESC"));
        } else {
            sql.append(" order by br.creationTime DESC");
        }
        return findNative(sql.toString(), EdsBugReport.class);
    }

    public List<Object[]> getBugsPerEmployees(String newStatusRef, String resolvedStatusRef, String underInvestStatusRef,
                                              String inProgressStatusRef, String ignoredStatusRef, String doneStatusRef, ListingFilterParameter fp) {
        String search = "";
        if (fp.getSortField() == null) {
            fp.setSortField(BugsPerEmployeesListItem.TOTAL_BUG);
        }
        if (fp.getSqlSearchKey() != null) {
            search = "and br.assignName like '%" + fp.getSqlSearchKey() + "%'";
        }
        return findNative("select distinct br.assign_id, br.assignName as " + BugsPerEmployeesListItem.EMPLOYEE + ", " +
                "(select count(b.status) from " + getPublic() + ".bugreport b    where b.status = '" + newStatusRef + "' and b.parent_id is null and ((br.assign_id is null and b.assign_id is  null) or b.assign_id= br.assign_id)) as " + BugsPerEmployeesListItem.STATUS_NEW + ", " +
                "(select count(b.status) from " + getPublic() + ".bugreport b    where b.status = '" + resolvedStatusRef + "' and b.parent_id is null and ((br.assign_id is null and b.assign_id is  null) or b.assign_id= br.assign_id)) as " + BugsPerEmployeesListItem.STATUS_RESOLVED + ", " +
                "(select count(b.status) from " + getPublic() + ".bugreport b    where b.status = '" + underInvestStatusRef + "' and b.parent_id is null and ((br.assign_id is null and b.assign_id is  null) or b.assign_id= br.assign_id)) as " + BugsPerEmployeesListItem.STATUS_UNDER_INVESTIGATION + ", " +
                "(select count(b.status) from " + getPublic() + ".bugreport b    where b.status = '" + inProgressStatusRef + "' and b.parent_id is null and ((br.assign_id is null and b.assign_id is  null) or b.assign_id= br.assign_id)) as " + BugsPerEmployeesListItem.STATUS_IN_PROGRESS + ", " +
                "(select count(b.status) from " + getPublic() + ".bugreport b    where b.status = '" + ignoredStatusRef + "' and b.parent_id is null and ((br.assign_id is null and b.assign_id is  null) or b.assign_id= br.assign_id)) as " + BugsPerEmployeesListItem.STATUS_IGNORED + ", " +
                "(select count(b.status) from " + getPublic() + ".bugreport b    where b.status = '" + doneStatusRef + "' and b.parent_id is null and ((br.assign_id is null and b.assign_id is  null) or b.assign_id= br.assign_id)) as " + BugsPerEmployeesListItem.STATUS_DONE + ", " +
                "count(br.*) as " + BugsPerEmployeesListItem.TOTAL_BUG + " " +
                "from " + getPublic() + ".bugreport br where br.parent_id is null " + search + "  group by br.assign_id, br.assignName order by " + fp.getSortField() + (fp.isAscending() ? " desc" : " asc") + " limit " + fp.getLimit() + " offset " + fp.getStart());
    }

    public List<Object[]> getBugsPerSection(String newStatusRef, String resolvedStatusRef, String underInvestStatusRef,
                                            String inProgressStatusRef, String ignoredStatusRef, String doneStatusRef, ListingFilterParameter fp) {
        String search = "";
        if (fp.getSortField() == null) {
            fp.setSortField(BugsPerEmployeesListItem.TOTAL_BUG);
        }
        if (fp.getSqlSearchKey() != null) {
            search = "and br.createdFrom like '%" + fp.getSqlSearchKey() + "%'";
        }
        return findNative("select distinct br.createdFrom as " + BugsPerEmployeesListItem.SECTION + ", " +
                "(select count(b.createdFrom) from " + getPublic() + ".bugreport b where b.status= '" + newStatusRef + "' and b.createdFrom=br.createdFrom) as " + BugsPerEmployeesListItem.STATUS_NEW + ", " +
                "(select count(b.createdFrom) from " + getPublic() + ".bugreport b where b.status= '" + resolvedStatusRef + "' and b.createdFrom=br.createdFrom) as " + BugsPerEmployeesListItem.STATUS_RESOLVED + ", " +
                "(select count(b.createdFrom) from " + getPublic() + ".bugreport b where b.status= '" + underInvestStatusRef + "' and b.createdFrom=br.createdFrom) as " + BugsPerEmployeesListItem.STATUS_UNDER_INVESTIGATION + ", " +
                "(select count(b.createdFrom) from " + getPublic() + ".bugreport b where b.status= '" + inProgressStatusRef + "' and b.createdFrom=br.createdFrom) as " + BugsPerEmployeesListItem.STATUS_IN_PROGRESS + ", " +
                "(select count(b.createdFrom) from " + getPublic() + ".bugreport b where b.status= '" + ignoredStatusRef + "' and b.createdFrom=br.createdFrom) as " + BugsPerEmployeesListItem.STATUS_IGNORED + ", " +
                "(select count(b.createdFrom) from " + getPublic() + ".bugreport b where b.status= '" + doneStatusRef + "' and b.createdFrom=br.createdFrom) as " + BugsPerEmployeesListItem.STATUS_DONE + ", " +
                "count(br.createdFrom) as " + BugsPerEmployeesListItem.TOTAL_BUG + " " +
                "from " + getPublic() + ".bugreport br where br.parent_id is null " + search + " group by br.createdFrom order by " + fp.getSortField() + (fp.isAscending() ? " desc" : " asc") + " limit " + fp.getLimit() + " offset " + fp.getStart());
    }

    public List<EdsBugReport> getFeedBacksByUser(Integer userID) {
        return findLimited("select bugs from EdsBugReport bugs where bugs.creator = ? order by bugs.creationTime desc", 10, userID);
    }

    @Override
    public int getBugsPerEmployeesCount(String newStatusRef, String resolvedStatusRef, String underInvestStatusRef, String inProgressStatusRef, String ignoredStatusRef, String doneStatusRef, ListingFilterParameter fp) {
        return Integer.parseInt(findNativeSingle("select count(*) as total " +
                "from " + getPublic() + ".bugreport br where br.parent_id is null ").toString());
    }
}
