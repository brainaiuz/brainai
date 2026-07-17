package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.customfields.EdsFixedAssetCustomFields;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;

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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/5/11
 * Time: 2:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "fixedasset")
public class EdsFixedAsset extends EdsObject implements ObjectHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerid")
    private EdsUser owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private EdsAccount account;//Category

    private String code;
    private String name;
    private String description;

    @Column(precision = 25, scale = 5)
    private BigDecimal cost;

    private Date creationDate;

    private BigDecimal usefulLife;

    @Column(precision = 25, scale = 5)
    private BigDecimal residualValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financedbyid")
    private EdsAccount financedBy;//Account

    @Column(name = "intnumber")
    private Integer intNumber;

    private Boolean calculateDepreciation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseinvoiceid")
    private EdsPurchaseInvoice purchaseInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseorderid")
    private EdsPurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesinvoiceid")
    private EdsSaleInvoice salesInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationid")
    private EdsLocation location;

    @Column(name = "imageid")
    private Integer image;

    private Boolean disposed;

    private Date disposedDate;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "showdescriptioninbarcode")
    private Boolean showDescriptionInBarcode;

    private String status;

    private Date creationTime;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsFixedAssetCustomFields customFields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_accountid")
    private EdsAccount assetAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_accountid")
    private EdsAccount expenseAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentid")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vatId")
    private EdsVat vat;
    @Column(name = "taxAmount", precision = 25, scale = 5)
    private BigDecimal taxAmount;

    private Integer taxCalculationType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fixedAsset", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<EdsDailyDepreciationRate> dailyDepreciationRateList = new ArrayList<>();

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getUsefulLife() {
        return usefulLife;
    }

    public void setUsefulLife(BigDecimal usefulLife) {
        this.usefulLife = usefulLife;
    }

    public BigDecimal getResidualValue() {
        return residualValue;
    }

    public void setResidualValue(BigDecimal residualValue) {
        this.residualValue = residualValue;
    }

    public EdsAccount getFinancedBy() {
        return financedBy;
    }

    public void setFinancedBy(EdsAccount financedBy) {
        this.financedBy = financedBy;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Boolean isCalculateDepreciation() {
        return calculateDepreciation != null ? calculateDepreciation : false;
    }

    public void setCalculateDepreciation(Boolean calculateDepreciation) {
        this.calculateDepreciation = calculateDepreciation;
    }

    public EdsPurchaseInvoice getPurchaseInvoice() {
        return purchaseInvoice;
    }

    public void setPurchaseInvoice(EdsPurchaseInvoice purchaseInvoice) {
        this.purchaseInvoice = purchaseInvoice;
    }

    public EdsPurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(EdsPurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public EdsSaleInvoice getSalesInvoice() {
        return salesInvoice;
    }

    public void setSalesInvoice(EdsSaleInvoice salesInvoice) {
        this.salesInvoice = salesInvoice;
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

    public Boolean getDisposed() {
        return disposed != null ? disposed : false;
    }

    public void setDisposed(Boolean disposed) {
        this.disposed = disposed;
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getShowDescriptionInBarcode() {
        return showDescriptionInBarcode != null ? showDescriptionInBarcode : false;
    }

    public void setShowDescriptionInBarcode(Boolean showDescriptionInBarcode) {
        this.showDescriptionInBarcode = showDescriptionInBarcode;
    }

    public EdsFixedAssetCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsFixedAssetCustomFields customFields) {
        this.customFields = customFields;
    }

    public EdsAccount getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(EdsAccount assetAccount) {
        this.assetAccount = assetAccount;
    }

    public EdsAccount getExpenseAccount() {
        return expenseAccount;
    }

    public void setExpenseAccount(EdsAccount expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    public EdsUser getOwner() {
        return owner;
    }

    public void setOwner(EdsUser owner) {
        this.owner = owner;
    }

    public Date getDisposedDate() {
        return disposedDate;
    }

    public void setDisposedDate(Date disposedDate) {
        this.disposedDate = disposedDate;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public List<EdsDailyDepreciationRate> getDailyDepreciationRateList() {
        return dailyDepreciationRateList;
    }

    public void setDailyDepreciationRateList(List<EdsDailyDepreciationRate> dailyDepreciationRateList) {
        this.dailyDepreciationRateList = dailyDepreciationRateList;
    }

    public void addDailyDepRateItem(EdsDailyDepreciationRate item) {
        item.setFixedAsset(this);
        dailyDepreciationRateList.add(item);
    }

    public EdsVat getVat() {
        return vat;
    }

    public void setVat(EdsVat vat) {
        this.vat = vat;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public BigDecimal getDailyDepreciationRateByDate(Date date) {
        if (getDailyDepreciationRateList() != null && !getDailyDepreciationRateList().isEmpty()) {

            Optional<EdsDailyDepreciationRate> dr = getDailyDepreciationRateList().stream()
                    .filter(ddr -> ddr.getFinancialYearStart().compareTo(date) <= 0 && ddr.getFinancialYearEnd().compareTo(date) >= 0)
                    .findFirst();

            if (dr.isPresent()) {
                return dr.get().getDailyDepreciation();
            }

            /*for (EdsDailyDepreciationRate dailyDepreciationRate : getDailyDepreciationRateList()) {
                if (dailyDepreciationRate.getFinancialYearStart().compareTo(date) <= 0 && dailyDepreciationRate.getFinancialYearEnd().compareTo(date) >= 0) {
                    return dailyDepreciationRate.getDailyDepreciation();
                }
            }*/
        }
        return BigDecimal.ZERO;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    @Override
    public void setLastUpdateTime(Date value) {

    }

    @Override
    public void setUpdater(EdsUser user) {

    }

    @Override
    public void setCreationTime(Date value) {
        this.creationTime = value;
    }

    @Override
    public void setCreator(EdsUser value) {

    }
}
