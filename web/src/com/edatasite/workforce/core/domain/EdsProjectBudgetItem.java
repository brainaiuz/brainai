package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/18/12
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "projectbudgetitem")
public class EdsProjectBudgetItem extends EdsObject {

    public static final String REVENUE = "REVENUE";
    public static final String EMPLOYEE_COST = "EMPLOYEE_COST";
    public static final String EXPENSE = "EXPENSE";
    public static final String ASSET = "ASSET";
    public static final String PURCHASE = "PURCHASE";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectbudgetid")
    private EdsProjectBudget projectBudget;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private EdsAccount account;

    @Column(precision = 25, scale = 5)
    private BigDecimal amount;

    private Integer month;
    private Integer year;

    private Boolean total;

    private String type;//REVENUE, EMPLOYEE_COST, EXPENSE, PURCHASE

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsProjectBudget getProjectBudget() {
        return projectBudget;
    }

    public void setProjectBudget(EdsProjectBudget projectBudget) {
        this.projectBudget = projectBudget;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Boolean isTotal() {
        return total != null ? total : false;
    }

    public void setTotal(Boolean total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeriodKey() {
        if (isTotal()) {
            return Constants.TOTAL_BUDGET;
        } else {
            return year + "_" + (month > 9 ? month : "0" + month);
        }
    }
}
