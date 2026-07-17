package com.edatasite.workforce.gwt.hrms.client.rpc;

public class VacancyItemForTelegram {
    private Integer companyId;
    private String name;
    private String status;
    private String percentage;
    private String number;

    public VacancyItemForTelegram() {
    }

    public VacancyItemForTelegram(Integer companyId, String name, String status, String percentage, String number) {
        this.companyId = companyId;
        this.name = name;
        this.status = status;
        this.percentage = percentage;
        this.number = number;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VacancyItemForTelegram)) return false;

        VacancyItemForTelegram that = (VacancyItemForTelegram) o;

        if (getCompanyId() != null ? !getCompanyId().equals(that.getCompanyId()) : that.getCompanyId() != null)
            return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getStatus() != null ? !getStatus().equals(that.getStatus()) : that.getStatus() != null) return false;
        if (getPercentage() != null ? !getPercentage().equals(that.getPercentage()) : that.getPercentage() != null)
            return false;
        if (getNumber() != null ? !getNumber().equals(that.getNumber()) : that.getNumber() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getCompanyId() != null ? getCompanyId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getPercentage() != null ? getPercentage().hashCode() : 0);
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "VacancyItemForTelegram{" +
                "companyId=" + companyId +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", percentage='" + percentage + '\'' +
                ", number='" + number + '\'' +
                '}';
    }
}
