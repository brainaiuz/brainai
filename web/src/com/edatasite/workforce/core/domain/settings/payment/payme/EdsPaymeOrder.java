package com.edatasite.workforce.core.domain.settings.payment.payme;


import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.enums.OrderStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "paymeOrder")
public class



EdsPaymeOrder extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long amount;

    @Column
    private Boolean delivered;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.UNPAID;

    public EdsPaymeOrder() {
    }

    public EdsPaymeOrder(Long id, Long amount, Boolean delivered, OrderStatus status) {
        this.id = id;
        this.amount = amount;
        this.delivered = delivered;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Boolean getDelivered() {
        return delivered;
    }

    public void setDelivered(Boolean delivered) {
        this.delivered = delivered;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public Integer getObjectID() {
        return 0;
    }
}
