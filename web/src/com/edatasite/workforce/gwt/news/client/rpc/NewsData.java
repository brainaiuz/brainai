package com.edatasite.workforce.gwt.news.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 5:52:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewsData implements IsSerializable {

    private Integer objectId;
    private String subject;
    private String owner;
    private Date publishedDate;
    private String shortDescription;
    private String fullDescription;
    private String creatorName;
    private Integer imageId;
    private Integer fileId;
    private String imageName;
    private String fileName;
    private ArrayList<NewsCategory> categories;
    private ArrayList<CrmAccountItem> suppliers;
    private Boolean showOptions;
    private ArrayList<NewsComment> comments;
    private Integer commentCount;
    private boolean visibility;
    private Integer creatorId;
    private Boolean showHomePage;
    //Additional Options
    private Integer formType;

    //Network news fields
    private boolean hasMemberPermission;
    private boolean hasAdminPermission;
    private boolean isBlog;
    private boolean isAnonymous;
    private boolean isNetwork;
    private boolean isGeneralNews;

    private boolean isToAllMembers;
    private Integer attachmentId;

    private Integer rating;
    private Integer viewsCount;
    private ArrayList<SelectItem> views;
    private String postedBy;

    private boolean currentOwner;
    private String imageUrl;
    private boolean isPressRelease;
    private boolean isNews;
    private boolean isTopNews;
    private boolean isFeatures;
    private boolean isOpinion;
    private boolean isSponsoredArticle;
    private boolean isEventArchive;
    private String author;
    private String fileLink;
    private String fileContentType;
    private String location;
    private SelectItem[] locationItems;
    private Integer locationID;
    private HistoryListItem[] commentList;

    private FileItem[] fileItems;

    private FileResource[] newsAttachmentsResource;


    public FileItem[] getFileItems() {
        return fileItems;
    }

    public void setFileItems(FileItem[] fileItems) {
        this.fileItems = fileItems;
    }

    public FileResource[] getNewsAttachmentsResource() {
        return newsAttachmentsResource;
    }

    public void setNewsAttachmentsResource(FileResource[] newsAttachmentsResource) {
        this.newsAttachmentsResource = newsAttachmentsResource;
    }

    public boolean isTopNews() {
        return isTopNews;
    }

    public void setTopNews(boolean topNews) {
        isTopNews = topNews;
    }

    public boolean isEventArchive() {
        return isEventArchive;
    }

    public void setEventArchive(boolean eventArchive) {
        isEventArchive = eventArchive;
    }

    public boolean isSponsoredArticle() {
        return isSponsoredArticle;
    }

    public void setSponsoredArticle(boolean sponsoredArticle) {
        isSponsoredArticle = sponsoredArticle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public boolean isOpinion() {
        return isOpinion;
    }

    public void setIsOpinion(boolean opinion) {
        isOpinion = opinion;
    }

    public boolean isWhitePaper() {
        return isWhitePaper;
    }

    public void setIsWhitePaper(boolean whitePaper) {
        isWhitePaper = whitePaper;
    }

    private boolean isWhitePaper;

    public boolean isPressRelease() {
        return isPressRelease;
    }

    public void setIsPressRelease(boolean pressRelease) {
        isPressRelease = pressRelease;
    }

    public boolean isNews() {
        return isNews;
    }

    public void setNews(boolean news) {
        isNews = news;
    }

    public boolean isFeatures() {
        return isFeatures;
    }

    public void setFeatures(boolean features) {
        isFeatures = features;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getObjectId() {

        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getSubject() {
        return subject;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public ArrayList<NewsCategory> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<NewsCategory> categories) {
        this.categories = categories;
    }

    public ArrayList<CrmAccountItem> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(ArrayList<CrmAccountItem> suppliers) {
        this.suppliers = suppliers;
    }

    public Boolean getShowOptions() {
        return showOptions;
    }

    public void setShowOptions(Boolean showOptions) {
        this.showOptions = showOptions;
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

    public ArrayList<NewsComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<NewsComment> comments) {
        this.comments = comments;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public boolean isVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public Integer getFormType() {
        return formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    public boolean isBlog() {
        return isBlog;
    }

    public void setBlog(boolean blog) {
        isBlog = blog;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public boolean isNetwork() {
        return isNetwork;
    }

    public void setNetwork(boolean network) {
        isNetwork = network;
    }

    public boolean isToAllMembers() {
        return isToAllMembers;
    }

    public void setToAllMembers(boolean toAllMembers) {
        isToAllMembers = toAllMembers;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public ArrayList<SelectItem> getViews() {
        return views;
    }

    public void setViews(ArrayList<SelectItem> views) {
        this.views = views;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }


    public boolean isCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(boolean currentOwner) {
        this.currentOwner = currentOwner;
    }

    public Integer getViewsCount() {
        return viewsCount;
    }

    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
    }

    public Boolean getShowHomePage() {
        return showHomePage;
    }

    public void setShowHomePage(Boolean showHomePage) {
        this.showHomePage = showHomePage;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public String getFileLink() {
        return fileLink;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public SelectItem[] getLocationItems() {
        return locationItems;
    }

    public void setLocationItems(SelectItem[] locationItems) {
        this.locationItems = locationItems;
    }

    public HistoryListItem[] getCommentList() {
        return commentList;
    }

    public void setCommentList(HistoryListItem[] commentList) {
        this.commentList = commentList;
    }
}
