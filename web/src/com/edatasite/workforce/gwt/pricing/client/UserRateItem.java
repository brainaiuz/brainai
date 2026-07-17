package com.edatasite.workforce.gwt.pricing.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Ilhombek
 * Date: 6/4/12
 * Time: 7:48 PM
 */
public class UserRateItem implements IsSerializable {

    public UserRateItem() {
    }

    private Double discountOneMonth;            //old
    private Double discountThreeMonth;          //old
    private Double discountSixMonth;            //old
    private Double discountTwentyMonth;         //old
    private Integer maxPayableUserCount;        //old
    private Double userRate;                    //old
    private Double userRateUpg;                 //old
    private int count;                 //old

    //---------////---------////---------////---------////---------////---------////---------////---------////---------//

    private String pricingPackageNAME;
    private String pricingPackageNAMEUpg;

    private Double pricePerPackage;
    private Integer userCountMinOneMonth;
    private Integer userCountMinThreeMonth;
    private Integer userCountMinSixMonth;
    private Integer userCountMaxTwentyMonth;
    //support package price
    private Double supportPackagePrice;
    private Double supportPackagePriceUpg;

    private String supportPackageNAME;

    private String supportPackageNAMEUpg;

    //---------////---------////---------////---------////---------////---------////---------////---------////---------//

    public Double getSupportPackagePrice() {
        return supportPackagePrice;
    }

    public void setSupportPackagePrice(Double supportPackagePrice) {
        this.supportPackagePrice = supportPackagePrice;
    }

    public Double getSupportPackagePriceUpg() {
        return supportPackagePriceUpg;
    }

    public void setSupportPackagePriceUpg(Double supportPackagePriceUpg) {
        this.supportPackagePriceUpg = supportPackagePriceUpg;
    }

    public String getPricingPackageNAMEUpg() {
        return pricingPackageNAMEUpg;
    }

    public void setPricingPackageNAMEUpg(String pricingPackageNAMEUpg) {
        this.pricingPackageNAMEUpg = pricingPackageNAMEUpg;
    }

    public String getSupportPackageNAMEUpg() {
        return supportPackageNAMEUpg;
    }

    public void setSupportPackageNAMEUpg(String supportPackageNAMEUpg) {
        this.supportPackageNAMEUpg = supportPackageNAMEUpg;
    }

    public String getSupportPackageNAME() {
        return supportPackageNAME;
    }

    public void setSupportPackageNAME(String supportPackageNAME) {
        this.supportPackageNAME = supportPackageNAME;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getPricingPackageNAME() {
        return pricingPackageNAME;
    }

    public void setPricingPackageNAME(String pricingPackageNAME) {
        this.pricingPackageNAME = pricingPackageNAME;
    }

    public Double getPricePerPackage() {
        return pricePerPackage;
    }

    public void setPricePerPackage(Double pricePerPackage) {
        this.pricePerPackage = pricePerPackage;
    }

    public Integer getUserCountMinOneMonth() {
        return userCountMinOneMonth;
    }

    public void setUserCountMinOneMonth(Integer userCountMinOneMonth) {
        this.userCountMinOneMonth = userCountMinOneMonth;
    }

    public Integer getUserCountMinThreeMonth() {
        return userCountMinThreeMonth;
    }

    public void setUserCountMinThreeMonth(Integer userCountMinThreeMonth) {
        this.userCountMinThreeMonth = userCountMinThreeMonth;
    }

    public Integer getUserCountMinSixMonth() {
        return userCountMinSixMonth;
    }

    public void setUserCountMinSixMonth(Integer userCountMinSixMonth) {
        this.userCountMinSixMonth = userCountMinSixMonth;
    }

    public Integer getUserCountMaxTwentyMonth() {
        return userCountMaxTwentyMonth;
    }

    public void setUserCountMaxTwentyMonth(Integer userCountMaxTwentyMonth) {
        this.userCountMaxTwentyMonth = userCountMaxTwentyMonth;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //---------////---------////---------////---------////---------////---------////---------////---------////---------//


    public Double getDiscountOneMonth() {
        return discountOneMonth;
    }

    public void setDiscountOneMonth(Double discountOneMonth) {
        this.discountOneMonth = discountOneMonth;
    }

    public Double getDiscountThreeMonth() {
        return discountThreeMonth;
    }

    public void setDiscountThreeMonth(Double discountThreeMonth) {
        this.discountThreeMonth = discountThreeMonth;
    }

    public Double getDiscountSixMonth() {
        return discountSixMonth;
    }

    public void setDiscountSixMonth(Double discountSixMonth) {
        this.discountSixMonth = discountSixMonth;
    }

    public Double getDiscountTwentyMonth() {
        return discountTwentyMonth;
    }

    public void setDiscountTwentyMonth(Double discountTwentyMonth) {
        this.discountTwentyMonth = discountTwentyMonth;
    }

    public Double getUserRate() {
        return userRate;
    }

    public void setUserRate(Double userRate) {
        this.userRate = userRate;
    }

    public Integer getMaxPayableUserCount() {
        return maxPayableUserCount;
    }

    public void setMaxPayableUserCount(Integer maxPayableUserCount) {
        this.maxPayableUserCount = maxPayableUserCount;
    }

    public Double getUserRateUpg() {
        return userRateUpg;
    }

    public void setUserRateUpg(Double userRateUpg) {
        this.userRateUpg = userRateUpg;
    }
}