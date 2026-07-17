package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AdjustmentItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 11/23/11
 * Time: 3:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "stock_adjustment")
public class EdsStockAdjustment extends EdsApprovable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    private String number;
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private EdsAccount account;

    @Type(type = "text")
    private String memo;

    private Boolean stockTransfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromwarehouseid")
    private EdsWarehouse fromWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "towarehouseid")
    private EdsWarehouse toWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromaccountid")
    private EdsAccount fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toaccountid")
    private EdsAccount toAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockTransferId")
    @ForeignKey(name = "none")
    private EdsStockTransfer stockTrans;

    private Boolean deleted = false;

    private Integer intNumber;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "adjustment_id")
    @OrderBy("objectID")
    private List<EdsAdjustmentItem> adjustmentItemList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'STOCK_ADJUSTMENT'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdBy")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifiedBy")
    private EdsUser updater;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @Column(name = "type")
    private String type; // "STOCK_ADJUSTMENT" or "STOCK_OUT"

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
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.STOCK_ADJUSTMENT_APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && Constants.STOCK_ADJUSTMENT_DECLINED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, Constants.STOCK_ADJUSTMENT_DECLINED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, Constants.STOCK_ADJUSTMENT_PENDING);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, Constants.STOCK_ADJUSTMENT_DECLINED);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, Constants.STOCK_ADJUSTMENT_DECLINED);
        }
        return null;
    }

    @Override
    public void updateRejectedStatus() {
        if (getOverallStatus() != null && Constants.STOCK_ADJUSTMENT_DECLINED.equals(getOverallStatus().getCode())) {
            ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
            setEntityStatus(referenceManager.findReference(Constants.STOCK_ADJUSTMENT_STATUS, Constants.STOCK_ADJUSTMENT_SUBMITTED));
        }
    }



    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(final Integer objectID) {
        this.objectID = objectID;
    }

    public List<EdsAdjustmentItem> getAdjustmentItemList() {
        return adjustmentItemList != null ? adjustmentItemList : new ArrayList<>();
    }

    public void setAdjustmentItemList(List<EdsAdjustmentItem> adjustmentItemList) {
        this.adjustmentItemList = adjustmentItemList;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Boolean isStockTransfer() {
        return stockTransfer != null ? stockTransfer : false;
    }

    public void setStockTransfer(Boolean stockTransfer) {
        this.stockTransfer = stockTransfer;
    }

    public EdsWarehouse getFromWarehouse() {
        return fromWarehouse;
    }

    public void setFromWarehouse(EdsWarehouse fromWarehouse) {
        this.fromWarehouse = fromWarehouse;
    }

    public EdsWarehouse getToWarehouse() {
        return toWarehouse;
    }

    public void setToWarehouse(EdsWarehouse toWarehouse) {
        this.toWarehouse = toWarehouse;
    }

    public EdsAccount getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(EdsAccount fromAccount) {
        this.fromAccount = fromAccount;
    }

    public EdsAccount getToAccount() {
        return toAccount;
    }

    public void setToAccount(EdsAccount toAccount) {
        this.toAccount = toAccount;
    }

    public Boolean isDeleted() {
        return deleted != null ? deleted : false;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsStockTransfer getStockTrans() {
        return stockTrans;
    }

    public void setStockTrans(EdsStockTransfer stockTrans) {
        this.stockTrans = stockTrans;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public SelectItem getCreatorAsSelectItem() {
        EdsUser _creator = getCreator();
        if (_creator != null)
            return new SelectItem(_creator.getObjectID(), _creator.getFullName());
        else
            return new SelectItem();
    }

    public SelectItem getUpdatorAsSelectItem() {
        EdsUser _updator = getUpdater();
        if (_updator != null)
            return new SelectItem(_updator.getObjectID(), _updator.getFullName());
        else
            return new SelectItem();
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime() {
        this.creationTime = new Date();
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime() {
        this.lastUpdateTime = new Date();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AdjustmentItem getDataAsRPC() {
        AdjustmentItem adjustmentItem = new AdjustmentItem();
        adjustmentItem.setObjectID(getObjectID());
        adjustmentItem.setStockTransfer(isStockTransfer());
        adjustmentItem.setDate(new DateNonConvertable(date));
        adjustmentItem.setNumber(number);
        adjustmentItem.setCreationTime(getCreationTime());
        adjustmentItem.setLastUpdateTime(getLastUpdateTime());
        adjustmentItem.setCreator(getCreatorAsSelectItem());
        adjustmentItem.setUpdater(getUpdatorAsSelectItem());

        if (getOverallStatus() != null)
            adjustmentItem.setStatusCode(getOverallStatus().getCode());

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            if (getCurrentApprover().getExactEmployee().isEmployee()) {
                EdsEmployee edsEmployee = getCurrentApprover().getExactEmployee().getEmployee();

                if (edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null)
                    adjustmentItem.setApprover(new SelectItem(edsEmployee.getObjectID(), edsEmployee.getProfile().getEmployeeCode() + " - " + edsEmployee.getFullName()));
                else
                    adjustmentItem.setApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());

            } else
                adjustmentItem.setApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }

        if (account != null)
            adjustmentItem.setAccount(account.createAccountItem());

        if (adjustmentItemList != null && adjustmentItemList.size() > 0) {
            ProductItem[] subItems = new ProductItem[adjustmentItemList.size()];
            int i = 0;
            for (EdsAdjustmentItem edsAdjItem : adjustmentItemList)
                subItems[i++] = edsAdjItem.getDataAsRPC();
            adjustmentItem.setProductItems(subItems);
        }
        adjustmentItem.setMemo(memo);
        if (adjustmentItem.isStockTransfer()) {
            adjustmentItem.setFromWarehouseID(getFromWarehouse().getObjectID());
            adjustmentItem.setToWarehouseID(getToWarehouse().getObjectID());
        }
        return adjustmentItem;
    }
}
