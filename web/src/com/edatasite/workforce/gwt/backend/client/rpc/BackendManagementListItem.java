package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * User: Ilhombek
 * Date: 4/23/12
 * Time: 3:55 PM
 */
public class BackendManagementListItem implements IsSerializable {

    public static final String ACTION = "action";                       // -1  -0
    public static final String COMPANY_ID = "companyID";                // -2  -1
    public static final String COMPANY_NAME = "companyName";            // -3  -2
    public static final String USER_NAME = "userName";                  // -4  -3
    public static final String CREATOR_NAME = "creatorName";            // -5  -4
    public static final String CREATION_TIME = "creationTime";          // -6  -5
    public static final String UPDATER_NAME = "updaterName";            // -7  -6
    public static final String UPDATE_TIME = "updateTime";              // -8  -7
    public static final String SALES_BACKEND = "enableSalesBackend";         // -9  -8
    public static final String SUPPORT_BACKEND = "enableSupportBackend";     // -10 -9
    public static final String ADMIN_BACKEND = "enableAdminBackend";       // -11 -10
    public static final String PARTNER_ADMIN_BACKEND = "enablePartnerAdminBackend";  // -11 -12
    //    public static final String PDF_BACKEND = "enablePDFBackend";             // -12 -11
    public static final String DEVELOPER_BACKEND = "enableDeveloperBackend"; // -13 -12
    public static final String HOST_NAME = "hostName";                       // -14 -13
    public static  final String Expire_Date = "Expire_Date";
    //    public  static  final String Not_Logged = "Not_Logged";
    private Integer objectID;                         // - 1
    private Integer companyID;                        // - 2
    private String companyName;                       // - 3
    private Integer creatorID;                        // - 4
    private String creatorName;                       // - 5
    private Date createTime;                          // - 6
    private String creatorOrUpdaterLocalComputerName; // - 7
    private String creatorOrUpdaterLocalHostIP;       // - 8
    private boolean enableSalesBackend;               // - 9
    private boolean enableSupportBackend;             // - 10
    private boolean enableAdminBackend;              // - 11
    private boolean enablePartnerAdminBackend;       // - 11_0
    //    private boolean enablePDFBackend;                 // - 11_0
    private boolean enableDeveloperBackend;           // - 11_0_0
    private String hostName;                          // - 11_1
    private Integer updaterID;                        // - 12
    private String updaterName;                       // - 13
    private Date updateTime;                          // - 14
    private Integer userID;                           // - 15
    private String userName;                          // - 16
	private String promotionCode;

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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreatorOrUpdaterLocalComputerName() {
        return creatorOrUpdaterLocalComputerName;
    }

    public void setCreatorOrUpdaterLocalComputerName(String creatorOrUpdaterLocalComputerName) {
        this.creatorOrUpdaterLocalComputerName = creatorOrUpdaterLocalComputerName;
    }

    public String getCreatorOrUpdaterLocalHostIP() {
        return creatorOrUpdaterLocalHostIP;
    }

    public void setCreatorOrUpdaterLocalHostIP(String creatorOrUpdaterLocalHostIP) {
        this.creatorOrUpdaterLocalHostIP = creatorOrUpdaterLocalHostIP;
    }

    public boolean isEnableSalesBackend() {
        return enableSalesBackend;
    }

    public void setEnableSalesBackend(boolean enableSalesBackend) {
        this.enableSalesBackend = enableSalesBackend;
    }

    public boolean isEnableSupportBackend() {
        return enableSupportBackend;
    }

    public void setEnableSupportBackend(boolean enableSupportBackend) {
        this.enableSupportBackend = enableSupportBackend;
    }

    public boolean isEnableAdminBackend() {
        return enableAdminBackend;
    }

    public void setEnableAdminBackend(boolean enableAdminBackend) {
        this.enableAdminBackend = enableAdminBackend;
    }

    public boolean isEnablePartnerAdminBackend() {
        return enablePartnerAdminBackend;
    }

    public void setEnablePartnerAdminBackend(boolean enablePartnerAdminBackend) {
        this.enablePartnerAdminBackend = enablePartnerAdminBackend;
    }

    /*    public boolean isEnablePDFBackend() {
        return enablePDFBackend;
    }

    public void setEnablePDFBackend(boolean enablePDFBackend) {
        this.enablePDFBackend = enablePDFBackend;
    }*/

    public boolean isEnableDeveloperBackend() {
        return enableDeveloperBackend;
    }

    public void setEnableDeveloperBackend(boolean enableDeveloperBackend) {
        this.enableDeveloperBackend = enableDeveloperBackend;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

	public void setPromotionCode(String promotionCode) {
		this.promotionCode = promotionCode;
	}

	public String getPromotionCode() {
		return promotionCode;
	}
}