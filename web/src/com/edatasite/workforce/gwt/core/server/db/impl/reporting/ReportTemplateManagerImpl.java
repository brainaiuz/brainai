package com.edatasite.workforce.gwt.core.server.db.impl.reporting;

import com.edatasite.workforce.core.domain.reporting.EdsReportTemplate;
import com.edatasite.workforce.gwt.core.client.rpc.ReportingListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.wfp.ReportTemplateManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 20.10.11
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */

@Repository("reportTemplateManager")
public class ReportTemplateManagerImpl extends BaseManager<EdsReportTemplate> implements ReportTemplateManager, PermissionConstants {

    public ReportTemplateManagerImpl() {
        super(EdsReportTemplate.class);
    }

    public ArrayList<EdsReportTemplate> getReportTemplateList(String categoryFromFilter, String roles, Integer companyID) {
        StringBuilder buffer = new StringBuilder(100);
        buffer.append(" select distinct t.* " +
                " from " + getPublic() + ".reporttemplate t " +
                " join \"" + companyID + "\".reportingpermission p on ('" + REPORTING_TEMPLATE + "_'||t.code=p.code or '" + REPORTING_TEMPLATE + "_'||t.code||'_'||" + companyID + "=p.code ) " +
                " join \"" + companyID + "\".rolepermission rp on rp.permissioncode=p.code and rp.roleCode in (" + roles + ") and rp.access='ALLOW' ");
        if (categoryFromFilter != null && !"".equals(categoryFromFilter)) {
            buffer.append(" AND t.categoryCode = '").append(categoryFromFilter + "' ");
        }
        buffer.append(" and t.isLibrary is not true ");
        buffer.append(" order by t.name asc");
        return (ArrayList<EdsReportTemplate>) findNative(buffer.toString(), EdsReportTemplate.class);
    }

    public ArrayList<EdsReportTemplate> getReportTemplateList(Boolean isCustom) {
        if (isCustom == null) {
            return (ArrayList<EdsReportTemplate>) find("FROM EdsReportTemplate t");
        } else {
            return (ArrayList<EdsReportTemplate>) find("FROM EdsReportTemplate t where t.isCustom =?", isCustom);
        }
    }

    public ArrayList<EdsReportTemplate> getReportTemplateList(Boolean isCustom, ListingFilterParameter filterParameter) {
        if (isCustom == null) {
            return (ArrayList<EdsReportTemplate>) find("FROM EdsReportTemplate t");
        } else {
            return (ArrayList<EdsReportTemplate>) findNative("SELECT t.* FROM reportTemplate t where t.code not ilike '%fake%' and t.isCustom is " + isCustom + " order by t.categoryCode, t.name limit " + filterParameter.getLimit() + " offset " + filterParameter.getStart(), EdsReportTemplate.class);
        }
    }

    public ArrayList<Integer> getReportTemplateIds(Boolean isCustom) {
        if (isCustom != null) {
            return (ArrayList<Integer>) findNative("select t.id FROM ReportTemplate t where t.code not ilike '%fake%' and  t.isCustom =?", isCustom);
        } else {
            return null;
        }
    }

