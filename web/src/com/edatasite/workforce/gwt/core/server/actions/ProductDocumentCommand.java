package com.edatasite.workforce.gwt.core.server.actions;

import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 18, 2010
 * Time: 2:17:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductDocumentCommand extends WfmCommand {

    private Integer pictureID;
    private Integer productID;
    private String imgType;

    public Integer getPictureID() {
        return pictureID;
    }

    public void setPictureID(Integer pictureID) {
        this.pictureID = pictureID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }
}