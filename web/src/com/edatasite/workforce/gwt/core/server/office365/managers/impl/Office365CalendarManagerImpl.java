package com.edatasite.workforce.gwt.core.server.office365.managers.impl;

import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.office365.domain.EdsOffice365Calendar;
import com.edatasite.workforce.gwt.core.server.office365.managers.Office365CalendarManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by umidbekkarimov on 11/23/15.
 */
@Transactional
@Service("office365CalendarManager")
public class Office365CalendarManagerImpl extends BaseManager<EdsOffice365Calendar> implements Office365CalendarManager {


    public Office365CalendarManagerImpl() {
        super(EdsOffice365Calendar.class);
    }

    @Override
    public EdsOffice365Calendar getUserCalendar() {
        return (EdsOffice365Calendar) findSingle("from EdsOffice365Calendar WHERE user = ?", this.getUser());
    }
}
