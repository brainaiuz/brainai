package com.edatasite.workforce.gwt.core.client.rpc;

import java.util.HashMap;

/**
 * Created by Abu Ayyub on 9/8/18.
 */
public class KpiPaymentRequestObject extends RequestObject {

    //reccuring subscription title
    private String reccurSubscripTitle;
    //users
    private String usersItem;
    private String usersUnitPrice;
    private String usersQty;
    private String usersTotalYear;

    //ess
    private String essItem;
    private String essUnitPrice;
    private String essQty;
    private String essTotalYear;

    //ess
    private String nonUserItem;
    private String nonUserUnitPrice;
    private String nonUserQty;
    private String nonUserTotalYear;

    //addons title and online training
    private String addonsTitle;
    private String addonsOnlineTrainingItem;
    private String addonsOnlineTrainingQty;
    private String addonsOnlineTrainingTotalYear;

    //initial set up package
    private String addonsInitialSetUpPackageItem;
    private String addonsInitialSetUpPackageQty;
    private String addonsInitialSetUpPackageTotalYear;

    //premium support
    private String addonsPremiumSupportItem;
    private String addonsPremiumSupportQty;
    private String addonsPremiumSupportTotalYear;

    //custom pdf
    private String addonsCustomPDFItem;
    private String addonsCustomPDFQty;
    private String addonsCustomPDFTotalYear;

    //extra storage
    private String addonsExtraStorageItem;
    private String addonsExtraStorageQty;
    private String addonsExtraStorageTotalYear;

    //dedicated developer
    private String addonsDedicatedDeveloperItem;
    private String addonsDedicatedDeveloperQty;
    private String addonsDedicatedDeveloperTotalYear;

    //users discount
    private String usersDiscountTitle;
    private String usersDiscountTotal;

    //total / subscription
    private String totalSubscriptionTitle;
    private String totalSubscriptionTotal;

    //total add-on
    private String totalAddonTitle;
    private String totalAddonTotal;

    //to be paid
    private String tobePaidTitle;
    private String tobePaidTotal;

    public KpiPaymentRequestObject() {
    }

    public KpiPaymentRequestObject(Integer objectID) {
        super(objectID);
    }

