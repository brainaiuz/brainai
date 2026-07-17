package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsDate;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 01.06.2009
 * Time: 20:54:29
 * To change this template use File | Settings | File Templates.
 */
public interface DateManager extends Manager<EdsDate> {
    Date lastEnteredDate();

    List<EdsDate> getDatesByDates(Date from, Date to);

    List<Date> getFromDatesByDates(Date from, Date to);

    List<Integer> getDateIDsByDates(Date from, Date to);

    EdsDate getDateByDate(Date date);
}
