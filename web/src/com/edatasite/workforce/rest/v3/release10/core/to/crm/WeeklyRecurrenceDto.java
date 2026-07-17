package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import java.util.List;

public class WeeklyRecurrenceDto {
    private List<Integer> days;

    public WeeklyRecurrenceDto() {
    }

    public WeeklyRecurrenceDto(List<Integer> days) {
        this.days = days;
    }

    public List<Integer> getDays() {
        return days;
    }

    public void setDays(List<Integer> days) {
        this.days = days;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeeklyRecurrenceDto)) return false;

        WeeklyRecurrenceDto that = (WeeklyRecurrenceDto) o;

        if (getDays() != null ? !getDays().equals(that.getDays()) : that.getDays() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getDays() != null ? getDays().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "WeeklyRecurrenceDto{" +
                "days=" + days +
                '}';
    }
}
