package com.edatasite.workforce.gwt.profile.client.rpc;

public class CompanyInfoForTelegram {
    private Integer companyId;
    private String officeNumber;
    private String mobileNumber;
    private String faxNumber;
    private String email;
    private String website;

    public CompanyInfoForTelegram() {
    }

    public CompanyInfoForTelegram(Integer companyId, String officeNumber, String mobileNumber, String faxNumber, String email, String website) {
        this.companyId = companyId;
        this.officeNumber = officeNumber;
        this.mobileNumber = mobileNumber;
        this.faxNumber = faxNumber;
        this.email = email;
        this.website = website;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getOfficeNumber() {
        return officeNumber;
    }

    public void setOfficeNumber(String officeNumber) {
        this.officeNumber = officeNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyInfoForTelegram)) return false;

        CompanyInfoForTelegram that = (CompanyInfoForTelegram) o;

        if (getCompanyId() != null ? !getCompanyId().equals(that.getCompanyId()) : that.getCompanyId() != null)
            return false;
        if (getOfficeNumber() != null ? !getOfficeNumber().equals(that.getOfficeNumber()) : that.getOfficeNumber() != null)
            return false;
        if (getMobileNumber() != null ? !getMobileNumber().equals(that.getMobileNumber()) : that.getMobileNumber() != null)
            return false;
        if (getFaxNumber() != null ? !getFaxNumber().equals(that.getFaxNumber()) : that.getFaxNumber() != null)
            return false;
        if (getEmail() != null ? !getEmail().equals(that.getEmail()) : that.getEmail() != null) return false;
        if (getWebsite() != null ? !getWebsite().equals(that.getWebsite()) : that.getWebsite() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getCompanyId() != null ? getCompanyId().hashCode() : 0;
        result = 31 * result + (getOfficeNumber() != null ? getOfficeNumber().hashCode() : 0);
        result = 31 * result + (getMobileNumber() != null ? getMobileNumber().hashCode() : 0);
        result = 31 * result + (getFaxNumber() != null ? getFaxNumber().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getWebsite() != null ? getWebsite().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CompanyInfoForTelegram{" +
                "companyId=" + companyId +
                ", officeNumber='" + officeNumber + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", faxNumber='" + faxNumber + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
