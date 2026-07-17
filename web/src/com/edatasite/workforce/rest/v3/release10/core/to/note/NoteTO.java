package com.edatasite.workforce.rest.v3.release10.core.to.note;

import com.edatasite.workforce.rest.v3.release10.core.utils.DateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

public class NoteTO {
    private Integer id;
    private String note;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date date;

    public NoteTO() {
    }

    public NoteTO(Integer id, String note, Date date) {
        this.id = id;
        this.note = note;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public Date getDate() {
        return date;
    }
}
