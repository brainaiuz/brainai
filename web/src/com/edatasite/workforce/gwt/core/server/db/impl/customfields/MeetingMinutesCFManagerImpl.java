package com.edatasite.workforce.gwt.core.server.db.impl.customfields;

import com.edatasite.workforce.core.domain.customfields.EdsMeetingMinutesCustomFields;
import com.edatasite.workforce.gwt.core.server.db.customfields.MeetingMinutesCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

/**
 * Created by Omonullo Abdullaev on 03.03.16.
 */
@Repository("meetingMinutesCFManager")
public class MeetingMinutesCFManagerImpl extends BaseManager<EdsMeetingMinutesCustomFields> implements MeetingMinutesCFManager {
    public MeetingMinutesCFManagerImpl() {
        super(EdsMeetingMinutesCustomFields.class);
    }

}
