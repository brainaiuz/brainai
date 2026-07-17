package com.edatasite.workforce.gwt.core.server.db.impl.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsMultiCashAdvance;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance.MultiCashAdvanceItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.MultiCashAdvanceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository("multiCashAdvanceManager")
public class MultiCashAdvanceManagerImpl extends BaseManager<EdsMultiCashAdvance> implements MultiCashAdvanceManager {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    public MultiCashAdvanceManagerImpl() {
        super(EdsMultiCashAdvance.class);
    }


    @Override
    public Integer getCashAdvanceIntNumber() {
        return (Integer) this.findSingle("select max(intNumber) from EdsMultiCashAdvance where (deleted is null or deleted <> true)");
    }


    @Override
    public boolean numberExists(final String numberString, final Integer objectId) {
        if (numberString != null && !"".equals(numberString.trim())) {
            final StringBuilder sql = new StringBuilder();
            if (objectId == null) {
                sql.append("select ca.objectID from EdsMultiCashAdvance ca where (ca.deleted is null or ca.deleted<>true) and ca.number = ?");
                return this.find(sql.toString(), numberString).size() > 0;
            } else {
                sql.append("select ca.objectID from EdsMultiCashAdvance ca where (ca.deleted is null or ca.deleted<>true) and ca.number = ? and ca.objectID <> ?");
                return this.find(sql.toString(), numberString, objectId).size() > 0;
            }
        }
        return false;
    }

    @Override
    public List<EdsMultiCashAdvance> getMultiCashAdvanceList(final ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder(" SELECT mca.* ");
        sql = sql.append(this.baseQuery(fp));

        if (fp.getSortField() != null) {
            sql.append(" order by ");
            if (MultiCashAdvanceItem.NUMBER.equals(fp.getSortField())) {
                sql.append("mca.number ");
            } else if (MultiCashAdvanceItem.DATE.equals(fp.getSortField())) {
                sql.append(" mca.requestdate ");
            } else if (MultiCashAdvanceItem.APPROVER.equals(fp.getSortField())) {
                sql.append(" mca.currentApprover ");
            } else {
                sql.append(" mca.id ");
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
            sql.append("order by mca.number desc");
        }
        if (fp.getLimit() > 0) {
            sql.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        }

        return this.findNative(sql.toString(), EdsMultiCashAdvance.class);
    }

    @Override
    public Integer getMultiCashAdvanceCount(final ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder(" SELECT count(mca.id) ");
        sql = sql.append(this.baseQuery(fp));

        final BigInteger totalCount = (BigInteger) this.findNativeSingle(sql.toString());
        return totalCount != null ? totalCount.intValue() : 0;
    }

    private StringBuilder baseQuery(final ListingFilterParameter fp) {
        final StringBuilder sql = new StringBuilder();
        final String companyID = BaseManager.getCompanyId();
        sql.append(" from ").append(companyID).append(".multicashadvance mca \n");

        sql.append("where ").append(ServerUtils.checkForDeleted("mca.deleted "));

        if (fp.getSqlSearchKey() != null) {
            sql.append(" and (");
            sql.append(" lower(mca.number) like '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(") ");
        }
        if (fp.getObjectsIds() != null) {
            sql.append(" and (");
            sql.append("mca.id in (" + fp.getObjectsIds() + ")");
        }
        return sql;
    }
}
