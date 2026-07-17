package com.finnetlimited.reportservice.core.server.db.dao.schema;

import com.edatasite.workforce.core.domain.reporting.EdsCompanyFavouriteReportTemplates;
import com.edatasite.workforce.gwt.core.client.rpc.SavedReportTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.reportingsystem.client.enumerable.FolderType;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ReportingTestDTO;
import com.edatasite.workforce.gwt.reportingsystem.client.rpc.SelectListRpc;
import com.finnetlimited.reportservice.core.server.db.schema.ReportingManager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * User: ${Dilsh0d}
 * Date: 06-Mar-2010
 * Time: 17:15:31
 */
@Repository("reportingManager")
public class ReportingManagerImpl extends BaseManager<EdsReport> implements ReportingManager, PermissionConstants {
    public ReportingManagerImpl() {
        super(EdsReport.class);
    }

    public List<Object[]> listObject(ListingFilterParameter filter) {
        filter.setSearchKey(filter.getSearchKey() == null ? "" : filter.getSearchKey().toLowerCase().replace("'", "''"));
        String roleCondition = (filter.getRoles() == null || "".equals(filter.getRoles())) ? "" : (" AND ( rp.rolecode in (" + filter.getRoles() + ") and rp.access='ALLOW') ");
        String select = " report.id,report.name,report.code,report.viewCode" +
                ", template.name as template,template.isLibrary,template.isCustom, template.order_number" +
                ", f.id folderId, f.name folder " +
                ", c.id categoryId,c.name category" +
                ", fav.id favId " +
                ", f.description folderDescription, report.description reportDescription, report.xmlTemplateId " +
                ", c_old.id old_categoryId,c_old.name old_category" +
                ", report.fakeReport, report.targetLink " +
                ", f.type folderType" +
                ", coalesce(u.firstName,'')||' '||coalesce(u.lastName,'') createdBy, report.creationDate" +
                ", report.reportType";
        String query = "SELECT DISTINCT r.* FROM ( SELECT " + select + ", f.sorder f_sorder,report.sorder r_sorder, c.sorder c_sorder, f.icon folderIcon, c.code categorycode  \n" +
                " FROM \"" + filter.getCompanyID() + "\".reporting AS report \n" +
                " JOIN \"" + filter.getCompanyID() + "\".folders AS f on report.folderid=f.id  \n" +
                " JOIN " + getPublic() + ".reportTemplate template on report.viewCode=template.code \n" +
                " left join " + getPublic() + ".reporttemplatecategory c on f.categorycode=c.code \n" +
                " left join " + getPublic() + ".reporttemplatecategory c_old on template.categorycode=c_old.code \n" +
                " left join \"" + filter.getCompanyID() + "\".companyFavouriteReportTemplates fav on fav.userId=" + filter.getUserID() + " and fav.reportingid=report.id \n" +
                " join " + getCompanyId() + ".mymodule mm on (c.modulecode is null or (c.modulecode = mm.code and mm.active is true)) " +
                " left JOIN \"" + filter.getCompanyID() + "\".reportingpermission p on p.context = 'REPORTING' and p.code = report.permissionCode \n " +
                " left JOIN \"" + filter.getCompanyID() + "\".rolepermission rp on rp.permissioncode=p.code \n" +
                " LEFT JOIN \"" + filter.getCompanyID() + "\".myUser u on u.id=report.createdBy_Id \n" +
                " WHERE report.deleted is not true AND f.deleted is not true \n" +
                " and lower(report.name) like '%" + (filter.getSearchKey() != null ? filter.getSearchKey().toLowerCase().trim() : "") + "%' " +
                (filter.getCategoryID() != null && filter.getCategoryID() != 0 ? (" and c.id = " + filter.getCategoryID()) : "") +
                (filter.getCategoryID() != null && filter.getCategoryID() == 0 ? (" and fav.id is not null ") : "") +
                " AND ( \n" +
                "       (f.domainName='" + filter.getSubscriptionTypeName() + "' AND f.type='" + FolderType.System.name() + "' " + roleCondition + ") \n" +
                "       OR (f.type='" + FolderType.Public.name() + "' " + roleCondition + " ) \n" +
                "       OR (f.userid=" + filter.getUserID() + " AND f.type='" + FolderType.Private.name() + "') \n" +
                "       OR report.createdBy_id=" + filter.getUserID() + " \n" +
                " ) ORDER BY c.sorder,template.order_number, f.name,report.name ) r ORDER BY r.c_sorder, r.f_sorder,r.r_sorder, r.order_number, r.folder,r.name \n";
        return findNative(query);
    }

