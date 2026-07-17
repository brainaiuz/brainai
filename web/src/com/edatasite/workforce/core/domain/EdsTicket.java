package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsEvent;
import com.edatasite.workforce.gwt.core.client.rpc.TicketItem;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 4/1/12
 * Time: 2:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "ticket")
public class EdsTicket extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    @Column(precision = 11, scale = 4)
    private BigDecimal price;

    private Integer qty;

    @Column(name = "is_free")
    private Boolean isFree;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsEvent event;

    private Boolean deleted = false;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "currencyId")
    private Integer currencyId;

    @Column(name = "minCount")
    private Integer minCount;

    @Column(name = "maxCount")
    private Integer maxCount;

    @Column(name = "topFee")
    private Boolean topFee;

    @Column(name = "inFee")
    private Boolean inFee;

    @Column(name = "salesStartDate")
    private Date salesStartDate;

    @Column(name = "salesEndDate")
    private Date salesEndDate;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public Boolean getFree() {
        return isFree;
    }

    public void setFree(Boolean free) {
        isFree = free;
    }

    public EdsEvent getEvent() {
        return event;
    }

    public void setEvent(EdsEvent event) {
        this.event = event;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getMinCount() {
        return minCount;
    }

    public void setMinCount(Integer minCount) {
        this.minCount = minCount;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Boolean isTopFee() {
        return topFee;
    }

    public void setTopFee(Boolean topFee) {
        this.topFee = topFee;
    }

    public Boolean isInFee() {
        return inFee;
    }

    public void setInFee(Boolean inFee) {
        this.inFee = inFee;
    }

    public Date getSalesStartDate() {
        return salesStartDate;
    }

    public void setSalesStartDate(Date salesStartDate) {
        this.salesStartDate = salesStartDate;
    }

    public Date getSalesEndDate() {
        return salesEndDate;
    }

    public void setSalesEndDate(Date salesEndDate) {
        this.salesEndDate = salesEndDate;
    }

    public TicketItem getRPC() {
        TicketItem ticket = new TicketItem();
        ticket.setObjectID(getObjectID());
        ticket.setName(getName());
        ticket.setQty(Double.valueOf(getQty()));
        ticket.setPrice(getPrice().doubleValue());
        if (getFree() != null && getFree())
            ticket.setFree(getFree());
        else
            ticket.setFree(false);

        if (getDescription() != null && getDescription().length() > 0)
            ticket.setDescription(getDescription());

        if (getPrice() == null || getPrice().intValue() == 0)
            ticket.setFree(true);

        if (getSalesStartDate() != null)
            ticket.setSalesStartDate(getSalesStartDate());

        if (getSalesEndDate() != null)
            ticket.setSalesEndDate(getSalesEndDate());

        if (isTopFee() != null && isTopFee())
            ticket.setTopFee(true);
        else
            ticket.setTopFee(false);
        if (isInFee() != null && isInFee())
            ticket.setInFee(true);
        else
            ticket.setInFee(false);
        if (getMaxCount() != null)
            ticket.setMaxTikcets(getMaxCount());
        if (getMinCount() != null)
            ticket.setMinTickets(getMinCount());
        ticket.setCurrencyId(getCurrencyId());
        return ticket;
    }
}
