package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsBankAccountCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 26.02.2009
 * Time: 19:28:11
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bankAccount")
public class EdsBankAccount extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private EdsAccount account;

    private String accountNumber;
    private String accauntName;
    private String bankBranch;
    private String bankAddress;
    private String swiftCode;
    private String ibanCode;
    private String sortCode;
    private String abaCode;
    private String agentID;
    private String bic; //bank identifier code

    private String streetAddress;
    private String city;

    @Column(name = "active", columnDefinition = " boolean DEFAULT true")
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryid")
    @ForeignKey(name = "none")
    private EdsCountry country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stateid")
    @ForeignKey(name = "none")
    private EdsRegion state;

    private String postCode;
    private String phoneNumber;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountid")
    private List<EdsBankAccountAttachment> attachmentsInAccount = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountid")
    private List<EdsBankStatement> statementsInAccount = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountid")
    private List<EdsBankTransfer> transfersInAccount = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountid")
    private List<EdsSaleInvoice> invoicesInAccount = new ArrayList<>();

    @Column(precision = 25, scale = 5)
    private BigDecimal openingAmount;

    private Date openingDate;

    @Column(precision = 25, scale = 10)
    private BigDecimal exchangeRate;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsBankAccountCustomFields customFields;


    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "bankAccount_owners",
            joinColumns = {@JoinColumn(name = "bankAccount_id")},
            inverseJoinColumns = {@JoinColumn(name = "owner_id")})
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsUser> owners = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "openingbalancetransaction")
//    private EdsBankTransaction openingBalanceTransaction;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    @Override
    public String getName() {
        return accauntName;
    }

    public String getAccauntName() {
        return accauntName;
    }

    public void setAccauntName(String accauntName) {
        this.accauntName = accauntName;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getIbanCode() {
        return ibanCode;
    }

    public void setIbanCode(String ibanCode) {
        this.ibanCode = ibanCode;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getAbaCode() {
        return abaCode;
    }

    public void setAbaCode(String abaCode) {
        this.abaCode = abaCode;
    }

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public EdsCountry getCountry() {
        return country;
    }

    public void setCountry(EdsCountry country) {
        this.country = country;
    }

    public EdsRegion getState() {
        return state;
    }

    public void setState(EdsRegion state) {
        this.state = state;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isUsedInSystem() {
        return false;
//        return attachmentsInAccount.size() > 0 || statementsInAccount.size() > 0 || transfersInAccount.size() > 0 || invoicesInAccount.size() > 0;
    }

    public SelectItem getAsSelectItem() {
        return new SelectItem(objectID, account.getName());
    }
    public SelectItem getAsSelectedAccountItem() {
        if (account != null) {
            return new SelectItem(account.getObjectID(), account.getName());
        }
        return null;
    }

    public BigDecimal getOpeningAmount() {
        return openingAmount;
    }

    public void setOpeningAmount(BigDecimal openingAmount) {
        this.openingAmount = openingAmount;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate != null ? exchangeRate : BigDecimal.ONE;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Boolean isActive() {
        return active != null ? active : true;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    //    public EdsBankTransaction getOpeningBalanceTransaction() {
//        return openingBalanceTransaction;
//    }
//
//    public void setOpeningBalanceTransaction(EdsBankTransaction openingBalanceTransaction) {
//        this.openingBalanceTransaction = openingBalanceTransaction;
//    }


    public EdsBankAccountCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsBankAccountCustomFields customFields) {
        this.customFields = customFields;
    }

    public BankAccountItem createBankAccountItem() {
        BankAccountItem bankAccountItem = new BankAccountItem(getAccount().getObjectID(), getAccount().getName(), getAccount().getBalance(),
                (getAccount().getCurrency() != null ? getAccount().getCurrency().createCurrencyItem() : null));
        bankAccountItem.setExchangeRate(getExchangeRate());
        return bankAccountItem;
    }

    public BankAccountItem createBankAccountItemForLookUp() {
        BankAccountItem bankAccountItem = new BankAccountItem(getAccount().getObjectID(), getAccount().getAccountCode() + " - " + getAccount().getName(), getAccount().getBalance(),
                (getAccount().getCurrency() != null ? getAccount().getCurrency().createCurrencyItem() : null));
        bankAccountItem.setExchangeRate(getExchangeRate());
        return bankAccountItem;
    }

    public BankAccountItem createBankItem() {
        BankAccountItem bankAccountItem = new BankAccountItem(getObjectID(), getAccount().getName(), getAccount().getBalance(),
                (getAccount().getCurrency() != null ? getAccount().getCurrency().createCurrencyItem() : null),getAccount().getAccountCode(),getAccountNumber());
        bankAccountItem.setExchangeRate(getExchangeRate());
        return bankAccountItem;
    }

    public BankAccountItem createBankItemForLookUp() {
        BankAccountItem bankAccountItem = new BankAccountItem(getObjectID(), getAccount().getAccountCode() + " - " + getAccount().getName(), getAccount().getBalance(),
                (getAccount().getCurrency() != null ? getAccount().getCurrency().createCurrencyItem() : null), getAccount().getAccountCode(), getAccountNumber());
        bankAccountItem.setExchangeRate(getExchangeRate());
        return bankAccountItem;
    }

    public BankAccountItem getBankAccountNameForLookup() {
        return new BankAccountItem(getObjectID(), getAccount().getName());
    }

    public BankAccountItem getBankAccountCodeForLookup() {
        return new BankAccountItem(getObjectID(), getAccount().getAccountCode());
    }

    public BankAccountItem getBankAccountNumberForLookup() {
        return new BankAccountItem(getObjectID(), getAccountNumber());
    }


    public List<EdsUser> getOwners() {
        return owners;
    }

    public void setOwners(List<EdsUser> owners) {
        this.owners = owners;
    }

    public HashMap<Integer, EdsUser> getOwnersMap() {
        HashMap<Integer, EdsUser> ownersMap = new HashMap<>();
        if (getOwners() == null || getOwners().isEmpty()) {
            return ownersMap;
        }
        getOwners().forEach(owner -> ownersMap.put(owner.getObjectID(), owner));
        return ownersMap;
    }
}