    public ArrayList<SelectItem> getMinimizedReportList(ListingFilterParameter filter) {
        ArrayList<SelectItem> result = new ArrayList<>();
        filter.setSearchKey(filter.getSearchKey() == null ? "" : filter.getSearchKey().toLowerCase().replace("'", "''"));
        String roleCondition = (filter.getRoles() == null || "".equals(filter.getRoles())) ? "" : (" AND ( rp.rolecode in (" + filter.getRoles() + ") and rp.access='ALLOW') ");
        String select = " report.id, report.name reportName, report.targetLink targetlink, c.name category, report.fakeReport isFake";
        String query = "SELECT DISTINCT r.* FROM ( SELECT " + select +
                " FROM \"" + filter.getCompanyID() + "\".reporting AS report \n" +
                " JOIN \"" + filter.getCompanyID() + "\".folders AS f on report.folderid=f.id  \n" +
                " JOIN " + getPublic() + ".reportTemplate template on report.viewCode=template.code \n" +
                " left join " + getPublic() + ".reporttemplatecategory c on f.categoryCode=c.code \n" +
                " join " + getCompanyId() + ".mymodule mm on (c.modulecode is null or (c.modulecode = mm.code and mm.active is true)) " +
                " left join " + getPublic() + ".reporttemplatecategory c_old on template.categorycode=c_old.code\n" +
                " left join \"" + filter.getCompanyID() + "\".companyFavouriteReportTemplates fav on fav.userId=" + filter.getUserID() + " and fav.reportingid=report.id \n" +
                " left JOIN \"" + filter.getCompanyID() + "\".reportingpermission p on p.context='REPORTING' and p.code=report.permissionCode \n" +
                " left JOIN \"" + filter.getCompanyID() + "\".rolepermission rp on rp.permissioncode=p.code \n" +
                " LEFT JOIN \"" + filter.getCompanyID() + "\".myUser u on u.id=report.createdBy_Id \n" +
                " WHERE report.deleted is not true AND f.deleted is not true \n" +
                " and lower(report.name) like '%" + (filter.getSearchKey() != null ? filter.getSearchKey().toLowerCase().trim() : "") + "%' " +
                (filter.getCategoryID() != null ? (" and c.id = " + filter.getCategoryID()) : "") +
                " AND ( \n" +
                "       (f.domainName='" + filter.getSubscriptionTypeName() + "' AND f.type='" + FolderType.System.name() + "' " + roleCondition + ") \n" +
                "       OR (f.type='" + FolderType.Public.name() + "' " + roleCondition + " ) \n" +
                "       OR (f.userid=" + filter.getUserID() + " AND f.type='" + FolderType.Private.name() + "') \n" +
                "       OR report.createdBy_id=" + filter.getUserID() + " \n" +
                " ) ORDER BY report.name ) r ORDER BY r.reportName \n";
        List<Object[]> nativeResult = findNative(query);
        if (nativeResult != null) {
            for (Object[] item : nativeResult) {
                Integer id = item[0] != null ? (Integer) item[0] : null;
                String name = item[1] != null ? (String) item[1] : null;
                String description = item[2] != null ? (String) item[2] : null;
                String category = item[3] != null ? (String) item[3] : null;
                boolean fake = item[4] != null && (boolean) item[4];
                result.add(new SelectItem(id, name, description, category, fake));
            }
        }
        return result;
    }

