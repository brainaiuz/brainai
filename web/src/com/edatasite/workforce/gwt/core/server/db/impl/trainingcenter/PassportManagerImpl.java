package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.trainingcenter.EdsPassport;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.PassportManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.passport.PassportData;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 14/06/14
 * Time: 16:51
 * To change this template use File | Settings | File Templates.
 */
@Repository("passportManager")
public class PassportManagerImpl extends BaseManager<EdsPassport> implements PassportManager {

    public PassportManagerImpl() {
        super(EdsPassport.class);
    }

    @Override
    public List<EdsPassport> getList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("select p from EdsPassport p ");
        sql.append("LEFT JOIN p.student student ");
        sql.append("LEFT JOIN student.contact contact ");
        sql.append("LEFT JOIN p.status status ");
        sql.append("WHERE p.deleted is not true ");

        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (lower(p.numberString||''||p.number) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(contact.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(contact.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(p.numberString) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(p.number) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(p.level) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(p.type) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(status.name) like '").append(fp.getSqlSearchKey()).append("') ");
        }
        sql.append("order by ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (PassportData.NUMBER.equals(fp.getSortField())) {
                sql.append("p.number");
            } else if (PassportData.CREATION_DATE.equals(fp.getSortField())) {
                sql.append("p.creationDate");
            } else if (PassportData.STUDENT.equals(fp.getSortField())) {
                sql.append("contact.lastName");
            } else if (PassportData.TYPE.equals(fp.getSortField())) {
                sql.append("p.type");
            } else {
                sql.append("p.creationDate desc");
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
            sql.append("p.creationDate desc");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("select count(p.objectID) from EdsPassport p ");
        sql.append("LEFT JOIN p.student student ");
        sql.append("LEFT JOIN student.contact contact ");
        sql.append("LEFT JOIN p.status status ");
        sql.append("WHERE p.deleted is not true ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (lower(p.numberString||''||p.number) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(contact.firstName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(contact.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(p.numberString) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(p.number) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(p.level) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(p.type) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(status.name) like '").append(fp.getSqlSearchKey()).append("') ");
        }
        return ((Long) findSingle(sql.toString())).intValue();
    }

    @Override
    public void deletePassport(Integer passportID) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ").append(getCompanyId()).append(".passport set deleted = true where id = " + passportID);
        updateNative(sql.toString());
    }

    @Override
    public EdsPassport findPassportByNumber(String numberString, String number) {
        return (EdsPassport) findSingle("SELECT p from EdsPassport p WHERE p.deleted is not true AND p.number=? AND p.numberString=?", number, numberString != null ? numberString : "");
    }
}
