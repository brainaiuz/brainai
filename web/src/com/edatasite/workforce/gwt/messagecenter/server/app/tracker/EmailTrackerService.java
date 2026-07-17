package com.edatasite.workforce.gwt.messagecenter.server.app.tracker;

import com.edatasite.workforce.core.domain.emailfetching.EdsEmailTracker;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface EmailTrackerService {

    EdsEmailTracker createTracker(EdsEmailTracker tracker);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void addTrackerToCrmContactOrLead(Integer trackerID, String edsEmailID);
}
