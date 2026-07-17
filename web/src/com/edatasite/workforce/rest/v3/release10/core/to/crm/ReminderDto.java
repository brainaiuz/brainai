package com.edatasite.workforce.rest.v3.release10.core.to.crm;

public class ReminderDto {
    private Integer type;
    private Integer times;

    public ReminderDto() {
    }

    public ReminderDto(Integer type, Integer times) {
        this.type = type;
        this.times = times;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReminderDto)) return false;

        ReminderDto that = (ReminderDto) o;

        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getTimes() != null ? !getTimes().equals(that.getTimes()) : that.getTimes() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getType() != null ? getType().hashCode() : 0;
        result = 31 * result + (getTimes() != null ? getTimes().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ReminderDto{" +
                "type=" + type +
                ", times=" + times +
                '}';
    }
}
