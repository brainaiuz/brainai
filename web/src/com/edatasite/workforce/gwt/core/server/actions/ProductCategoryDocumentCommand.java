package com.edatasite.workforce.gwt.core.server.actions;

import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 7:33:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCategoryDocumentCommand extends WfmCommand {

    private Integer pictureID;
    private Integer categoryID;
    private String imgType;

    public Integer getPictureID() {
        return pictureID;
    }

    public void setPictureID(Integer pictureID) {
        this.pictureID = pictureID;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }
}