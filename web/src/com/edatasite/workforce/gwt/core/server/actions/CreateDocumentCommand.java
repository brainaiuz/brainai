package com.edatasite.workforce.gwt.core.server.actions;

import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;

public class CreateDocumentCommand extends WfmCommand {

    private Integer hostID;
    private Integer attachmentID;
    private String logoType;
    private Integer imageWidth;
    private Integer imageHeight;
    private String imgType;
    private Integer companyID;
    private String folderName;
    private String notdownloadable;
    private String withoutResize = "false";

    public Integer getHostID() {
        return hostID;
    }

    public void setHostID(Integer hostID) {
        this.hostID = hostID;
    }

    public Integer getAttachmentID() {
        return attachmentID;
    }

    public void setAttachmentID(Integer attachmentID) {
        this.attachmentID = attachmentID;
    }

    public String getLogoType() {
        return logoType;
    }

    public void setLogoType(String logoType) {
        this.logoType = logoType;
    }

    public Integer getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(Integer imageWidth) {
        this.imageWidth = imageWidth;
    }

    public Integer getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(Integer imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getNotdownloadable() {
        return notdownloadable;
    }

    public void setNotdownloadable(String notdownloadable) {
        this.notdownloadable = notdownloadable;
    }

    public Boolean getWithoutResize() {
        return withoutResize != null && !withoutResize.isEmpty() && "true".equals(withoutResize);
    }

    public void setWithoutResize(String withoutResize) {
        this.withoutResize = withoutResize;
    }
}