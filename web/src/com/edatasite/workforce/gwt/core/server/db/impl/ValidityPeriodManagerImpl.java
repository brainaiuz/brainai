package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.ValidityPeriodManager;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sher
 * Date: 7/27/12
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("validityPeriodManager")
public class ValidityPeriodManagerImpl extends BaseManager<EdsValidityPeriod> implements ValidityPeriodManager {

    public ValidityPeriodManagerImpl() {
        super(EdsValidityPeriod.class);
    }

    @Autowired
    private ReferenceManager referenceManager;

    @Override
    public List<EdsValidityPeriod> list(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select v from EdsValidityPeriod v ");
        if (StringUtils.isEmpty(fp.getStatusCode())) {
            sql.append(" where v.deleted is not true ");
        } else {
            sql.append(",in (v.periodTypeItems) pt where v.deleted is not true and pt.code in(?)");
        }
        String sortOrder = fp.isAscending() ? "" : " desc";
        if (fp.getSortField() != null) {

            String sortField = "";
            if ("name".equals(fp.getSortField())) {
                sortField = " v.name ";
            }
            if ("description".equals(fp.getSortField())) {
                sortField = " v.description ";
            }
            if ("period".equals(fp.getSortField())) {
                sortField = " v.fromDate ";
            }
            if ("toDate".equals(fp.getSortField())) {
                sortField = " v.toDate ";
            }
            if (!sortField.isEmpty()) {
                sql.append(" ORDER BY " + sortField + sortOrder);
            }
        }
        if (StringUtils.isEmpty(fp.getStatusCode())) {
            return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
        } else {
            return findInterval(sql.toString(), fp.getStart(), fp.getLimit(), fp.getStatusCode());
        }
    }

    @Override
    public Long listSize(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(v.objectID) from EdsValidityPeriod v ");
        if (StringUtils.isEmpty(fp.getStatusCode())) {
            sql.append(" where v.deleted is not true ");
            return (Long) findSingle(sql.toString());
        } else {
            sql.append(",in (v.periodTypeItems) pt where v.deleted is not true and pt.code in(?)");
            return (Long) findSingle(sql.toString(), fp.getStatusCode());
        }
    }

    @Override
    public boolean checkOverlaps(ValidityPeriodItem item) {
        if (!item.getPeriodTypeCodeItems().contains(ValidityPeriodItem.VALIDITY_PERIOD_BONUS)) {
            return false;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("fromDate", item.getFromDate());
        map.put("toDate", item.getToDate());
        map.put("periodTypeCode", item.getPeriodTypeCodeItems());

        StringBuilder sql = new StringBuilder();
        sql.append("select count(v.id) from EdsValidityPeriod v, in(v.periodTypeItems) pt where v.deleted is not true and ((v.fromDate between :fromDate and :toDate) or (:fromDate between v.fromDate and v.toDate)) and pt.code in (:periodTypeCode)");

        if (item.getId() != null) {
            map.put("objectId", item.getId());
            sql.append(" and v.objectID not in(:objectId)");
        }

        Long count = (Long) findSingleByNamedParams(sql.toString(), map);

        return count > 0;
    }

    @Override
    public boolean isFirstTime() {
        Long size = (Long) findSingle("select count(objectID) from EdsValidityPeriod");
        return size == 0;
    }

    @Override
    public void createDefaultValidityPeriods() {
        EdsUser user = getUser();

        Calendar fromCal = new GregorianCalendar();
        fromCal.set(Calendar.MONTH, 0);
        fromCal.set(Calendar.DAY_OF_MONTH, 1);
        fromCal.set(Calendar.AM_PM, 0);
        fromCal.set(Calendar.HOUR, 0);
        fromCal.set(Calendar.MINUTE, 0);
        fromCal.set(Calendar.SECOND, 0);
        fromCal.set(Calendar.MILLISECOND, 0);

        Calendar endCal = new GregorianCalendar();
        endCal.set(Calendar.MONTH, 11);
        endCal.set(Calendar.DAY_OF_MONTH, 31);
        endCal.set(Calendar.AM_PM, 0);
        endCal.set(Calendar.HOUR, 23);
        endCal.set(Calendar.MINUTE, 59);
        endCal.set(Calendar.SECOND, 59);
        endCal.set(Calendar.MILLISECOND, 0);

        EdsValidityPeriod validityPeriod = new EdsValidityPeriod();
        String datePattern = ServerUtils.getShortDateFormat(user);
        String name = ServerUtils.dateFormat(fromCal.getTime(), datePattern) + " - " + ServerUtils.dateFormat(endCal.getTime(), datePattern);
        validityPeriod.setName(name);
        validityPeriod.setDescription(name);
        validityPeriod.setFromDate(user.getServerDateByUserDate(fromCal.getTime()));
        validityPeriod.setToDate(user.getServerDateByUserDate(endCal.getTime()));
        validityPeriod.setDeleted(false);
        validityPeriod.getPeriodTypeItems().add(referenceManager.findReference(ValidityPeriodItem._VALIDITY_PERIOD_TYPE, ValidityPeriodItem.VALIDITY_PERIOD_GOAL));
        validityPeriod.getPeriodTypeItems().add(referenceManager.findReference(ValidityPeriodItem._VALIDITY_PERIOD_TYPE, ValidityPeriodItem.VALIDITY_PERIOD_APPRAISAL));
        create(validityPeriod);
    }

    @Override
    public EdsValidityPeriod getCurrentValidityPeriod(String validityPeriodType) {
        return (EdsValidityPeriod) findSingle("select v from EdsValidityPeriod v, in(v.periodTypeItems) pt where (current_timestamp between v.fromDate and v.toDate) and v.deleted=false and pt.code in(?)", validityPeriodType);
    }

    @Override
    public ValidityPeriodItem[] getValidityPeriods(ListingFilterParameter fp) {
        List<EdsValidityPeriod> validityPeriods = list(fp);
        ValidityPeriodItem[] selectItems = new ValidityPeriodItem[validityPeriods.size()];
        int i = 0;
        for (EdsValidityPeriod period : validityPeriods) {
            selectItems[i++] = period.getDTO();

        }
        return selectItems;
    }
}
