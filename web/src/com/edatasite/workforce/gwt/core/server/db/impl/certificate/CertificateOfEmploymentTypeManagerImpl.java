package com.edatasite.workforce.gwt.core.server.db.impl.certificate;

import com.edatasite.workforce.core.domain.certificate.EdsCertificateOfEmploymentType;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.certificate.CertificateOfEmploymentTypeManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.CertificateItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Khasan on 30.09.14.
 */
@Repository("certificateOfEmploymentTypeManager")
public class CertificateOfEmploymentTypeManagerImpl extends BaseManager<EdsCertificateOfEmploymentType> implements CertificateOfEmploymentTypeManager {

    public CertificateOfEmploymentTypeManagerImpl() {
        super(EdsCertificateOfEmploymentType.class);
    }

    @Override
    public List<EdsCertificateOfEmploymentType> getCertificateTypeList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("select cerType from EdsCertificateOfEmploymentType cerType ");
        sql.append("LEFT JOIN cerType.createrBy employee ");
        sql.append("WHERE cerType.deleted is false ");

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (lower(cerType.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(cerType.description) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(employee.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(employee.lastName) like '").append(fp.getSqlSearchKey()).append("') ");

        }
        sql.append("order by ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (CertificateItem.NAME.equals(fp.getSortField())) {
                sql.append("cerType.name");
            } else if (CertificateItem.DESCRIPTION.equals(fp.getSortField())) {
                sql.append("cerType.description");
            } else if (CertificateItem.ISSUED_DATE.equals(fp.getSortField())) {
                sql.append("cerType.creationDate");
            } else if (CertificateItem.EMPLOYEE.equals(fp.getSortField())) {
                sql.append("employee.firstName");
            } else if (CertificateItem.CERTIFICATE_TYPE.equals(fp.getSortField())) {
                sql.append(" cerType.type.description ");
            } else {
                sql.append(" cerType.creationDate ");
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sql.append(" asc");
                } else {
                    sql.append(" desc");
                }
            } else {
                sql.append(" desc");
            }
        } else {
            sql.append(" cerType.creationDate desc");
        }
        int limit = 20;
        if (fp.getLimit() != null) {
            limit = fp.getLimit();
        }
        return findInterval(sql.toString(), fp.getStart(), limit);
    }

    @Override
    public Integer getCertificateTypeTotalCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(cerType.objectID) FROM EdsCertificateOfEmploymentType cerType ");
        sql.append("LEFT JOIN cerType.createrBy employee ");
        sql.append("WHERE cerType.deleted is false ");
        if (fp.getSqlSearchKey() != null && !fp.getSqlSearchKey().isEmpty()) {
            sql.append(" AND (lower(cerType.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(employee.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(employee.lastName) like '").append(fp.getSqlSearchKey()).append("') ");
        }
        Long count = (Long) findSingle(sql.toString());
        return count != null ? count.intValue() : 0;
    }

    @Override
    public EdsCertificateOfEmploymentType getByName(String name) {
        return (EdsCertificateOfEmploymentType) findSingle("select ct from EdsCertificateOfEmploymentType ct where ct.deleted is not true and lower(ct.name) = '" + name.toLowerCase() + "'");
    }
}
