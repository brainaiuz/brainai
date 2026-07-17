package com.edatasite.workforce.gwt.core.server.db.impl;


import com.edatasite.workforce.core.domain.EdsDate;
import com.edatasite.workforce.gwt.core.server.db.DateManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 01.06.2009
 * Time: 20:56:32
 * To change this template use File | Settings | File Templates.
 */
@Repository("dateManager")
public class DateManagerImpl extends BaseManager<EdsDate> implements DateManager {

    public DateManagerImpl() {
        super(EdsDate.class);
    }

    public Date lastEnteredDate() {
        return (Date) findSingle("SELECT max(d.fromDate) FROM EdsDate d");
    }

    public List<EdsDate> getDatesByDates(Date from, Date to) {
        return find("SELECT d from EdsDate d where d.fromDate between ? and ? order by d.fromDate", from, to);
    }

    public List<Date> getFromDatesByDates(Date from, Date to) {
        return findNative("SELECT d.from_date from " + getPublic() + ".datejoin d where d.from_date between ? and ? group by d.from_date order by d.from_date", from, to);
    }

    public List<Integer> getDateIDsByDates(Date from, Date to) {
        return find("SELECT d.id from EdsDate d where d.fromDate between ? and ?", from, to);
    }

    public EdsDate getDateByDate(Date date) {
        return (EdsDate) findSingle("SELECT d from EdsDate d where d.fromDate = ?", date);
    }
}
