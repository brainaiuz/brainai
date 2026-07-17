package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: June 3, 2011
 * Time: 4:58:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class MDiscountItem extends MSelectItem {

    private String code;
    private Integer type;
    private Boolean isActive;
    private Double percentage;
    private Double fixedAmount;

    private String currencySymbol;

    private List<Integer> appliedProductIDs;

    public MDiscountItem() {
    }

    public MDiscountItem(DiscountItem discountItem) {
        this.objectID = discountItem.getId();
        this.name = discountItem.getName();
        this.description = discountItem.getDescription();
        this.code = discountItem.getCode();
        this.type = discountItem.getType();
        this.isActive = discountItem.isActive();
        this.percentage = (discountItem.getPercentage() != null ? discountItem.getPercentage().doubleValue() : null);
        this.fixedAmount = (discountItem.getFixedAmount() != null ? discountItem.getFixedAmount().doubleValue() : null);
        this.currencySymbol = discountItem.getCurrencySymbol();
        if (discountItem.getAppliedProductIDs() != null){
            this.appliedProductIDs = Arrays.asList(discountItem.getAppliedProductIDs());
        }
    }

    public MDiscountItem(Integer id, String name) {
        super(id, name);
    }


}
