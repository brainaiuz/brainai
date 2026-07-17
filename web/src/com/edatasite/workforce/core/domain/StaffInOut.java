package com.edatasite.workforce.core.domain;

import java.util.Date;

/**
 * Created by Djuraev on 11/20/14.
 */
public interface StaffInOut {

    Integer getObjectID();

    EdsUser getEmployee();

    Date getStartDate();

    Date getEndDate();

    EdsReference getStatus();

    Integer getTimeSlotId();
}
