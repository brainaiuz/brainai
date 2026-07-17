package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsDependent;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.DependentManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: unni
 * Date: Oct 22, 2009
 * Time: 10:50:25 AM
 */
@Repository("dependentManager")
public class DependentManagerImpl extends BaseManager<EdsDependent> implements DependentManager {

    public DependentManagerImpl() {
        super(EdsDependent.class);
    }

    public List<EdsDependent> getDependentList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT dep.* FROM ").append(getCompanyId()).append(".dependent as dep \n");
        sql.append("LEFT OUTER JOIN ").append(getCompanyId()).append(".myuser mu ON (mu.id=dep.user_id) \n");
        if (fp.isFromCandidate() && fp.getContactID() != null) {
            sql.append("LEFT JOIN ").append(getCompanyId()).append(".crmContact c on dep.candidateid = c.id \n");
        }
        sql.append("LEFT OUTER JOIN ").append(getPublic()).append(".country co ON (co.id=dep.countryid) \n");
        sql.append(" WHERE (dep.deleted=false or dep.deleted is null) \n");
        //filter employee ID
        if (fp.isFromCandidate() && fp.getContactID() != null) {
            sql.append(" AND c.id =").append(fp.getContactID());
        }
        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            sql.append(" AND mu.id=").append(fp.getEmployeeId()).append(" \n");
        }
        //searching
        if (fp.getSqlSearchKey() != null && !"".equals(fp.getSqlSearchKey())) {
            sql.append(" AND lower(dep.firstname) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(dep.lastName) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(dep.relationship) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(dep.phone1) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR lower(dep.phone2) like '").append(fp.getSqlSearchKey()).append("' \n");
        }
        //sorting
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            sql.append(" ORDER BY ");
            if (fp.getSortField().equals(DependentItem.FIRSTNAME) || fp.getSortField().equals(DependentItem.ACTION)) {
                sql.append(" dep.firstname ");
            } else if (fp.getSortField().equals(DependentItem.LASTNAME)) {
                sql.append(" dep.lastName ");
            } else if (fp.getSortField().equals(DependentItem.RELATIONSHIP)) {
                sql.append("  dep.relationship ");
            } else if (fp.getSortField().equals(DependentItem.PHONE1)) {
                sql.append(" dep.phone1 ");
            } else if (fp.getSortField().equals(DependentItem.PHONE2)) {
                sql.append("  dep.phone2 ");
            } else {
                sql.append("dep.firstname ");
            }
            if (!fp.isAscending()) {
                sql.append("DESC");
            }
        }

        return findNative(sql.toString(), EdsDependent.class);
    }

    public List<EdsDependent> getDependentList(EdsEmployee employee) {
        return find("from EdsDependent d where " +
                " d.user=? and (d.deleted=false or d.deleted is null) order by id", employee);
    }

    @Override
    public List<EdsDependent> getDependenstByCandidate(EdsCrmContact candidate) {
        return find("from EdsDependent d where " +
                " d.candidate=? and (d.deleted=false or d.deleted is null) order by id", candidate);
    }

    @Override
    public void deleteRelatedDependents(Integer id, String relation) {
        update("update EdsDependent set deleted = true where " + (relation != null && relation.equals("CANDIDATE") ? " candidate" : " user") +
                ".objectID = " + id);
    }
}
