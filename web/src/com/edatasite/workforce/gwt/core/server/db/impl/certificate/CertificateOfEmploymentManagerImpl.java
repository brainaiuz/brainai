package com.edatasite.workforce.gwt.core.server.db.impl.certificate;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmployment;
import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmploymentType;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Khasan on 11.09.14.
 */
@Repository("certificateOfEmploymentManager")
public class CertificateOfEmploymentManagerImpl extends BaseManager<EdsCertificateOfEmployment> implements CertificateOfEmploymentManager {
    public CertificateOfEmploymentManagerImpl() {
        super(EdsCertificateOfEmployment.class);
    }

    @Autowired
    EmployeeManager employeeManager;

    @Override
    public NumberData getCertificateNumber() {
        Integer intNumber = (Integer) findSingle("select cert.intNumber from EdsCertificateOfEmployment cert order by cert.objectID desc");

        NumberData numberData = new NumberData();
        numberData.setIntNumber(intNumber != null ? intNumber + 1 : 1);
        numberData.setNumberString("CERT");
        numberData.setNumberFormat("CERT_0001");
        return numberData;
    }

    @Override
    public List<EdsCertificateOfEmploymentType> getCertificateTypes() {
        return find("select ct from EdsCertificateOfEmploymentType ct where ct.deleted = false order by ct.objectID");
    }

    @Override
    public List<EdsCertificateOfEmploymentType> getCertificateTypesWithPermission() {
        String roleCodes = getUser().getRolesCodeAsString();
        StringBuilder sql = new StringBuilder();
        sql.append("select ct.* from ").append(getCompanyId()).append(".certificateofemploymenttype ct");
        if (roleCodes.contains(Constants.ADMIN_CODE)) {
            sql.append(" inner join ").append(getPublic()).append(".permission p on ('CERTIFICATE_OF_EMPLOYMENT_' || (replace(ct.formid, '_FORM', '')) || '_ADD_").append(getCompanyId().replace("\"", "")).append("' = p.code) ");
            sql.append(" WHERE (p.companyId is null or p.companyId = ").append(getCompanyId().replace("\"", "")).append(") ");
        } else {
            sql.append(" inner join ").append(getCompanyId()).append(".rolepermission rp on ('CERTIFICATE_OF_EMPLOYMENT_' || (replace(ct.formid, '_FORM', '')) || '_ADD_").append(getCompanyId().replace("\"", "")).append("' = rp.permissioncode) ");
            sql.append(" WHERE access = 'ALLOW' and rp.rolecode in (").append(roleCodes).append(")");
        }
        sql.append(" and ct.deleted = false group by ct.id order by ct.name");
        return findNative(sql.toString(), EdsCertificateOfEmploymentType.class);
    }

    @Override
    public EdsCertificateOfEmploymentType getCertificateType(Integer certificateTypeID) {
        return (EdsCertificateOfEmploymentType) findSingle("select ct from EdsCertificateOfEmploymentType ct where ct.objectID=?", certificateTypeID);
    }

