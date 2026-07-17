package com.edatasite.workforce.gwt.news.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 7:30:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewsListItem implements IsSerializable {

    private Integer objectId;
    private String subject;
    private Integer comments;
    private String categoryName;
    private String locationName;
    private Date date;
    private String dateAsString;
    private String postedBy;
    private String fullText;
    private String shortDescription;
    private String logoURL;
    private String newsLinkURL;
    private String creator;
    private boolean isBlog;
    private boolean visibility;
    private boolean isAnonymCreator;
    private String type;
    private String owner;

    private ArrayList<NewsCategory> categories;
    private boolean isGeneralNews;

    private boolean hasMemberPermission;
    private boolean hasAdminPermission;
    public static final String ACTION = "action";
    public static final String SUBJECT = "SUBJECT";
    public static final String CATEGORY = "CATEGORY";
    public static final String LOCATION = "LOCATION";
    public static final String DATE = "DATE";
    public static final String POSTED_BY = "POSTED_BY";
    public static final String VISIBILITY = "VISIBILITY";
    public static final String OWNER = "OWNER";
    public static final String COMMENT = "comments";


    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getSubject() {
        return subject == null ? "" : subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isGeneralNews() {
        return isGeneralNews;
    }

    public void setGeneralNews(boolean generalNews) {
        isGeneralNews = generalNews;
    }

    public boolean isAnonymCreator() {
        return isAnonymCreator;
    }

    public void setAnonymCreator(boolean anonymCreator) {
        isAnonymCreator = anonymCreator;
    }

    public boolean isBlog() {
        return isBlog;
    }

    public void setBlog(boolean blog) {
        isBlog = blog;
    }

    public ArrayList<NewsCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<NewsCategory> categories) {
        this.categories = categories;
    }


    public Integer getComments() {
        return comments;
    }

    public void setComments(Integer comments) {
        if (comments == null) {
            this.comments = 0;
        }
        this.comments = comments;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDateAsString() {
        return dateAsString;
    }

    public void setDateAsString(String dateAsString) {
        this.dateAsString = dateAsString;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getNewsLinkURL() {
        return newsLinkURL;
    }

    public void setNewsLinkURL(String newsLinkURL) {
        this.newsLinkURL = newsLinkURL;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public boolean isHasMemberPermission() {
        return hasMemberPermission;
    }

    public void setHasMemberPermission(boolean hasMemberPermission) {
        this.hasMemberPermission = hasMemberPermission;
    }

    public boolean isHasAdminPermission() {
        return hasAdminPermission;
    }

    public void setHasAdminPermission(boolean hasAdminPermission) {
        this.hasAdminPermission = hasAdminPermission;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
