package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsHoliday;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.HolidayManager;
import org.springframework.stereotype.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository("holidayManager")
public class HolidayManagerImpl extends BaseManager<EdsHoliday> implements HolidayManager {

    public HolidayManagerImpl() {
        super(EdsHoliday.class);
    }

    public List<EdsHoliday> list(ListingFilterParameter fp, boolean isTotal) {
        StringBuilder sql = new StringBuilder();

        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        sql.append("select distinct h from EdsHoliday h ");
        sql.append("left join h.locations ls ");

        sql.append("where h.deleted=false ");
        if (fp.getSqlSearchKey() != null) {
            sql.append("and (lower(h.name) like ");
            sql.append("'").append(fp.getSqlSearchKey()).append("'");
            sql.append(" or ");
            sql.append("lower(h.description) like ");
            sql.append("'").append(fp.getSqlSearchKey()).append("'");
            sql.append(") ");
        }
        if (fp.getYear() != null) {
            sql.append(" and date_part('year', h.date) = '").append(fp.getYear()).append("'");
        }
        if (fp.getLocationId() != null) {
            sql.append(" and ls.objectID=" + fp.getLocationId() + " ");
        }
        sql.append(" order by ");

        if (fp.getSortField() == null) {
            sql.append("h.date");
        } else {
            if (fp.getSortField().equals(HolidayItem.NAME)) {
                sql.append("h.name");
            } else if (fp.getSortField().equals(HolidayItem.DESCRIPTION)) {
                sql.append("h.description");
            } else if (fp.getSortField().equals(HolidayItem.FROM)) {
                sql.append("h.date");
            } else if (fp.getSortField().equals(HolidayItem.TO)) {
                sql.append(" h.endDate ");
            } else if (fp.getSortField().equals(HolidayItem.TAKEN_FROM_VACTION_ALLOWANCE) || fp.getSortField().equals(HolidayItem.TAKEN_FROM_ANNUAL_LEAVE_ALLOWANCE)) {
                sql.append("h.takeAnnual");
            } else if (fp.getSortField().equals(HolidayItem.DAY_OFF)) {
                sql.append("h.dayOff");
            }
        }
        sql.append((fp.isAscending() ? " asc " : " desc "));
        if (isTotal) {
            return find(sql.toString());
        }

        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    public List<EdsHoliday> getHolidays(EdsLocation location, ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        if (location == null) {
            sql.append("select h.* from ").append(getCompanyId()).append(".holiday h ").append("where h.deleted=false and (select count(hl.holiday_id) from " + getCompanyId() + ".holiday_location hl where h.deleted=false and hl.holiday_id = h.id)=0 and ");
        } else {
            sql.append("select h.* from ").append(getCompanyId()).append(".holiday h ").append("inner join ").append(getCompanyId() + ".holiday_location ls on ls.holiday_id = h.id where h.deleted=false and ls.locations_id=" + location.getObjectID() + " and ");
        }
        GregorianCalendar date = new GregorianCalendar();
        date.setTime(new Date());
        date.set(Calendar.MONTH, Calendar.JANUARY);
        date.set(Calendar.DATE, 1);
        date.set(Calendar.HOUR, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        sql.append("((h.recurrenceid is not null and extract(epoch from h.date)*1000 >= ").append(date.getTimeInMillis()).append(") or (h.recurrenceid is null)) ");
        if (fp != null && fp.isShowHolidays()) {
            sql.append(" and h.dayOff=true");
        }
        if (fp != null && fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(h.name) like '");
            sql.append(fp.getSqlSearchKey());
            sql.append("' or ");
            sql.append(" lower(h.description) like '");
            sql.append(fp.getSqlSearchKey());
            sql.append("') ");
        }
        if (fp != null && fp.getSortField() != null && !"".equals(fp.getSortField())) {
            String suff = " order by ";
            if (fp.getSortField().equals(HolidayItem.NAME)) {
                sql.append(suff).append(" h.name ");
            } else if (fp.getSortField().equals(HolidayItem.DESCRIPTION)) {
                sql.append(suff).append(" h.description ");
            } else if (fp.getSortField().equals(HolidayItem.FROM)) {
                sql.append(suff).append(" h.date ");
            } else if (fp.getSortField().equals(HolidayItem.TO)) {
                sql.append(suff).append(" h.enddate ");
            } else if (fp.getSortField().equals(HolidayItem.TAKEN_FROM_VACTION_ALLOWANCE) || fp.getSortField().equals(HolidayItem.TAKEN_FROM_ANNUAL_LEAVE_ALLOWANCE)) {
                sql.append(suff).append(" h.takeannual ");
            }
            if (!fp.isAscending()) {
                if (!sql.toString().contains(suff)) {
                    sql.append(suff).append("DESC ");
                } else {
                    sql.append("DESC ");
                }
            }
        } else {
            sql.append(" order by h.date asc");
        }
        return findNative(sql.toString(), EdsHoliday.class);
    }

    public List<EdsHoliday> getCalendarHolidays(EdsLocation location, Date start, Date end) {
        Map params = new HashMap();
        params.put("start", start);
        params.put("end", end);
        if (location == null) {
            return findByNamedParams("select h from EdsHoliday h where h.deleted=false and " +
                    "(h.date<=:end and h.endDate>=:start) order by h.date desc", params);
        }

        params.put("locationID", location.getObjectID());
        return findByNamedParams("select h from EdsHoliday h left join h.locations ls where " +
                "(ls.objectID=:locationID or h.locations.size = 0) and (h.deleted=false and h.date<=:end and h.endDate>=:start) order by h.date desc", params);
    }

    public ArrayList<EdsHoliday> getHolidaysByRecurrenceID(Integer recurrenceID) {
        return (ArrayList<EdsHoliday>) find("select h from EdsHoliday h where h.deleted=false and h.recurrenceID=? and h.date >= now()", recurrenceID);
    }

    public void deleteHoliday(Integer holidayID) {
        EdsHoliday holiday = get(holidayID);
        if (holiday != null) {
            if (holiday.getRecurrenceID() != null) {
                DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                GregorianCalendar date = new GregorianCalendar(getUser().getUserTimezone());
                updateNative(
                        "DELETE FROM " + getCompanyId() + ".holiday_location " +
                                "WHERE holiday_id IN (" +
                                "   SELECT h.id FROM " + getCompanyId() + ".holiday h " +
                                "   WHERE h.id >= " + holidayID +
                                "     AND h.date >= '" + format.format(date.getTime()) + "'" +
                                "     AND h.recurrenceid = " + holiday.getRecurrenceID().toString() +
                                ")"
                );
                update(
                        "UPDATE EdsHoliday h SET h.deleted = true " +
                                "WHERE h.objectID >= ? AND h.recurrenceID = ?",
                        holidayID, holiday.getRecurrenceID()
                );
                update(
                        "DELETE FROM EdsRecurrence " +
                                "WHERE objectID = ? " +
                                "  AND (SELECT COUNT(h.objectID) FROM EdsHoliday h " +
                                "       WHERE h.recurrenceID = ?) = 0",
                        holiday.getRecurrenceID(),
                        holiday.getRecurrenceID()
                );
            } else {
                updateNative(
                        "DELETE FROM " + getCompanyId() + ".holiday_location " +
                                "WHERE holiday_id = " + holidayID.toString()
                );
                updateNative(
                        "UPDATE " + getCompanyId() + ".holiday " +
                                "SET deleted = true " +
                                "WHERE id = " + holidayID
                );
            }
        }
    }

    public List<EdsHoliday> getHolidaysByDatesAndLocation(Date start, Date end, EdsLocation location) {
        if (end == null) {
            return find("select h from EdsHoliday h left join h.locations ls where h.deleted=false and date >= ? and (h.locations.size = 0 or ls.objectID= ?)", start, (location != null ? location.getObjectID() : Integer.valueOf(0)));
        } else {
            return find("select h from EdsHoliday h left join h.locations ls where h.deleted=false and date between ? and ? and (h.locations.size = 0 or ls.objectID= ?)", start, end, (location != null ? location.getObjectID() : Integer.valueOf(0)));
        }
    }

    @Override
    public List<EdsHoliday> getLocationHolidays(Date start, EdsLocation location) {
        return find("select h from EdsHoliday h left join h.locations ls where h.deleted=false and " +
                "date >= ? " + ((location != null ? "and ls.objectID = " + location.getObjectID() : " and ls is null")), start);
    }

    @Override
    public void removeRecurrenceIDFromRecurringHoliday(Integer recurrenceID) {
        update("update EdsHoliday set recurrenceID = null where recurrenceID=?", recurrenceID);
    }
}