    public HashMap<String, String> getRequestParams() {
        HashMap<String, String> parametersMap = new HashMap<>();

        parametersMap.put("objectID", getObjectID() != null ? getObjectID().toString() : "");
        parametersMap.put("reccurSubscripTitle", getReccurSubscripTitle() != null ? getReccurSubscripTitle() : "");
        parametersMap.put("usersItem", getUsersItem() != null ? getUsersItem() : "");
        parametersMap.put("usersUnitPrice", getUsersUnitPrice() != null ? getUsersUnitPrice() : "");
        parametersMap.put("usersQty", getUsersQty() != null ? getUsersQty() : "");
        parametersMap.put("usersTotalYear", getUsersTotalYear() != null ? getUsersTotalYear() : "");

        parametersMap.put("essItem", getEssItem() != null ? getEssItem() : "");
        parametersMap.put("essUnitPrice", getEssUnitPrice() != null ? getEssUnitPrice() : "");
        parametersMap.put("essQty", getEssQty() != null ? getEssQty() : "");
        parametersMap.put("essTotalYear", getEssTotalYear() != null ? getEssTotalYear() : "");

        parametersMap.put("nonUserItem", getNonUserItem() != null ? getNonUserItem() : "");
        parametersMap.put("nonUserUnitPrice", getNonUserUnitPrice() != null ? getNonUserUnitPrice() : "");
        parametersMap.put("nonUserQty", getNonUserQty() != null ? getNonUserQty() : "");
        parametersMap.put("nonUserTotalYear", getNonUserTotalYear() != null ? getNonUserTotalYear() : "");

        parametersMap.put("addonsTitle", getAddonsTitle() != null ? getAddonsTitle() : "");
        parametersMap.put("addonsOnlineTrainingItem", getAddonsOnlineTrainingItem() != null ? getAddonsOnlineTrainingItem() : "");
        parametersMap.put("addonsOnlineTrainingQty", getAddonsOnlineTrainingQty() != null ? getAddonsOnlineTrainingQty() : "");
        parametersMap.put("addonsOnlineTrainingTotalYear", getAddonsOnlineTrainingTotalYear() != null ? getAddonsOnlineTrainingTotalYear() : "");

        parametersMap.put("addonsInitialSetUpPackageItem", getAddonsInitialSetUpPackageItem() != null ? getAddonsInitialSetUpPackageItem() : "");
        parametersMap.put("addonsInitialSetUpPackageQty", getAddonsInitialSetUpPackageQty() != null ? getAddonsInitialSetUpPackageQty() : "");
        parametersMap.put("addonsInitialSetUpPackageTotalYear", getAddonsInitialSetUpPackageTotalYear() != null ? getAddonsInitialSetUpPackageTotalYear() : "");

        parametersMap.put("addonsPremiumSupportItem", getAddonsPremiumSupportItem() != null ? getAddonsPremiumSupportItem() : "");
        parametersMap.put("addonsPremiumSupportQty", getAddonsPremiumSupportQty() != null ? getAddonsPremiumSupportQty() : "");
        parametersMap.put("addonsPremiumSupportTotalYear", getAddonsPremiumSupportTotalYear() != null ? getAddonsPremiumSupportTotalYear() : "");

        parametersMap.put("addonsCustomPDFItem", getAddonsCustomPDFItem() != null ? getAddonsCustomPDFItem() : "");
        parametersMap.put("addonsCustomPDFQty", getAddonsCustomPDFQty() != null ? getAddonsCustomPDFQty() : "");
        parametersMap.put("addonsCustomPDFTotalYear", getAddonsCustomPDFTotalYear() != null ? getAddonsCustomPDFTotalYear() : "");

        parametersMap.put("addonsExtraStorageItem", getAddonsExtraStorageItem() != null ? getAddonsExtraStorageItem() : "");
        parametersMap.put("addonsExtraStorageQty", getAddonsExtraStorageQty() != null ? getAddonsExtraStorageQty() : "");
        parametersMap.put("addonsExtraStorageTotalYear", getAddonsExtraStorageTotalYear() != null ? getAddonsExtraStorageTotalYear() : "");

        parametersMap.put("addonsDedicatedDeveloperItem", getAddonsDedicatedDeveloperItem() != null ? getAddonsDedicatedDeveloperItem() : "");
        parametersMap.put("addonsDedicatedDeveloperQty", getAddonsDedicatedDeveloperQty() != null ? getAddonsDedicatedDeveloperQty() : "");
        parametersMap.put("addonsDedicatedDeveloperTotalYear", getAddonsDedicatedDeveloperTotalYear() != null ? getAddonsDedicatedDeveloperTotalYear() : "");

        parametersMap.put("usersDiscountTitle", getUsersDiscountTitle() != null ? getUsersDiscountTitle() : "");
        parametersMap.put("usersDiscountTotal", getUsersDiscountTotal() != null ? getUsersDiscountTotal() : "");

        parametersMap.put("totalSubscriptionTitle", getTotalSubscriptionTitle() != null ? getTotalSubscriptionTitle() : "");
        parametersMap.put("totalSubscriptionTotal", getTotalSubscriptionTotal() != null ? getTotalSubscriptionTotal() : "");

        parametersMap.put("totalAddonTitle", getTotalAddonTitle() != null ? getTotalAddonTitle() : "");
        parametersMap.put("totalAddonTotal", getTotalAddonTotal() != null ? getTotalAddonTotal() : "");

        parametersMap.put("tobePaidTitle", getTobePaidTitle() != null ? getTobePaidTitle() : "");
        parametersMap.put("tobePaidTotal", getTobePaidTotal() != null ? getTobePaidTotal() : "");

        return parametersMap;
    }

    //reccuring subscription title
    public String getReccurSubscripTitle() {
        return reccurSubscripTitle;
    }

    public void setReccurSubscripTitle(String reccurSubscripTitle) {
        this.reccurSubscripTitle = reccurSubscripTitle;
    }

    //users
    public String getUsersItem() {
        return usersItem;
    }

    public void setUsersItem(String usersItem) {
        this.usersItem = usersItem;
    }

    public String getUsersUnitPrice() {
        return usersUnitPrice;
    }

    public void setUsersUnitPrice(String usersUnitPrice) {
        this.usersUnitPrice = usersUnitPrice;
    }

    public String getUsersQty() {
        return usersQty;
    }

    public void setUsersQty(String usersQty) {
        this.usersQty = usersQty;
    }

    public String getUsersTotalYear() {
        return usersTotalYear;
    }

    public void setUsersTotalYear(String usersTotalYear) {
        this.usersTotalYear = usersTotalYear;
    }

