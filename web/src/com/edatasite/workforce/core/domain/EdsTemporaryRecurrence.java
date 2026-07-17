package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Sardorbek Khalimboev
 * Date: February 12, 2022
 * Time: 12:02:17 PM
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "temporary_recurrence")
public class EdsTemporaryRecurrence extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Integer type;

    private Date startDate;

    private Integer endType;

    private Boolean endaftercertainoccur;

    private Date endDate;

    private Integer dailyPatternOptions;

    private Integer occurrence;

    private Integer interval;

    private Boolean sunday = false;

    private Boolean monday = false;

    private Boolean tuesday = false;

    private Boolean wednesday = false;

    private Boolean thursday = false;

    private Boolean friday = false;

    private Boolean saturday = false;

    private Integer monthlyOrYearlyPatternOption;

    private Integer monthlyOrYearlyDay;

    private Integer yearlyMonth;

    private Integer customPatternDayNumber;

    private Integer customPatternDay;

    //  ---------------------------- Business objects ----------------------------

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "jobId")
    private EdsRecurrenceJob job;

    private String userTimeZone;

    private Integer busObjectId;

    private String busObjectParams;

    private boolean changed = false;

    private boolean toMe = false;

    private boolean toClient = false;

    private boolean deleted = false;

    private Integer companyID;

    private Integer userID;

    private Date extendDate;

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer attempts = 0;

    private String status;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Integer getEndType() {
        return endType;
    }

    public void setEndType(Integer endType) {
        this.endType = endType;
    }

    public Boolean isEndaftercertainoccur() {
        return endaftercertainoccur;
    }

    public void setEndaftercertainoccur(Boolean endaftercertainoccur) {
        this.endaftercertainoccur = endaftercertainoccur;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getOccurrence() {
        return occurrence;
    }

    public void setOccurrence(Integer occurrence) {
        this.occurrence = occurrence;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Boolean isSunday() {
        return sunday;
    }

    public void setSunday(Boolean sunday) {
        this.sunday = sunday;
    }

    public Boolean isMonday() {
        return monday;
    }

    public void setMonday(Boolean monday) {
        this.monday = monday;
    }

    public Boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(Boolean tuesday) {
        this.tuesday = tuesday;
    }

    public Boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(Boolean wednesday) {
        this.wednesday = wednesday;
    }

    public Boolean isThursday() {
        return thursday;
    }

    public void setThursday(Boolean thursday) {
        this.thursday = thursday;
    }

    public Boolean isFriday() {
        return friday;
    }

    public void setFriday(Boolean friday) {
        this.friday = friday;
    }

    public Boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(Boolean saturday) {
        this.saturday = saturday;
    }

    public Integer getMonthlyOrYearlyDay() {
        return monthlyOrYearlyDay;
    }

    public void setMonthlyOrYearlyDay(Integer montlyOrYearlyDay) {
        this.monthlyOrYearlyDay = montlyOrYearlyDay;
    }

    public Integer getYearlyMonth() {
        return yearlyMonth;
    }

    public void setYearlyMonth(Integer yearlyMonth) {
        this.yearlyMonth = yearlyMonth;
    }

    public Integer getDailyPatternOptions() {
        return dailyPatternOptions;
    }

    public void setDailyPatternOptions(Integer dailyPatternOptions) {
        this.dailyPatternOptions = dailyPatternOptions;
    }

    public Integer getCustomPatternDayNumber() {
        return customPatternDayNumber;
    }

    public void setCustomPatternDayNumber(Integer customPatternDayNumber) {
        this.customPatternDayNumber = customPatternDayNumber;
    }

    public Integer getCustomPatternDay() {
        return customPatternDay;
    }

    public void setCustomPatternDay(Integer customPatternDay) {
        this.customPatternDay = customPatternDay;
    }

    public Integer getMonthlyOrYearlyPatternOption() {
        return monthlyOrYearlyPatternOption;
    }

    public void setMonthlyOrYearlyPatternOption(Integer monthlyOrYearlyPatternOption) {
        this.monthlyOrYearlyPatternOption = monthlyOrYearlyPatternOption;
    }

    public EdsRecurrenceJob getJob() {
        return job;
    }

    public void setJob(EdsRecurrenceJob job) {
        this.job = job;
    }

    public String getUserTimeZone() {
        return userTimeZone;
    }

    public void setUserTimeZone(String userTimeZone) {
        this.userTimeZone = userTimeZone;
    }

    public Integer getBusObjectId() {
        return busObjectId;
    }

    public void setBusObjectId(Integer busObjectId) {
        this.busObjectId = busObjectId;
    }

    public String getBusObjectParams() {
        return busObjectParams;
    }

    public void setBusObjectParams(String busObjectParams) {
        this.busObjectParams = busObjectParams;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean getToMe() {
        return toMe;
    }

    public void setToMe(boolean toMe) {
        this.toMe = toMe;
    }

    public boolean getToClient() {
        return toClient;
    }

    public void setToClient(boolean toClient) {
        this.toClient = toClient;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Date getExtendDate() {
        return extendDate;
    }

    public void setExtendDate(Date extendDate) {
        this.extendDate = extendDate;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RecurrenceJobItem createRecurrenceItem(Integer jobType) {
        RecurrenceJobItem item = new RecurrenceJobItem();
        item.setObjectId(getObjectID());
        item.setJobType(jobType);
        item.setBusObjectId(getBusObjectId());
        item.setBusObjectParams(getBusObjectParams());
        item.setType(getType());
        item.setStatus(getStatus());

        if (getDailyPatternOptions() != null) {
            item.setDailyPatternOptions(getDailyPatternOptions());
        }
        if (getInterval() != null) {
            item.setInterval(getInterval());
        }
        if (getStartDate() != null) {
            item.setStartDate(getStartDate());
        }
        if (getEndDate() != null) {
            item.setEndDate(getEndDate());
        }
        if (getOccurrence() != null) {
            item.setOccurrence(getOccurrence());
        }
        item.setSunday(isSunday());
        item.setMonday(isMonday());
        item.setTuesday(isTuesday());
        item.setWednesday(isWednesday());
        item.setThursday(isThursday());
        item.setFriday(isFriday());
        item.setSaturday(isSaturday());
        item.setEndType(getEndType());
        if (getMonthlyOrYearlyDay() != null) {
            item.setMonthlyOrYearlyDay(getMonthlyOrYearlyDay());
        }
        if (getCustomPatternDay() != null) {
            item.setCustomPatternDay(getCustomPatternDay());
        }
        if (getDailyPatternOptions() != null) {
            item.setDailyPatternOptions(getDailyPatternOptions());
        }
        if (getMonthlyOrYearlyPatternOption() != null) {
            item.setMonthlyOrYearlyPatternOption(getMonthlyOrYearlyPatternOption());
        }
        if (getMonthlyOrYearlyDay() != null) {
            item.setMonthlyOrYearlyDay(getMonthlyOrYearlyDay());
        }
        if (getYearlyMonth() != null) {
            item.setYearlyMonth(getYearlyMonth());
        }
        item.setToMe(getToMe());
        item.setToClient(getToClient());
        return item;
    }
}
