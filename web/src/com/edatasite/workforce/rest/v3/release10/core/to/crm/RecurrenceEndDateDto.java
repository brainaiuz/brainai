package com.edatasite.workforce.rest.v3.release10.core.to.crm;

import com.edatasite.workforce.rest.v3.release10.core.to.DateDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdDTO;

public class RecurrenceEndDateDto {
    private boolean never;
    private IdDTO after;
    private DateDto until;

    public RecurrenceEndDateDto() {
    }

    public RecurrenceEndDateDto(boolean never, IdDTO after, DateDto until) {
        this.never = never;
        this.after = after;
        this.until = until;
    }

    public boolean isNever() {
        return never;
    }

    public void setNever(boolean never) {
        this.never = never;
    }

    public IdDTO getAfter() {
        return after;
    }

    public void setAfter(IdDTO after) {
        this.after = after;
    }

    public DateDto getUntil() {
        return until;
    }

    public void setUntil(DateDto until) {
        this.until = until;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecurrenceEndDateDto)) return false;

        RecurrenceEndDateDto that = (RecurrenceEndDateDto) o;

        if (isNever() != that.isNever()) return false;
        if (getAfter() != null ? !getAfter().equals(that.getAfter()) : that.getAfter() != null) return false;
        if (getUntil() != null ? !getUntil().equals(that.getUntil()) : that.getUntil() != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (isNever() ? 1 : 0);
        result = 31 * result + (getAfter() != null ? getAfter().hashCode() : 0);
        result = 31 * result + (getUntil() != null ? getUntil().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RecurrenceEndDateDto{" +
                "never=" + never +
                ", after=" + after +
                ", until=" + until +
                '}';
    }
}
