package com.edatasite.workforce.gwt.accounting.client.rpc.product;

import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dilshod Madrahimov on 2/25/15.
 */
public class StockTransferItem extends HasApprovers implements IsSerializable, Serializable {

    public static final String ACTION = "action";
    public static final String TRANSFER_NAME = "transferName";
    public static final String TRANSFER_NUMBER = "transferNumber";
    public static final String TRANSFER_DATE = "date";
    public static final String STATUS = "status";

    private Integer objectId;
    private String transferName;
    private ArrayList<AdjustmentItem> adjustmentItemList = new ArrayList<>();
    private FileItem[] attachments;
    private FileResource[] attachmentResources;
    private HistoryListItem[] historyList;
    private String statusCode;
    private DateNonConvertable date;
    private Date ncDate;
    private String layoutHtml;
    private String number;
    private Integer intNumber;
    private SelectItem[] templates;
    private Integer selectedTemplateId;
    private Integer currentUserId;
    private boolean isApprover;
    private SelectItem approver;
    private boolean isApproverSaved;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getTransferName() {
        return transferName;
    }

    public void setTransferName(String transferName) {
        this.transferName = transferName;
    }

    public ArrayList<AdjustmentItem> getAdjustmentItemList() {
        return adjustmentItemList;
    }

    public void setAdjustmentItemList(ArrayList<AdjustmentItem> adjustmentItemList) {
        this.adjustmentItemList = adjustmentItemList;
    }

    public String getLayoutHtml() {
        return layoutHtml;
    }

    public void setLayoutHtml(String layoutHtml) {
        this.layoutHtml = layoutHtml;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public FileResource[] getAttachmentResources() {
        return attachmentResources;
    }

    public void setAttachmentResources(FileResource[] attachmentResources) {
        this.attachmentResources = attachmentResources;
    }

    public HistoryListItem[] getHistoryList() {
        return historyList;
    }

    public void setHistoryList(HistoryListItem[] historyList) {
        this.historyList = historyList;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public Date getNcDate() {
        return ncDate;
    }

    public void setNcDate(Date ncDate) {
        this.ncDate = ncDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public Integer getSelectedTemplateId() {
        return selectedTemplateId;
    }

    public void setSelectedTemplateId(Integer selectedTemplateId) {
        this.selectedTemplateId = selectedTemplateId;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public boolean isApprover() {
        return isApprover;
    }

    public void setApprover(boolean approver) {
        isApprover = approver;
    }

    public boolean isApproverSaved() {
        return isApproverSaved;
    }

    public void setApproverSaved(boolean approverSaved) {
        isApproverSaved = approverSaved;
    }
}
