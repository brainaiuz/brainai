package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/15/12
 * Time: 5:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class BankCheckData implements IsSerializable {

    public static final String ACTION = "action";
    public static final String NUMBER = "number";
    public static final String BANK_ACCOUNT = "bankAccount";
    public static final String PAY_TO = "payTo";
    public static final String DATE = "date";
    public static final String AMOUNT = "amount";
    public static final String ADDRESS = "address";
    public static final String MEMO = "memo";
    public static final String AMOUNT_STRING_WORD = "amountStringWord";
    public static final String STATUS = "status";
    public static final String CREATOR = "creator";
    public static final String PROJECT = "project";

    private Integer objectID;
    private BankAccountItem bankAccount;
    private NumberData numberData;
    private String payTo;
    private DateNonConvertable date;
    private BigDecimal amount;
    private String address;
    private String memo;
    private Boolean toBePrinted;
    private String currencyName;
    private String amountStringWord;
    private String creator;
    private SelectItem project;

    private boolean isEditable;

    private BankCheckItem[] items;

    private String qbCheckId;
    private String qbEditSequence;
    private String code;
    private String layoutHtml;
    private boolean fromQuickbooks;

    private boolean enabledPostDatedTransaction;
    private boolean postDatedTransaction;
    private SelectItem[] templates;

    private CurrencyItem currencyItem;
    private BigDecimal exchageRate;

    public BankCheckData() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public BankAccountItem getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountItem bankAccount) {
        this.bankAccount = bankAccount;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public String getPayTo() {
        return payTo;
    }

    public void setPayTo(String payTo) {
        this.payTo = payTo;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Boolean getToBePrinted() {
        return toBePrinted != null ? toBePrinted : false;
    }

    public void setToBePrinted(Boolean toBePrinted) {
        this.toBePrinted = toBePrinted;
    }

    public BankCheckItem[] getItems() {
        return items;
    }

    public void setItems(BankCheckItem[] items) {
        this.items = items;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getAmountStringWord() {
        return amountStringWord;
    }

    public void setAmountStringWord(String amountStringWord) {
        this.amountStringWord = amountStringWord;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public String getQbCheckId() {
        return qbCheckId;
    }

    public void setQbCheckId(String qbCheckId) {
        this.qbCheckId = qbCheckId;
    }

    public String getQbEditSequence() {
        return qbEditSequence;
    }

    public void setQbEditSequence(String qbEditSequence) {
        this.qbEditSequence = qbEditSequence;
    }

    public String getLayoutHtml() {
        return layoutHtml;
    }

    public void setLayoutHtml(String layoutHtml) {
        this.layoutHtml = layoutHtml;
    }

    public boolean isFromQuickbooks() {
        return fromQuickbooks;
    }

    public void setFromQuickbooks(boolean fromQuickbooks) {
        this.fromQuickbooks = fromQuickbooks;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isPostDatedTransaction() {
        return postDatedTransaction;
    }

    public void setPostDatedTransaction(boolean postDatedTransaction) {
        this.postDatedTransaction = postDatedTransaction;
    }

    public boolean isEnabledPostDatedTransaction() {
        return enabledPostDatedTransaction;
    }

    public void setEnabledPostDatedTransaction(boolean enabledPostDatedTransaction) {
        this.enabledPostDatedTransaction = enabledPostDatedTransaction;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public CurrencyItem getCurrencyItem() {
        return currencyItem;
    }

    public void setCurrencyItem(CurrencyItem currencyItem) {
        this.currencyItem = currencyItem;
    }

    public BigDecimal getExchageRate() {
        return exchageRate;
    }

    public void setExchageRate(BigDecimal exchageRate) {
        this.exchageRate = exchageRate;
    }
}
