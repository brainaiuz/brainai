package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTemporaryRecurrence;
import com.edatasite.workforce.gwt.core.server.db.TemporaryRecurrenceManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Mar 4, 2010
 * Time: 4:46:59 AM
 * To change this template use File | Settings | File Templates.
 */

@Repository("temporaryRecurrenceManager")
public class TemporaryRecurrenceManagerImpl extends BaseManager<EdsTemporaryRecurrence> implements TemporaryRecurrenceManager {


    public TemporaryRecurrenceManagerImpl() {
        super(EdsTemporaryRecurrence.class);
    }
}
