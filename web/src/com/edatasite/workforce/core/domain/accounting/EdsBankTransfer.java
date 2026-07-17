package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.customfields.EdsBankTransferCustomFields;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 16.07.2010
 * Time: 17:52:44
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "spendreceivemoney")
public class EdsBankTransfer extends EdsObject implements ObjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    //0 = No Tax, 1 = Tax Inclusive, 2 = Tax Exclusive
    private Integer taxCalculationType;
    //0 = RECEIVE, 1 = SPEND , 2=CASH_RECEIPT , 3=CASH_PAYMENT
    private Integer transferType;
    //0 = RECEIVE_MONEY, 1 = SPEND_MONEY
    private Integer formType;

    private String name;
    private Date date;
    private String reference;
    @Column(precision = 25, scale = 5)
    private BigDecimal requiredTotal;
    @Column(precision = 25, scale = 5)
    private BigDecimal subtotal;
    @Column(precision = 25, scale = 5)
    private BigDecimal taxTotal;
    @Column(precision = 25, scale = 5)
    private BigDecimal taxForeignTotal;
    @Column(precision = 25, scale = 5)
    private BigDecimal total;
    @Column(name = "number")
    private String number;

    @Column(name = "intNumber")
    private Integer intNumber;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "banktransferid")
    private List<EdsBankTransferItem> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionid")
    private EdsBankTransferTransaction bankTransferTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountid")
    private EdsBankAccount bankAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    private Boolean deleted = false;

    private Boolean postDatedTransaction = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashAccountId")
    private EdsAccount cashAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdfTemplateId")
    @ForeignKey(name = "none")
    private EdsCompanyPdfTemplate pdfTemplate;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsBankTransferCustomFields customFields;

    private String checkNumber;
    private Date lastUpdated;
    private Date creationDate;

    @Column(name = "tax_treatment")
    private Integer taxtreatmentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_treatment", insertable=false, updatable=false)
    private EdsReference taxTreatment;

    @Column(name = "placeofsupply_gcc_countryid")
    private Integer placeofsupplyGCCCountryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeofsupply_gcc_countryid", insertable=false, updatable=false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsCountry placeOfSupplyGCCCountry;

    @Column(name = "placeofsupply_id")
    private Integer placeofsupplyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeofsupply_id", insertable=false, updatable=false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsRegion placeOfSupply;

    @Column(name = "reversecharge_applicable", columnDefinition = "boolean default false")
    private boolean reverseChargeApplicable = false;

    @Transient
    List<CompanyCustomFieldItem> itemCustomFields;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public Integer getFormType() {
        return formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public BigDecimal getRequiredTotal() {
        return requiredTotal;
    }

    public void setRequiredTotal(BigDecimal requiredTotal) {
        this.requiredTotal = requiredTotal;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BigDecimal getTaxForeignTotal() {
        return taxForeignTotal;
    }

    public void setTaxForeignTotal(BigDecimal taxForeignTotal) {
        this.taxForeignTotal = taxForeignTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public List<EdsBankTransferItem> getItems() {
        return items;
    }

    public void setItems(List<EdsBankTransferItem> items) {
        this.items = items;
    }

    public EdsBankTransferTransaction getBankTransferTransaction() {
        return bankTransferTransaction;
    }

    public void setBankTransferTransaction(EdsBankTransferTransaction bankTransferTransaction) {
        this.bankTransferTransaction = bankTransferTransaction;
    }

    public EdsBankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(EdsBankAccount bankAccount) {
        this.bankAccount = bankAccount;
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

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsAccount getCashAccount() {
        return cashAccount;
    }

    public void setCashAccount(EdsAccount cashAccount) {
        this.cashAccount = cashAccount;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public EdsCompanyPdfTemplate getPdfTemplate() {
        return pdfTemplate;
    }

    public void setPdfTemplate(EdsCompanyPdfTemplate pdfTemplate) {
        this.pdfTemplate = pdfTemplate;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public EdsBankTransferCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsBankTransferCustomFields customFields) {
        this.customFields = customFields;
    }

    public List<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(List<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public Integer getTaxtreatmentId() {
        return taxtreatmentId;
    }

    public void setTaxtreatmentId(Integer taxtreatmentId) {
        this.taxtreatmentId = taxtreatmentId;
    }

    public EdsReference getTaxTreatment() {
        return taxTreatment;
    }

    public void setTaxTreatment(EdsReference taxTreatment) {
        this.taxTreatment = taxTreatment;
    }

    public Integer getPlaceofsupplyGCCCountryId() {
        return placeofsupplyGCCCountryId;
    }

    public void setPlaceofsupplyGCCCountryId(Integer placeofsupplyGCCCountryId) {
        this.placeofsupplyGCCCountryId = placeofsupplyGCCCountryId;
    }

    public EdsCountry getPlaceOfSupplyGCCCountry() {
        return placeOfSupplyGCCCountry;
    }

    public void setPlaceOfSupplyGCCCountry(EdsCountry placeOfSupplyGCCCountry) {
        this.placeOfSupplyGCCCountry = placeOfSupplyGCCCountry;
    }

    public Integer getPlaceofsupplyId() {
        return placeofsupplyId;
    }

    public void setPlaceofsupplyId(Integer placeofsupplyId) {
        this.placeofsupplyId = placeofsupplyId;
    }

    public EdsRegion getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(EdsRegion placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public boolean isReverseChargeApplicable() {
        return reverseChargeApplicable;
    }

    public void setReverseChargeApplicable(boolean reverseChargeApplicable) {
        this.reverseChargeApplicable = reverseChargeApplicable;
    }

    public void addItem(EdsBankTransferItem item) {
        item.setMoneyTransfer(this);
        items.add(item);
    }

    public NewManualTransaction creatTransferData() {
        NewManualTransaction data = new NewManualTransaction();
        data.setObjectId(objectID);
        data.setBankAccountItem(getBankAccount() != null ? getBankAccount().createBankItem() : null);
        data.setNarration(name);
        data.setDate(new DateNonConvertable(date));
        data.setReference(reference);
        data.setRequiredTotal(requiredTotal);
        data.setSubtotal(subtotal);
        data.setTaxTotal(taxTotal);
        data.setTaxForeignTotal(taxForeignTotal);
        data.setTotal(total);
        data.setTaxCalculationType(taxCalculationType);
        data.setTransferType(transferType);
        data.setCurrency(getCurrency().createCurrencyItem());
        data.setExchangeRate(getExchangeRate());
        data.setNumber(number != null ? number : "");
        data.setIntNumber(intNumber);
        data.setCheckNumber(checkNumber);
        data.setPostDatedTransaction(isPostDatedTransaction());
        if (getProject() != null) {
            data.setProject(getProject().getAsSelectItem());
        }
        Set<String> projectsInLine = new HashSet<>();
        for (EdsBankTransferItem bti : items) {
            if (bti.getProject() != null) {
                if (!StringUtils.isEmpty(bti.getProject().getNumber())) {
                    projectsInLine.add(bti.getProject().getNumber() + " -> " + bti.getProject().getName());
                } else {
                    projectsInLine.add(bti.getProject().getName());
                }
            }
        }
        if (!projectsInLine.isEmpty()) {
            ArrayList<String> list = new ArrayList<>();
            list.addAll(projectsInLine);
            data.setProject(new SelectItem(null, ServerUtils.getAsCommoDelimited(list, "", " , ")));
        }

        if (getCashAccount() != null) {
            SelectItem cashAccountItem = new SelectItem();
            cashAccountItem.setId(getCashAccount().getObjectID());
            cashAccountItem.setName((getCashAccount().getCodeString() != null ? getCashAccount().getCodeString() + " -> " : "")+getCashAccount().getName());
            cashAccountItem.setDescription(getCashAccount().getCodeString());
            cashAccountItem.setReferenceCode(getCashAccount().getCodeString());

            data.setCashAccount(cashAccountItem);
        }
        try {
            if (pdfTemplate != null && !pdfTemplate.getDeleted()) {
                data.setPdfTemplateID(pdfTemplate.getObjectID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        NewManualTransactionItem[] itemsArray = new NewManualTransactionItem[items.size()];
        int inc = 0;
        for (EdsBankTransferItem i : items) {
            itemsArray[inc] = i.createTransferItem();
            if (getItemCustomFields() != null && i.getBankTransferItemCustomFields() != null) {
                ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();

                for (CompanyCustomFieldItem cf : getItemCustomFields()) {
                    customFieldItems.add(cf.cloneObject());
                }
                itemsArray[inc].setItemCustomFields(CustomFieldsUtils.setRPCCustomFieldItems(i.getBankTransferItemCustomFields(), customFieldItems));
            }
            inc++;
        }
        data.setItems(itemsArray);
        return data;
    }

    public Date getCreationDate() {
        return creationDate;
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

    @Override
    public void setLastUpdateTime(Date value) {
        lastUpdated = value;
    }

    @Override
    public void setUpdater(EdsUser user) {

    }

    @Override
    public void setCreationTime(Date value) {
        this.creationDate = value;
    }

    @Override
    public void setCreator(EdsUser value) {
        this.creator = value;
    }
}
