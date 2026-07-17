package com.edatasite.workforce.gwt.core.client.rpc;

public class CompanyData {
    private Integer companyId;
    private String name;
    private Integer localeId;
    private Integer currencyId;

    private String rating;
    private Integer industryId;
    private String selectedApps;
    private String accountingTool;
    private String whatDoesYourOrgDo;

    private boolean preventWorkflow;

    private Address address;

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLocaleId() {
        return localeId;
    }

    public void setLocaleId(Integer localeId) {
        this.localeId = localeId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getIndustryId() {
        return industryId;
    }

    public void setIndustryId(Integer industryId) {
        this.industryId = industryId;
    }

    public String getSelectedApps() {
        return selectedApps;
    }

    public void setSelectedApps(String selectedApps) {
        this.selectedApps = selectedApps;
    }

    public String getAccountingTool() {
        return accountingTool;
    }

    public void setAccountingTool(String accountingTool) {
        this.accountingTool = accountingTool;
    }

    public String getWhatDoesYourOrgDo() {
        return whatDoesYourOrgDo;
    }

    public void setWhatDoesYourOrgDo(String whatDoesYourOrgDo) {
        this.whatDoesYourOrgDo = whatDoesYourOrgDo;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public boolean isPreventWorkflow() {
        return preventWorkflow;
    }

    public void setPreventWorkflow(boolean preventWorkflow) {
        this.preventWorkflow = preventWorkflow;
    }
}
