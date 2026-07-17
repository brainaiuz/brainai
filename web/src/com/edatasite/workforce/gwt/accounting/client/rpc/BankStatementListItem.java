package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 17.05.2010
 * Time: 11:34:47
 * To change this template use File | Settings | File Templates.
 */
public class BankStatementListItem implements IsSerializable {
    public static String ACTION = "action";
    public static String STATUS = "status";
    public static String IMPORTED_DATE = "importedDate";
    public static String START_DATE = "startDate";
    public static String END_DATE = "endDate";
    public static String START_BALANCE = "startBalance";
    public static String END_BALANCE = "endBalance";
    private Integer objectID;
    private Date importedDate;
    private Date startDate;
    private Date endDate;
    private BigDecimal startBalance;
    private BigDecimal endBalance;

    private boolean reconciled;

    public BankStatementListItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getImportedDate() {
        return importedDate;
    }

    public void setImportedDate(Date importedDate) {
        this.importedDate = importedDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getStartBalance() {
        return startBalance;
    }

    public void setStartBalance(BigDecimal startBalance) {
        this.startBalance = startBalance;
    }

    public BigDecimal getEndBalance() {
        return endBalance;
    }

    public void setEndBalance(BigDecimal endBalance) {
        this.endBalance = endBalance;
    }

    public boolean isReconciled() {
        return reconciled;
    }

    public void setReconciled(boolean reconciled) {
        this.reconciled = reconciled;
    }
}
