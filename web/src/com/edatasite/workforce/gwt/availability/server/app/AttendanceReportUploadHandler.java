package com.edatasite.workforce.gwt.availability.server.app;

import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.server.app.WfmCommandServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;
import com.edatasite.workforce.gwt.core.server.servlets.WfmCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 26.09.2009
 * Time: 20:20:24
 * To change this template use File | Settings | File Templates.
 */
public class AttendanceReportUploadHandler extends WfmCommandHandler {

    @Autowired
    private WfmCommandServiceLocal wfmCommandServiceLocal;
    @Autowired
    private TimeSlotManager timeSlotManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;

    public void execute(Object command) throws Throwable {
        // Import executer
        WfmCommand document = (WfmCommand) command;
        String[] values;
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.FINGERPRINT_DEVICE_ENABLED)) {
            values = wfmCommandServiceLocal.createAttendanceReportHoursUploadHandler(document);
        } else {
            values = wfmCommandServiceLocal.createAttendanceReportUploadHandler(document);
        }
        setReturnValues(values[0]);
        setErrorString(values[1]);
    }
}
