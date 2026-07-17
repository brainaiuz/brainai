package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Jul 13, 2010
 * Time: 11:01:33 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "timesheetsettings")
public class EdsTimeSheetSettings extends EdsObject {

    public static EdsTimeSheetSettings DEFAULT_SETTINGS = new EdsTimeSheetSettings();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private int maxMinutesPerDay = 12 * 60;//12 hours.
    private int minMinutesPerDay = 6 * 60;//6 hours.

    private boolean validateDailyTimesheets;
    private boolean validateTimesheetApproval;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public int getMaxHoursPerDay() {
        return maxMinutesPerDay;
    }

    public void setMaxHoursPerDay(int maxHoursPerDay) {
        this.maxMinutesPerDay = maxHoursPerDay;
    }

    public int getMinHoursPerDay() {
        return minMinutesPerDay;
    }

    public void setMinHoursPerDay(int minHoursPerDay) {
        this.minMinutesPerDay = minHoursPerDay;
    }

    public boolean isValidateDailyTimesheets() {
        return validateDailyTimesheets;
    }

    public void setValidateDailyTimesheets(boolean validateDailyTimesheets) {
        this.validateDailyTimesheets = validateDailyTimesheets;
    }

    public boolean isValidateTimesheetApproval() {
        return validateTimesheetApproval;
    }

    public void setValidateTimesheetApproval(boolean validateTimesheetApproval) {
        this.validateTimesheetApproval = validateTimesheetApproval;
    }
}
