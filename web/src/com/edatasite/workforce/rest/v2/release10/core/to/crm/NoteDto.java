package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

public class NoteDto {
    private Integer id;
    @NotNull(message = "text field is required")
    private String text;
    @NotNull(message = "visibility is required")
    @Pattern(regexp = "PRIVATE|INTERNAL|PUBLIC", message = "visibility must be one of PRIVATE/INTERNAL/PUBLIC")
    private String visibility;

    @JsonIgnore
    private Integer entityId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date eventDate;
    private String employee;

    public NoteDto() {
    }

    public NoteDto(Integer id, String text, String visibility, Integer entityId) {
        this.id = id;
        this.text = text;
        this.visibility = visibility;
        this.entityId = entityId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NoteDto)) return false;

        NoteDto noteDto = (NoteDto) o;

        if (getId() != null ? !getId().equals(noteDto.getId()) : noteDto.getId() != null) return false;
        if (getText() != null ? !getText().equals(noteDto.getText()) : noteDto.getText() != null) return false;
        if (getVisibility() != null ? !getVisibility().equals(noteDto.getVisibility()) : noteDto.getVisibility() != null)
            return false;
        if (getEntityId() != null ? !getEntityId().equals(noteDto.getEntityId()) : noteDto.getEntityId() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getText() != null ? getText().hashCode() : 0);
        result = 31 * result + (getVisibility() != null ? getVisibility().hashCode() : 0);
        result = 31 * result + (getEntityId() != null ? getEntityId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NoteDto{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", visibility='" + visibility + '\'' +
                ", entityId=" + entityId +
                '}';
    }
}
