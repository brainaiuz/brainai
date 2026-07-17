package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.enums.Gender;
import com.edatasite.workforce.gwt.core.client.enums.LeaveReasonType;
import com.edatasite.workforce.gwt.core.client.enums.TypeOption;
import com.edatasite.workforce.gwt.core.client.enums.UnitType;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hurshid on 12/15/2018
 */
public class ReasonItem extends SelectItem {

    public static final Integer NAME_EXISTS = -1;
    public static final Integer SHORT_NAME_EXISTS = -2;

    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CODE = "code";

    private Integer id;
    private String name;
    private String code;
    private String description;
    private boolean isSystemReference;
    private Double leaveDaysOld = 0.0;
    private Double leaveDaysNew = 0.0;
    private Double probationDays = 0.0;
    private boolean isActive;
    private boolean includeHolidays;
    private boolean includeDayOffs;
    private boolean exceptionalWorkingDay;
    private String shortName;
    private String hexColor;
    private boolean attendanceLR;
    private boolean autoApprove;
    private boolean hasProrata;
    private Gender gender;
    private UnitType unitType;
    private TypeOption typeOption;
    private Map<LeaveReasonType, List<SelectItem>> relationMap;
    private boolean isLeaveToAll;
    private boolean isProbationToAll;
    private DateNonConvertable openingBalanceDate;
    private ReferenceLocale referenceLocale;
    private ReferenceLocale localeItem;
    private DateNonConvertable effectiveFrom;
    private String effectiveDate;
    private DateNonConvertable modifiedDate;
    private String modifiedBy;
    private List<ReasonItem> reasonHistoryList;
    private String integrationCode;
    private String redirectUrl;
    private boolean markAsDraft;
    private List<LeaveReasonLocationDaysItem> locationDaysList;

    public List<ReasonItem> getReasonHistoryList() {
        return reasonHistoryList;
    }

    public void setReasonHistoryList(List<ReasonItem> reasonHistoryList) {
        this.reasonHistoryList = reasonHistoryList;
    }

    public ReasonItem() {
    }

    public ReasonItem(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSystemReference() {
        return isSystemReference;
    }

    public void setSystemReference(boolean systemReference) {
        isSystemReference = systemReference;
    }

    public Double getLeaveDaysOld() {
        if (leaveDaysOld == null) {
            return 0d;
        }
        return leaveDaysOld;
    }

    public void setLeaveDaysOld(Double leaveDaysOld) {
        this.leaveDaysOld = leaveDaysOld;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isIncludeHolidays() {
        return includeHolidays;
    }

    public void setIncludeHolidays(boolean includeHolidays) {
        this.includeHolidays = includeHolidays;
    }

    public boolean isIncludeDayOffs() {
        return includeDayOffs;
    }

    public void setIncludeDayOffs(boolean includeDayOffs) {
        this.includeDayOffs = includeDayOffs;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public boolean isAttendanceLR() {
        return attendanceLR;
    }

    public void setAttendanceLR(boolean attendanceLR) {
        this.attendanceLR = attendanceLR;
    }

    public boolean isAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public boolean isHasProrata() {
        return hasProrata;
    }

    public void setHasProrata(boolean hasProrata) {
        this.hasProrata = hasProrata;
    }

    public void addRelationType(LeaveReasonType type, List<SelectItem> items) {
        getRelationMap().put(type, items);
    }

    public Map<LeaveReasonType, List<SelectItem>> getRelationMap() {
        if (relationMap == null) {
            relationMap = new HashMap<>();
        }
        return relationMap;
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
        this.unitType = unitType;
    }

    public TypeOption getTypeOption() {
        return typeOption;
    }

    public void setTypeOption(TypeOption typeOption) {
        this.typeOption = typeOption;
    }

    public Double getProbationDays() {
        if (probationDays == null) {
            probationDays = 0d;
        }
        return probationDays;
    }

    public void setProbationDays(Double probationDays) {
        this.probationDays = probationDays;
    }

    public boolean isLeaveToAll() {
        return isLeaveToAll;
    }

    public void setLeaveToAll(boolean leaveToAll) {
        isLeaveToAll = leaveToAll;
    }

    public boolean isProbationToAll() {
        return isProbationToAll;
    }

    public void setProbationToAll(boolean probationToAll) {
        isProbationToAll = probationToAll;
    }

    public DateNonConvertable getOpeningBalanceDate() {
        return openingBalanceDate;
    }

    public void setOpeningBalanceDate(DateNonConvertable openingBalanceDate) {
        this.openingBalanceDate = openingBalanceDate;
    }

    public boolean isExceptionalWorkingDay() {
        return exceptionalWorkingDay;
    }

    public void setExceptionalWorkingDay(boolean exceptionalWorkingDay) {
        this.exceptionalWorkingDay = exceptionalWorkingDay;
    }

    public ReferenceLocale getReferenceLocale() {
        return referenceLocale;
    }

    public void setReferenceLocale(ReferenceLocale referenceLocale) {
        this.referenceLocale = referenceLocale;
    }

    public ReferenceLocale getLocaleItem() {
        return localeItem;
    }

    public void setLocaleItem(ReferenceLocale localeItem) {
        this.localeItem = localeItem;
    }

    public DateNonConvertable getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(DateNonConvertable effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public DateNonConvertable getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(DateNonConvertable modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Double getLeaveDaysNew() {
        if (leaveDaysNew == null) {
            return 0d;
        }
        return leaveDaysNew;
    }

    public void setLeaveDaysNew(Double leaveDaysNew) {
        this.leaveDaysNew = leaveDaysNew;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getIntegrationCode() {
        return integrationCode;
    }

    public void setIntegrationCode(String integrationCode) {
        this.integrationCode = integrationCode;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public boolean isMarkAsDraft() {
        return markAsDraft;
    }

    public void setMarkAsDraft(boolean markAsDraft) {
        this.markAsDraft = markAsDraft;
    }

    public List<LeaveReasonLocationDaysItem> getLocationDaysList() {
        if (locationDaysList == null) {
            locationDaysList = new ArrayList<>();
        }
        return locationDaysList;
    }

    public void setLocationDaysList(List<LeaveReasonLocationDaysItem> locationDaysList) {
        this.locationDaysList = locationDaysList;
    }
}
