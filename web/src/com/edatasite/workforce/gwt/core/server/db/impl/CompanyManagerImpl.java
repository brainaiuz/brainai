package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.tools.EdsSchemaUpdater;
import com.edatasite.workforce.gwt.backend.client.rpc.SchemaListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.app.WfmJpaTemplate;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.*;

@Repository("companyManager")
public class CompanyManagerImpl extends BaseManager<EdsCompany> implements CompanyManager {

    public CompanyManagerImpl() {
        super(EdsCompany.class);
    }

    public EdsCompany getCompany(Integer objectID) {
        return (EdsCompany) findSingle("select c from EdsCompany c where c.objectID=?", objectID);
    }

    public List<EdsCompany> getCompanies() {
        return find("select company from EdsCompany company where (company.active = true or company.active is null) order by company.registrationDate desc");
    }

    public ArrayList<String> getXmlBackupEnableCompanies() {
        return (ArrayList<String>) find("select c.objectID||'' from EdsCompany c where (c.active = true or c.active is null) and c.companySettings.enableXmlBuckup=true order by c.registrationDate desc");
    }

    public List<EdsCompany> getOccupiedCompanies() {
        return find("select company from EdsCompany company where (company.active = true or company.active is null) and company.isFree is false order by company.registrationDate desc");
    }

    public List<EdsCompany> getCompaniesByRegDate(Date startTime, Date endTime) {
        return find("FROM EdsCompany c WHERE (c.registrationDate between '" + startTime + "' and '" + endTime + "') and c.objectID <> 1");
    }

    public List<EdsCompany> getCompaniesUsedSystemByDate(Date startTime, Date endTime) {
        return find("from EdsCompany c where c.objectID <> 1 and c.lastUpdateTime between '" + startTime + "' and '" + endTime + "'");
    }

    public List<Date> getSignupCompaniesByDate(Date startDate, Date endDate) {
        return find("select c.registrationDate from EdsCompany c where" +
                " c.registrationDate >=? and c.registrationDate <=? and (c.testCompany = false or c.testCompany is null) ", startDate, endDate);
    }