    //ess
    public String getEssItem() {
        return essItem;
    }

    public void setEssItem(String essItem) {
        this.essItem = essItem;
    }

    public String getEssUnitPrice() {
        return essUnitPrice;
    }

    public void setEssUnitPrice(String essUnitPrice) {
        this.essUnitPrice = essUnitPrice;
    }

    public String getEssQty() {
        return essQty;
    }

    public void setEssQty(String essQty) {
        this.essQty = essQty;
    }

    public String getEssTotalYear() {
        return essTotalYear;
    }

    public void setEssTotalYear(String essTotalYear) {
        this.essTotalYear = essTotalYear;
    }

    //nonUser
    public String getNonUserItem() {
        return nonUserItem;
    }

    public void setNonUserItem(String nonUserItem) {
        this.nonUserItem = nonUserItem;
    }

    public String getNonUserUnitPrice() {
        return nonUserUnitPrice;
    }

    public void setNonUserUnitPrice(String nonUserUnitPrice) {
        this.nonUserUnitPrice = nonUserUnitPrice;
    }

    public String getNonUserQty() {
        return nonUserQty;
    }

    public void setNonUserQty(String nonUserQty) {
        this.nonUserQty = nonUserQty;
    }

    public String getNonUserTotalYear() {
        return nonUserTotalYear;
    }

    public void setNonUserTotalYear(String nonUserTotalYear) {
        this.nonUserTotalYear = nonUserTotalYear;
    }

    //addons title and online training
    public String getAddonsTitle() {
        return addonsTitle;
    }

    public void setAddonsTitle(String addonsTitle) {
        this.addonsTitle = addonsTitle;
    }

    public String getAddonsOnlineTrainingItem() {
        return addonsOnlineTrainingItem;
    }

    public void setAddonsOnlineTrainingItem(String addonsOnlineTrainingItem) {
        this.addonsOnlineTrainingItem = addonsOnlineTrainingItem;
    }

    public String getAddonsOnlineTrainingQty() {
        return addonsOnlineTrainingQty;
    }

    public void setAddonsOnlineTrainingQty(String addonsOnlineTrainingQty) {
        this.addonsOnlineTrainingQty = addonsOnlineTrainingQty;
    }

    public String getAddonsOnlineTrainingTotalYear() {
        return addonsOnlineTrainingTotalYear;
    }

    public void setAddonsOnlineTrainingTotalYear(String addonsOnlineTrainingTotalYear) {
        this.addonsOnlineTrainingTotalYear = addonsOnlineTrainingTotalYear;
    }

    //initial set up package
    public String getAddonsInitialSetUpPackageItem() {
        return addonsInitialSetUpPackageItem;
    }

    public void setAddonsInitialSetUpPackageItem(String addonsInitialSetUpPackageItem) {
        this.addonsInitialSetUpPackageItem = addonsInitialSetUpPackageItem;
    }

    public String getAddonsInitialSetUpPackageQty() {
        return addonsInitialSetUpPackageQty;
    }

    public void setAddonsInitialSetUpPackageQty(String addonsInitialSetUpPackageQty) {
        this.addonsInitialSetUpPackageQty = addonsInitialSetUpPackageQty;
    }

    public String getAddonsInitialSetUpPackageTotalYear() {
        return addonsInitialSetUpPackageTotalYear;
    }

    public void setAddonsInitialSetUpPackageTotalYear(String addonsInitialSetUpPackageTotalYear) {
        this.addonsInitialSetUpPackageTotalYear = addonsInitialSetUpPackageTotalYear;
    }

    //premium support
    public String getAddonsPremiumSupportItem() {
        return addonsPremiumSupportItem;
    }

    public void setAddonsPremiumSupportItem(String addonsPremiumSupportItem) {
        this.addonsPremiumSupportItem = addonsPremiumSupportItem;
    }

    public String getAddonsPremiumSupportQty() {
        return addonsPremiumSupportQty;
    }

    public void setAddonsPremiumSupportQty(String addonsPremiumSupportQty) {
        this.addonsPremiumSupportQty = addonsPremiumSupportQty;
    }

    public String getAddonsPremiumSupportTotalYear() {
        return addonsPremiumSupportTotalYear;
    }

    public void setAddonsPremiumSupportTotalYear(String addonsPremiumSupportTotalYear) {
        this.addonsPremiumSupportTotalYear = addonsPremiumSupportTotalYear;
    }

