package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Dilshod Madrahimov on 4/2/15 2:20 PM
 */
public class CommentTO implements IsSerializable {

    Integer id;
    String message;
    Integer noteId;
    UserTO user;
    Long creationDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserTO getUser() {
        return user;
    }

    public void setUser(UserTO user) {
        this.user = user;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public CommentTO() {
    }

    public NewsComment wrap(CommentTO commentTO) {
        NewsComment commentItem = new NewsComment();
        commentItem.setCommentId(commentTO.getId());
        commentItem.setNewsId(commentTO.getNoteId());
        commentItem.setComment(commentTO.getMessage());
        commentItem.setDate(WrapUtils.longToDate(commentTO.getCreationDate()));
        if (commentTO.getUser() != null) {
            commentItem.setUsername(commentTO.getUser().getName());
            commentItem.setEmployeeImageUrl(commentTO.getUser().getImageUrl());
        }
        return commentItem;
    }

    public CommentTO(NewsComment commentItem) {
        this.id = commentItem.getCommentId();
        this.noteId = commentItem.getNewsId();
        this.message = commentItem.getComment();
        UserTO userTO = new UserTO();
        userTO.setName(commentItem.getUsername());
        userTO.setImageUrl(commentItem.getEmployeeImageUrl());
        this.user = userTO;
        this.creationDate = WrapUtils.dateToLong(commentItem.getDate());
    }

}
