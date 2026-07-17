package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

public class SettingsRequest {
    private String commonName;
    private String serialNumber;
    private String organizationIdentifier;
    private String organizationUnitName;
    private String organizationName;
    private String countryName;
    private String invoiceType;
    private String location;
    private String industry;
    private String otpNumber;

    public SettingsRequest() {
    }

    public SettingsRequest(Object commonName, Object serialNumber, Object organizationIdentifier, Object organizationUnitName, Object organizationName, Object countryName, Object invoiceType, Object location, Object industry) {
        this.commonName = (String)commonName;
        this.serialNumber = (String)serialNumber;
        this.organizationIdentifier = (String)organizationIdentifier;
        this.organizationUnitName = (String)organizationUnitName;
        this.organizationName = (String)organizationName;
        this.countryName = (String)countryName;
        this.invoiceType = (String)invoiceType;
        this.location = (String)location;
        this.industry = (String)industry;
    }



    public String getCommonName() {
        return this.commonName;
    }

    public String getSerialNumber() {
        return this.serialNumber;
    }

    public String getOrganizationIdentifier() {
        return this.organizationIdentifier;
    }

    public String getOrganizationUnitName() {
        return this.organizationUnitName;
    }

    public String getOrganizationName() {
        return this.organizationName;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public String getInvoiceType() {
        return this.invoiceType;
    }

    public String getLocation() {
        return this.location;
    }

    public String getIndustry() {
        return this.industry;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setOrganizationIdentifier(String organizationIdentifier) {
        this.organizationIdentifier = organizationIdentifier;
    }

    public void setOrganizationUnitName(String organizationUnitName) {
        this.organizationUnitName = organizationUnitName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SettingsRequest)) {
            return false;
        } else {
            SettingsRequest other = (SettingsRequest)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label139: {
                    Object this$commonName = this.getCommonName();
                    Object other$commonName = other.getCommonName();
                    if (this$commonName == null) {
                        if (other$commonName == null) {
                            break label139;
                        }
                    } else if (this$commonName.equals(other$commonName)) {
                        break label139;
                    }

                    return false;
                }

                Object this$serialNumber = this.getSerialNumber();
                Object other$serialNumber = other.getSerialNumber();
                if (this$serialNumber == null) {
                    if (other$serialNumber != null) {
                        return false;
                    }
                } else if (!this$serialNumber.equals(other$serialNumber)) {
                    return false;
                }

                label125: {
                    Object this$organizationIdentifier = this.getOrganizationIdentifier();
                    Object other$organizationIdentifier = other.getOrganizationIdentifier();
                    if (this$organizationIdentifier == null) {
                        if (other$organizationIdentifier == null) {
                            break label125;
                        }
                    } else if (this$organizationIdentifier.equals(other$organizationIdentifier)) {
                        break label125;
                    }

                    return false;
                }

                label118: {
                    Object this$organizationUnitName = this.getOrganizationUnitName();
                    Object other$organizationUnitName = other.getOrganizationUnitName();
                    if (this$organizationUnitName == null) {
                        if (other$organizationUnitName == null) {
                            break label118;
                        }
                    } else if (this$organizationUnitName.equals(other$organizationUnitName)) {
                        break label118;
                    }

                    return false;
                }

                Object this$organizationName = this.getOrganizationName();
                Object other$organizationName = other.getOrganizationName();
                if (this$organizationName == null) {
                    if (other$organizationName != null) {
                        return false;
                    }
                } else if (!this$organizationName.equals(other$organizationName)) {
                    return false;
                }

                label104: {
                    Object this$countryName = this.getCountryName();
                    Object other$countryName = other.getCountryName();
                    if (this$countryName == null) {
                        if (other$countryName == null) {
                            break label104;
                        }
                    } else if (this$countryName.equals(other$countryName)) {
                        break label104;
                    }

                    return false;
                }

                label97: {
                    Object this$invoiceType = this.getInvoiceType();
                    Object other$invoiceType = other.getInvoiceType();
                    if (this$invoiceType == null) {
                        if (other$invoiceType == null) {
                            break label97;
                        }
                    } else if (this$invoiceType.equals(other$invoiceType)) {
                        break label97;
                    }

                    return false;
                }

                Object this$location = this.getLocation();
                Object other$location = other.getLocation();
                if (this$location == null) {
                    if (other$location != null) {
                        return false;
                    }
                } else if (!this$location.equals(other$location)) {
                    return false;
                }

                Object this$industry = this.getIndustry();
                Object other$industry = other.getIndustry();
                if (this$industry == null) {
                    if (other$industry != null) {
                        return false;
                    }
                } else if (!this$industry.equals(other$industry)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof SettingsRequest;
    }

    public int hashCode() {
        int result = 1;
        Object $commonName = this.getCommonName();
        result = result * 59 + ($commonName == null ? 43 : $commonName.hashCode());
        Object $serialNumber = this.getSerialNumber();
        result = result * 59 + ($serialNumber == null ? 43 : $serialNumber.hashCode());
        Object $organizationIdentifier = this.getOrganizationIdentifier();
        result = result * 59 + ($organizationIdentifier == null ? 43 : $organizationIdentifier.hashCode());
        Object $organizationUnitName = this.getOrganizationUnitName();
        result = result * 59 + ($organizationUnitName == null ? 43 : $organizationUnitName.hashCode());
        Object $organizationName = this.getOrganizationName();
        result = result * 59 + ($organizationName == null ? 43 : $organizationName.hashCode());
        Object $countryName = this.getCountryName();
        result = result * 59 + ($countryName == null ? 43 : $countryName.hashCode());
        Object $invoiceType = this.getInvoiceType();
        result = result * 59 + ($invoiceType == null ? 43 : $invoiceType.hashCode());
        Object $location = this.getLocation();
        result = result * 59 + ($location == null ? 43 : $location.hashCode());
        Object $industry = this.getIndustry();
        result = result * 59 + ($industry == null ? 43 : $industry.hashCode());
        Object $otpNumber = this.getOtpNumber();
        result = result * 59 + ($otpNumber == null ? 43 : $otpNumber.hashCode());
        return result;
    }

    public String toString() {
        return "CsrInputDto(commonName=" + this.getCommonName() + ", serialNumber=" + this.getSerialNumber() + ", organizationIdentifier=" + this.getOrganizationIdentifier() + ", organizationUnitName=" + this.getOrganizationUnitName() + ", organizationName=" + this.getOrganizationName() + ", countryName=" + this.getCountryName() + ", invoiceType=" + this.getInvoiceType() + ", location=" + this.getLocation() + ", industry=" + this.getIndustry() + ")";
    }

    public String getOtpNumber() {
        return otpNumber;
    }

    public void setOtpNumber(String otpNumber) {
        this.otpNumber = otpNumber;
    }
}
