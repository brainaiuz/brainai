package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsRecurrenceJob;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceJobManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Apr 15, 2010
 * Time: 3:10:07 PM
 * To change this template use File | Settings | File Templates.
 */

@Repository("recurrenceJobManager")
public class RecurrenceJobManagerImpl extends BaseManager<EdsRecurrenceJob> implements RecurrenceJobManager {
    public RecurrenceJobManagerImpl() {
        super(EdsRecurrenceJob.class);
    }
}
