package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBrigada;
import com.edatasite.workforce.core.domain.EdsBrigadaEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.BrigadaManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("brigadaManager")
public class BrigadaManagerImpl extends BaseManager<EdsBrigada> implements BrigadaManager {
    public BrigadaManagerImpl() {
        super(EdsBrigada.class);
    }

    @Override
    public List<EdsBrigada> getList(ListingFilterParameter fp, Integer userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct(b) from  EdsBrigada b  left join b.owners owners where b.deleted is not true " + (userId != null ? " and (" + userId + " in (b.managerid,b.backup_ManagerId,b.backup_ManagerId2,b.backup_ManagerId3,b.backup_ManagerId4,b.backup_ManagerId5,b.backup_ManagerId6,b.backup_ManagerId7,b.backup_ManagerId8,b.backup_ManagerId9,b.backup_ManagerId10,b.creator) or owners.objectID =  " + userId + ")" : ""));

        if (fp.getSearchKey() != null && !fp.getSearchKey().isEmpty()) {
            sql.append(" and  (lower(b.number) like '%" + fp.getSearchKey().toLowerCase() + "%'  ");
            sql.append("  or  lower(b.name)   like '%" + fp.getSearchKey().toLowerCase() + "%' )");
        }
        String sqlOrder = "";
        if (fp.getSortField() != null) {
            sql.append(" order by b.");
            if ("createdBy".equals(fp.getSortField())) {
                sql.append("creator");
            } else if ("Created date".equals(fp.getSortField())) {
                sql.append("creationTime");
            } else if ("Modified By".equals(fp.getSortField())) {
                sql.append("updater");
            } else if ("Modified date".equals(fp.getSortField())) {
                sql.append("lastUpdateTime");
            } else {
                sql.append(fp.getSortField());
            }

            if (fp.getValueMap().get("ASC").equals("true")) {
                sql.append(" asc");
            } else {
                sql.append(" desc");
            }
        }

        if (fp.getSortField() == null) {
            sql.append(" order by b.objectID desc");
        }

        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public List<EdsBrigada> getBrigadasForLookUp(Integer userId) {
        return findNative("select * from  " + getCompanyId() + ".brigadas where isDeleted is not true and  " + userId + " in (managerid,backup_ManagerId,backup_ManagerId2,backup_ManagerId3,backup_ManagerId4,backup_ManagerId5,backup_ManagerId6,backup_ManagerId7,backup_ManagerId8,backup_ManagerId9,backup_ManagerId10,creatorid) ", EdsBrigada.class);
    }

    @Override
    public void deleteBrigada(EdsBrigada project) {
        update("update EdsBrigada p set p.deleted=true " +
                "where p=? and p.deleted<>true", project);
    }

    @Override
    public List<EdsBrigadaEmployee> getEmployeesByBrigada(Integer projectId) {
        return find("select distinct pe from EdsBrigadaEmployee pe where pe.project.objectID=? and pe.deleted<>true ", projectId);

    }

    @Override
    public List<Integer> getActiveTeamsId() {
        return (List<Integer>) findNative("select distinct id from  " + getCompanyId() + " .brigadas where isDeleted is not true ");
    }

    @Override
    public Integer getBrigadaLastIntNumber() {
        return (Integer) findSingle("select bce.intNumber from EdsBrigada bce where (bce.deleted = false or bce.deleted is null) and bce.intNumber is not null order by bce.intNumber desc");
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter fp, Integer userId) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count (distinct b.id ) from ").append(getCompanyId());
        sql.append(".brigadas b left join " + getCompanyId() + ".brigada_owners bo on (b.id = bo.brigada_id)  where isDeleted is not true " + (userId != null ? " and (" + userId + " in (managerid,backup_ManagerId,backup_ManagerId2,backup_ManagerId3,backup_ManagerId4,backup_ManagerId5,backup_ManagerId6,backup_ManagerId7,backup_ManagerId8,backup_ManagerId9,backup_ManagerId10,creatorid) or bo.owner_id  =  " + userId + ")" : ""));
        return Integer.parseInt(findNativeSingle(sql.toString()).toString());
    }

    @Override
    public ArrayList<String> getSavedBrigadasForThisPeriod(ArrayList<Integer> ids, String period, Integer shiftId, Integer shiftType) {
        String brigadaIds = ServerUtils.getAsCommoDelimited(ids, "0", ",");
        StringBuilder sqltext = new StringBuilder();
        sqltext.append("select " + (shiftType == LookUpConstants.BRIGADA_ID ? "distinct(b.name)" : "distinct(b.firstName || ' ' || b.lastName || ' ' || b.middleName)") + " from  " + getCompanyId() + ".shift sh  \n" +
                "join " + getCompanyId() + ".shift_items shi on sh.id = shi.shift_id join " + getCompanyId() + (shiftType == LookUpConstants.BRIGADA_ID ? ".brigadas b on shi.groupId = b.id " : ".myUser b on shi.groupId = b.id ") + " where TO_CHAR(sh.period,'YYYY-MM') = '" + period + "' and shi.groupid in (" + brigadaIds + ")" + (shiftId != null ? " and sh.id <> " + shiftId : ""));
        sqltext.append(" and sh.deleted is not true and sh.lookupType = 45");
        return (ArrayList<String>) findNative(sqltext.toString());
    }

    @Override
    public ArrayList<String> getSavedOvertimeForThisPeriod(ArrayList<Integer> ids, String overtimeQuery, String period, Integer shiftId, Integer type) {
        StringBuilder sqltext = new StringBuilder();
        sqltext.append("select distinct(b.firstName || ' ' || b.lastName || ' ' || b.middleName)" + " from  " + getCompanyId() + ".shift sh  \n" + "join " + getCompanyId() + ".shift_items shi on sh.id = shi.shift_id join " + getCompanyId() + ".myUser b on shi.groupId = b.id " + " where TO_CHAR(sh.period,'YYYY-MM') = '" + period + "'" + (shiftId != null ? " and sh.id <> " + shiftId : ""));
        sqltext.append(" and sh.deleted is not true and sh.lookupType = " + type);
        if (!overtimeQuery.isEmpty()) {
            sqltext.append(" and (" + overtimeQuery + ")");
        }
        return (ArrayList<String>) findNative(sqltext.toString());
    }
}
