package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCandidateChanges;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.CandidateChangesManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository("candidateChangesManager")
public class CandidateChangesManagerImpl extends BaseManager<EdsCandidateChanges> implements CandidateChangesManager {

    @Autowired
    private ReferenceManager referenceManager;

    public CandidateChangesManagerImpl() {
        super(EdsCandidateChanges.class);
    }


    @Override
    public List<HistoryItem> changeList(ListingFilterParameter fp) {
        List<HistoryItem> itemList = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("select cc.field,  cc.fromStringValue,  cc.fromNumberValue,  cc.fromDateValue,cc.fromReferenceId, ");
        sql.append("cc.toStringValue, cc.toNumberValue, cc.toDateValue, cc.modificationDate, cc.toReferenceId, ");
        sql.append("CASE WHEN cc.isSuperUser THEN 'KPI Support' ELSE u.firstname||' '||u.lastname END as updater  ");
        sql.append("from ").append(getCompanyId()).append(".candidateChanges cc ");
        sql.append("left join ").append(getCompanyId()).append(".crmcontact c on cc.candidateid=c.id ");
        sql.append("left join ").append(getCompanyId()).append(".myuser u on u.id=cc.updater_id ");
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
            if (((Integer) obj[4]) != null) {
                EdsReference fromReference = referenceManager.get((Integer) obj[4]);
                item.setFromReference(fromReference.getLocalizedName());
            }
            if (((Integer) obj[9]) != null) {
                EdsReference toReference = referenceManager.get((Integer) obj[9]);
                item.setToReference(toReference.getLocalizedName());
            }
            item.setField((String) obj[0]);
            item.setFromStringValue((String) obj[1]);
            item.setFromNumberValue((BigDecimal) obj[2]);
            item.setFromDateValue((Date) obj[3]);
            item.setToStringValue((String) obj[5]);
            item.setToNumberValue((BigDecimal) obj[6]);
            item.setToDateValue((Date) obj[7]);
            item.setUpdatedDate((Date) obj[8]);
            item.setUserName((String) obj[10]);
            itemList.add(item);
        }
        return itemList;
    }

    private StringBuilder getWhere(StringBuilder sql, ListingFilterParameter fp) {
        if (fp.getEntityID() != null) {
            sql.append(" and (c.id=").append(fp.getEntityID()).append(") ");
        }
        if (fp.getStartDate() != null) {
            sql.append(" and (date(cc.modificationDate)>='").append(new SimpleDateFormat("yyyy-MM-dd").format(fp.getStartDate())).append("') ");
        }
        if (fp.getEndDate() != null) {
            sql.append(" and (date(cc.modificationDate)<='").append(new SimpleDateFormat("yyyy-MM-dd").format(fp.getEndDate())).append("') ");
        }

        if (fp.getSqlSearchKey() != null) {
            String searchKey = fp.getSqlSearchKey();
            sql.append("and (").append("lower(u.firstname||' '||u.lastname) like '").append(searchKey).append("' ");
            sql.append("or ").append("lower(cc.field) like '").append(searchKey).append("' ");
            sql.append("or ").append("lower(cc.fromStringvalue) like '").append(searchKey).append("' ");
            sql.append("or ").append("lower(cc.toStringvalue) like '").append(searchKey).append("' ");
            sql.append("or ").append("cc.fromnumbervalue||'' like '").append(searchKey).append("' ");
            sql.append("or ").append("cc.tonumbervalue||'' like '").append(searchKey).append("' ");
            sql.append("or ").append("cc.fromDatevalue||'' like '").append(searchKey).append("' ");
            sql.append("or ").append("cc.toDatevalue||'' like '").append(searchKey).append("') ");
        }
        return sql;
    }

    @Override
    public Long getChangesCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(count) from (");
        sql.append("select count(cc.id) from ").append(getCompanyId()).append(".candidateChanges cc  ");
        sql.append("left join ").append(getCompanyId()).append(".crmcontact c on cc.candidateid=c.id ");
        sql.append("left join ").append(getCompanyId()).append(".myuser u on u.id=cc.updater_id ");
        sql.append(" where 1=1 ");
        getWhere(sql, fp);
        sql.append(") t");
        return ((BigDecimal) findNativeSingle(sql.toString())).longValue();
    }
}
