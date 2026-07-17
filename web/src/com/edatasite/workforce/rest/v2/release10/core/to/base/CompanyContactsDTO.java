package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * User : Akhror on 18/09/2021
 */
public class CompanyContactsDTO extends ResponseData {
    private String officeNumber;
    private String mobileNumber;
    private String faxNumber;
    private String email;
    private String website;

    public CompanyContactsDTO() {
    }

    public CompanyContactsDTO(String officeNumber, String mobileNumber, String faxNumber, String email, String website) {
        this.officeNumber = officeNumber;
        this.mobileNumber = mobileNumber;
        this.faxNumber = faxNumber;
        this.email = email;
        this.website = website;
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
        if (!(o instanceof CompanyContactsDTO)) return false;

        CompanyContactsDTO that = (CompanyContactsDTO) o;

        if (officeNumber != null ? !officeNumber.equals(that.officeNumber) : that.officeNumber != null) return false;
        if (mobileNumber != null ? !mobileNumber.equals(that.mobileNumber) : that.mobileNumber != null) return false;
        if (faxNumber != null ? !faxNumber.equals(that.faxNumber) : that.faxNumber != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (website != null ? !website.equals(that.website) : that.website != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = officeNumber != null ? officeNumber.hashCode() : 0;
        result = 31 * result + (mobileNumber != null ? mobileNumber.hashCode() : 0);
        result = 31 * result + (faxNumber != null ? faxNumber.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (website != null ? website.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CompanyContactsDTO{" +
                "officeNumber='" + officeNumber + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", faxNumber='" + faxNumber + '\'' +
                ", email='" + email + '\'' +
                ", website='" + website + '\'' +
                '}';
    }
}