    @Override
    public List<EdsCertificateOfEmployment> getCertificateList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        boolean isEmployeeCodeInteger = employeeManager.isIntegerEmployeeCodeEnabled();
        String companyID = getCompanyId();
        String orderCode = "";
        if (isEmployeeCodeInteger && fp.getSearchKey() != null) {
            orderCode = (String) findNativeSingle("select ep.employeeCode from " + getCompanyId() + ".employee e \n" +
                    "left join " + getCompanyId() + ".myuser mu on mu.id=e.id\n" +
                    "left join " + getCompanyId() + ".employeeprofile ep on e.profileid=ep.id \n" +
                    "where mu.deleted = false and regexp_replace(ep.employeeCode, '[a-z]', '', 'gi')='" + fp.getSearchKey() + "'");
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select ctf.* ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField()) && CertificateItem.ISSUED_BY.equals(fp.getSortField())) {
            sql.append(" , (select mu1.firstname from ").append(companyID).append(".myuser mu1 where mu1.id = ctf.creatirid) issuedby ");
        }
        sql.append(" from ").append(companyID).append(".certificateofemployment ctf ");

        sql.append("left join ").append(companyID).append(".certificateofemploymenttype ctType on ctf.certificatetypeid=ctType.id ");
        sql.append("left join ").append(companyID).append(".employee e on ctf.employeeid=e.id ");
        sql.append("left join ").append(companyID).append(".myuser u on u.id=e.id ");
        if (isEmployeeCodeInteger) {
            sql.append("left join (select id, contact_id, cast(NULLIF(regexp_replace(employeeCode, '[[:alpha:]]', '', 'gi'), '') as int) employeeCode from ").append(companyID).append(".employeeprofile) pr on (pr.id = e.profileid) ");
        } else {
            sql.append("left join ").append(companyID).append(".employeeprofile pr on (pr.id = e.profileid) ");
        }
        sql.append("WHERE ctf.deleted is false ");

        if (fp.getType() != null && !fp.getType().equals(0)) {
            sql.append(" and ctType.id=" + fp.getType());
        }
        EdsUser user = getUser();
        if (!ServerUtils.hasPermission(PermissionConstants.CETIFICATE_OF_EMPLOYMENT_SEE_ALL_CERTIFICATE_LIST) && user != null) {
            sql.append(" and e.id=" + user.getObjectID());
        } else if (fp.getEmployeeId() != null) {
            sql.append(" and e.id= " + fp.getEmployeeId());
        }
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            if (isEmployeeCodeInteger && orderCode != null && !"".equals(orderCode)) {
                sql.append(" AND ( to_char(pr.employeeCode, '999999') like '%").append(fp.getSqlSearchKey()).append("') ");
            } else {
                sql.append(" AND (lower(ctf.number) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append(" OR lower(ctType.name) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append(" OR lower(u.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
                sql.append(" OR lower(u.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
                if (isEmployeeCodeInteger) {
                    sql.append("OR to_char(pr.employeeCode, '999999') like '%").append(fp.getSqlSearchKey()).append("') ");
                } else {
                    sql.append("OR lower(pr.employeeCode) like '%").append(fp.getSqlSearchKey()).append("') ");
                }
            }
        }
        String sortOrder = fp.isAscending() ? "" : " desc ";
        sql.append("ORDER BY ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (CertificateItem.NUMBER.equals(fp.getSortField())) {
                sql.append("ctf.number");
            } else if (CertificateItem.ISSUED_DATE.equals(fp.getSortField())) {
                sql.append("ctf.creationDate");
            } else if (CertificateItem.EMPLOYEE.equals(fp.getSortField())) {
                sql.append("u.firstName");
            } else if (CertificateItem.EMPLOYEE_CODE.equals(fp.getSortField())) {
                sql.append("pr.employeeCode");
            } else if (CertificateItem.CERTIFICATE_TYPE.equals(fp.getSortField())) {
                sql.append("ctType.name");
            } else if (CertificateItem.ISSUED_BY.equals(fp.getSortField())) {
                sql.append("issuedby");
            } else {
                sql.append(" ctf.creationDate ");
            }
            sql.append(sortOrder);
        } else {
            if (!"".equals(orderCode)) {
                sql.append(" pr.employeeCode asc ");
            } else {
                sql.append(" ctf.creationDate desc");
            }
        }
        return findNative(sql.toString(), EdsCertificateOfEmployment.class);
    }

    @Override
    public boolean isCertificateForm(String formId) {
        return !find("select ct.objectID from EdsCertificateOfEmploymentType ct where ct.formID=?", formId).isEmpty();
    }

    @Override
    public List<EdsCertificateOfEmployment> getCertificatesForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select c from EdsCertificateOfEmployment c ");
        sql.append(" where  ").append(ServerUtils.checkForDeleted("c.deleted"));
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            params.put("modifiedDate", solrReindex.getLastUpdateTime());
            sql.append(" and c.lastUpdateTime >= :modifiedDate");
            if (solrReindex.getLastUpdateEndTime() != null) {
                sql.append(" and c.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        sql.append(" order by c.objectID ");

        return findIntervalByNamedParams(sql.toString(), start, limit, params);
    }

    public List<Integer> getCertificateIdsByIds(String ids) {
        return find("SELECT c.objectID FROM EdsCertificateOfEmployment c WHERE c.objectID IN(" + ids + ") and " + ServerUtils.checkForDeleted("c.deleted"));
    }

    public List<Integer> getCertificateIdsWithLimit(Integer start, Integer limit) {
        return findInterval("select c.objectID from EdsCertificateOfEmployment c where " + ServerUtils.checkForDeleted("c.deleted"), start, limit);
    }

    @Override
    public List<EdsCertificateOfEmployment> getCertificatesByIds(String Ids) {
        return findNative("select * from " + getCompanyId() + ".certificateofemployment where " + ServerUtils.checkForDeleted("deleted") + " and id in(" + Ids + ")", EdsCertificateOfEmployment.class);
    }

    public List<Integer> getCompanyDeletedCertificatesForSolr(SolrReindexRpc solrReindex) {
        StringBuilder newsSqlQuery = new StringBuilder("SELECT ns.objectID FROM EdsCertificateOfEmployment ns WHERE ns.deleted=true");
        newsSqlQuery.append(" AND ns.lastUpdateTime>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        if (solrReindex.getLastUpdateEndTime() != null) {
            newsSqlQuery.append(" and ns.lastUpdateTime<='").append(solrReindex.getLastUpdateEndTime()).append("'");
        }
        return (List<Integer>) find(newsSqlQuery.toString());
    }
}
