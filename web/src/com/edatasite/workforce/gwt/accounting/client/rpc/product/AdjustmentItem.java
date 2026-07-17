package com.edatasite.workforce.gwt.accounting.client.rpc.product;

import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 11/23/11
 * Time: 2:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdjustmentItem extends HasApprovers {

    public static final String NUMBER_EXISTS = "NUMBER_EXISTS";
    public static final String STATUS = "status";

    private Integer objectID;

    private DateNonConvertable date;

    private ProductItem[] productItems;

    private SelectItem account;

    private ProductSelectItem product;

    private String memo;

    private String number;

    private Boolean isStockTransfer;

    private Integer fromWarehouseID;

    private Integer toWarehouseID;

    private Integer journalID;

    private Integer intNumber;
    private BankTransferNumberData bankTransferNumberData;

    private FileItem[] attachments;

    private ArrayList<Integer> rfpIds;

    private String statusCode;

    private HistoryListItem[] historyList;

    private Integer currentUserId;
    private boolean isApprover;
    private SelectItem approver;
    private boolean isApproverSaved;
    private Date creationTime;
    private Date lastUpdateTime;
    private SelectItem creator;
    private SelectItem updater;
    private String type;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public ProductItem[] getProductItems() {
        return productItems;
    }

    public void setProductItems(ProductItem[] productItems) {
        this.productItems = productItems;
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }

    public ProductSelectItem getProduct() {
        return product;
    }

    public void setProduct(ProductSelectItem product) {
        this.product = product;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Boolean isStockTransfer() {
        return isStockTransfer != null ? isStockTransfer : false;
    }

    public void setStockTransfer(Boolean stockTransfer) {
        isStockTransfer = stockTransfer;
    }

    public Integer getFromWarehouseID() {
        return fromWarehouseID;
    }

    public void setFromWarehouseID(Integer fromWarehouseID) {
        this.fromWarehouseID = fromWarehouseID;
    }

    public Integer getToWarehouseID() {
        return toWarehouseID;
    }

    public void setToWarehouseID(Integer toWarehouseID) {
        this.toWarehouseID = toWarehouseID;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public ArrayList<Integer> getRfpIds() {
        if (rfpIds == null) rfpIds = new ArrayList<>();
        return rfpIds;
    }

    public void setRfpIds(ArrayList<Integer> rfpIds) {
        this.rfpIds = rfpIds;
    }

    public Boolean getStockTransfer() {
        return isStockTransfer;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public BankTransferNumberData getBankTransferNumberData() {
        return bankTransferNumberData;
    }

    public void setBankTransferNumberData(BankTransferNumberData bankTransferNumberData) {
        this.bankTransferNumberData = bankTransferNumberData;
    }

    public Integer getJournalID() {
        return this.journalID;
    }

    public void setJournalID(final Integer journalID) {
        this.journalID = journalID;
    }

    public String getStatusCode() {
        return this.statusCode;
    }

    public void setStatusCode(final String statusCode) {
        this.statusCode = statusCode;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public HistoryListItem[] getHistoryList() {
        return this.historyList;
    }

    public void setHistoryList(final HistoryListItem[] historyList) {
        this.historyList = historyList;
    }

    public Integer getCurrentUserId() {
        return this.currentUserId;
    }

    public void setCurrentUserId(final Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public boolean isApprover() {
        return this.isApprover;
    }

    public void setApprover(final boolean approver) {
        this.isApprover = approver;
    }

    public SelectItem getApprover() {
        return this.approver;
    }

    public void setApprover(final SelectItem approver) {
        this.approver = approver;
    }

    public boolean isApproverSaved() {
        return this.isApproverSaved;
    }

    public void setApproverSaved(final boolean approverSaved) {
        this.isApproverSaved = approverSaved;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setUpdater(SelectItem updater) {
        this.updater = updater;
    }

    public SelectItem getUpdater() {
        return updater;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
