package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.rest.base.enums.NoteEnum;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 3/30/15.
 */
public class NoteTO implements IsSerializable {

    Integer id;
    String subject;
    String comment;
    UserTO user;
    SelectItemTO visibility;
    Long creationDate;
    Integer relatedId;
    Integer relatedToId;
    Integer commentCount;
    AttachmentTO attachment;

    public NoteTO() {
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserTO getUser() {
        return user;
    }

    public void setUser(UserTO user) {
        this.user = user;
    }

    public SelectItemTO getVisibility() {
        return visibility;
    }

    public void setVisibility(SelectItemTO visibility) {
        this.visibility = visibility;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(Integer relatedId) {
        this.relatedId = relatedId;
    }

    public Integer getRelatedToId() {
        return relatedToId;
    }

    public void setRelatedToId(Integer relatedToId) {
        this.relatedToId = relatedToId;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public AttachmentTO getAttachment() {
        return attachment;
    }

    public void setAttachment(AttachmentTO attachment) {
        this.attachment = attachment;
    }

    public HistoryListItem wrap(NoteTO noteTO) {
        HistoryListItem noteItem = new HistoryListItem();
        noteItem.setObjectID(noteTO.getId());
        noteItem.setSubject(noteTO.getSubject());
        noteItem.setComment(noteTO.getComment());
        noteItem.setRelatedId(noteTO.getRelatedId());
        noteItem.setRelatedToId(noteTO.getRelatedToId());
        noteItem.setEventDate(WrapUtils.longToDate(noteTO.getCreationDate()));
        if (getAttachment() != null) {
            noteItem.setAttachmentID(noteTO.getAttachment().getBodyId());
        }

        if (NoteEnum.INTERNAL.getCode().equalsIgnoreCase(noteTO.getVisibility().getCode())) {
            noteItem.setVisibility(null);
        } else {
            noteItem.setVisibility(NoteEnum.PRIVATE.getCode().equals(noteTO.getVisibility().getCode()));
        }

        return noteItem;
    }

    public NoteTO(HistoryListItem noteItem) {
        this.id = noteItem.getObjectID();
        this.subject = noteItem.getSubject();
        this.comment = noteItem.getComment(true);
        UserTO userTO = new UserTO(noteItem.getEmployeeID(), noteItem.getEmployee());
        userTO.setImageUrl(noteItem.getEmployeeImageUrl());
        this.user = userTO;


        if (noteItem.isVisibility() == null) {
            this.visibility = new SelectItemTO(NoteEnum.INTERNAL.getName(), NoteEnum.INTERNAL.getCode());
        } else if (noteItem.isVisibility()) {
            this.visibility = new SelectItemTO(NoteEnum.PRIVATE.getName(), NoteEnum.PRIVATE.getCode());
        } else {
            this.visibility = new SelectItemTO(NoteEnum.PUBLIC.getName(), NoteEnum.PUBLIC.getCode());
        }

        this.creationDate = WrapUtils.dateToLong(noteItem.getEventDate());
        this.relatedId = noteItem.getRelatedId();
        this.relatedToId = noteItem.getRelatedToId();
    }
}
