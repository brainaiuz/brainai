package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 3/19/15.
 */
public class TaskNoteTO implements IsSerializable {

    Integer id;
    String subject;
    String note;
    SelectItemTO visibility;
    Long creationDate;
    List<TaskCommentTO> comments;
    UserTO user;
    static final WfmStrings wfmStrings = WfmStrings.App.get();

    public TaskNoteTO() {

    }

    public TaskNoteTO(HistoryListItem noteItem) {
        this.id = noteItem.getObjectID();
        this.subject = noteItem.getSubject();
        this.note = noteItem.getComment();

        SelectItemTO visibility = new SelectItemTO();
        visibility.setCode(noteItem.isVisibility() == null ? wfmStrings.internal() : noteItem.isVisibility() ? wfmStrings.priv() : wfmStrings.pub());
        visibility.setName(visibility.getCode());
        this.visibility = visibility;
        this.user = new UserTO(null, noteItem.getEmployee());
        this.creationDate = WrapUtils.dateToLong(noteItem.getEventDate());
        List<TaskCommentTO> commentList = new ArrayList<>();
        if (noteItem.getNotesComments() != null && noteItem.getNotesComments().length > 0) {
            for (NewsComment comment : noteItem.getNotesComments()) {
                TaskCommentTO commentTO = new TaskCommentTO();
                commentTO.setId(comment.getCommentId());
                commentTO.setComment(comment.getComment());
                commentTO.setCreationDate(WrapUtils.dateToLong(comment.getDate()));

                UserTO user = new UserTO();
                user.setName(comment.getUsername());
                user.setImageUrl(comment.getEmployeeImageUrl());
                commentTO.setUser(user);
                commentList.add(commentTO);
            }
        }
        this.comments = commentList;
    }

    public HistoryListItem wrap(TaskNoteTO noteTO) {
        HistoryListItem item = new HistoryListItem();
        item.setSubject(noteTO.getSubject());
        item.setComment(noteTO.getNote());
        item.setEmployee(noteTO.getUser() != null ? noteTO.getUser().getName() : "");
        Boolean isVisible = null;
        if (noteTO.getVisibility() != null && "".equals(noteTO.getVisibility())) {
            if (wfmStrings.pub().equals(noteTO.getVisibility().getName())) {
                isVisible = false;
            } else if (wfmStrings.priv().equals(noteTO.getVisibility().getName())) {
                isVisible = true;
            }
        }
        item.setVisibility(isVisible);
        return item;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public List<TaskCommentTO> getComments() {
        return comments;
    }

    public void setComments(List<TaskCommentTO> comments) {
        this.comments = comments;
    }

    public UserTO getUser() {
        return user;
    }

    public void setUser(UserTO user) {
        this.user = user;
    }


}
