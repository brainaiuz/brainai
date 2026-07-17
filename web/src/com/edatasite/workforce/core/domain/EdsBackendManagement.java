package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendManagementListItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Ilhombek
 * Date: 4/23/12
 * Time: 12:49 PM
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "backendManagement")
public class EdsBackendManagement extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID; //ObjectID

    @Column(name = "company_id")
    private Integer companyID; //Company ID

    @Column(name = "companyName")
    private String companyName; //Company Name

    @Column(name = "creationTime")
    private Date creationTime; //Creation Time

    @Column(name = "creator_id")
    private Integer creatorID; //Creator ID

    @Column(name = "creatorName")
    private String creatorName; //Creator Name

    @Column(name = "deleted")
    private Boolean deleted = false; //Deleted

    @Column(name = "enableSalesBackend")
    private Boolean enableSalesBackend; //Sales Backend shown to User

    @Column(name = "enableSupportBackend")
    private Boolean enableSupportBackend; //Support Backend shown to User

    @Column(name = "enableAdminBackend")
    private Boolean enableAdminBackend; //System Backend shown to User

    @Column(name = "enablePartnerAdminBackend", columnDefinition = "boolean default false")
    private boolean enablePartnerAdminBackend = false;

   /* @Column(name = "enablePDFBackend")
    private Boolean enablePDFBackend; //PDF Backend shown to User*/

    @Column(name = "enableDeveloperBackend")
    private Boolean enableDeveloperBackend; //Reporting Backend shown to User

    @Column(name = "localComputerIPAddress")
    private String localComputerIPAddress; //Creator/Updater Local Host Address

    @Column(name = "localComputerName")
    private String localComputerName; //Creator/Updater Local Host Name

    @Column(name = "updateTime")
    private Date updateTime; //Last Update Time

    @Column(name = "updater_id")
    private Integer updaterID; //Last Updater ID

    @Column(name = "updaterName")
    private String updaterName; //Last Updater Name

    @Column(name = "user_id")
    private Integer userID; //User ID

    @Column(name = "userName")
    private String userName; //User Name

    @Column(name = "promotionalCode")
    private String promotionalCode; //for Promotional Code field in sign up page

    @Column(name = "hostNames")
    private String hostNames;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Integer getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Integer creatorID) {
        this.creatorID = creatorID;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Boolean getDeleted() {
        return deleted != null ? deleted : Boolean.FALSE;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getEnableSalesBackend() {
        return enableSalesBackend != null ? enableSalesBackend : Boolean.FALSE;
    }

    public void setEnableSalesBackend(Boolean enableSalesBackend) {
        this.enableSalesBackend = enableSalesBackend;
    }

    public Boolean getEnableSupportBackend() {
        return enableSupportBackend != null ? enableSupportBackend : Boolean.FALSE;
    }

    public void setEnableSupportBackend(Boolean enableSupportBackend) {
        this.enableSupportBackend = enableSupportBackend;
    }

    public Boolean getEnableAdminBackend() {
        return enableAdminBackend != null ? enableAdminBackend : Boolean.FALSE;
    }

    public void setEnableAdminBackend(Boolean enableSystemBackend) {
        this.enableAdminBackend = enableSystemBackend;
    }

    public boolean getEnablePartnerAdminBackend() {
        return enablePartnerAdminBackend;
    }

    public void setEnablePartnerAdminBackend(boolean enablePartnerAdminBackend) {
        this.enablePartnerAdminBackend = enablePartnerAdminBackend;
    }


  /*  public Boolean getEnablePDFBackend() {
        return enablePDFBackend != null ? enablePDFBackend : Boolean.FALSE;
    }

    public void setEnablePDFBackend(Boolean enablePDFBackend) {
        this.enablePDFBackend = enablePDFBackend;
    }*/

    public Boolean getEnableDeveloperBackend() {
        return enableDeveloperBackend != null ? enableDeveloperBackend : Boolean.FALSE;
    }

    public void setEnableDeveloperBackend(Boolean enableReportingBackend) {
        this.enableDeveloperBackend = enableReportingBackend;
    }

    public String getLocalComputerIPAddress() {
        return localComputerIPAddress;
    }

    public void setLocalComputerIPAddress(String localComputerIPAddress) {
        this.localComputerIPAddress = localComputerIPAddress;
    }

    public String getLocalComputerName() {
        return localComputerName;
    }

    public void setLocalComputerName(String localComputerName) {
        this.localComputerName = localComputerName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getUpdaterID() {
        return updaterID;
    }

    public void setUpdaterID(Integer updaterID) {
        this.updaterID = updaterID;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPromotionalCode() {
        return promotionalCode;
    }

    public void setPromotionalCode(String promotionalCode) {
        this.promotionalCode = promotionalCode;
    }

    public String getHostNames() {
        return hostNames;
    }

    public void setHostNames(String hostNames) {
        this.hostNames = hostNames;
    }

    public BackendManagementListItem getRPC() {
        BackendManagementListItem item = new BackendManagementListItem();
        item.setObjectID(getObjectID());
        item.setCompanyID(getCompanyID());
        item.setCompanyName(getCompanyName());
        item.setUserName(getUserName());
        item.setCreatorName(getCreatorName());
        item.setCreateTime(getCreationTime());
        item.setUpdaterName(getUpdaterName());
        item.setUpdateTime(getUpdateTime());
        item.setUpdaterID(getUpdaterID());
        item.setCreatorID(getCreatorID());
        item.setHostName(getHostNames());
        item.setPromotionCode(getPromotionalCode());
//        item.setEnablePDFBackend(getEnablePDFBackend());
        item.setEnableDeveloperBackend(getEnableDeveloperBackend());
        item.setEnableSalesBackend(getEnableSalesBackend());
        item.setEnableSupportBackend(getEnableSupportBackend());
        item.setEnableAdminBackend(getEnableAdminBackend());
        item.setEnablePartnerAdminBackend(getEnablePartnerAdminBackend());

        return item;
    }
}