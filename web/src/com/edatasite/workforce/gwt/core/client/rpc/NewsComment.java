package com.edatasite.workforce.gwt.core.client.rpc;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 24, 2009
 * Time: 11:57:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewsComment implements Serializable {
    private Integer newsId;
    private Integer commentId;
    private String username;
    private Date date;
    private String comment;
    private String employeeImageUrl;

    //Network news comment
    private Integer commentatorId;

    private String commentatorName;
    private Boolean userContact;

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getNewsId() {
        return newsId;
    }

    public void setNewsId(Integer newsId) {
        this.newsId = newsId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmployeeImageUrl() {
        return employeeImageUrl;
    }

    public void setEmployeeImageUrl(String employeeImageUrl) {
        this.employeeImageUrl = employeeImageUrl;
    }

    public Integer getCommentatorId() {
        return commentatorId;
    }

    public void setCommentatorId(Integer commentatorId) {
        this.commentatorId = commentatorId;
    }

    public String getCommentatorName() {
        return commentatorName;
    }

    public void setCommentatorName(String commentatorName) {
        this.commentatorName = commentatorName;
    }

    public Boolean getUserContact() {
        return userContact;
    }

    public void setUserContact(Boolean userContact) {
        this.userContact = userContact;
    }
}
