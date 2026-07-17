package com.edatasite.workforce.rest.v3.release10.core.to.crm;

public class DailyRecurrenceDto {
    private Integer interval;

    public DailyRecurrenceDto() {
    }

    public DailyRecurrenceDto(Integer interval) {
        this.interval = interval;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DailyRecurrenceDto)) return false;

        DailyRecurrenceDto that = (DailyRecurrenceDto) o;

        if (getInterval() != null ? !getInterval().equals(that.getInterval()) : that.getInterval() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getInterval() != null ? getInterval().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DailyRecurrenceDto{" +
                "interval=" + interval +
                '}';
    }
}
