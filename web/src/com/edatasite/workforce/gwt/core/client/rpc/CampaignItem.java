package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 18:22:40
 */
public class CampaignItem implements IsSerializable {

    public static final String ACTION = "action";
    public static final String OWNER = "owner";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String STATUS = "status";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";

    private Integer objectId;
    private String name;

    private SelectItem[] types;
    private Integer typeId;
    private String type;

    private SelectItem[] assignees;
    private Integer assigneeId;
    private String assignee;

    private SelectItem[] statuss;
    private Integer statusId;
    private String status;
    private String statusCode;
    private String typeCode;

    private Date startDate;
    private Date endDate;

    private Double expectedRevenue;
    private Double budgetCost;
    private Double actualCost;
    private Double expectedResponse;
    private String numberSent;
    private HistoryList history;

    private ArrayList<HistoryListItem> notes;
    private SelectItem user;

    public ArrayList<HistoryListItem> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<HistoryListItem> notes) {
        this.notes = notes;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SelectItem[] getTypes() {
        return types;
    }

    public void setTypes(SelectItem[] types) {
        this.types = types;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SelectItem[] getStatuss() {
        return statuss;
    }

    public void setStatuss(SelectItem[] statuss) {
        this.statuss = statuss;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getExpectedRevenue() {
        return expectedRevenue;
    }

    public void setExpectedRevenue(Double expectedRevenue) {
        this.expectedRevenue = expectedRevenue;
    }

    public Double getBudgetCost() {
        return budgetCost;
    }

    public void setBudgetCost(Double budgetCost) {
        this.budgetCost = budgetCost;
    }

    public Double getActualCost() {
        return actualCost;
    }

    public void setActualCost(Double actualCost) {
        this.actualCost = actualCost;
    }

    public Double getExpectedResponse() {
        return expectedResponse;
    }

    public void setExpectedResponse(Double expectedResponse) {
        this.expectedResponse = expectedResponse;
    }

    public String getNumberSent() {
        return numberSent;
    }

    public void setNumberSent(String numberSent) {
        this.numberSent = numberSent;
    }

    public SelectItem[] getAssignees() {
        return assignees;
    }

    public void setAssignees(SelectItem[] assignees) {
        this.assignees = assignees;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public HistoryList getHistory() {
        return history;
    }

    public void setHistory(HistoryList history) {
        this.history = history;
    }

    public void setUser(SelectItem user) {
        this.user=user;
    }

    public SelectItem getUser() {
        return user;
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<CampaignItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (CampaignItem item : selectedItems) {
            ids.add(item.getObjectId());
        }
        return ids;
    }
}