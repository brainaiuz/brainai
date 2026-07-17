package com.edatasite.workforce.gwt.news.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.Date;
import java.util.List;

public class NewsSolr {

    private Integer id;
    private String subject;
    private String fullText;
    private Date date;
    private Date creationTime;
    private boolean anonym;
    private UserSolr user;
    private boolean visible;
    private boolean generalNews;
    private boolean blog;
    private SelectItem location;
    private String ownerName;
    private boolean deleted;
    private List<SelectItem> categories;
    private Integer viewCount;

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

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isAnonym() {
        return anonym;
    }

    public void setAnonym(Boolean anonym) {
        this.anonym = anonym != null && anonym;
    }

    public UserSolr getUser() {
        return user;
    }

    public void setUser(UserSolr user) {
        this.user = user;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible != null ? visible : false;
    }

    public boolean isGeneralNews() {
        return generalNews;
    }

    public void setGeneralNews(Boolean generalNews) {
        this.generalNews = generalNews != null ? generalNews : false;
    }

    public boolean isBlog() {
        return blog;
    }

    public void setBlog(Boolean blog) {
        this.blog = blog != null ? blog : false;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null && deleted;
    }

    public void setCategories(List<SelectItem> categories) {
        this.categories = categories;
    }

    public List<SelectItem> getCategories() {
        return categories;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}
