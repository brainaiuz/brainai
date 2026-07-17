package com.edatasite.workforce.core.domain.settings.payment.payme;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.enums.OrderCancelReason;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.enums.TransactionState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "paymeTransaction")
public class EdsPaymeTransaction extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String paycomId;

    @Column
    private long paycomTime;

    @Column
    private long createTime = new Date().getTime();

    @Column
    private long performTime;

    @Column
    private long cancelTime;

    @Column
    private OrderCancelReason reason;

    @Column(nullable = false)
    private TransactionState state = TransactionState.STATE_IN_PROGRESS;

    @OneToOne
    private EdsInvoice invoice;

    public EdsPaymeTransaction() {
    }

    public EdsPaymeTransaction(Long id, String paycomId, long paycomTime, long createTime, long performTime, long cancelTime, OrderCancelReason reason, TransactionState state, EdsInvoice invoice) {
        this.id = id;
        this.paycomId = paycomId;
        this.paycomTime = paycomTime;
        this.createTime = createTime;
        this.performTime = performTime;
        this.cancelTime = cancelTime;
        this.reason = reason;
        this.state = state;
        this.invoice = invoice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaycomId() {
        return paycomId;
    }

    public void setPaycomId(String paycomId) {
        this.paycomId = paycomId;
    }

    public long getPaycomTime() {
        return paycomTime;
    }

    public void setPaycomTime(long paycomTime) {
        this.paycomTime = paycomTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getPerformTime() {
        return performTime;
    }

    public void setPerformTime(long performTime) {
        this.performTime = performTime;
    }

    public long getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(long cancelTime) {
        this.cancelTime = cancelTime;
    }

    public OrderCancelReason getReason() {
        return reason;
    }

    public void setReason(OrderCancelReason reason) {
        this.reason = reason;
    }

    public TransactionState getState() {
        return state;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    public EdsInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(EdsInvoice invoice) {
        this.invoice = invoice;
    }

    @Override
    public Integer getObjectID() {
        return 0;
    }
}
