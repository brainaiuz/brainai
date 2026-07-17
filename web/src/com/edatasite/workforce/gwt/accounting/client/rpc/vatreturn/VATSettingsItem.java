package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

public class VATSettingsItem implements IsSerializable {

    private String taxIdNumber;
    private String taxIdDesplayName;
    private boolean enableContractOutsite;
    private DateNonConvertable taxGenerationDate;
    private SelectItem taxPeriod;
    private DateNonConvertable lastTaxGeneratedDate;
    private SelectItem lastTaxGeneratedStatus;
    private boolean hasUnfiledReturn;
    private boolean submitVatManually;
    private boolean hmrcAuthorized;
    private boolean agent;

    public String getTaxIdNumber() {
        return taxIdNumber;
    }

    public void setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber;
    }

    public String getTaxIdDesplayName() {
        return taxIdDesplayName;
    }

    public void setTaxIdDesplayName(String taxIdDesplayName) {
        this.taxIdDesplayName = taxIdDesplayName;
    }

    public boolean isEnableContractOutsite() {
        return enableContractOutsite;
    }

    public void setEnableContractOutsite(boolean enableContractOutsite) {
        this.enableContractOutsite = enableContractOutsite;
    }

    public DateNonConvertable getTaxGenerationDate() {
        return taxGenerationDate;
    }

    public void setTaxGenerationDate(DateNonConvertable taxGenerationDate) {
        this.taxGenerationDate = taxGenerationDate;
    }

    public SelectItem getTaxPeriod() {
        return taxPeriod;
    }

    public void setTaxPeriod(SelectItem taxPeriod) {
        this.taxPeriod = taxPeriod;
    }

    public DateNonConvertable getLastTaxGeneratedDate() {
        return lastTaxGeneratedDate;
    }

    public void setLastTaxGeneratedDate(DateNonConvertable lastTaxGeneratedDate) {
        this.lastTaxGeneratedDate = lastTaxGeneratedDate;
    }

    public SelectItem getLastTaxGeneratedStatus() {
        return lastTaxGeneratedStatus;
    }

    public void setLastTaxGeneratedStatus(SelectItem lastTaxGeneratedStatus) {
        this.lastTaxGeneratedStatus = lastTaxGeneratedStatus;
    }

    public boolean hasUnfiledReturn() {
        return hasUnfiledReturn;
    }

    public void setHasUnfiledReturn(boolean hasUnfiledReturn) {
        this.hasUnfiledReturn = hasUnfiledReturn;
    }

    public boolean isSubmitVatManually() {
        return submitVatManually;
    }

    public void setSubmitVatManually(boolean submitVatManually) {
        this.submitVatManually = submitVatManually;
    }

    public boolean isHmrcAuthorized() {
        return hmrcAuthorized;
    }

    public void setHmrcAuthorized(boolean hmrcAuthorized) {
        this.hmrcAuthorized = hmrcAuthorized;
    }

    public boolean isAgent() {
        return agent;
    }

    public void setAgent(boolean agent) {
        this.agent = agent;
    }
}
