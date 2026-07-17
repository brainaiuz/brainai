package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankCheckData;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankCheckItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/15/12
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bankcheck")
public class EdsBankCheck extends EdsObject implements ObjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountid")
    private EdsBankAccount bankAccount;

    private Integer intNumber;
    private Date date;

    @Column(precision = 25, scale = 5)
    private BigDecimal amount;

    @Type(type = "text")
    private String number;
    @Type(type = "text")
    private String payTo;
    @Type(type = "text")
    private String address;
    @Type(type = "text")
    private String memo;

    @Type(type = "text")
    private String amountString;

    private Boolean toPrint = false;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "bankcheckid")
    @OrderBy("objectID")
    private List<EdsBankCheckItem> bankCheckItems = new ArrayList<>();

    @Column(name = "quickbook_check_id")
    private String quickbookCheckID;

    @Column(name = "quickbook_edit_sequence")
    private String quickbookEditSequence;

    @Column(name = "external_guid")
    private String externalGUID;

    @Column(name = "deleted")
    private Boolean deleted = false;

    private Boolean postDatedTransaction = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsBankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(EdsBankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPayTo() {
        return payTo;
    }

    public void setPayTo(String payTo) {
        this.payTo = payTo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public Boolean getToPrint() {
        return toPrint;
    }

    public void setToPrint(Boolean toPrint) {
        this.toPrint = toPrint;
    }

    public List<EdsBankCheckItem> getBankCheckItems() {
        return bankCheckItems;
    }

    public void setBankCheckItems(List<EdsBankCheckItem> bankCheckItems) {
        this.bankCheckItems = bankCheckItems;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amountString) {
        this.amountString = amountString;
    }

    public String getQuickbookCheckID() {
        return quickbookCheckID;
    }

    public void setQuickbookCheckID(String quickbookCheckID) {
        this.quickbookCheckID = quickbookCheckID;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    public String getExternalGUID() {
        return externalGUID;
    }

    public void setExternalGUID(String externalGUID) {
        this.externalGUID = externalGUID;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isPostDatedTransaction() {
        return postDatedTransaction != null ? postDatedTransaction : false;
    }

    public void setPostDatedTransaction(Boolean postDatedTransaction) {
        this.postDatedTransaction = postDatedTransaction;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public void setLastUpdateTime(Date value) {

    }

    @Override
    public void setUpdater(EdsUser user) {

    }

    @Override
    public void setCreationTime(Date value) {

    }

    @Override
    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public BankCheckData createBankCheckData(boolean forEdit) {
        BankCheckData checkData = new BankCheckData();
        checkData.setObjectID(getObjectID());
        checkData.setBankAccount(getBankAccount().createBankAccountItem());
        if (getIntNumber() != null) {
            checkData.setNumberData(new NumberData(getNumber(), getIntNumber()));
            checkData.getNumberData().setNumberFormat("CK_0001");
        }
        checkData.setPayTo(getPayTo());
        checkData.setDate(new DateNonConvertable(getDate()));
        checkData.setAmount(getAmount());
        checkData.setAddress(getAddress());
        checkData.setMemo(getMemo());
        checkData.setToBePrinted(getToPrint());
        checkData.setAmountStringWord(getAmountString());
        checkData.setPostDatedTransaction(isPostDatedTransaction());
        checkData.setCreator(getCreator() != null ? getCreator().getName() : "");
        checkData.setExchageRate(getExchangeRate());
        if (currency != null) {
            checkData.setCurrencyItem(currency.createCurrencyItem());
            checkData.setCurrencyName(checkData.getCurrencyItem().getName());
        }
        if (this.getProject() != null) {
            String projectName = "";
            if (!StringUtils.isEmpty(this.getProject().getNumber())) {
                projectName = this.getProject().getNumber() + " -> " + this.getProject().getName();
            } else {
                projectName = this.getProject().getName();
            }
            checkData.setProject(new SelectItem(this.getProject().getObjectID(), projectName));
        }
        if (forEdit) {
            List<EdsBankCheckItem> edsCheckItems = getBankCheckItems();
            BankCheckItem[] checkItems = new BankCheckItem[edsCheckItems.size()];
            int i = 0;
            Set<String> projectsInLine = new HashSet<>();
            for (EdsBankCheckItem bci : edsCheckItems) {
                checkItems[i] = new BankCheckItem();
                checkItems[i].setAccount(bci.getAccount().createAccountItem());
                checkItems[i].setAmount(bci.getAmount());
                checkItems[i].setDescription(bci.getDescription());
                if (bci.getCrmAccount() != null) {
                    checkItems[i].setCrmAccount(bci.getCrmAccount().getAsSelectItem());
                }
                String projectName = "";
                if (bci.getProject() != null) {
                    if (!StringUtils.isEmpty(bci.getProject().getNumber())) {
                        projectName = bci.getProject().getNumber() + " -> " + bci.getProject().getName();
                    } else {
                        projectName = bci.getProject().getName();
                    }
                    projectsInLine.add(projectName);
                    checkItems[i].setProject(new SelectItem(bci.getProject().getObjectID(), projectName));
                } else if (checkData.getProject() != null) {
                    if (!StringUtils.isEmpty(checkData.getProject().getNumber())) {
                        projectName = checkData.getProject().getNumber() + " -> " + checkData.getProject().getName();
                    } else {
                        projectName = checkData.getProject().getName();
                    }
                    projectsInLine.add(projectName);
                    checkItems[i].setProject(new SelectItem(checkData.getProject().getId(), projectName));
                }
                checkItems[i].setClient(bci.getClient() != null ? bci.getClient().getAsSelectItem() : null);
                checkItems[i].setQuickbookItemID(bci.getQuickbookCheckID());
                i++;
            }
            checkData.setItems(checkItems);
            if (!projectsInLine.isEmpty()) {
                ArrayList<String> list = new ArrayList<>(projectsInLine);
                checkData.setProject(new SelectItem(null, ServerUtils.getAsCommoDelimited(list, "", " , ")));
            }
        }
        return checkData;
    }
}
