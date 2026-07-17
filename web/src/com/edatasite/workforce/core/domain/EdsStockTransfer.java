package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AdjustmentItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.StockTransferItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by Dilshod Madrahimov on 2/25/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "stockTransfer")
public class EdsStockTransfer extends EdsApprovable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "transferName")
    private String transferName;

    private Date date;

    @Column(columnDefinition = "boolean default false")
    private Boolean deleted = false;

    private String number;
    private Integer intNumber;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "stockTransferId")
    @Where(clause = "deleted = 'false' or deleted is null")
    @OrderBy("objectID")
    private List<EdsStockAdjustment> items = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'STOCK_TRANSFER'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator")
    private EdsUser creator;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getTransferName() {
        return transferName;
    }

    public void setTransferName(String transferName) {
        this.transferName = transferName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        if (!ServerUtils.equalsDate(this.date, date)) {
            addChange(CustomFormConstants.ACCOUNTING.STOCK_TRANSFER.DATE);
        }
        this.date = date;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<EdsStockAdjustment> getItems() {
        return items;
    }

    public void setItems(List<EdsStockAdjustment> items) {
        this.items = items;
    }


    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public String getNumber() {
        if (!ServerUtils.equalsString(this.number, number)) {
            addChange(CustomFormConstants.ACCOUNTING.STOCK_TRANSFER.NUMBER);
        }
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

    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    @Override
    public void setEntityStatus(EdsReference status) {
        setOverallStatus(status);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.STOCK_TRANSFER_APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.STOCK_TRANSFER_DECLINED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, Constants.STOCK_TRANSFER_DECLINED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, Constants.STOCK_TRANSFER_PENDING);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, Constants.STOCK_TRANSFER_DECLINED);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, Constants.STOCK_TRANSFER_DECLINED);
        }
        return null;
    }

    @Override
    public void updateRejectedStatus() {
        if (getOverallStatus() != null && Constants.STOCK_TRANSFER_DECLINED.equals(getOverallStatus().getCode())) {
            ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
            setEntityStatus(referenceManager.findReference(Constants.STOCK_TRANSFER_STATUS, Constants.STOCK_TRANSFER_SUBMITTED));
        }
    }

    public StockTransferItem getStockTransferItem() {
        StockTransferItem item = new StockTransferItem();
        item.setObjectId(getObjectID());
        item.setTransferName(getTransferName());
        item.setDate(new DateNonConvertable(date));
        return item;
    }

    public StockTransferItem getAsRPC() {
        StockTransferItem item = new StockTransferItem();
        item.setObjectId(getObjectID());
        item.setTransferName(getTransferName());
        item.setNumber(getNumber());
        item.setIntNumber(getIntNumber());
        if (getDate() != null) {
            item.setDate(new DateNonConvertable(getDate()));
        }
        if (getOverallStatus() != null) {
            item.setStatusCode(getOverallStatus().getCode());
        }

        ArrayList<AdjustmentItem> itemList = new ArrayList<>();
        if (getItems() != null && !getItems().isEmpty()) {
            for (EdsStockAdjustment edsStockAdjustment : getItems()) {
                AdjustmentItem adjustmentItem = new AdjustmentItem();
                adjustmentItem.setObjectID(edsStockAdjustment.getObjectID());
                if (!edsStockAdjustment.getAdjustmentItemList().isEmpty()) {
                    EdsAdjustmentItem edsAdjustmentItemFrom = edsStockAdjustment.getAdjustmentItemList().get(0);
                    adjustmentItem.setProduct(edsAdjustmentItemFrom.getItem().getAsProductSelectItem());

                    ProductItem fromWarehouseItem = new ProductItem();
                    fromWarehouseItem.setLineItemID(edsAdjustmentItemFrom.getObjectID());
                    if (edsStockAdjustment.getFromWarehouse() != null) {
                        fromWarehouseItem.setWarehouseId(edsStockAdjustment.getFromWarehouse().getObjectID());
                        fromWarehouseItem.setWarehouseName(edsStockAdjustment.getFromWarehouse().getName());
                    }
                    if (edsStockAdjustment.getFromAccount() != null) {
                        fromWarehouseItem.setAccountID(edsStockAdjustment.getFromAccount().getObjectID());
                        fromWarehouseItem.setAccount(edsStockAdjustment.getFromAccount().getName());
                    }
                    fromWarehouseItem.setUsedQty(edsAdjustmentItemFrom.getUsedQty());
                    fromWarehouseItem.setTrackBatchesEnabled(edsAdjustmentItemFrom.getItem().getTrackBatchesEnabled());

                    EdsAdjustmentItem edsAdjustmentItemTo = edsStockAdjustment.getAdjustmentItemList().get(1);
                    ProductItem toWarehouseItem = new ProductItem();
                    toWarehouseItem.setLineItemID(edsAdjustmentItemTo.getObjectID());
                    if (edsStockAdjustment.getToWarehouse() != null) {
                        toWarehouseItem.setWarehouseId(edsStockAdjustment.getToWarehouse().getObjectID());
                        toWarehouseItem.setWarehouseName(edsStockAdjustment.getToWarehouse().getName());
                    }
                    if (edsStockAdjustment.getToAccount() != null) {
                        toWarehouseItem.setAccountID(edsStockAdjustment.getToAccount().getObjectID());
                        toWarehouseItem.setAccount(edsStockAdjustment.getToAccount().getName());
                    }
                    toWarehouseItem.setNewQty(edsAdjustmentItemTo.getNewQty());
                    toWarehouseItem.setTrackBatchesEnabled(edsAdjustmentItemTo.getItem().getTrackBatchesEnabled());

                    ProductItem uom = new ProductItem();
                    if (edsAdjustmentItemTo.getMeasurement() != null &&
                            edsAdjustmentItemTo.getMeasurement().getObjectID() != null &&
                            edsAdjustmentItemTo.getMeasurement().getName() != null) {
                        uom.setUnitMeasurementId(edsAdjustmentItemTo.getMeasurement().getObjectID());
                        uom.setUnitMeasurementName(edsAdjustmentItemTo.getMeasurement().getName());
                    }

                    adjustmentItem.setProductItems(new ProductItem[]{fromWarehouseItem, toWarehouseItem, uom});

                    itemList.add(adjustmentItem);
                }
            }
        }

        item.setAdjustmentItemList(itemList);

        return item;
    }
}
