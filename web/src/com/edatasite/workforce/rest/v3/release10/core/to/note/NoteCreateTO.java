package com.edatasite.workforce.rest.v3.release10.core.to.note;

import com.edatasite.workforce.rest.v2.release10.enums.NoteRelationEnum;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class NoteCreateTO {
    @NotNull
    private NoteRelationEnum type;
    @Min(value = 0)
    @NotNull
    private Integer itemId;
    @NotNull
    @NotBlank
    private String note;

    public NoteRelationEnum getType() {
        return type;
    }

    public void setType(NoteRelationEnum type) {
        this.type = type;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
