package com.edatasite.workforce.rest.v3.release10.core.to.crm;

public class MonthlyRecurrenceDto {
    private Integer dailyInterval;
    private Integer monthlyInterval;

    public MonthlyRecurrenceDto() {
    }

    public MonthlyRecurrenceDto(Integer dailyInterval, Integer monthlyInterval) {
        this.dailyInterval = dailyInterval;
        this.monthlyInterval = monthlyInterval;
    }

    public Integer getDailyInterval() {
        return dailyInterval;
    }

    public void setDailyInterval(Integer dailyInterval) {
        this.dailyInterval = dailyInterval;
    }

    public Integer getMonthlyInterval() {
        return monthlyInterval;
    }

    public void setMonthlyInterval(Integer monthlyInterval) {
        this.monthlyInterval = monthlyInterval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonthlyRecurrenceDto)) return false;

        MonthlyRecurrenceDto that = (MonthlyRecurrenceDto) o;

        if (getDailyInterval() != null ? !getDailyInterval().equals(that.getDailyInterval()) : that.getDailyInterval() != null)
            return false;
        if (getMonthlyInterval() != null ? !getMonthlyInterval().equals(that.getMonthlyInterval()) : that.getMonthlyInterval() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getDailyInterval() != null ? getDailyInterval().hashCode() : 0;
        result = 31 * result + (getMonthlyInterval() != null ? getMonthlyInterval().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MonthlyRecurrenceDto{" +
                "dailyInterval=" + dailyInterval +
                ", monthlyInterval=" + monthlyInterval +
                '}';
    }
}
