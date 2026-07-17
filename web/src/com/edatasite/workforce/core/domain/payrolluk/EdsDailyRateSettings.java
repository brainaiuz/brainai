package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.payroll.client.ui.view.dailyRateCalculation.DailyRateSettings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "dailyratesettings")
public class EdsDailyRateSettings extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @Column(name="excludeHoliday")
    private boolean excludeHoliday;
    @Column(name="excludeDayOffs")
    private boolean excludeDayOffs;
    @Column(name="workDaysInMonth")
    private Integer workDaysInMonth;

    @Column(name = "dailyRateType", nullable = false, columnDefinition = "varchar(255) default 'TYPE_CALENDAR'")
    private String dailyRateType;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public boolean getExcludeHoliday() {
        return excludeHoliday;
    }

    public void setExcludeHoliday(boolean excludeHoliday) {
        this.excludeHoliday = excludeHoliday;
    }

    public boolean getExcludeDayOffs() {
        return excludeDayOffs;
    }

    public void setExcludeDayOffs(boolean excludeDayOffs) {
        this.excludeDayOffs = excludeDayOffs;
    }

    public Integer getWorkDaysInMonth() {
        return workDaysInMonth;
    }

    public void setWorkDaysInMonth(Integer workDaysInMonth) {
        this.workDaysInMonth = workDaysInMonth;
    }

    public String getDailyRateType() {
        return dailyRateType;
    }

    public void setDailyRateType(String dailyRateType) {
        this.dailyRateType = dailyRateType;
    }

    public DailyRateSettings getRPC() {
        DailyRateSettings rpc = new DailyRateSettings();
        rpc.setDailyRateType(getDailyRateType());
        rpc.setWorkDaysInMonth(getWorkDaysInMonth());
        rpc.setExcludeDayOffs(getExcludeDayOffs());
        rpc.setExcludeHoliday(getExcludeHoliday());
        return rpc;
    }
}
