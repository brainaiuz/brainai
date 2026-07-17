package com.workforcetrack.mobile.rpc.opportunity;

import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 9/21/11
 * Time: 5:32 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class MOpportunityItem {

    private Integer opportunityID;
    private Integer itemID;
    private String itemName;
    private String description;
    private BigDecimal price;
    private BigDecimal qty;
    private BigDecimal discount;
    private BigDecimal discountAmount;


    public MOpportunityItem() {

    }

    public MOpportunityItem(OpportunityItem opportunityItem) {
        this.opportunityID = opportunityItem.getOpportunityID();
        this.itemID = opportunityItem.getItemID();
        this.itemName = opportunityItem.getItemName();
        this.description = opportunityItem.getDescription();
        this.price = opportunityItem.getPrice();
        this.qty = opportunityItem.getQty();
        this.discount = opportunityItem.getDiscountPercent();
        this.discountAmount = opportunityItem.getDiscountAmount();
    }

    public OpportunityItem convertToOpportunityItem(OpportunityItem opportunityItem) {
        if (opportunityItem == null) {
            opportunityItem = new OpportunityItem();
        }

        opportunityItem.setOpportunityID(this.opportunityID);
        opportunityItem.setItemID(this.itemID);
        opportunityItem.setItemName(this.itemName);
        opportunityItem.setDescription(this.description);
        opportunityItem.setPrice(this.price);
        opportunityItem.setQty(this.qty);
        opportunityItem.setDiscountPercent(this.discount);
        opportunityItem.setDiscountAmount(this.discountAmount);

        return opportunityItem;
    }


    public Integer getOpportunityID() {
        return opportunityID;
    }

    public void setOpportunityID(Integer opportunityID) {
        this.opportunityID = opportunityID;
    }

    public Integer getItemID() {
        return itemID;
    }

    public void setItemID(Integer itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
