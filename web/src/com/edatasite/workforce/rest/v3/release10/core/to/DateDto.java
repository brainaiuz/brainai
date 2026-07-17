package com.edatasite.workforce.rest.v3.release10.core.to;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class DateDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date date;

    public DateDto() {
    }

    public DateDto(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DateDto)) return false;

        DateDto dateDto = (DateDto) o;

        if (date != null ? !date.equals(dateDto.date) : dateDto.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return date != null ? date.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DateDto{" +
                "date=" + date +
                '}';
    }
}
