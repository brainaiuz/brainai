package com.edatasite.workforce.gwt.core.client.rpc.form;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Fatkhulla Nigmatjonov
 * Date: 2/27/13
 * Time: 3:30 PM
 */
public class LocalizationPermissionItem implements IsSerializable {

    private Integer companyID;
    private String companName;
    private Boolean code;
    private Boolean defaultText;
    private Boolean en;
    private Boolean ru;
    private Boolean arabic;
    private Boolean turkish;
    private Boolean ger;
    private Boolean spa;
    private Boolean fr;
    private Boolean por;
    private Boolean neder;
    private Boolean ita;
    private Boolean thai;


    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanName() {
        return companName;
    }

    public void setCompanName(String companName) {
        this.companName = companName;
    }

    public Boolean getDefaultText() {
        return defaultText == null ? false : defaultText;
    }

    public void setDefaultText(Boolean defaultText) {
        this.defaultText = defaultText;
    }

    public Boolean getEn() {
        return en == null ? false : en;
    }

    public void setEn(Boolean en) {
        this.en = en;
    }

    public Boolean getRu() {
        return ru == null ? false : ru;
    }

    public void setRu(Boolean ru) {
        this.ru = ru;
    }

    public Boolean getArabic() {
        return arabic == null ? false : arabic;
    }

    public void setArabic(Boolean arabic) {
        this.arabic = arabic;
    }

    public Boolean getTurkish() {
        return turkish == null ? false : turkish;
    }

    public void setTurkish(Boolean turkish) {
        this.turkish = turkish;
    }

    public Boolean getGer() {
        return ger == null ? false : ger;
    }

    public void setGer(Boolean ger) {
        this.ger = ger;
    }

    public Boolean getSpa() {
        return spa == null ? false : spa;
    }

    public void setSpa(Boolean spa) {
        this.spa = spa;
    }

    public Boolean getFr() {
        return fr == null ? false : fr;
    }

    public void setFr(Boolean fr) {
        this.fr = fr;
    }

    public Boolean getPor() {
        return por == null ? false : por;
    }

    public void setPor(Boolean por) {
        this.por = por;
    }

    public Boolean getNeder() {
        return neder == null ? false : neder;
    }

    public void setNeder(Boolean neder) {
        this.neder = neder;
    }

    public Boolean getIta() {
        return ita == null ? false : ita;
    }

    public void setIta(Boolean ita) {
        this.ita = ita;
    }

    public Boolean getThai() {
        return thai == null ? false : thai;
    }

    public void setThai(Boolean thai) {
        this.thai = thai;
    }

    public Boolean getCode() {
        return code == null ? false : code;
    }

    public void setCode(Boolean code) {
        this.code = code;
    }

    public int getAllowedColumnsCount(){
        int count = 0;
        if (getArabic())
            count++;
        if (getDefaultText())
            count++;
        if (getEn())
            count++;
        if (getRu())
            count++;
        if (getSpa())
            count++;
        if (getIta())
            count++;
        if (getPor())
            count++;
        if (getFr())
            count++;
        if (getNeder())
            count++;
        if (getTurkish())
            count++;
        if (getThai())
            count++;
        if (getCode())
            count++;
        return count;
    }
}
