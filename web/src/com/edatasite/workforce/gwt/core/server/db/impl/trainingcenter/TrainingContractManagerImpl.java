package com.edatasite.workforce.gwt.core.server.db.impl.trainingcenter;

import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.trainingcenter.EdsCourse;
import com.edatasite.workforce.core.domain.trainingcenter.EdsTrainingContract;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.trainingcenter.TrainingContractManager;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TrainingContractItem;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Babayev xushnud
 * Date: 8/16/12
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("trainingContractManager")
public class TrainingContractManagerImpl extends BaseManager<EdsTrainingContract> implements TrainingContractManager {
    public TrainingContractManagerImpl() {
        super(EdsTrainingContract.class);
    }


    public List<EdsTrainingContract> list(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder("select tc from EdsTrainingContract tc ");
        sql.append("left join tc.account account ");
        sql.append("where (tc.deleted is null or tc.deleted is false) ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (lower(tc.name) like '").append(fp.getSqlSearchKey()).append("'");
            sql.append(" OR lower(tc.description) like '").append(fp.getSqlSearchKey()).append("'");
            sql.append(" OR lower(tc.account.name) like '").append(fp.getSqlSearchKey()).append("')");
        }
        sql.append("order by ");
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (TrainingContractItem.NAME.equals(fp.getSortField())) {
                sql.append("tc.name");
            } else if (TrainingContractItem.START_DATE.equals(fp.getSortField())) {
                sql.append("tc.startDate");
            } else if (TrainingContractItem.END_DATE.equals(fp.getSortField())) {
                sql.append("tc.endDate");
            } else if (TrainingContractItem.ACCOUNT.equals(fp.getSortField())) {
                sql.append("account.name");
            } else {
                sql.append(" tc.name desc");
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
            sql.append(" tc.updatedDate desc nulls last");
        }
        int limit = 20;
        if (fp.getLimit() != null) {
            limit = fp.getLimit();
        }
        return findInterval(sql.toString(), fp.getStart(), limit);
    }

    public Integer getContractTotalCount(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder("select count(tc.objectID) from EdsTrainingContract tc ");
        sql.append("left join tc.account account ");
        sql.append("where (tc.deleted is null or tc.deleted is false) ");
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (lower(tc.name) like '").append(fp.getSqlSearchKey()).append("'");
            sql.append(" OR lower(tc.description) like '").append(fp.getSqlSearchKey()).append("'");
            sql.append(" OR lower(tc.account.name) like '").append(fp.getSqlSearchKey()).append("')");
        }
        return ((Long) findSingle(sql.toString())).intValue();
    }

    @Override
    public List<EdsTrainingContract> getKeyClientList(EdsCrmAccount customer, Date nowDate) {
        return getKeyClientList(customer.getObjectID(), nowDate);
    }

    @Override
    public List<EdsTrainingContract> getKeyClientList(Integer customerID, Date nowDate) {
        EdsCrmAccount customer = (EdsCrmAccount) findSingle("SELECT ca FROM EdsCrmAccount ca WHERE ca.objectID = ?", customerID);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<EdsTrainingContract> contractList = find("SELECT tc FROM EdsTrainingContract tc WHERE (tc.deleted IS NULL OR tc.deleted=FALSE) and tc.account.objectID=? AND (to_date('" + format.format(nowDate) + "','yyyy-MM-dd') BETWEEN tc.startDate AND tc.endDate)", customerID);

        if ((contractList == null || contractList.size() == 0) && customer.getParent() != null) {
            contractList = getKeyClientList(customer.getParent().getObjectID(), nowDate);
        }

        return contractList;
    }

    @Override
    public List<EdsCourse> getCourses(Integer contractID) {
        return find("SELECT tc.courses FROM EdsTrainingContract tc WHERE (tc.deleted IS NULL OR tc.deleted=FALSE) AND tc.objectID=?", contractID);
    }


}
