package com.edatasite.workforce.rest.v3.release10.core.to.hrms;

import com.edatasite.workforce.rest.v2.release10.core.to.base.AttachmentTO;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

public class NewsDTO {
    private Integer id;
    @NotNull(message = "Subject is required")
    @NotBlank(message = "Subject cannot be empty")
    private String subject;

    private IdCode location;
    private List<IdCode> categories;
    private boolean internal;

    private String shortText;
    private String fullText;

    private ItemDto author;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date date;

    private List<AttachmentTO> attachments;

    public NewsDTO() {
    }

    public NewsDTO(Integer id, String subject, IdCode location, List<IdCode> categories, boolean internal, String shortText, String fullText, ItemDto author, Date date, List<AttachmentTO> attachments) {
        this.id = id;
        this.subject = subject;
        this.location = location;
        this.categories = categories;
        this.internal = internal;
        this.shortText = shortText;
        this.fullText = fullText;
        this.author = author;
        this.date = date;
        this.attachments = attachments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public IdCode getLocation() {
        return location;
    }

    public void setLocation(IdCode location) {
        this.location = location;
    }

    public List<IdCode> getCategories() {
        return categories;
    }

    public void setCategories(List<IdCode> categories) {
        this.categories = categories;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public ItemDto getAuthor() {
        return author;
    }

    public void setAuthor(ItemDto author) {
        this.author = author;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<AttachmentTO> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AttachmentTO> attachments) {
        this.attachments = attachments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsDTO)) return false;

        NewsDTO newsDTO = (NewsDTO) o;

        if (isInternal() != newsDTO.isInternal()) return false;
        if (getId() != null ? !getId().equals(newsDTO.getId()) : newsDTO.getId() != null) return false;
        if (getSubject() != null ? !getSubject().equals(newsDTO.getSubject()) : newsDTO.getSubject() != null)
            return false;
        if (getLocation() != null ? !getLocation().equals(newsDTO.getLocation()) : newsDTO.getLocation() != null)
            return false;
        if (getCategories() != null ? !getCategories().equals(newsDTO.getCategories()) : newsDTO.getCategories() != null)
            return false;
        if (getShortText() != null ? !getShortText().equals(newsDTO.getShortText()) : newsDTO.getShortText() != null)
            return false;
        if (getFullText() != null ? !getFullText().equals(newsDTO.getFullText()) : newsDTO.getFullText() != null)
            return false;
        if (getAuthor() != null ? !getAuthor().equals(newsDTO.getAuthor()) : newsDTO.getAuthor() != null) return false;
        if (getDate() != null ? !getDate().equals(newsDTO.getDate()) : newsDTO.getDate() != null) return false;
        if (getAttachments() != null ? !getAttachments().equals(newsDTO.getAttachments()) : newsDTO.getAttachments() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        result = 31 * result + (getLocation() != null ? getLocation().hashCode() : 0);
        result = 31 * result + (getCategories() != null ? getCategories().hashCode() : 0);
        result = 31 * result + (isInternal() ? 1 : 0);
        result = 31 * result + (getShortText() != null ? getShortText().hashCode() : 0);
        result = 31 * result + (getFullText() != null ? getFullText().hashCode() : 0);
        result = 31 * result + (getAuthor() != null ? getAuthor().hashCode() : 0);
        result = 31 * result + (getDate() != null ? getDate().hashCode() : 0);
        result = 31 * result + (getAttachments() != null ? getAttachments().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "NewsDTO{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", location=" + location +
                ", categories=" + categories +
                ", internal=" + internal +
                ", shortText='" + shortText + '\'' +
                ", fullText='" + fullText + '\'' +
                ", author=" + author +
                ", date=" + date +
                ", attachments=" + attachments +
                '}';
    }
}
