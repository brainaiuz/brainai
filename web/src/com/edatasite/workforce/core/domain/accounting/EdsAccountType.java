package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 16.02.2009
 * Time: 20:04:34
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "accountType")
public class EdsAccountType extends EdsObject {

    public static final String ASSETS = "ASSETS";
    public static final String LIABILITIES = "LIABILITIES";
    public static final String EQUITY = "EQUITY";
    public static final String EXPENSES = "EXPENSES";
    public static final String REVENUE = "REVENUE";
    public static final String CREDIT_CARD = "CREDIT CARD";

    public static final String CURRENT_ASSET = "CURRENT_ASSET";
    public static final String FIXED_ASSET = "FIXED_ASSET";
    public static final String NON_CURRENT_ASSET = "NON_CURRENT_ASSET";
    public static final String LIABILITY = "LIABILITY";
    public static final String CURRENT_LIABILITY = "CURRENT_LIABILITY";
    public static final String LONG_TERM_LIABILITY = "LONG_TERM_LIABILITY";
    public static final String BANK = "BANK";
    public static final String PREPAYMENT = "PREPAYMENT";

    //////////////////REVENUE//////////////////////
//    public static final String REVENUE = "REVENUE";
    public static final String OTHER_INCOME = "OTHER_INCOME";
    public static final String SALES = "SALES";
    ///////////////EXPENSES/////////////////////

    public static final String DIRECT_EXPENSES = "DIRECT_EXPENSES";
    public static final String COST_OF_SALES = "COST_OF_SALES";
    public static final String OVERHEAD = "OVERHEAD";
    public static final String DEPRECIATION = "DEPRECIATION";

    public static final String INCOME_TAX = "INCOME_TAX";//It is only used to calculate P&L

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;

    private String category;

    private String code;

    @Column(name = "sorder")
    private Integer order = 0;

    public Integer getObjectID() {
        return objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
