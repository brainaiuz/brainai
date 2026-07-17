package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Sherali
 * Date: Apr 3, 2009
 * Time: 1:04:17 PM
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "subscriptionHistory")
public class EdsSubscriptionHistory extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    private Integer users = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    private EdsUsagePlan usagePlan;
    private Integer noAccessUsers;
    private Integer essUsers;
    private Boolean isPaid = false;
    private Integer storage = 0;
    private float totalAmount = 0;
    private float discount = 0;
    private float taxt = 0;
    @Column(name = "payment_StartDate")
    private Date payment_StartDate;
    @Column(name = "payment_EndDate")
    private Date payment_EndDate;

    private String supportPackageNAME;
    private String categoryCODE;

    private Boolean accountsModule;
    private Boolean humanModule;
    private Boolean salesModule;
    private Boolean projectModule;
    private Boolean payrollModule;

    @Column(name = "addon_online_training")
    private Double addonOnlineTraining = 0d;
    @Column(name = "addon_initial_setup")
    private Double addonInitialSetup = 0d;
    @Column(name = "addon_extra_storage")
    private Double addonExtraStorage = 0d;
    @Column(name = "addon_custompdf")
    private Double addonCustomPDFTemplate = 0d;
    @Column(name = "addon_dedicated_dev")
    private Double addonDedicatedDeveloper = 0d;
    @Column(name = "addon_dedicated_accountmangr")
    private Double addonDedicatedAccountManager = 0d;

    private Boolean isUKCompany = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public EdsUsagePlan getUsagePlan() {
        return usagePlan;
    }

    public void setUsagePlan(EdsUsagePlan usagePlan) {
        this.usagePlan = usagePlan;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
        this.storage = storage;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getTaxt() {
        return taxt;
    }

    public void setTaxt(float taxt) {
        this.taxt = taxt;
    }

    public Date getPayment_StartDate() {
        return payment_StartDate;
    }

    public void setPayment_StartDate(Date payment_StartDate) {
        this.payment_StartDate = payment_StartDate;
    }

    public Date getPayment_EndDate() {
        return payment_EndDate;
    }

    public void setPayment_EndDate(Date payment_EndDate) {
        this.payment_EndDate = payment_EndDate;
    }

    public String getSupportPackageNAME() {
        return supportPackageNAME;
    }

    public void setSupportPackageNAME(String supportPackageNAME) {
        this.supportPackageNAME = supportPackageNAME;
    }

    public String getCategoryCODE() {
        return categoryCODE;
    }

    public void setCategoryCODE(String categoryCODE) {
        this.categoryCODE = categoryCODE;
    }

    public Boolean isUKCompany() {
        return isUKCompany;
    }

    public void setUKCompany(Boolean UKCompany) {
        isUKCompany = UKCompany;
    }

    public Boolean getAccountsModule() {
        return accountsModule;
    }

    public void setAccountsModule(Boolean accountsModule) {
        this.accountsModule = accountsModule;
    }

    public Boolean getHumanModule() {
        return humanModule;
    }

    public void setHumanModule(Boolean humanModule) {
        this.humanModule = humanModule;
    }

    public Boolean getSalesModule() {
        return salesModule;
    }

    public void setSalesModule(Boolean salesModule) {
        this.salesModule = salesModule;
    }

    public Boolean getProjectModule() {
        return projectModule;
    }

    public void setProjectModule(Boolean projectModule) {
        this.projectModule = projectModule;
    }

    public Boolean getPayrollModule() {
        return payrollModule;
    }

    public void setPayrollModule(Boolean payrollModule) {
        this.payrollModule = payrollModule;
    }

    public Boolean getUKCompany() {
        return isUKCompany;
    }

    public Integer getNoAccessUsers() {
        return noAccessUsers;
    }

    public void setNoAccessUsers(Integer noAccessUsers) {
        this.noAccessUsers = noAccessUsers;
    }

    public Integer getEssUsers() {
        return essUsers;
    }

    public void setEssUsers(Integer essUsers) {
        this.essUsers = essUsers;
    }

    public Double getAddonOnlineTraining() {
        return addonOnlineTraining!=null?addonOnlineTraining:0;
    }

    public void setAddonOnlineTraining(Double addonOnlineTraining) {
        this.addonOnlineTraining = addonOnlineTraining;
    }

    public Double getAddonInitialSetup() {
        return addonInitialSetup!=null?addonInitialSetup:0;
    }

    public void setAddonInitialSetup(Double addonInitialSetup) {
        this.addonInitialSetup = addonInitialSetup;
    }

    public Double getAddonExtraStorage() {
        return addonExtraStorage!=null?addonExtraStorage:0;
    }

    public void setAddonExtraStorage(Double addonExtraStorage) {
        this.addonExtraStorage = addonExtraStorage;
    }

    public Double getAddonCustomPDFTemplate() {
        return addonCustomPDFTemplate!=null?addonCustomPDFTemplate:0;
    }

    public void setAddonCustomPDFTemplate(Double addonCustomPDFTemplate) {
        this.addonCustomPDFTemplate = addonCustomPDFTemplate;
    }

    public Double getAddonDedicatedDeveloper() {
        return addonDedicatedDeveloper!=null?addonDedicatedDeveloper:0;
    }

    public void setAddonDedicatedDeveloper(Double addonDedicatedDeveloper) {
        this.addonDedicatedDeveloper = addonDedicatedDeveloper;
    }

    public Double getAddonDedicatedAccountManager() {
        return addonDedicatedAccountManager!=null?addonDedicatedAccountManager:0;
    }

    public void setAddonDedicatedAccountManager(Double addonDedicatedAccountManager) {
        this.addonDedicatedAccountManager = addonDedicatedAccountManager;
    }
}