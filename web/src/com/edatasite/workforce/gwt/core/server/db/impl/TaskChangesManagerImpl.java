package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTaskChanges;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.TaskChangesManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("taskChangesManager")
public class TaskChangesManagerImpl extends BaseManager<EdsTaskChanges> implements TaskChangesManager {

    @Autowired
    private ReferenceManager referenceManager;

    public TaskChangesManagerImpl() {
        super(EdsTaskChanges.class);
    }


    @Override
    public List<HistoryItem> changeList(ListingFilterParameter fp) {
        List<HistoryItem> itemList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select t.number, tch.field,  tch.fromStringValue,  tch.fromNumberValue,  tch.fromDateValue,tch.fromReferenceId, ");
        sql.append("tch.toStringValue, tch.toNumberValue, tch.toDateValue, tch.modificationDate, tch.toReferenceId, ");
        sql.append("CASE WHEN tch.isSuperUser THEN 'KPI Support' ELSE u.firstname||' '||u.lastname END as updater  ");
        sql.append("from ").append(getCompanyId()).append(".taskChanges tch ");
        sql.append("left join ").append(getCompanyId()).append(".task t on tch.entityid=t.id ");
        sql.append("left join ").append(getCompanyId()).append(".myuser u on u.id=tch.updater_id ");
        sql.append("where 1=1 ");

        getWhere(sql, fp);

        if (StringUtils.isNotBlank(fp.getSortField())) {
            String code = fp.getSortField();
            if (HistoryItem.ENTITY_NAME.equals(code)) {
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
            if (((Integer) obj[5]) != null) {
                EdsReference fromReference = referenceManager.get((Integer) obj[5]);
                item.setFromReference(fromReference.getLocalizedName());
            }
            if (((Integer) obj[10]) != null) {
                EdsReference toReference = referenceManager.get((Integer) obj[10]);
                item.setToReference(toReference.getLocalizedName());
            }
            item.setEntityName((String) obj[0]);
            item.setField((String) obj[1]);
            item.setFromStringValue((String) obj[2]);
            item.setFromNumberValue((BigDecimal) obj[3]);
            item.setFromDateValue((Date) obj[4]);
            item.setToStringValue((String) obj[6]);
            item.setToNumberValue((BigDecimal) obj[7]);
            item.setToDateValue((Date) obj[8]);
            item.setUpdatedDate((Date) obj[9]);
            item.setUserName((String) obj[11]);
            itemList.add(item);
        }
        return itemList;
    }

    private StringBuilder getWhere(StringBuilder sql, ListingFilterParameter fp) {
        if (fp.getEntityID() != null) {
            sql.append(" and (t.id=").append(fp.getEntityID()).append(") ");
        }
        if (fp.getStartDate() != null) {
            sql.append(" and (date(tch.modificationDate)>='").append(new SimpleDateFormat("yyyy-MM-dd").format(fp.getStartDate())).append("') ");
        }
        if (fp.getEndDate() != null) {
            sql.append(" and (date(tch.modificationDate)<='").append(new SimpleDateFormat("yyyy-MM-dd").format(fp.getEndDate())).append("') ");
        }

        if (fp.getSqlSearchKey() != null) {
            String searchKey = fp.getSqlSearchKey();
            sql.append("and (").append("lower(u.firstname||' '||u.lastname) like '").append(searchKey).append("' ");
            sql.append("or ").append("lower(tch.field) like '").append(searchKey).append("' ");
            sql.append("or ").append("lower(tch.fromStringvalue) like '").append(searchKey).append("' ");
            sql.append("or ").append("lower(tch.toStringvalue) like '").append(searchKey).append("' ");
            sql.append("or ").append("tch.fromnumbervalue||'' like '").append(searchKey).append("' ");
            sql.append("or ").append("tch.tonumbervalue||'' like '").append(searchKey).append("' ");
            sql.append("or ").append("tch.fromDatevalue||'' like '").append(searchKey).append("' ");
            sql.append("or ").append("tch.toDatevalue||'' like '").append(searchKey).append("') ");
        }
        return sql;
    }

    @Override
    public Long getChangesCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(count) from (");
        sql.append("select count(tch.id) from ").append(getCompanyId()).append(".taskChanges tch  ");
        sql.append("left join ").append(getCompanyId()).append(".task t on tch.entityid=t.id ");
        sql.append("left join ").append(getCompanyId()).append(".myuser u on u.id=tch.updater_id ");
        sql.append(" where 1=1 ");
        getWhere(sql, fp);
        sql.append(") t");
        return ((BigDecimal) findNativeSingle(sql.toString())).longValue();
    }
}
