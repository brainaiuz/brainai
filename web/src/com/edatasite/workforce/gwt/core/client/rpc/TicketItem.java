package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 4/3/12
 * Time: 2:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class TicketItem implements IsSerializable {

    private Integer objectID;
    private String name;
    private Double qty;
    private Double price;
    private boolean isFree;
    private Integer currencyId;
    private Integer minTickets;
    private Integer maxTikcets;
    private String description;
    private boolean topFee;
    private boolean inFee;
    private Date salesStartDate;
    private Date salesEndDate;

    public TicketItem() {
    }

    public TicketItem(Integer objectID, String name) {
        this.objectID = objectID;
        this.name = name;
    }

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

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price != null ? price : 0d;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getMinTickets() {
        return minTickets;
    }

    public void setMinTickets(Integer minTickets) {
        this.minTickets = minTickets;
    }

    public Integer getMaxTikcets() {
        return maxTikcets;
    }

    public void setMaxTikcets(Integer maxTikcets) {
        this.maxTikcets = maxTikcets;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTopFee() {
        return topFee;
    }

    public void setTopFee(boolean topFee) {
        this.topFee = topFee;
    }

    public boolean isInFee() {
        return inFee;
    }

    public void setInFee(boolean inFee) {
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

    //    @Override
//    public boolean equals(Object obj) {
//        if (this.getObjectID() == null || obj == null || ((TicketItem) obj).getObjectID() == null) {
//            return false;
//        }
//        return this.getObjectID().equals(((TicketItem) obj).getObjectID());
//    }


    @Override
    public boolean equals(Object obj) {
        boolean isExistingItem = !(this.getObjectID() == null || obj == null || ((TicketItem) obj).getObjectID() == null);
        boolean isNewItem = !(this.getName() == null || obj == null || ((TicketItem) obj).getName() == null);


        if (!isExistingItem && !isNewItem) {
            return false;
        }

        return isExistingItem ? this.getObjectID().equals(((TicketItem) obj).getObjectID()) : this.getName().equals(((TicketItem) obj).getName());
    }
}