    public List<Object[]> getCategories() {
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct c.id, c.code, c.name \n")
                .append("from ").append(getCompanyId()).append(".reporting as report \n")
                .append("join ").append(getCompanyId()).append(".folders as f on report.folderid = f.id \n")
                .append("join ").append(getPublic()).append(".reporttemplate template on report.viewcode = template.code \n")
                .append("join ").append(getPublic()).append(".reporttemplatecategory c on f.categoryCode = c.code \n")
                .append(" join ").append(getCompanyId()).append(".mymodule mm on (c.modulecode is null or (c.modulecode = mm.code and mm.active is true)) ")
                .append(" left join ").append(getPublic()).append(".reporttemplatecategory c_old on template.categoryCode = c_old.code")
                .append(" left join ").append(getCompanyId()).append(".companyfavouritereporttemplates fav on fav.userId = ").append(getUser().getObjectID()).append(" and fav.reportingid = report.id")
                .append(" left join ").append(getCompanyId()).append(".reportingpermission p on p.context = 'REPORTING' AND p.code = report.permissioncode")
                .append(" left join ").append(getCompanyId()).append(".rolepermission rp on rp.permissioncode = p.code")
                .append(" left join ").append(getCompanyId()).append(".myuser u on u.id = report.createdby_id")
                .append(" WHERE report.deleted IS NOT TRUE AND f.deleted IS NOT TRUE \n")
                .append(" AND ( \n")
                .append("(f.domainName = '#' AND f.type = 'System') \n")
                .append(" OR f.type = 'Public' \n")
                .append(" OR (f.userid = 1 AND f.type = 'Private') \n")
                .append(" OR report.createdBy_id = 1 \n")
                .append(")");
        return findNative(sql.toString());
    }

    public ArrayList<SelectListRpc> getReports(ListingFilterParameter filter) {
        ArrayList<SelectListRpc> result = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct report.id reportId,")
                .append(" report.name reportName,")
                .append(" report.description reportDescription,")
                .append(" f.name folderName,")
                .append(" report.targetlink,")
                .append(" c.name categoryName, ")
                .append(" fav.id favourited, ")
                .append(" report.creationDate creationDate, ")
                .append(" mu.firstname  ||' '||  mu.lastname createdBy, ")
                .append(" report.modificationDate modificationDate, ")
                .append(" mr.firstname ||' '|| mr.lastname modifiedBy, ")
                .append(" report.fakeReport isFakeReport ")
                .append(" from ").append(getCompanyId()).append(".reporting as report")
                .append(" join ").append(getCompanyId()).append(".folders as f on report.folderid = f.id")
                .append(" join ").append("reporttemplate template on report.viewcode = template.code")
                .append(" left join ").append(getPublic()).append(".reporttemplatecategory c on f.categoryCode = c.code")
                .append(" join ").append(getCompanyId()).append(".mymodule mm on (c.modulecode is null or (c.modulecode = mm.code and mm.active is true)) ")
                .append(" left join ").append(getCompanyId()).append(".reportingpermission p on p.context = 'REPORTING' AND p.code = report.permissioncode")
                .append(" left join ").append(getPublic()).append(".reporttemplatecategory c_old on template.categoryCode = c_old.code ")
                .append(" left join ").append(getCompanyId()).append(".companyfavouritereporttemplates fav on fav.userId = ").append(getUser().getObjectID()).append(" and fav.reportingid = report.id")
                .append(" left join ").append(getCompanyId()).append(".rolepermission rp on rp.permissioncode = p.code")
                .append(" left join ").append(getCompanyId()).append(".myuser mu on mu.id = report.createdBy_id")
                .append(" left join ").append(getCompanyId()).append(".myuser mr on mu.id = report.modifiedBy_id")
                .append(" WHERE report.deleted IS NOT TRUE AND f.deleted IS NOT TRUE")
                .append(" AND (")
                .append(" (f.domainName = '#' AND f.type = 'System')")
                .append(" OR f.type = 'Public'")
                .append(" OR (f.userid = 1 AND f.type = 'Private')")
                .append(" OR report.createdBy_id = 1 ")
                .append(")");
        if (filter.getCategoryID() != 0) {
            sql.append(" AND c.id = ").append(filter.getCategoryID());
        }
        if (filter.getCategory() != null && !filter.getCategory().isEmpty()) {
            sql.append(" AND f.categorycode= '").append(filter.getCategory() + "'");
        }
        if (filter.isFavourite()) {
            sql.append(" AND fav.reportingid = report.id");
        }
        if (!StringUtils.isEmpty(filter.getSearchKey())) {
            sql.append(" AND (")
                    .append(" lower(report.name) like '%").append(filter.getSearchKey().toLowerCase()).append("%'")
                    .append(" OR lower(report.description) like '%").append(filter.getSearchKey().toLowerCase()).append("%'")
                    .append(" OR lower(f.name) like '%").append(filter.getSearchKey().toLowerCase()).append("%'")
                    .append(")");
        }
        sql.append(" ORDER BY");
        if (!StringUtils.isEmpty(filter.getSortField()) && !SelectListRpc.FOLDER.equals(filter.getSortField())) {
            if (SelectListRpc.NAME.equals(filter.getSortField())) {
                sql.append(" report.name");
            } else if (SelectListRpc.DESCRIPTION.equals(filter.getSortField())) {
                sql.append(" report.description");
            } else if (SelectListRpc.FOLDER.equals(filter.getSortField())) {
                sql.append(" f.name");
            } else {
                sql.append(" report.id");
            }
            if (filter.getSortDir() == 2) {
                sql.append(" desc");
            }
        } else {
            sql.append(" f.name asc, report.name asc ");
        }
        if (filter.getStart() != null && filter.getLimit() != null) {
            sql.append(" offset ").append(filter.getStart()).append(" limit ").append(filter.getLimit());
        }
        List<Object[]> objects = findNative(sql.toString());
        for (Object[] object : objects) {
            SelectListRpc item = new SelectListRpc();
            item.setId((Integer) object[0]);
            item.setName((String) object[1]);
            item.setDescription((String) object[2]);
            item.setFolder((String) object[3]);
            item.setTargetLink((String) object[4]);
            item.setCategory((String) object[5]);
            item.setFavourited(object[6] != null);
            item.setCreatedDate((Date) object[7]);
            item.setCreatedBy((String) object[8]);
            item.setModifiedDate((Date) object[9]);
            item.setModifiedBy(object[10] != null ? (String) object[10] : null);
            item.setFakeReport(object[11] != null ? (Boolean) object[11] : false);
            result.add(item);
        }
        return result;
    }

