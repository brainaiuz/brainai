package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsCertificate;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.CertificateManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.certificate.CertificateData;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/17/12
 * Time: 7:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("certificateManager")
public class CertificateManagerImpl extends BaseManager<EdsCertificate> implements CertificateManager {
    public CertificateManagerImpl() {
        super(EdsCertificate.class);
    }

    /*@Override
    public List<EdsCertificate> getCertificateList(ListingFilterParameter filterParameter) {
        return find("select cert from EdsCertificate cert order by id desc");
    }*/
    public List<EdsCertificate> getCertificateList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("select ctf from EdsCertificate ctf ");
        sql.append("LEFT JOIN ctf.certificateType ctType ");
        sql.append("LEFT JOIN ctf.student student ");
        sql.append("LEFT JOIN student.contact contact ");
        sql.append("WHERE 1=1 ");

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (lower(ctf.number) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(ctType.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(contact.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(contact.lastName) like '").append(fp.getSqlSearchKey()).append("') ");

        }
        sql.append("order by ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (CertificateData.NUMBER.equals(fp.getSortField())) {
                sql.append("ctf.number");
            } else if (CertificateData.CREATION_DATE.equals(fp.getSortField())) {
                sql.append("ctf.creationDate");
            } else if (CertificateData.STUDENT.equals(fp.getSortField())) {
                sql.append("contact.firstName");
            } else if (CertificateData.CERTIFICATE_TYPE.equals(fp.getSortField())) {
                sql.append("ctType.name");
            } else {
                sql.append(" ctf.creationDate");
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
            sql.append(" ctf.creationDate desc");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getCertificateTotalCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(ctf.objectID) FROM EdsCertificate ctf ");
        sql.append("LEFT JOIN ctf.certificateType ctType ");
        sql.append("LEFT JOIN ctf.student student ");
        sql.append("LEFT JOIN student.contact contact ");
        sql.append(" WHERE 1=1 ");
        if (fp.getSqlSearchKey() != null && !fp.getSqlSearchKey().isEmpty()) {
            sql.append(" AND (lower(ctf.number) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(ctType.name) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(contact.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(contact.lastName) like '").append(fp.getSqlSearchKey()).append("') ");
        }
        Long count = (Long) findSingle(sql.toString());
        return count != null ? count.intValue() : 0;
    }


    @Override
    public NumberData generateNumberData(Integer certificateTypeID) {
        Integer intNumber = (Integer) findSingle("select cert.intNumber from EdsCertificate cert where cert.certificateType.objectID = ? order by cert.objectID desc", certificateTypeID);

        NumberData numberData = new NumberData();
        numberData.setIntNumber(intNumber != null ? intNumber + 1 : 1);
        numberData.setNumberString("");
        numberData.setNumberFormat("_0001");
        return numberData;
    }

    @Override
    public void deleteCertificateItems(Integer certificateID) {
        update("delete from EdsCertificateItem where certificate.objectID = ?", certificateID);
    }

    @Override
    public void deleteCertificate(Integer certificateID) {
        update("delete from EdsCertificate  ct where ct.objectID = ?", certificateID);
    }

    @Override
    public boolean isCertificateNumberExists(String number, Integer certificateTypeId, Integer objectID) {
        if (objectID != null) {
            return find("select p from EdsCertificate p where p.number = ? and p.certificateType.objectID = ? and p.objectID != ?", number.trim(), certificateTypeId, objectID).size() > 0;
        } else {
            return find("select p from EdsCertificate p where p.number = ? and p.certificateType.objectID = ?", number.trim(), certificateTypeId).size() > 0;
        }
    }
}
