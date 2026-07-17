package com.edatasite.workforce.rest.v3.release10.core.to.crm;

public class YearlyRecurrenceDto {
    private Integer month;
    private Integer type;
    private Integer day;

    public YearlyRecurrenceDto() {
    }

    public YearlyRecurrenceDto(Integer month, Integer type, Integer day) {
        this.month = month;
        this.type = type;
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof YearlyRecurrenceDto)) return false;

        YearlyRecurrenceDto that = (YearlyRecurrenceDto) o;

        if (month != null ? !month.equals(that.month) : that.month != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (day != null ? !day.equals(that.day) : that.day != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = month != null ? month.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (day != null ? day.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "YearlyRecurrenceDto{" +
                "month=" + month +
                ", type=" + type +
                ", day=" + day +
                '}';
    }
}
