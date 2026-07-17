package com.edatasite.workforce.rest.base.helpers;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.LinkedHashMap;

/**
 * Created by Dilsh0d Madrahimov on 11/30/2016 6:38 PM.
 */
public class MListingFilterParameter implements IsSerializable {

    private Integer objectId;
    private Integer employeeId;
    private Integer entityId;
    private Integer currencyId;
    private Long startDate;
    private Long endDate;
    private Long date;
    private Integer year;
    private String from;
    private String to;

    private String relationType;
    private Integer relationId;
    private Integer relationToId;

    private String startDateStr;
    private String endDateStr;

    private Integer categoryId;
    private Boolean showChild;

    private String status;
    private Integer statusId;
    private String[] statusCodes;

    private Boolean active;
    private Boolean paid;
    private Boolean takenFromAnnualLeaveAllowance;

    private String searchKey;
    private String number;

    private Integer start;
    private Integer limit;
    private Integer sortDir; // 1 - Asc, 2 - Desc
    private String sortField;

    private DynamicDto customReplacements;

    public MListingFilterParameter() {

    }

    public MListingFilterParameter(String searchKey, Integer start, Integer limit) {
        this.searchKey = searchKey;
        this.start = start;
        this.limit = limit;
    }

    public ListingFilterParameter convertToFilterParameters() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setObjectId(this.objectId);
        fp.setEntityID(this.entityId);
        fp.setEmployeeId(this.employeeId);
        fp.setStartDate(WrapUtils.longToDate(this.startDate));
        fp.setEndDate(WrapUtils.longToDate(this.endDate));
        fp.setDate(WrapUtils.longToDate(this.date));
        fp.setSearchKey(this.searchKey);
        fp.setStart(this.start == null ? 0 : this.start);
        fp.setLimit(this.limit == null ? 20 : this.limit);
        fp.setSortDir(this.sortDir);
        fp.setSortField(this.sortField);
        fp.setStartDateStr(this.startDateStr);
        fp.setEndDateStr(this.endDateStr);
        fp.setCategoryID(this.categoryId);
        fp.setShowChild(this.showChild);
        fp.setPresentActive(this.active);
        fp.setStatusCode(this.status);
        fp.setStatusID(this.statusId);
        fp.setYear(this.year);
        fp.setFromMobile(true);
        fp.setStatusCodes(this.statusCodes);
        fp.setRelationType(this.relationType);
        fp.setRelationID(this.relationId);
        fp.setRelationToID(this.relationToId);
        fp.setPaid(this.paid);
        fp.setNumber(this.number);
        if (this.startDate != null) {
            fp.setStartDateNC(ApiConstants.dateFormatFilter.format(ServerUtils.getStartDate(WrapUtils.longToDate(this.startDate))));
        }
        if (this.endDate != null) {
            fp.setStartDateNC(ApiConstants.dateFormatFilter.format(ServerUtils.getStartDate(WrapUtils.longToDate(this.endDate))));
        }

        return fp;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getSortDir() {
        return sortDir;
    }

    public void setSortDir(Integer sortDir) {
        this.sortDir = sortDir;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Boolean isPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean isTakenFromAnnualLeaveAllowance() {
        if (takenFromAnnualLeaveAllowance == null) {
            takenFromAnnualLeaveAllowance = false;
        }
        return takenFromAnnualLeaveAllowance;
    }

    public void setTakenFromAnnualLeaveAllowance(Boolean takenFromAnnualLeaveAllowance) {
        this.takenFromAnnualLeaveAllowance = takenFromAnnualLeaveAllowance;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Boolean getShowChild() {
        return showChild;
    }

    public void setShowChild(Boolean showChild) {
        this.showChild = showChild;
    }

    public DynamicDto getCustomReplacements() {
        return customReplacements;
    }

    public void setCustomReplacements(DynamicDto customReplacements) {
        this.customReplacements = customReplacements;
    }
}