    @Override
    public List<Object[]> getSchemaList(ListingFilterParameter fp) {
        String facetFilter = "";
        FacetFilterRpc schemaFacetFilter = fp.getFacetFilter();
        if (schemaFacetFilter != null && schemaFacetFilter.getFacetContentMap().size() != 0) {
            String schemaKey = FacetContentType.SchemaFacetFilter.getContentCode()[0];
            if (schemaFacetFilter.getFacetContentMap().get(schemaKey).getFacetItems().length != 0) {
                SelectItem[] items = schemaFacetFilter.getFacetContentMap().get(schemaKey).getFacetItems();
                facetFilter = " AND (";
                boolean appendOperator = false;
                StringBuilder facetFilterBuilder = new StringBuilder(facetFilter);
                for (SelectItem item : items) {
                    if (appendOperator) {
                        facetFilterBuilder.append(" OR ");
                    } else {
                        appendOperator = true;
                    }
                    facetFilterBuilder.append(" c.isFree = ").append(item.getName());
                }
                facetFilter = facetFilterBuilder.toString();
                facetFilter = facetFilter + ") ";
            }
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT CAST (ns.nspname AS int4) as id, c.name as name FROM pg_namespace ns " +
                "LEFT JOIN " + getPublic() + ".company c ON c.id=CAST (ns.nspname AS int4) " +
                "WHERE ns.nspname ~ '^[0-9]+$' ");
        if (fp.getSqlSearchKey() != null) {
            sql.append(" AND (lower(c.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(''||c.id||'') like '").append(fp.getSqlSearchKey()).append("') ");
        }
        sql.append(facetFilter);
        if (fp.isShowActive()) {
            sql.append(" AND c.isFree=false ");
        }
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            String sortedField = "id";
            if (SchemaListItem.COMPANY_NAME.equals(fp.getSortField())) {
                sortedField = "c.name";
            }
            sql.append(" ORDER BY " + sortedField + (fp.isAscending() ? " ASC" : " DESC"));
        } else {
            sql.append(" ORDER BY CAST (ns.nspname AS int4) DESC, c.registrationDate DESC");
        }
        return (List<Object[]>) findNative(sql.toString());
    }

    public ListingObjectItem<EdsCompany> getNonTestCompanies(ListingFilterParameter fp, List<String> existingCompanyList) {

        String facetFilter = "";
        FacetFilterRpc schemaFacetFilter = fp.getFacetFilter();
        if (schemaFacetFilter != null && schemaFacetFilter.getFacetContentMap().size() != 0) {
            String schemaKey = FacetContentType.SchemaFacetFilter.getContentCode()[0];
            if (schemaFacetFilter.getFacetContentMap().get(schemaKey).getFacetItems().length != 0) {
                SelectItem[] items = schemaFacetFilter.getFacetContentMap().get(schemaKey).getFacetItems();
                facetFilter = " AND (";
                boolean appendOperator = false;
                StringBuilder facetFilterBuilder = new StringBuilder(facetFilter);
                for (SelectItem item : items) {
                    if (appendOperator) {
                        facetFilterBuilder.append(" OR ");
                    } else {
                        appendOperator = true;
                    }
                    facetFilterBuilder.append(" company.isFree = ").append(item.getName());
                }
                facetFilter = facetFilterBuilder.toString();
                facetFilter = facetFilter + ") ";
            }
        }
        StringBuilder sql = new StringBuilder();
        sql.append("FROM EdsCompany co, EdsCompanySystemSettings css  ");

        sql.append("WHERE co.objectID = css.company.objectID AND co.deleted = false ");
        if (existingCompanyList != null && existingCompanyList.size() > 0) {
            sql.append(" and co.objectID in (").append(ServerUtils.getAsCommoDelimited(existingCompanyList, "0", ",")).append(")");
        }
        if (fp.getParams() != null && !"".equals(fp.getParams())) {
            if (!"All".equals(fp.getParams())) {
                sql.append(" and css.host in (").append(Utils.getStringEachValueWithParentheses(fp.getParams())).append(") ");
            }
        }
        if (fp.getAccountCode() != null && !"".equals(fp.getAccountCode())) {
            sql.append(" and css.companySignedUpFrom='").append(fp.getAccountCode()).append("' "); // with promotional code
        }
        if (fp.getSqlSearchKey() != null) {
            sql.append(" AND (lower(co.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(''||co.objectID||'') like '").append(fp.getSqlSearchKey()).append("') ");
        }
        if (fp.isShowActive()) {
            facetFilter = facetFilter + " AND co.isFree=false ";
        } else {
            sql.append(" AND (co.testCompany = false OR co.testCompany is null) ");
        }
        sql.append(facetFilter);
        Long totalCount = (Long) findSingle("SELECT count(co.objectID)" + sql);
        sql.append(" ORDER BY co.registrationDate DESC ");

        List<EdsCompany> result = findInterval("SELECT co " + sql,fp.getStart(),fp.getLimit());

        return new ListingObjectItem<>(result, totalCount);
    }

    public List<Object[]> getCompanySchemas(ListingFilterParameter fp) {
        String keyword = fp.getSqlSearchKey();
        String facetFilter = "";
        FacetFilterRpc schemaFacetFilter = fp.getFacetFilter();
        if (schemaFacetFilter != null && schemaFacetFilter.getFacetContentMap().size() != 0) {
            String schemaKey = FacetContentType.SchemaFacetFilter.getContentCode()[0];
            if (schemaFacetFilter.getFacetContentMap().get(schemaKey).getFacetItems().length != 0) {
                SelectItem[] items = schemaFacetFilter.getFacetContentMap().get(schemaKey).getFacetItems();
                facetFilter = " AND (";
                boolean appendOperator = false;
                StringBuilder facetFilterBuilder = new StringBuilder(facetFilter);
                for (SelectItem item : items) {
                    if (appendOperator) {
                        facetFilterBuilder.append(" OR ");
                    } else {
                        appendOperator = true;
                    }
                    facetFilterBuilder.append(" c.isFree = ").append(item.getName());
                }
                facetFilter = facetFilterBuilder.toString();
                facetFilter = facetFilter + ") ";
            }
        }
        String hostS = "";
        if (fp.getParams() != null && !"".equals(fp.getParams())) {
            if (!"All".equals(fp.getParams())) {
                hostS += " and css.host in (" + Utils.getStringEachValueWithParentheses(fp.getParams()) + ")";
            }
        }
        if (fp.getAccountCode() != null && !"".equals(fp.getAccountCode())) {
            hostS += " and c.promocode='" + fp.getAccountCode() + "' "; // with promotional code
        }
        if (keyword != null && !keyword.equals("")) {

            return (List<Object[]>) findNative("SELECT ns.nspname as id, c.name as name FROM pg_namespace ns " +
                    " INNER JOIN " + getPublic() + ".company c ON c.id=CAST (ns.nspname AS int4) " +
                    " INNER JOIN " + getPublic() + ".companySystemSettings css ON css.companyid= c.id " +
                    " WHERE ns.nspname ~ '^[0-9]+$' AND c.isdeleted = false AND " +
                    " ( lower(c.name) like '" + keyword + "' OR ns.nspname like '" + keyword + "' ) " + facetFilter + hostS +
                    " ORDER BY ns.nspname, c.registrationDate DESC");
        } else {
            return (List<Object[]>) findNative("SELECT ns.nspname as id, c.name as name FROM pg_namespace ns  " +
                    " INNER JOIN " + getPublic() + ".company c ON c.id=CAST (ns.nspname AS int4) " +
                    " INNER JOIN " + getPublic() + ".companySystemSettings css ON css.companyid= c.id " +
                    " WHERE ns.nspname ~ '^[0-9]+$' AND c.isdeleted = false " +
                    facetFilter + hostS +
                    " ORDER BY ns.nspname, c.registrationDate DESC");
        }
    }

    public List<String> getExistingSchemas() {
        return findNative("SELECT nspname FROM pg_namespace WHERE nspname ~ '^[0-9]+$' ORDER BY nspname");
    }

    public List<String> getExistingSchemasWithTemplate() {
        return findNative("SELECT ns.nspname FROM pg_namespace ns WHERE nspname ~ '^[0-9]+$' " +
                           "or ns.nspname like 'template_%'" +
                           "or ns.nspname = '0_template'" +
                           "ORDER BY nspname");
    }

    public boolean schemaExists(String schemaName) {
        return findNativeSingle("SELECT nspname FROM pg_namespace WHERE nspname= ? ORDER BY nspname", schemaName) != null;

    }

    @Override
    public Integer getMaxSchemaId() {
        List<String> existingSchemas = getExistingSchemas();
        Integer maxId = 0;
        for (String name : existingSchemas) {
            Integer id = maxId;
            try {
                id = Integer.valueOf(name);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (maxId < id) {
                maxId = id;
            }
        }
        return maxId;
    }

    public List<EdsCompany> getCompaniesByIDs(String objectIDs) {
        return find("select company from EdsCompany company where company.objectID in (" + objectIDs + ")");
    }

    @Override
    public List<Integer> getCompaniesId() {
        return find("select company.objectID from EdsCompany company where (company.active = true or company.active is null) order by company.registrationDate desc");
    }

    public List<Integer> getCompaniesIdsList(Boolean isPaid) {
        if (isPaid != null) {
            return findNative("select company.id from " + getPublic() + ".company company " +
                    "join " + getPublic() + ".usageplan up on up.company_id = company.id " +
                    "where (company.active = true or company.active is null) and company.isdeleted is not true and company.testCompany is not true " +
                    "and up.paymentStatus = 259 and up.isPaid = " + isPaid + " order by company.registrationDate desc");
        } else {
            return findNative("select company.id from " + getPublic() + ".company company " +
                    "join " + getPublic() + ".usageplan up on up.company_id = company.id " +
                    "where (company.active = true or company.active is null) and company.isdeleted is not true and company.testCompany is not true " +
                    "and up.paymentStatus = 259 order by company.registrationDate desc");
        }
    }

    public Integer getCurrentFreeSchemasCount() {
        return ((BigInteger) this.findNativeSingle("select count(*) from " + getPublic() + ".pg_namespace where nspname ~ '^[0-9]' and cast(nspname as integer) > (select max(id) from company)")).intValue();
    }

    public List<Integer> getReallyExistingCompanyIds() {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<String> schemaList = new ArrayList<String>();
        try {
            EdsSchemaUpdater.setDbUrl(WfmJpaTemplate.getDataBaseURL());
            schemaList = EdsSchemaUpdater.getSchemaList();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        map.put("schemalist", schemaList);
        return findByNamedParams("select c.objectID from EdsCompany c where (c.active = true or c.active is null) and cast(c.objectID as string) in (:schemalist) order by c.registrationDate desc", map);
    }

    @Override
    @Transactional
    public void updateCompanyActive(Boolean active, Integer companyId) {
        String sql = "update " + getPublic() + ".company set active = " + active + " where id = " + companyId;
        updateNative(sql);
    }
    @Override
    @Transactional
    public void updateTestCompany(Integer id) {
        update("update EdsCompany set testCompany = true where id = ?", id);
    }

}
