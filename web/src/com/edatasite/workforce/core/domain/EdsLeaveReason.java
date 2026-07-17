package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsAudit;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.enums.HistoryType;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.enums.Gender;
import com.edatasite.workforce.gwt.core.client.enums.TypeOption;
import com.edatasite.workforce.gwt.core.client.enums.UnitType;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.apache.commons.lang.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Hurshid on 12/15/2018
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "leave_reason")
public class EdsLeaveReason extends EdsAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String code;

    private String name;

    private String description;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean isSystemReference = false;

    @Column(name = "isActive", columnDefinition = " boolean DEFAULT true")
    private boolean isActive = true;

    @Column(name = "hasProrata", columnDefinition = " boolean DEFAULT false")
    private boolean hasProrata = false;

    @Column(name = "probationDays", columnDefinition = "Decimal(10,2) default 0.00")
    private Double probationDays = 0.0;

    @Column(name = "leaveDays", nullable = false, columnDefinition = "Decimal(10,2) default 0.00")
    private Double leaveDays = 0.0;

    @Column(name = "shortname")
    private String shortName;

    @Column(name = "attendanceLR", columnDefinition = " boolean DEFAULT false")
    private Boolean attendanceLR;

    @Column(name = "autoApprove", columnDefinition = " boolean DEFAULT false")
    private Boolean autoApprove;

    @Column(name = "color")
    private String color;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private UnitType unitType;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localeId")
    private EdsReferenceLocale locale;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(32) default 'NOT_ALLOW_EXCEED_ALLOWANCE'")
    private TypeOption typeOption = TypeOption.NOT_ALLOW_EXCEED_ALLOWANCE;

    private Boolean includeDayOffs;

    @Column(name = "exceptional_timeslot")
    private Boolean exceptionalTimeslot;

    private Boolean includeHolidays;

    private Date updatedDate;
    @Column(name = "effective_date")
    private Date effectiveDate;

    @OneToMany(mappedBy = "reasonId", fetch = FetchType.LAZY)
    private List<EdsLeaveReasonHistory> leaveReasonHistoryList = new ArrayList<>();

    @Column(name = "deleted", columnDefinition = " boolean DEFAULT false")
    private Boolean deleted;

    private BigDecimal duration;

    @Column(name = "integration_code")
    private String integrationCode;
    @Column(name = "redirect_url")
    private String redirectUrl;
    private Integer minLeaveDays;
    @Column(name = "mark_as_draft")
    private Boolean markAsDraft;

    @Override
    public HistoryType getHistoryType() {
        return HistoryType.LEAVE_REASON;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        if (getLocale() != null) {
            String lang = ServerUtils.getUserLocale().getLanguage();
            if (StringUtils.isNotBlank(getLocale().getLocaleByCode(lang))) {
                return getLocale().getLocaleByCode(lang);
            }
        }
        return name;
    }

    public String getRealName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (!ServerUtils.equalsString(this.description, description)) {
            addHistoryChange("Description", this.description, description);
        }
        this.description = description;
    }

    public boolean isSystemReference() {
        return isSystemReference;
    }

    public void setSystemReference(boolean systemReference) {
        isSystemReference = systemReference;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        if (!Objects.equals(this.isActive, active)) {
            addHistoryChange("Active", this.isActive, active);
        }
        isActive = active;
    }

    public boolean hasProrata() {
        return hasProrata;
    }

    public void setHasProrata(boolean hasProrata) {
        if (!Objects.equals(this.hasProrata, hasProrata)) {
            addHistoryChange("Prorata", this.hasProrata, hasProrata);
        }
        this.hasProrata = hasProrata;
    }

    public Double getProbationDays() {
        return probationDays;
    }

    public void setProbationDays(Double probationDays) {
        if (!ServerUtils.equalsDouble(this.probationDays, probationDays)) {
            addHistoryChange("Probation Days", this.probationDays, probationDays);
        }
        this.probationDays = probationDays;
    }

    public Double getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Double leaveDays) {
        if (!ServerUtils.equalsDouble(this.leaveDays, leaveDays)) {
            addHistoryChange("Leave Days", this.leaveDays, leaveDays);
        }
        this.leaveDays = leaveDays;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        if (!ServerUtils.equalsString(this.shortName, shortName)) {
            addHistoryChange("Short Name", this.shortName, shortName);
        }
        this.shortName = shortName;
    }

    public void setAttendanceLR(Boolean attendanceLR) {
        this.attendanceLR = attendanceLR;
    }

    public void setAutoApprove(Boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        if (!ServerUtils.equalsString(this.color, color)) {
            addHistoryChange("Color", this.color, color);
        }
        this.color = color;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        if (!Objects.equals(this.unitType, unitType)) {
            addHistoryChange("Leave Unit", this.unitType != null ? this.unitType.name() : "", unitType != null ? unitType.name() : "");
        }
        this.unitType = unitType;
    }

    public TypeOption getTypeOption() {
        return typeOption;
    }

    public void setTypeOption(TypeOption typeOption) {
        if (!Objects.equals(this.typeOption, typeOption)) {
            addHistoryChange("Type Option", this.typeOption != null ? this.typeOption.name() : "", typeOption != null ? typeOption.name() : "");
        }
        this.typeOption = typeOption;
    }

    public void setIncludeDayOffs(Boolean includeDayOffs) {
        if (!Objects.equals(this.includeDayOffs, includeDayOffs)) {
            addHistoryChange("Include Day Offs", this.includeDayOffs, includeDayOffs);
        }
        this.includeDayOffs = includeDayOffs;
    }

    public Boolean getExceptionalTimeslot() {
        if (exceptionalTimeslot == null) {
            exceptionalTimeslot = Boolean.FALSE;
        }
        return exceptionalTimeslot;
    }

    public void setExceptionalTimeslot(Boolean saturdayIsWorkingDay) {
        if (!Objects.equals(this.exceptionalTimeslot, saturdayIsWorkingDay)) {
            addHistoryChange("Include Day Offs", this.exceptionalTimeslot, saturdayIsWorkingDay);
        }
        this.exceptionalTimeslot = saturdayIsWorkingDay;
    }

    public void setIncludeHolidays(Boolean includeHolidays) {
        this.includeHolidays = includeHolidays;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getIncludeDayOffs() {
        if (includeDayOffs == null) {
            includeDayOffs = Boolean.FALSE;
        }
        return includeDayOffs;
    }

    public Boolean getIncludeHolidays() {
        if (includeHolidays == null) {
            includeHolidays = Boolean.FALSE;
        }
        return includeHolidays;
    }

    public Boolean getAttendanceLR() {
        if (attendanceLR == null) {
            return false;
        }
        return attendanceLR;
    }

    public Boolean getAutoApprove() {
        if (autoApprove == null) {
            return false;
        }
        return autoApprove;
    }

    public List<ReasonItem> getLeaveReasonRpcList() {
        List<EdsLeaveReasonHistory> list = getLeaveReasonHistoryList();
        List<ReasonItem> itemList = new ArrayList<>();
        for (EdsLeaveReasonHistory reason : list) {
            ReasonItem item = new ReasonItem();
            item = reason.toRPC();
            itemList.add(item);
        }
        return itemList;
    }

    public ReasonItem toRPC() {
        ReasonItem item = new ReasonItem(getObjectID(), getRealName(), getCode());
        item.setDescription(StringUtils.isEmpty(getDescription()) ? getCode() : getDescription());
        item.setActive(isActive());
        item.setSystemReference(isSystemReference());
        item.setLeaveDaysOld(getLeaveDays());
        item.setProbationDays(getProbationDays());
        item.setIncludeHolidays(getIncludeHolidays());
        item.setIncludeDayOffs(getIncludeDayOffs());
        item.setExceptionalWorkingDay(getExceptionalTimeslot());
        if (getColor() != null && getColor().length() > 0) {
            item.setHexColor(getColor().replace("#", ""));
        }
        item.setShortName(getShortName());
        item.setAttendanceLR(getAttendanceLR());
        item.setAutoApprove(getAutoApprove());
        item.setHasProrata(hasProrata());
        item.setGender(getGender());
        item.setUnitType(getUnitType());
        item.setTypeOption(getTypeOption());
        item.setDuration(getDuration());
        item.setLocaleItem(getLocale() != null ? getLocale().toRPC() : null);
        item.setReasonHistoryList(getLeaveReasonRpcList());
        item.setIntegrationCode(getIntegrationCode());
        item.setRedirectUrl(getRedirectUrl());
        item.setMarkAsDraft(getMarkAsDraft());
        return item;
    }

    public BigDecimal getDuration() {
        return duration;
    }

    public void setDuration(BigDecimal duration) {
        this.duration = duration;
    }

    public EdsReferenceLocale getLocale() {
        return locale;
    }

    public void setLocale(EdsReferenceLocale locale) {
        this.locale = locale;
    }

    public List<EdsLeaveReasonHistory> getLeaveReasonHistoryList() {
        return leaveReasonHistoryList;
    }

    public void setLeaveReasonHistoryList(List<EdsLeaveReasonHistory> leaveReasonHistoryList) {
        this.leaveReasonHistoryList = leaveReasonHistoryList;
    }

    public String getIntegrationCode() {
        return integrationCode;
    }

    public void setIntegrationCode(String integrationCode) {
        this.integrationCode = integrationCode;
    }

    public Integer getMinLeaveDays() {
        return minLeaveDays;
    }

    public void setMinLeaveDays(Integer minLeaveDays) {
        this.minLeaveDays = minLeaveDays;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public Boolean getMarkAsDraft() {
        return markAsDraft != null ? markAsDraft : false;
    }

    public void setMarkAsDraft(Boolean markAsDraft) {
        this.markAsDraft = markAsDraft;
    }
}
