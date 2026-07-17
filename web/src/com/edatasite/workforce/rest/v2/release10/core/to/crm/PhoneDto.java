package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class PhoneDto {
    @Pattern(regexp = "HOME|WORK|MOBILE|FAX|WHATSAPP|TELEGRAM|VIBER", message = "phoneCategory must be one of HOME/WORK/MOBILE/FAX/WHATSAPP/TELEGRAM/VIBER")
    @NotNull(message = "phoneCategory is required")
    private String phoneCategory;
    private String number;
    private boolean primary;

    public PhoneDto() {
    }

    public PhoneDto(String phoneCategory, String number, boolean primary) {
        this.phoneCategory = phoneCategory;
        this.number = number;
        this.primary = primary;
    }

    public String getPhoneCategory() {
        return phoneCategory;
    }

    public void setPhoneCategory(String phoneCategory) {
        this.phoneCategory = phoneCategory;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhoneDto)) return false;

        PhoneDto phoneDto = (PhoneDto) o;

        if (isPrimary() != phoneDto.isPrimary()) return false;
        if (getPhoneCategory() != null ? !getPhoneCategory().equals(phoneDto.getPhoneCategory()) : phoneDto.getPhoneCategory() != null)
            return false;
        if (getNumber() != null ? !getNumber().equals(phoneDto.getNumber()) : phoneDto.getNumber() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getPhoneCategory() != null ? getPhoneCategory().hashCode() : 0;
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        result = 31 * result + (isPrimary() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PhoneDto{" +
                "phoneCategory='" + phoneCategory + '\'' +
                ", number='" + number + '\'' +
                ", primary=" + primary +
                '}';
    }
}
