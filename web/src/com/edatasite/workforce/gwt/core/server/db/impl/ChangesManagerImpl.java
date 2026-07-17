package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.settings.EdsChanges;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ChangesManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hurshid on 8/16/2017.
 */
@Repository("changesManager")
public class ChangesManagerImpl extends BaseManager<EdsChanges> implements ChangesManager {

    public ChangesManagerImpl() {
        super(EdsChanges.class);
    }

    @Override
    public List<HistoryItem> changeList(ListingFilterParameter fp) {
        List<HistoryItem> itemList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select mu.id, ep.employeecode as employeecode, COALESCE(mu.firstname, '') || ' ' || COALESCE(mu.lastname, '')  as username, ch.field,  ch.fromStringValue,  ch.fromNumberValue,  ch.fromDateValue,");
        sql.append("ch.toStringValue, ch.toNumberValue, ch.toDateValue, ch.modificationDate, ");
        sql.append("CASE WHEN ch.isSuperUser THEN 'KPI Support' ELSE COALESCE(u.firstname, '') || ' ' || COALESCE(u.lastname, '') END as updater  ");
        sql.append("from ").append(getCompanyId()).append(".changes ch ");
        sql.append("left join ").append(getCompanyId()).append(".myuser mu on ch.entityid=mu.id ");
        sql.append("left join ").append(getCompanyId()).append(".myuser u on u.id=ch.updater_id ");
        sql.append("left join ").append(getCompanyId()).append(".employeeprofile ep on mu.id=ep.employeeid ");
        sql.append("where 1=1 ");

        getWhere(sql, fp);

        if (StringUtils.isNotBlank(fp.getSortField())) {
            String code = fp.getSortField();
            if (HistoryItem.ENTITY_CODE.equals(code)) {
                sql.append(" ORDER BY ep.employeecode ");
            } else if (HistoryItem.ENTITY_NAME.equals(code)) {
                sql.append(" ORDER BY username ");
            } else if (HistoryItem.FIELD_ID.equals(code)) {
                sql.append(" ORDER BY field ");
            } else if (HistoryItem.MODIFIED_BY.equals(code)) {
                sql.append(" ORDER BY updater ");
            } else if (HistoryItem.MODIFIED_DATE.equals(code)) {
                sql.append(" ORDER BY modificationDate ");
            } else {
                sql.append(" ORDER BY modificationDate ");
            }
            sql.append(!fp.isAscending() ? " desc " : " ");
        } else {
            sql.append("ORDER BY modificationDate desc ");
        }

        if (fp.getLimit() > 0) {
            sql.append(" limit ").append(fp.getLimit());
        }
        if (fp.getStart() > 0) {
            sql.append(" offset ").append(fp.getStart());
        }
        List<Object[]> list = findNative(sql.toString());
        for (Object[] obj : list) {
            HistoryItem item = new HistoryItem();
            item.setUserID((Integer) obj[0]);
            item.setEmployeeCode((String) obj[1]);
            item.setEntityName((String) obj[2]);
            item.setField((String) obj[3]);
            item.setFromStringValue((String) obj[4]);
            item.setFromNumberValue((BigDecimal) obj[5]);
            item.setFromDateValue((Date) obj[6]);
            item.setToStringValue((String) obj[7]);
            item.setToNumberValue((BigDecimal) obj[8]);
            item.setToDateValue((Date) obj[9]);
            item.setUpdatedDate((Date) obj[10]);
            item.setUserName((String) obj[11]);
            itemList.add(item);
        }
        return itemList;
    }


    public String changedFieldByDate(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(fp.getDate());

        sql.append("SELECT ch.fromStringValue FROM ")
                .append(getCompanyId())
                .append(".changes ch WHERE ch.field = '")
                .append(fp.getName().replace("'", "''"))
                .append("' AND ch.entityID = ")
                .append(fp.getEntityID())
                .append(" AND DATE(ch.modificationDate) = '")
                .append(format)
                .append("' order by ch.modificationDate desc");
        return (String) findNativeSingle(sql.toString());
    }

    private StringBuilder getWhere(StringBuilder sql, ListingFilterParameter fp) {
//        sql.append(" and ch.historyType=").append("'").append(HistoryType.EMPLOYEE).append("'");
        if (fp.getEntityID() != null) {
            sql.append(" and (mu.id=").append(fp.getEntityID()).append(") ");
        }
        if (fp.getEmployeeId() != null) {
            sql.append(" and (mu.id=").append(fp.getEmployeeId()).append(") ");
        }
        if (fp.getStartDate() != null) {
            sql.append(" and (date(ch.modificationDate)>='").append(new SimpleDateFormat("yyyy-MM-dd").format(fp.getStartDate())).append("') ");
        }
        if (fp.getEndDate() != null) {
            sql.append(" and (date(ch.modificationDate)<='").append(new SimpleDateFormat("yyyy-MM-dd").format(fp.getEndDate())).append("') ");
        }
        sql.append(" and (ch.field !='Basic Salary' or ").append(ServerUtils.hasPermission(PermissionConstants.HRMS_PAYROLL_DEDUCTION_CATEGORIES)).append(") ");

        if (fp.getSqlSearchKey() != null) {
            String searchKey = fp.getSqlSearchKey();
            sql.append("and (").append("lower(mu.firstname||' '||mu.lastname) like '").append(searchKey).append("' ");
            sql.append("or ").append("lower(u.firstname||' '||u.lastname) like '").append(searchKey).append("' ");
            sql.append("or ").append("lower(ep.employeecode) like '%").append(searchKey).append("' ");
            sql.append("or ").append("lower(ch.field) like '").append(searchKey).append("' ");
            sql.append("or ").append("lower(ch.fromStringvalue) like '").append(searchKey).append("' ");
            sql.append("or ").append("lower(ch.toStringvalue) like '").append(searchKey).append("' ");
            sql.append("or ").append("ch.fromnumbervalue||'' like '").append(searchKey).append("' ");
            sql.append("or ").append("ch.tonumbervalue||'' like '").append(searchKey).append("' ");
            sql.append("or ").append("ch.fromDatevalue||'' like '").append(searchKey).append("' ");
            sql.append("or ").append("ch.toDatevalue||'' like '").append(searchKey).append("') ");
        }
        return sql;
    }

    @Override
    public Long getChangesCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(count) from (");
        sql.append("select count(ch.id) from ").append(getCompanyId()).append(".myuser mu  ");
        sql.append("left join ").append(getCompanyId()).append(".changes ch on ch.entityid=mu.id ");
        sql.append("left join ").append(getCompanyId()).append(".myuser u on u.id=ch.updater_id ");
        sql.append("left join ").append(getCompanyId()).append(".employeeprofile ep on mu.id=ep.employeeid ");
        sql.append(" where 1=1 ");
        getWhere(sql, fp);
        sql.append(") t");
        return ((BigDecimal) findNativeSingle(sql.toString())).longValue();
    }
}