    public Integer getReportsCount(ListingFilterParameter filter) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(distinct report.id)")
                .append(" from ").append(getCompanyId()).append(".reporting as report")
                .append(" join ").append(getCompanyId()).append(".folders as f on report.folderid = f.id")
                .append(" join ").append(getPublic()).append(".reporttemplate template on report.viewcode = template.code")
                .append(" left join").append(getPublic()).append(".reporttemplatecategory c on f.categoryCode = c.code")
                .append(" left join ").append(getCompanyId()).append(".reportingpermission p on p.context = 'REPORTING' AND p.code = report.permissioncode")
                .append(" join ").append(getCompanyId()).append(".mymodule mm on (c.modulecode is null or (c.modulecode = mm.code and mm.active is true) ) ")
                .append(" left join ").append(getPublic()).append(".reporttemplatecategory c_old on template.categoryCode = c_old.code ")
                .append(" left join ").append(getCompanyId()).append(".companyfavouritereporttemplates fav on fav.userId = ").append(getUser().getObjectID()).append(" and fav.reportingid = report.id")
                .append(" left join ").append(getCompanyId()).append(".rolepermission rp on rp.permissioncode = p.code")
                .append(" WHERE report.deleted IS NOT TRUE AND f.deleted IS NOT TRUE")
                .append(" AND (")
                .append(" (f.domainName = '#' AND f.type = 'System')")
                .append(" OR f.type = 'Public'")
                .append(" OR (f.userid = 1 AND f.type = 'Private')")
                .append(" OR report.createdBy_id = 1 ")
                .append(")");
        if (filter.getCategoryID() != 0) {
            sql.append(" AND c.id = ").append(filter.getCategoryID());
        } else {
            sql.append(" AND fav.id is not null");
        }
        if (!StringUtils.isEmpty(filter.getSearchKey())) {
            sql.append(" AND (")
                    .append(" lower(report.name) like '%").append(filter.getSearchKey().toLowerCase()).append("%'")
                    .append(" OR lower(report.description) like '%").append(filter.getSearchKey().toLowerCase()).append("%'")
                    .append(" OR lower(f.name) like '%").append(filter.getSearchKey().toLowerCase()).append("%'")
                    .append(")");
        }
        BigInteger count = (BigInteger) findNativeSingle(sql.toString());
        return count.intValue();
    }

    @Transactional
    @Override
    public void addOrRemoveProject(Integer reportId, boolean addProject) {
        updateNative("update " + getCompanyId() + ".reporting set addproject=" + addProject + " where id=" + reportId);
    }

    @Override
    public ArrayList<EdsReport> getReletedProjectReports() {
        return (ArrayList<EdsReport>) find("SELECT report FROM EdsReport AS report WHERE (report.deleted is null OR report.deleted <> true) and addProject = true order by report.sorder");
    }

    public boolean hasReport(String name, Integer reportId) {
        Boolean hasReport;
        if (reportId != null) {
            hasReport = ((Long) findSingle("SELECT count(report) FROM EdsReport AS report WHERE (report.deleted is null OR report.deleted <> true) AND lower(report.code)=? AND report.objectID != ?", name.toLowerCase(), reportId)) > 0;
        } else {
            hasReport = ((Long) findSingle("SELECT count(report) FROM EdsReport AS report WHERE (report.deleted is null OR report.deleted <> true) AND lower(report.code)=?", name.toLowerCase())) > 0;
        }
        return hasReport != null && hasReport;
    }

    public ArrayList<EdsReport> getFolderReports(Integer folderId) {
        return (ArrayList<EdsReport>) find("SELECT report FROM EdsReport AS report join report.folderid AS folder " +
                " WHERE (report.deleted is null OR report.deleted <> true) " +
                " AND (folder.deleted is null OR folder.deleted <> true) AND folder.objectID=" + folderId);
    }

    public ArrayList<EdsReport> getFavReports(Integer userid) {
        return (ArrayList<EdsReport>) find("SELECT report " +
                " FROM EdsCompanyFavouriteReportTemplates AS cf" +
                " LEFT JOIN cf.reporting AS report " +
                " WHERE (report.deleted is null OR report.deleted <> true) AND cf.user.objectID=" + userid);
    }

    public void deleteFavouriteReportTemplate(Integer userid, Integer reportingid, Integer companyId) {
        updateNative("DELETE FROM \"" + companyId + "\".companyfavouritereporttemplates " +
                "WHERE userid = " + userid + " ANd reportingid = " + reportingid);
    }

    public void createFavouriteReportTemplate(Integer userId, Integer reportid, Integer companyId) {
        updateNative("INSERT INTO \"" + companyId + "\".companyfavouritereporttemplates  (reportingid, userid) VALUES (" + reportid + ", " + userId + ")");
    }

    public EdsCompanyFavouriteReportTemplates getFavouriteReportTemplate(Integer userid, Integer reportid, Integer companyId) {
        return (EdsCompanyFavouriteReportTemplates) findSingle("SELECT rp FROM EdsCompanyFavouriteReportTemplates AS rp LEFT JOIN rp.reporting as report " +
                "WHERE (report.deleted is null OR report.deleted <> true) AND report.objectID=" + reportid + " AND rp.user.objectID=" + userid + " ORDER BY report.name");
    }

    public Integer getCategoryByReport(Integer reportid, Integer companyid) {
        return (Integer) findNativeSingle("SELECT rtc.id FROM \"" + companyid + "\".reporting AS r " +
                " JOIN " + getPublic() + ".reporttemplate AS rt " +
                " ON r.viewCode = rt.code " +
                " JOIN " + getPublic() + ".reporttemplatecategory rtc " +
                " ON rt.categoryCode = rtc.code " +
                " WHERE r.deleted is not true AND r.id = " + reportid);
    }

    public Boolean getReportStar(Integer reportid, Integer userid) {
        return ((Long) findSingle("SELECT count(fr) FROM EdsCompanyFavouriteReportTemplates AS fr LEFT JOIN fr.reporting as report " +
                "WHERE (report.deleted is null OR report.deleted <> true) AND fr.user.objectID=" + userid + " And report.objectID=" + reportid)) == 0;
    }

    @Override
    public Integer getReportListCount(ListingFilterParameter filterParameter) {
        String sql = "select count(r) from EdsReport r,EdsReportTemplate t where (t.code = r.viewCode or t is null) and (r.deleted =false or r.deleted is null) ";
        if (filterParameter.getSearchKey() != null && !filterParameter.getSearchKey().isEmpty()) {
            sql += " and (lower(r.name) like '%" + filterParameter.getSearchKey().toLowerCase() + "%'" +
                    " or lower(r.code) like '%" + filterParameter.getSearchKey().toLowerCase() + "%' " +
                    " or lower(r.viewName) like '%" + filterParameter.getSearchKey().toLowerCase() + "%' " +
                    " or lower(t.name) like '%" + filterParameter.getSearchKey().toLowerCase() + "%' " +
                    " or lower(t.code) like '%" + filterParameter.getSearchKey().toLowerCase() + "%') ";
        }
        return ((Long) findSingle(sql)).intValue();
    }

    @Override
    public String getGenerateUpdateCommand(ListingFilterParameter filterParametrs) {
        if (filterParametrs == null || filterParametrs.getCompanyID() == null || filterParametrs.getObjectId() == null || filterParametrs.getParams() == null || "".equals(filterParametrs.getParams())) {
            return null;
        }
        return "UPDATE \"" + filterParametrs.getCompanyID() + "\".REPORTING SET " + filterParametrs.getParams() + " WHERE ID=" + filterParametrs.getObjectId();
    }

    @Override
    public EdsReport getByCode(String code) {
        String companyID = ServerSecurityContext.getInstance().getCompanyId();
        return (EdsReport) findNativeSingle("SELECT report.* FROM \"" + companyID + "\".reporting report where (report.deleted is null OR report.deleted is not true) AND report.code='" + code + "'", EdsReport.class);
    }

    @Override
    public ArrayList<EdsReport> getReportListByCompany(int schema) {
        return (ArrayList<EdsReport>) findNative("SELECT report.* FROM \"" + schema + "\".reporting report WHERE (report.deleted is null OR report.deleted is not true) ", EdsReport.class);
    }

    @Override
    public EdsReport getByCompany(Integer id, Integer companyID) {
        return (EdsReport) findNativeSingle("SELECT report.* FROM \"" + companyID + "\".reporting AS report where (report.deleted is null OR report.deleted is not true) AND report.id=" + id, EdsReport.class);
    }

    @Override
    public void deleteWithRelation(String reportingPermission, String reportCode, Integer reportId, Integer companyID) {
        Integer currentUserId = null;
        if (companyID != null && companyID.toString().equals(ServerSecurityContext.getInstance().getCompanyId())) {
            if (getUser() != null) {
                currentUserId = getUser().getObjectID();
            }
        }
        String permissionCode = reportingPermission != null ? reportingPermission : PermissionConstants.REPORTING_SAVED_REPORT + "_" + reportCode + "_" + companyID;
        updateNative(" UPDATE  \"" + companyID + "\".reporting SET code=code||'###'||id, deleted = true, modificationDate=now(),creationDate=coalesce(creationDate,now()), modifiedBy_id=" + currentUserId + " WHERE id=" + reportId + "; " +
                "delete from \"" + companyID + "\".rolepermission where permissioncode=E'" + permissionCode + "';\n " +
                "delete from \"" + companyID + "\".dashboard_components where component_id = (select id from \"" + companyID + "\".default_components where report_code=E'" + reportCode + "');\n " +
                "delete from \"" + companyID + "\".default_components where report_code=E'" + reportCode + "';\n " +
                "delete from \"" + companyID + "\".permission_context where permissioncode=E'" + permissionCode + "';\n " +
                "delete from \"" + companyID + "\".reportingpermission where code=E'" + permissionCode + "' and context='" + PermissionConstants.REPORTING + "'; ");
    }

    @Override
    public ArrayList<Integer> getEmployeeIDsByReportID(Integer reportID) {
        ArrayList<Integer> list = (ArrayList<Integer>) find("SELECT t.objectID FROM EdsReport report JOIN report.targetUsers t WHERE (report.deleted is null OR report.deleted <> true) AND report.objectID=?", reportID);
        ArrayList<Integer> emplyeeList = new ArrayList<>();
        int i = -1, n = list.size();
        while (++i < n) {
            emplyeeList.add(list.get(i));
        }
        return emplyeeList;
    }

    @Override
    public boolean makeTestingReportSchema(Integer mySchema) {
        BigInteger count = (BigInteger) findNativeSingle("select count(*) from information_schema.columns " +
                " where table_schema='" + mySchema + "' and lower(table_name)=lower('reporting') " +
                " and (lower(column_name)=lower('id') or lower(column_name)=lower('companyid') or lower(column_name)=lower('last_exception') or lower(column_name)=lower('issuccess'))");
        if (count != null) {
            return Integer.parseInt(count.toString()) < 3;
        }
        return false;
    }

    @Override
    public List<Object[]> getReportList(ListingFilterParameter filterParameter) {
        String sql = "select distinct r,t,f from EdsReport r join r.folderid f, EdsReportTemplate t where (t.code = r.viewCode or t is null) and (r.deleted =false or r.deleted is null) ";
        if (filterParameter.getSearchKey() != null && !filterParameter.getSearchKey().isEmpty()) {
            sql += " and (lower(r.name) like '%" + filterParameter.getSearchKey().toLowerCase() + "%'" +
                    " or lower(r.code) like '%" + filterParameter.getSearchKey().toLowerCase() + "%' " +
                    " or lower(r.viewName) like '%" + filterParameter.getSearchKey().toLowerCase() + "%' " +
                    " or lower(t.name) like '%" + filterParameter.getSearchKey().toLowerCase() + "%' " +
                    " or lower(t.code) like '%" + filterParameter.getSearchKey().toLowerCase() + "%') ";
        }
        if (Boolean.TRUE.equals(filterParameter.isSelected())) {
            sql += "  and r.synchronization is true ";
        }
        if (null != filterParameter.getSortField() && !filterParameter.getSortField().isEmpty()) {
            if (Arrays.asList("name", "fakeReport", "viewName", "synchronization").contains(filterParameter.getSortField())) {
                sql += " order by r." + filterParameter.getSortField();
            } else if ("folder".equals(filterParameter.getSortField())) {
                sql += " order by f.name";
            } else {
                sql += " order by t." + filterParameter.getSortField();
            }
        } else {
            sql += " order by r.id";
        }
        sql += filterParameter.isAscending() ? " ASC" : " DESC";
        return (List<Object[]>) findInterval(sql, filterParameter.getStart(), filterParameter.getLimit());
    }

    @Override
    public String setParametersNative(ReportingTestDTO testDTO, Integer companyid) {
        StringBuilder query = new StringBuilder("insert into reportingTest (modified_by, user_name, report_id, company_id, tested_date, report_name, module_name, is_success, last_exception, time_spent) ");
        query.append("values (").append("'").append(testDTO.getModifiedBy()).append("', '").append(testDTO.getUserName()).append("', " + testDTO.getReportID()).append(", " + testDTO.getCompanyID()).append(", '" + testDTO.getTestedDate()).append("', '" + testDTO.getReportName())
                .append("', '" + testDTO.getModuleName()).append("', " + testDTO.getSuccess()).append(", '" + testDTO.getLastException()).append("', " + testDTO.getTimeSpent()).append(")");
        return query.toString();
    }

    @Override
    public String setParametersNative(HashMap<String, String> map, Integer companyid) {
        String query = "update \"" + companyid + "\".reporting ";
        if (map != null && "true".equals(map.get("issuccess"))) {
            query += " set issuccess ='true'";
        } else {
            query += " set issuccess ='false', last_exception='" + map.get("last_exception") + "'";
        }
        query += " where id=" + map.get("id");
        return query;
    }

    @Override
    public EdsReport getReport(Integer exceltemplateid) {
        String companyID = ServerSecurityContext.getInstance().getCompanyId();
        return (EdsReport) findNativeSingle("SELECT t.* FROM \"" + companyID + "\".reporting t where (t.deleted is null OR t.deleted is not true) AND exceltemplateid='" + exceltemplateid + "'", EdsReport.class);
    }

    @Override
    public String changeColumnNamePatch(SavedReportTemplate item) {
        int n = 0;
        String columnName = switch (item.getType()) {
            case 0 -> "selectcolumns";
            case 1 -> "arraycolumns";
            case 2 -> "groupColumns";
            default -> "";
        };
        String replacement = "replace(E'#'||" + columnName + "||'#',E'#'||E'" + item.getFrom() + "'||'#','#" + item.getTo() + "#')";
        updateNative("update \"" + item.getCompanyID() + "\".reporting " +
                "set " + columnName + "=" +
                "substring(" +
                replacement +
                ",2,length(" +
                replacement +
                ")-2)"
                + " where viewCode='" + item.getViewCode() + "';");
        return n + "{" + item.getCompanyID() + "}";
    }

    @Override
    public ArrayList<EdsReport> findAll() {
        return (ArrayList<EdsReport>) find("SELECT report from EdsReport report WHERE (report.deleted is null OR report.deleted <> true)");
    }

}
