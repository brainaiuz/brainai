package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountMultiRangeItem;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 3, 2010
 * Time: 3:25:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "discounts")
public class EdsDiscount extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String code;

    private String name;

    @Type(type = "text")
    private String description;

    private Integer type;

    @Column(precision = 10, scale = 5)
    private BigDecimal percentage;

    @Column(precision = 25, scale = 5)
    private BigDecimal fixedAmount;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "discount")
    private List<EdsDiscountMultiRangeValue> multiRangeValueList = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "discount_applied_products",
            joinColumns = {@JoinColumn(name = "discount_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")})
    private List<EdsItem> appliedProducts = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "discount_applied_clients",
            joinColumns = {@JoinColumn(name = "discount_id")},
            inverseJoinColumns = {@JoinColumn(name = "client_id")})
    private List<EdsCrmAccount> appliedClients = new ArrayList<>();

    private Boolean isActive = true;

    private Boolean deleted = false;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<EdsDiscountMultiRangeValue> getMultiRangeValueList() {
        return multiRangeValueList;
    }

    public void setMultiRangeValueList(List<EdsDiscountMultiRangeValue> multiRangeValueList) {
        this.multiRangeValueList = multiRangeValueList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<EdsItem> getAppliedProducts() {
        return appliedProducts;
    }

    public void setAppliedProducts(List<EdsItem> appliedProducts) {
        this.appliedProducts = appliedProducts;
    }

    public List<EdsCrmAccount> getAppliedClients() {
        return appliedClients;
    }

    public void setAppliedClients(List<EdsCrmAccount> appliedClients) {
        this.appliedClients = appliedClients;
    }

    public static DiscountItem[] getItemDiscounts(List<EdsDiscount> discounts) {

        if (discounts != null && discounts.size() > 0) {
            DiscountItem[] discountItems = new DiscountItem[discounts.size() + 2];
            discountItems[0] = new DiscountItem(0, "Percentage");
            discountItems[1] = new DiscountItem(1, "Fixed Amount");

            int i = 2;
            for (EdsDiscount discount : discounts) {
                discountItems[i] = new DiscountItem();

                discountItems[i].setId(discount.getObjectID());
                discountItems[i].setName(discount.getName());
                discountItems[i].setCode(discount.getCode());
                discountItems[i].setDescription(discount.getDescription());
                discountItems[i].setActive(discount.isActive());
                discountItems[i].setType(discount.getType());
                discountItems[i].setPercentage(discount.getPercentage());
                discountItems[i].setFixedAmount(discount.getFixedAmount());

                //init discount multi range values
                initDiscountMultiRangeData(discountItems[i], discount.getMultiRangeValueList());

                i++;
            }

            return discountItems;
        }

        return new DiscountItem[0];
    }

    public DiscountItem getRPC() {
        DiscountItem discountItem = new DiscountItem();

        discountItem.setId(this.getObjectID());
        discountItem.setName(this.getName());
        discountItem.setCode(this.getCode());
        discountItem.setDescription(this.getDescription());
        discountItem.setActive(this.isActive());
        discountItem.setType(this.getType());
        discountItem.setPercentage(this.getPercentage());
        discountItem.setFixedAmount(this.getFixedAmount());

        //init discount multi range values
        initDiscountMultiRangeData(discountItem, this.getMultiRangeValueList());
        return discountItem;
    }

    private static void initDiscountMultiRangeData(DiscountItem discountItem, List<EdsDiscountMultiRangeValue> multiRangeValues) {

        List<DiscountMultiRangeItem> multiRangeItems = new ArrayList<>();

        if (multiRangeValues != null && multiRangeValues.size() > 0) {
            for (EdsDiscountMultiRangeValue multiRangeValue : multiRangeValues) {
                DiscountMultiRangeItem multiRangeItem = new DiscountMultiRangeItem();
                multiRangeItem.setId(multiRangeValue.getObjectID());
                multiRangeItem.setType(multiRangeValue.getType());
                multiRangeItem.setFromQty(multiRangeValue.getFromQty());
                multiRangeItem.setToQty(multiRangeValue.getToQty());
                multiRangeItem.setFromAmount(multiRangeValue.getFromAmount());
                multiRangeItem.setToAmount(multiRangeValue.getToAmount());
                multiRangeItem.setPercentage(multiRangeValue.getPercentage());
                multiRangeItem.setFixedAmount(multiRangeValue.getFixedAmount());

                discountItem.setMultiRangeDiscountType(multiRangeValue.getType());

                multiRangeItems.add(multiRangeItem);
            }
        }

        discountItem.setMultiRangeItems(multiRangeItems.toArray(new DiscountMultiRangeItem[]{}));
    }
}
