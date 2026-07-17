package com.edatasite.workforce.rest.v3.release10.hrms.dto;

public class IMWebAddressDto {
    private String type;
    private String address;

    public IMWebAddressDto() {
    }

    public IMWebAddressDto(String type, String address) {
        this.type = type;
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IMWebAddressDto)) return false;

        IMWebAddressDto that = (IMWebAddressDto) o;

        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getAddress() != null ? !getAddress().equals(that.getAddress()) : that.getAddress() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "IMWebAddressDto{" +
                "type='" + type + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
