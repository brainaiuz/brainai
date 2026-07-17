package com.edatasite.workforce.rest.v3.release10.core.to.crm;

public class RecurrenceTypeDto {
    private DailyRecurrenceDto daily;
    private WeeklyRecurrenceDto weekly;
    private MonthlyRecurrenceDto monthly;
    private YearlyRecurrenceDto yearly;
    private boolean evenDays;
    private boolean oddDays;

    public RecurrenceTypeDto() {
    }

    public RecurrenceTypeDto(DailyRecurrenceDto daily, WeeklyRecurrenceDto weekly, MonthlyRecurrenceDto monthly, YearlyRecurrenceDto yearly, boolean evenDays, boolean oddDays) {
        this.daily = daily;
        this.weekly = weekly;
        this.monthly = monthly;
        this.yearly = yearly;
        this.evenDays = evenDays;
        this.oddDays = oddDays;
    }

    public DailyRecurrenceDto getDaily() {
        return daily;
    }

    public void setDaily(DailyRecurrenceDto daily) {
        this.daily = daily;
    }

    public WeeklyRecurrenceDto getWeekly() {
        return weekly;
    }

    public void setWeekly(WeeklyRecurrenceDto weekly) {
        this.weekly = weekly;
    }

    public MonthlyRecurrenceDto getMonthly() {
        return monthly;
    }

    public void setMonthly(MonthlyRecurrenceDto monthly) {
        this.monthly = monthly;
    }

    public YearlyRecurrenceDto getYearly() {
        return yearly;
    }

    public void setYearly(YearlyRecurrenceDto yearly) {
        this.yearly = yearly;
    }

    public boolean isEvenDays() {
        return evenDays;
    }

    public void setEvenDays(boolean evenDays) {
        this.evenDays = evenDays;
    }

    public boolean isOddDays() {
        return oddDays;
    }

    public void setOddDays(boolean oddDays) {
        this.oddDays = oddDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecurrenceTypeDto)) return false;

        RecurrenceTypeDto that = (RecurrenceTypeDto) o;

        if (isEvenDays() != that.isEvenDays()) return false;
        if (isOddDays() != that.isOddDays()) return false;
        if (getDaily() != null ? !getDaily().equals(that.getDaily()) : that.getDaily() != null) return false;
        if (getWeekly() != null ? !getWeekly().equals(that.getWeekly()) : that.getWeekly() != null) return false;
        if (getMonthly() != null ? !getMonthly().equals(that.getMonthly()) : that.getMonthly() != null) return false;
        if (getYearly() != null ? !getYearly().equals(that.getYearly()) : that.getYearly() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getDaily() != null ? getDaily().hashCode() : 0;
        result = 31 * result + (getWeekly() != null ? getWeekly().hashCode() : 0);
        result = 31 * result + (getMonthly() != null ? getMonthly().hashCode() : 0);
        result = 31 * result + (getYearly() != null ? getYearly().hashCode() : 0);
        result = 31 * result + (isEvenDays() ? 1 : 0);
        result = 31 * result + (isOddDays() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RecurrenceTypeDto{" +
                "daily=" + daily +
                ", weekly=" + weekly +
                ", monthly=" + monthly +
                ", yearly=" + yearly +
                ", evenDays=" + evenDays +
                ", oddDays=" + oddDays +
                '}';
    }
}
