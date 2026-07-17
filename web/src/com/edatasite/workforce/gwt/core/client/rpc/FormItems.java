package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FormItems implements IsSerializable, ListingCustomFields {

    public static final String STATUS = "status";
    public static final String UPDATED_DATE = "updated_date";
    public static final String UPDATER = "updater";
    public static final String CREATED_DATE = "created_date";
    public static final String CREATER = "creator";
    public static final String APPROVER = "approver";
    public static final String QUIZ = "quiz";  /// add to PDf or excel generation
    private Integer objectID;
    private String objectKey;
    private String formID;
    private String formName;
    private Boolean hasApproval;
    private boolean isCopy;
    private String entityName;

    private Date modifiedData;
    private Date createdDate;
    private String creator;
    private String updater;
    private String autoNumber;
    private String status;
    private String statusCode;
    private Integer currentApproverId;
    private String currentApproverName;
    private String prevApproverName;
    private Integer currentUserId;
    private ArrayList<RelationItem> relations;
    private ArrayList<ApproverItemMini> approvers;
    private SelectItem[] templates;
    private SelectItem currentApproverSelectItem;

    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldsMap;
    private HashMap<String, ArrayList<CustomTableRpc>> tableItems = new HashMap<>();
    private String timer;
    private String durationTime;
    private String welcomeMessage;
    private String endOfTimeMessage;
    private Integer attempt;
    private String timerStartedAt;
    private Boolean isQuizForm;
    private String quizResult;
    private boolean isAnonymous;
    private Integer relationId;
    private String relationObjectKey;
    private String relationType;
    private BigDecimal score;


    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public Boolean getHasApproval() {
        return hasApproval != null ? hasApproval : false;
    }

    public void setHasApproval(Boolean hasApproval) {
        this.hasApproval = hasApproval;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public Date getModifiedData() {
        return modifiedData;
    }

    public void setModifiedData(Date modifiedData) {
        this.modifiedData = modifiedData;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public HashMap<String, ArrayList<CustomTableRpc>> getTableItems() {
        return tableItems;
    }

    public void setTableItems(HashMap<String, ArrayList<CustomTableRpc>> tableItems) {
        this.tableItems = tableItems;
    }

    public String getAutoNumber() {
        return this.autoNumber;
    }

    public void setAutoNumber(final String autoNumber) {
        this.autoNumber = autoNumber;
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

    public Integer getCurrentApproverId() {
        return currentApproverId;
    }

    public void setCurrentApproverId(Integer currentApproverId) {
        this.currentApproverId = currentApproverId;
    }

    public String getCurrentApproverName() {
        return currentApproverName;
    }

    public void setCurrentApproverName(String currentApproverName) {
        this.currentApproverName = currentApproverName;
    }

    public String getPrevApproverName() {
        return prevApproverName;
    }

    public void setPrevApproverName(String prevApproverName) {
        this.prevApproverName = prevApproverName;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public ArrayList<RelationItem> getRelations() {
        return relations;
    }

    public void setRelations(ArrayList<RelationItem> relations) {
        this.relations = relations;
    }

    public ArrayList<ApproverItemMini> getApprovers() {
        return approvers;
    }

    public void setApprovers(ArrayList<ApproverItemMini> approvers) {
        this.approvers = approvers;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public boolean isCopy() {
        return this.isCopy;
    }

    public void setCopy(final boolean copy) {
        this.isCopy = copy;
    }

    public SelectItem getCurrentApproverSelectItem() {
        return currentApproverSelectItem;
    }

    public void setCurrentApproverSelectItem(SelectItem currentApproverSelectItem) {
        this.currentApproverSelectItem = currentApproverSelectItem;
    }

    public String getEntityName() {
        return this.entityName;
    }

    public void setEntityName(final String entityName) {
        this.entityName = entityName;
    }

    public String getTimer() {
        return timer;
    }

    public void setTimer(String timer) {
        this.timer = timer;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public String getEndOfTimeMessage() {
        return endOfTimeMessage;
    }

    public void setEndOfTimeMessage(String endOfTimeMessage) {
        this.endOfTimeMessage = endOfTimeMessage;
    }

    public Integer getAttempt() {
        return attempt;
    }

    public void setAttempt(Integer attempt) {
        this.attempt = attempt;
    }

    public String getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(String durationTime) {
        this.durationTime = durationTime;
    }

    public String getTimerStartedAt() {
        return timerStartedAt;
    }

    public void setTimerStartedAt(String timerStartedAt) {
        this.timerStartedAt = timerStartedAt;
    }

    public Boolean getQuizForm() {
        return isQuizForm;
    }

    public void setQuizForm(Boolean quizForm) {
        isQuizForm = quizForm;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getQuizResult() {
        return quizResult;
    }

    public void setQuizResult(String quizResult) {
        this.quizResult = quizResult;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getRelationObjectKey() {
        return relationObjectKey;
    }

    public void setRelationObjectKey(String relationObjectKey) {
        this.relationObjectKey = relationObjectKey;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
