package com.edatasite.workforce.core.domain.fifo;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EntityType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "fifo_manual_review")
public class EdsFifoManualReview extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "company_id", nullable = false)
    private Integer companyId;

    @Column(name = "invoice_id", nullable = false)
    private Integer invoiceId;

    @Column(name = "transaction_id", nullable = false)
    private Integer transactionId;

    @OneToMany(mappedBy = "failure", cascade = CascadeType.ALL)
    private List<EdsFifoItem> items;

    @Column(name = "failure_reason")
    private String failureReason;

    @Enumerated(EnumType.STRING)
    private EntityType type;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public Integer getObjectID() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public List<EdsFifoItem> getItems() {
        return items;
    }

    public void setItems(List<EdsFifoItem> items) {
        this.items = items;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