    //custom pdf
    public String getAddonsCustomPDFItem() {
        return addonsCustomPDFItem;
    }

    public void setAddonsCustomPDFItem(String addonsCustomPDFItem) {
        this.addonsCustomPDFItem = addonsCustomPDFItem;
    }

    public String getAddonsCustomPDFQty() {
        return addonsCustomPDFQty;
    }

    public void setAddonsCustomPDFQty(String addonsCustomPDFQty) {
        this.addonsCustomPDFQty = addonsCustomPDFQty;
    }

    public String getAddonsCustomPDFTotalYear() {
        return addonsCustomPDFTotalYear;
    }

    public void setAddonsCustomPDFTotalYear(String addonsCustomPDFTotalYear) {
        this.addonsCustomPDFTotalYear = addonsCustomPDFTotalYear;
    }

    //extra storage
    public String getAddonsExtraStorageItem() {
        return addonsExtraStorageItem;
    }

    public void setAddonsExtraStorageItem(String addonsExtraStorageItem) {
        this.addonsExtraStorageItem = addonsExtraStorageItem;
    }

    public String getAddonsExtraStorageQty() {
        return addonsExtraStorageQty;
    }

    public void setAddonsExtraStorageQty(String addonsExtraStorageQty) {
        this.addonsExtraStorageQty = addonsExtraStorageQty;
    }

    public String getAddonsExtraStorageTotalYear() {
        return addonsExtraStorageTotalYear;
    }

    public void setAddonsExtraStorageTotalYear(String addonsExtraStorageTotalYear) {
        this.addonsExtraStorageTotalYear = addonsExtraStorageTotalYear;
    }

    //dedicated developer
    public String getAddonsDedicatedDeveloperItem() {
        return addonsDedicatedDeveloperItem;
    }

    public void setAddonsDedicatedDeveloperItem(String addonsDedicatedDeveloperItem) {
        this.addonsDedicatedDeveloperItem = addonsDedicatedDeveloperItem;
    }

    public String getAddonsDedicatedDeveloperQty() {
        return addonsDedicatedDeveloperQty;
    }

    public void setAddonsDedicatedDeveloperQty(String addonsDedicatedDeveloperQty) {
        this.addonsDedicatedDeveloperQty = addonsDedicatedDeveloperQty;
    }

    public String getAddonsDedicatedDeveloperTotalYear() {
        return addonsDedicatedDeveloperTotalYear;
    }

    public void setAddonsDedicatedDeveloperTotalYear(String addonsDedicatedDeveloperTotalYear) {
        this.addonsDedicatedDeveloperTotalYear = addonsDedicatedDeveloperTotalYear;
    }

    //users discount
    public String getUsersDiscountTitle() {
        return usersDiscountTitle;
    }

    public void setUsersDiscountTitle(String usersDiscountTitle) {
        this.usersDiscountTitle = usersDiscountTitle;
    }

    public String getUsersDiscountTotal() {
        return usersDiscountTotal;
    }

    public void setUsersDiscountTotal(String usersDiscountTotal) {
        this.usersDiscountTotal = usersDiscountTotal;
    }

    //total / subscription
    public String getTotalSubscriptionTitle() {
        return totalSubscriptionTitle;
    }

    public void setTotalSubscriptionTitle(String totalSubscriptionTitle) {
        this.totalSubscriptionTitle = totalSubscriptionTitle;
    }

    public String getTotalSubscriptionTotal() {
        return totalSubscriptionTotal;
    }

    public void setTotalSubscriptionTotal(String totalSubscriptionTotal) {
        this.totalSubscriptionTotal = totalSubscriptionTotal;
    }

    //total add-on
    public String getTotalAddonTitle() {
        return totalAddonTitle;
    }

    public void setTotalAddonTitle(String totalAddonTitle) {
        this.totalAddonTitle = totalAddonTitle;
    }

    public String getTotalAddonTotal() {
        return totalAddonTotal;
    }

    public void setTotalAddonTotal(String totalAddonTotal) {
        this.totalAddonTotal = totalAddonTotal;
    }

    //to be paid
    public String getTobePaidTitle() {
        return tobePaidTitle;
    }

    public void setTobePaidTitle(String tobePaidTitle) {
        this.tobePaidTitle = tobePaidTitle;
    }

    public String getTobePaidTotal() {
        return tobePaidTotal;
    }

    public void setTobePaidTotal(String tobePaidTotal) {
        this.tobePaidTotal = tobePaidTotal;
    }
}
