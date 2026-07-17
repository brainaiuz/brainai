package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class EmailDto {

    @NotNull(message = "email field is required")
    @Email(message = "Email is not Valid")
    private String email;
    private boolean primary;

    public EmailDto() {
    }

    public EmailDto(String email, boolean primary) {
        this.email = email;
        this.primary = primary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        if (!(o instanceof EmailDto)) return false;

        EmailDto emailDto = (EmailDto) o;

        if (isPrimary() != emailDto.isPrimary()) return false;
        if (getEmail() != null ? !getEmail().equals(emailDto.getEmail()) : emailDto.getEmail() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getEmail() != null ? getEmail().hashCode() : 0;
        result = 31 * result + (isPrimary() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EmailDto{" +
                "email='" + email + '\'' +
                ", primary=" + primary +
                '}';
    }
}