    public ListResult<ReportingListItem> getReportingXMLTemplateList(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder("select DISTINCT rt.id, rt.name, rt.body, case when coalesce(rt.isCustom, true) then 'Custom' else 'Default' end as type, ")
                .append(" coalesce(rt.isLibrary,false) islibrary, coalesce(coalesce(rt.order_number, ord.order_number),0) order_number, coalesce( rt.isCustom,false) isCustom ")
                .append(" from ").append(getPublic()).append(".reporttemplate rt left join reportTemplateCategory rtc on rtc.code=rt.categorycode " +
                        ((filterParameter.getCompanyID() != null) ? ("left join \"" + filterParameter.getCompanyID() + "\".reportingpermission p on p.context='REPORTING' and (p.code='" + REPORTING_TEMPLATE + "_'||rt.code or p.code='" + REPORTING_TEMPLATE + "_'||rt.code||'_'||" + filterParameter.getCompanyID() + " ) join \"" + filterParameter.getCompanyID() + "\".rolepermission rp on rp.permissioncode=p.code and rp.access='ALLOW'") : "") +
                        " left join (select rtc2.id,coalesce(max(order_number)+1,0) order_number from ").append(getPublic()).append(".reporttemplate r2 inner join ").append(getPublic()).append(".reporttemplatecategory rtc2 on r2.categorycode=rtc2.code group by rtc2.id) ord on rtc.id=ord.id ");
        if (filterParameter.getLibrary() == null || !filterParameter.getLibrary()) {
            sql.append(" and islibrary is not true ");
        }
        if (filterParameter.getSearchKey() != null && !"".equals(filterParameter.getSearchKey())) {
            if (sql.toString().contains("where")) {
                sql.append(" and lower(rt.name) like '%").append(filterParameter.getSearchKey().toLowerCase()).append("%' ");
            } else {
                sql.append(" where lower(rt.name) like '%").append(filterParameter.getSearchKey().toLowerCase()).append("%' ");
            }
        }
        //sql.append("group by type, rt.id, rt.name, rt.body, coalesce(rt.isLibrary,false),coalesce(rt.order_number, ord.order_number) ");
        sql.append("order by ");
        if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
            if ("templateName".equals(filterParameter.getSortField())) {
                sql.append("rt.name ").append(filterParameter.isAscending() ? "asc " : "desc ");
            } else if ("templateType".equals(filterParameter.getSortField())) {
                sql.append("type ").append(filterParameter.isAscending() ? "asc " : "desc ");
            } else if ("order".equals(filterParameter.getSortField())) {
                sql.append(" coalesce(coalesce(rt.order_number, ord.order_number),0) ").append(filterParameter.isAscending() ? "asc " : "desc ");
            } else {
                sql.append(" id ").append(filterParameter.isAscending() ? "asc " : "desc ");
            }
        } else {
            sql.append("type desc, coalesce(coalesce(rt.order_number, ord.order_number),0) asc, rt.id desc ");
        }
        List<Object> list = (List<Object>) findNative(sql.toString());
        int totalCount = list != null ? list.size() : 0;
        ArrayList<ReportingListItem> result = new ArrayList<>();
        List<Object[]> templateList = findNativeLimited(sql + "offset " + filterParameter.getStart(), filterParameter.getLimit());
        if (templateList != null) {
            for (Object[] object : templateList) {
                ReportingListItem item = new ReportingListItem((Integer) object[0], (String) object[1], (String) object[2], (String) object[3]);
                item.setLibrary((Boolean) object[4]);
                item.setOrder(Integer.valueOf(String.valueOf(object[5])));
                item.setCustom(Boolean.valueOf(String.valueOf(object[6])));
                result.add(item);
            }
        }
        return new ListResult<>(result, totalCount);
    }

    @Override
    public void create(EdsReportTemplate edsReportTemplate) {
        if (edsReportTemplate != null && edsReportTemplate.getObjectID() == null && (edsReportTemplate.getCode() == null || "".equals(edsReportTemplate.getCode()))) {
            edsReportTemplate.setCode();
        }
        super.create(edsReportTemplate);
    }

    @Override
    public EdsReportTemplate getByCode(String viewCode) {
        if (viewCode == null || viewCode.isEmpty()) {
            return null;
        }
        return (EdsReportTemplate) findSingle("select t from EdsReportTemplate t where t.code=?", viewCode);
    }

    @Override
    public Integer getIdByCode(String viewCode) {
        if (viewCode == null || viewCode.isEmpty()) {
            return null;
        }
        return (Integer) findSingle("select t.id from EdsReportTemplate t where t.code=?", viewCode);
    }
    @Override
    public void updateTemplate(String code, EdsReportTemplate changeTemplate) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("update reporttemplate set name=E'").append(changeTemplate.getName().replace("'", "''")).append("', ");
        sqlQuery.append(" categoryCode=").append(changeTemplate.getCategoryCode()).append(", ");
        sqlQuery.append(" body=E'").append(changeTemplate.getBody().replace("'", "''")).append("', ");
        sqlQuery.append(" isCustom=").append(changeTemplate.getCustom()).append(", ");
        sqlQuery.append(" isLibrary=").append(changeTemplate.getLibrary()).append(", ");
        if (changeTemplate.getAuditInfo() != null) {
            if (changeTemplate.getAuditInfo().getCreationDate() != null)
                sqlQuery.append(" creationDate='").append(changeTemplate.getAuditInfo().getCreationDate()).append("', ");
            if (changeTemplate.getAuditInfo().getModificationDate() != null)
                sqlQuery.append(" modificationDate='").append(changeTemplate.getAuditInfo().getModificationDate()).append("', ");
        }
        sqlQuery.append(" order_number=").append(changeTemplate.getOrder_number()).append(" ");
        sqlQuery.append(" where code=E'").append(code).append("'");
        if (changeTemplate.getAuditInfo() != null && changeTemplate.getAuditInfo().getModificationDate() != null) {
            sqlQuery.append(" and (modificationDate is null OR modificationDate < '").append(changeTemplate.getAuditInfo().getModificationDate()).append("')");
        }
        updateNative(sqlQuery.toString());
    }
    @Override
    public void insertTemplate(String code, EdsReportTemplate changeTemplate) {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append(" INSERT INTO reportTemplate(name,categoryCode,body,isCustom,isLibrary,code,order_number) ");
        sqlQuery.append(" select E'").append(changeTemplate.getName().replace("'", "''")).append("',E'").append(changeTemplate.getCategoryCode()).append("',E'").append(changeTemplate.getBody().replace("'", "''")).append("',").append(changeTemplate.getCustom()).append(",").append(changeTemplate.getLibrary()).append(",'").append(changeTemplate.getCode().replace("'", "''")).append("',").append(changeTemplate.getOrder_number()).append("; ");

        updateNative(sqlQuery.toString());
    }

    public ArrayList<Integer> getReportTemplateIdsForBackup() {
        return (ArrayList<Integer>) findNative("select distinct t.id  from reporttemplate t where t.useInBackup is true ");
    }

}
